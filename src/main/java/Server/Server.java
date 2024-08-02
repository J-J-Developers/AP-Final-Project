package Server;

import GamePlay.*;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int PORT = 6666;
    private static Map<String, ClientHandler> clients = new HashMap<>();
    private static List<ClientHandler> randomPlayers = new ArrayList<>();
    private static Map<String, List<ClientHandler>> friendGroups = new HashMap<>();
    private static ArrayList<Game> AllGames = new ArrayList<>();

    static Gson gson = new Gson();

    public Server(){
    }


    public static void main(String[] args) {
        System.out.println("Server started...");
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String command = scanner.nextLine();
                if (command.equals("GAMES STATUS")) {
                    printGamesStatus();
                }else if (command.startsWith("game result ")){
                    try {
                        printGameResult(Integer.parseInt(command.substring(12, 13)));
                    } catch (NumberFormatException e){
                        System.out.println("Correct format is: game result <gameIndex>");
                    }
                } else if (command.startsWith("game players ")) {
                    try {
                        printGamePlayers(Integer.parseInt(command.substring(13, 14)));
                    } catch (NumberFormatException e){
                        System.out.println("Correct format is: game players <gameIndex>");
                    }
                } else if (command.startsWith("game rounds ")) {
                    try {
                        printGameRounds(Integer.parseInt(command.substring(12, 13)));
                    } catch (NumberFormatException e){
                        System.out.println("Correct format is: game rounds <gameIndex>");
                    }
                } else if (command.startsWith("game round sets ")) {
                    try {
                        printRoundSets(Integer.parseInt(command.substring(16, 17)), Integer.parseInt(command.substring(18, 19)));
                    } catch (NumberFormatException e){
                        System.out.println("Correct format is: game round sets <gameIndex>  <roundIndex>");
                    }
                } else if (command.startsWith("set cards ")) {
                    try {
                        printSetCards(Integer.parseInt(command.substring(10, 11)), Integer.parseInt(command.substring(12, 13)), Integer.parseInt(command.substring(14, 15)));
                    } catch (NumberFormatException e){
                        System.out.println("Correct format is: set cards <gameIndex>  <roundIndex>  <setIndex>");
                    }
                }
            }
        }).start();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start(); // شروع ترد جدید برای مدیریت کلاینت
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ClientHandler  implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;
        private int playerIndex;
        private int gameIndex = -1;
        private int winedSets = 0;
        private int winedRounds = 0;

        public int getPlayerIndex() {
            return playerIndex;
        }

        public void setPlayerIndex(int playerIndex) {
            this.playerIndex = playerIndex;
        }

        public void setGameIndex(int gameIndex){
            this.gameIndex =gameIndex;
        }

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        public String getNickname(){
            return nickname;
        }
        public void sendMessage(String message){
            out.println(message);
        }

        public int getPlayerWinedSets() {
            return winedSets;
        }
        public void addToPlayerWinedSets(){
            winedSets ++;
        }
        public void playerZeroing(){
            winedSets = 0;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    try {
                    String message = in.readLine();
                    if (message == null) {
                        break; // قطع ارتباط در صورت دریافت پیام null
                    }
                    if (message.startsWith("I PUT:")){
                        String JsonCardString = message.substring(6);
                        Card card = gson.fromJson(JsonCardString, Card.class);
                        AllGames.get(gameIndex).gameRounds.getLast().gameSets.getLast().getBordMap().put(playerIndex,card);
                        if (AllGames.get(gameIndex).gameRounds.getLast().gameSets.getLast().getBordCards().isEmpty()){
                            AllGames.get(gameIndex).gameRounds.getLast().gameSets.getLast().setBordType(card.getType());
                        }
                        AllGames.get(gameIndex).gameRounds.getLast().gameSets.getLast().bordCards.add(card);
                        AllGames.get(gameIndex).gameRounds.getLast().gameSets.getLast().updateBordCards(JsonCardString,playerIndex);
                        AllGames.get(gameIndex).gameRounds.getLast().gameSets.getLast().playerCardSelected();
                    }
                    if (message.startsWith("RUL IS:")){
                        String rul = message.substring(7);
                        AllGames.get(gameIndex).gameRounds.getLast().setRulType(rul);
                        for (int i = 0; i < 4; i++) {
                            AllGames.get(gameIndex).roomPlayers.get(i).sendMessage("RUL IS:" + rul);
                        }
                        AllGames.get(gameIndex).gameRounds.getLast().rulerCardSelected();
                    }


                    System.out.println( "from Game " + gameIndex +",Player " + playerIndex+ " :" + message);


                    //دستورات مربوط به جوین شدن در بازی
                    String[] parts = message.split("::");
                    String command = parts[0];
                    switch (command) {
                        case "create" -> handleCreate(parts);
                        case "join" -> handleJoin(parts);
                        case "random" -> handleRandom(parts);
                    }
                    } catch (IOException e) {
                        System.out.println("Connection error: " + e.getMessage());
                        if (gameIndex != -1){
                            connectionLostMessage(gameIndex,socket);
                        }
                        break; // خروج از حلقه در صورت بروز خطا
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // حذف کلاینت از لیست در صورت قطع ارتباط
                if (nickname != null) {
                    clients.remove(nickname);
                    System.out.println("Client disconnected: " + nickname);
                }
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing resources: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        // create order for friendly game
        private void handleCreate(String[] parts) {
            if (parts.length < 3) {
                out.println("Error: Missing nickname or token"); // پیام خطا در صورت نبودن نام مستعار یا توکن
                return;
            }
            this.nickname = parts[1];
            String token = parts[2];

            clients.put(nickname, this); // اضافه کردن کلاینت به لیست
            List<ClientHandler> newGroup = new ArrayList<>();
            newGroup.add(this); // ایجاد گروه جدید دوستانه با توکن
            friendGroups.put(token, newGroup); // اضافه کردن گروه به لیست گروه‌ها
            out.println("Success: Game created with token " + token);
        }

        // join order for friendly game
        private void handleJoin(String[] parts) {
            if (parts.length < 3) {
                out.println("Error: Missing nickname or token"); // پیام خطا در صورت نبودن نام مستعار یا توکن
                return;
            }
            this.nickname = parts[1];
            String token = parts[2];

            synchronized (friendGroups) { // استفاده از synchronized برای جلوگیری از شرایط رقابتی
                if (!friendGroups.containsKey(token)) {
                    out.println("Error: Invalid token"); // پیام خطا در صورت نبودن توکن
                    return;
                }

                List<ClientHandler> group = friendGroups.get(token);
                group.add(this); // اضافه کردن کلاینت به گروه

                if (group.size() >= 4) { // اگر تعداد اعضای گروه به ۴ نفر رسید
                    startGame(group); // شروع بازی با گروه
                    friendGroups.remove(token); // حذف گروه پس از شروع بازی
                } else {
                    out.println("Waiting for more friends to join...");
                }
            }
        }

        // random order
        private void handleRandom(String[] parts) {
            this.nickname = parts[1];
            clients.put(nickname, this); // اضافه کردن کلاینت به لیست
            joinRandomGroup(); // پیوستن به گروه تصادفی
        }

        //join to a random group
        private void joinRandomGroup() {
            randomPlayers.add(this); // اضافه کردن کلاینت به لیست بازیکنان تصادفی
            if (randomPlayers.size() % 4 == 0) { // اگر تعداد بازیکنان به ۴ نفر رسید
                List<ClientHandler> newGroup = new ArrayList<>(); // ساخت گروه جدید
                for (int i = 0; i < 4; i++) {
                    newGroup.add(randomPlayers.remove(0)); // اضافه کردن ۴ کلاینت به گروه جدید
                }
                startGame(newGroup); // شروع بازی با گروه جدید
            } else {
                out.println("Waiting for more players to join...");
            }
        }

        // starting game for a group
        private void startGame(List<ClientHandler> group) {
            Game newGame = new Game(group);
            AllGames.add(newGame);
            group.get(0).setPlayerIndex(0);
            group.get(1).setPlayerIndex(1);
            group.get(2).setPlayerIndex(2);
            group.get(3).setPlayerIndex(3);
            group.get(0).setGameIndex(AllGames.size() - 1);
            group.get(1).setGameIndex(AllGames.size() - 1);
            group.get(2).setGameIndex(AllGames.size() - 1);
            group.get(3).setGameIndex(AllGames.size() - 1);
            System.out.println("*****  " + group.size());
            new Thread(() ->{
                try{
                    newGame.startMatch();
                }
                catch (NullPointerException e){
                    throw e;
                }
            }).start();
            System.out.println("-----|  " + group.size());
        }
    }

    public static void connectionLostMessage(int gameIndex,Socket socket) throws IOException {
        Iterator<ClientHandler> iterator = AllGames.get(gameIndex).roomPlayers.iterator();
        while (iterator.hasNext()) {
            ClientHandler player = iterator.next();
            if (socket.isClosed()){
                throw new IOException("Socket is closed!");
            } else {
                player.out.println("SOME ONE'S CONNECTION LOST");
            }
        }
    }


    public static void printGamesStatus() {
        for (int i = 0; i < AllGames.size(); i++) {
            System.out.println("♣️♥️♠️♦️♣️♥️♠️♦️♣️♥️♠️♦️♣️♥️♠️♦️♣️♥️♠️♦️");
            System.out.println("🏆Game number: " + ( i + 1));
            System.out.println(" Rounds played: " + AllGames.get(i).gameRounds.size());
            System.out.println(" Game members: ");
            for (int x = 0; x < AllGames.get(i).roomTeams.size(); x++) {
                Team team = AllGames.get(i).roomTeams.get(x);
                System.out.println("Team " + (x + 1) + " with players: ");
                System.out.println("  Player: " + team.p1.getNickname() + " - Index: " + team.p1.getPlayerIndex());
                System.out.println("  Player: " + team.p2.getNickname() + " - Index: " + team.p2.getPlayerIndex());
            }

            System.out.println("--------------------------------------------");
            System.out.println("Team Wins:");
            System.out.printf("  %-10s %-10s %-10s\n", "Team", "Rounds", "Sets");
            System.out.printf("  %-10s %-10d %-10d\n", "Team 1", AllGames.get(i).roomTeams.get(0).getTeamWinedRounds(), AllGames.get(i).roomTeams.get(0).getTeamWinedSets());
            System.out.printf("  %-10s %-10d %-10d\n", "Team 2", AllGames.get(i).roomTeams.get(1).getTeamWinedRounds(), AllGames.get(i).roomTeams.get(1).getTeamWinedSets());
            System.out.println("--------------------------------------------");
            System.out.println("Rounds information: ");

            for (int j =0; j < AllGames.get(i).gameRounds.size(); j++) {
                System.out.println("    Round number: " + (j + 1) );
                System.out.println("   👑Round ruler: " + AllGames.get(i).gameRounds.get(j).getRuler().nickname);
                System.out.println("    Round RulType: " + AllGames.get(i).gameRounds.get(j).getRulType());
                System.out.println("    Sets played in round: " + AllGames.get(i).gameRounds.get(j).gameSets.size());
                System.out.println("    Sets information: ");

                for (int k = 0; k < AllGames.get(i).gameRounds.get(j).gameSets.size(); k++) {
                    System.out.println("        Set number: " + ( k + 1 ) );
                    System.out.println("        Set first player: " + AllGames.get(i).gameRounds.get(j).gameSets.get(k).getFirstPlayer().nickname);
                    System.out.println("        Set BordType: " + AllGames.get(i).gameRounds.get(j).gameSets.get(k).getBordType());
                    System.out.println("        Cards played: " + AllGames.get(i).gameRounds.get(j).gameSets.get(k).bordCards.size());
                    try {
                        System.out.println("        🥇Winner: " + AllGames.get(i).roomPlayers.get(AllGames.get(i).gameRounds.get(j).gameSets.get(k).winner()).nickname);
                    }catch (IndexOutOfBoundsException e){
                        System.out.println("        Nobody didnt put any card yet!");
                    }

                    System.out.println("        Cards on the board:");
                    System.out.printf("      %-15s %-10s\n", "Player", "Card");
                    for (Map.Entry<Integer, Card> entry : AllGames.get(i).getGameRounds().get(j).gameSets.get(k).bordMap.entrySet()) {
                        int playerIndex = entry.getKey();
                        Card card = entry.getValue();
                        System.out.printf("      %-15s %-10s\n", AllGames.get(i).roomPlayers.get(playerIndex).nickname, card.getNumber() + " " + card.getType());
                    }

                    System.out.println("    ----------------------");
                }
            }
        }
    }
    public static void printGameResult(int gameIndex) {
        if (gameIndex < 0 || gameIndex >= AllGames.size()) {
            System.out.println("Invalid game index!");
            return;
        }
        System.out.println("--------------------------------------------");
        System.out.println("🏆Game number: " + (gameIndex + 1));
        System.out.println("Team Wins:");
        System.out.printf("  %-10s %-10s %-10s\n", "Team", "Rounds", "Sets");
        System.out.printf("  %-10s %-10d %-10d\n", "Team 1", AllGames.get(gameIndex).roomTeams.get(0).getTeamWinedRounds(), AllGames.get(gameIndex).roomTeams.get(0).getTeamWinedSets());
        System.out.printf("  %-10s %-10d %-10d\n", "Team 2", AllGames.get(gameIndex).roomTeams.get(1).getTeamWinedRounds(), AllGames.get(gameIndex).roomTeams.get(1).getTeamWinedSets());
        System.out.println("--------------------------------------------");
    }
    public static void printGamePlayers(int gameIndex) {
        if (gameIndex < 0 || gameIndex >= AllGames.size()) {
            System.out.println("Invalid game index!");
            return;
        }
        System.out.println("--------------------------------------------");
        System.out.println("🏆Game number: " + (gameIndex + 1));
        System.out.println("Game members: ");
        for (int x = 0; x < AllGames.get(gameIndex).roomTeams.size(); x++) {
            Team team = AllGames.get(gameIndex).roomTeams.get(x);
            System.out.println("Team " + (x + 1) + " with players: ");
            System.out.println("  Player: " + team.p1.getNickname() + " - Index: " + team.p1.getPlayerIndex());
            System.out.println("  Player: " + team.p2.getNickname() + " - Index: " + team.p2.getPlayerIndex());
        }
        System.out.println("--------------------------------------------");
    }
    public static void printGameRounds(int gameIndex) {
        if (gameIndex < 0 || gameIndex >= AllGames.size()) {
            System.out.println("Invalid game index!");
            return;
        }
        System.out.println("--------------------------------------------");
        System.out.println("🏆Game number: " + (gameIndex + 1));
        System.out.println("Rounds information: ");
        for (int j = 0; j < AllGames.get(gameIndex).gameRounds.size(); j++) {
            System.out.println("Round number: " + (j + 1));
            System.out.println("    Round ruler: " + AllGames.get(gameIndex).gameRounds.get(j).getRuler().nickname);
            System.out.println("    Round RulType: " + AllGames.get(gameIndex).gameRounds.get(j).getRulType());
            System.out.println("    Sets played in round: " + AllGames.get(gameIndex).gameRounds.get(j).gameSets.size());
        }
        System.out.println("--------------------------------------------");
    }
    public static void printRoundSets(int gameIndex, int roundIndex) {
        if (gameIndex < 0 || gameIndex >= AllGames.size() || roundIndex < 0 || roundIndex >= AllGames.get(gameIndex).gameRounds.size()) {
            System.out.println("Invalid game or round index!");
            return;
        }
        System.out.println("--------------------------------------------");
        System.out.println("🏆Game number: " + (gameIndex + 1));
        System.out.println("Round number: " + (roundIndex + 1));
        System.out.println("Sets information: ");
        for (int k = 0; k < AllGames.get(gameIndex).gameRounds.get(roundIndex).gameSets.size(); k++) {
            System.out.println("    Set number: " + (k + 1));
            System.out.println("        Set first player: " + AllGames.get(gameIndex).gameRounds.get(roundIndex).gameSets.get(k).getFirstPlayer().nickname);
            System.out.println("        Set BordType: " + AllGames.get(gameIndex).gameRounds.get(roundIndex).gameSets.get(k).getBordType());
            System.out.println("        Cards played: " + AllGames.get(gameIndex).gameRounds.get(roundIndex).gameSets.get(k).bordCards.size());
            try {
                System.out.println("        🥇Winner: " + AllGames.get(gameIndex).roomPlayers.get(AllGames.get(gameIndex).gameRounds.get(roundIndex).gameSets.get(k).winner()).nickname);
            }catch (IndexOutOfBoundsException e){
                System.out.println("Nobody didnt put any card yet!");
            }
        }
        System.out.println("--------------------------------------------");
    }
    public static void printSetCards(int gameIndex, int roundIndex, int setIndex) {
        if (gameIndex < 0 || gameIndex >= AllGames.size() || roundIndex < 0 || roundIndex >= AllGames.get(gameIndex).gameRounds.size() || setIndex < 0 || setIndex >= AllGames.get(gameIndex).gameRounds.get(roundIndex).gameSets.size()) {
            System.out.println("Invalid game, round, or set index!");
            return;
        }
        System.out.println("--------------------------------------------");
        System.out.println("🏆Game number: " + (gameIndex + 1));
        System.out.println("Round number: " + (roundIndex + 1));
        System.out.println("Set number: " + (setIndex + 1));
        System.out.println("    Cards on the board:");
        System.out.printf("  %-15s %-10s\n", "Player", "Card");
        for (Map.Entry<Integer, Card> entry : AllGames.get(gameIndex).getGameRounds().get(roundIndex).gameSets.get(setIndex).bordMap.entrySet()) {
            int playerIndex = entry.getKey();
            Card card = entry.getValue();
            System.out.printf("  %-15s %-10s\n", AllGames.get(gameIndex).roomPlayers.get(playerIndex).nickname, card.getNumber() + " " + card.getType());
        }
        System.out.println("--------------------------------------------");
    }

}