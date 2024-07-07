package Server;
import Client.Client;
import GamePlay.Card;
import GamePlay.CardBox;
//import GamePlay.Team;
import GamePlay.Game;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int PORT = 6666; // پورت سرور
    private static Map<String, ClientHandler> clients = new HashMap<>(); // لیست کلاینت‌ها
    private static List<ClientHandler> randomPlayers = new ArrayList<>(); // لیست بازیکنان رندوم
    private static Map<String, List<ClientHandler>> friendGroups = new HashMap<>(); // گروه‌های دوستانه
    private static ArrayList<Game> AllGames = new ArrayList<>();

    static Gson gson = new Gson();

    public Server(){
    }


    public static void main(String[] args) {
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // منتظر اتصال کلاینت‌های جدید می‌ماند
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start(); // شروع ترد جدید برای مدیریت کلاینت
            }
        } catch (IOException e) {
            e.printStackTrace(); // چاپ خطا در صورت مشکل در اتصال
        }
    }

    public static ArrayList<Game> getAllGames() {
        return AllGames;
    }

    public static void setAllGames(ArrayList<Game> allGames) {
        AllGames = allGames;
    }

    // کلاس مدیریت کلاینت‌ها
    public static class ClientHandler  implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;
        private int playerIndex;
        private int winedSets = 0;
        private int winedRounds = 0;

        public int getPlayerIndex() {
            return playerIndex;
        }

        public void setPlayerIndex(int playerIndex) {
            this.playerIndex = playerIndex;
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
        public int getPlayerWinedRounds() {
            return winedRounds;
        }
        public void addToPlayerWinedRounds(){
            winedRounds++;
        }

        @Override
        public void run() {
            try {
                // تنظیم ورودی و خروجی برای ارتباط با کلاینت
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    // دریافت پیام از کلاینت
                    String message = in.readLine();
                    if (message == null) {
                        break; // قطع ارتباط در صورت دریافت پیام null
                    }
                    if (message.startsWith("I PUT:")){
                        String JsonCardString = message.substring(6);
                        Card card = gson.fromJson(JsonCardString, Card.class);
                        AllGames.getLast().gameRounds.getLast().gameSets.getLast().getBordMap().put(playerIndex,card);
                        if (AllGames.getLast().gameRounds.getLast().gameSets.getLast().getBordCards().isEmpty()){
                            AllGames.getLast().gameRounds.getLast().gameSets.getLast().setBordType(card.getType());
                        }
                        AllGames.getLast().gameRounds.getLast().gameSets.getLast().getBordCards().add(card);
                        AllGames.getLast().gameRounds.getLast().gameSets.getLast().updateBordCards(JsonCardString,playerIndex);
                        AllGames.getLast().gameRounds.getLast().gameSets.getLast().playerCardSelected();
                    }
                    if (message.startsWith("RUL IS:")){
                        String rul = message.substring(7);
                        AllGames.getLast().gameRounds.getLast().setRulType(rul);
                        for (int i = 0; i < 4; i++) {
                            AllGames.getLast().roomPlayers.get(i).sendMessage("RUL IS:" + rul);
                        }
                        AllGames.getLast().gameRounds.getLast().rulerCardSelected();
                    }


                    System.out.println("Player " + playerIndex+ " :" + message);


                    //دستورات مربوط به جوین شدن در بازی
                    String[] parts = message.split(" "); // جدا کردن دستورات و پارامترها
                    String command = parts[0]; // اولین قسمت پیام به عنوان دستور
                    // پردازش دستورها
                    if (command.equals("create")) {
                        handleCreate(parts); // فراخوانی تابع ایجاد بازی دوستانه
                    } else if (command.equals("join")) {
                        handleJoin(parts); // فراخوانی تابع پیوستن به گروه
                    } else if (command.equals("random")) {
                        handleRandom(parts); // فراخوانی تابع پیوستن به گروه تصادفی
                    } else if (command.equals("message")) {
                        //handleMessage(parts); // فراخوانی تابع ارسال پیام به گروه
                    } else {
                        //out.println("Unknown command: " + command); // پیام خطا برای دستور ناشناخته
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // چاپ خطا در صورت بروز مشکل در ارتباط
            }
            finally {
                // حذف کلاینت از لیست در صورت قطع ارتباط
                if (nickname != null) {
                    clients.remove(nickname);
                    System.out.println("Client disconnected: " + nickname);
                }
                try {
                    socket.close(); // بستن سوکت
                } catch (IOException e) {
                    e.printStackTrace(); // چاپ خطا در صورت بروز مشکل در بستن سوکت
                }
            }
        }

        // پردازش دستور create برای ایجاد بازی دوستانه
        private void handleCreate(String[] parts) {
            if (parts.length < 3) {
                out.println("Error: Missing nickname or token"); // پیام خطا در صورت نبودن نام مستعار یا توکن
                return;
            }
            this.nickname = parts[1]; // دریافت نام مستعار
            String token = parts[2]; // دریافت توکن از کلاینت

            clients.put(nickname, this); // اضافه کردن کلاینت به لیست
            List<ClientHandler> newGroup = new ArrayList<>();
            newGroup.add(this); // ایجاد گروه جدید دوستانه با توکن
            friendGroups.put(token, newGroup); // اضافه کردن گروه به لیست گروه‌ها
            out.println("Success: Game created with token " + token); // ارسال پیام موفقیت به کلاینت همراه با توکن
        }

        // پردازش دستور join برای پیوستن به بازی دوستانه
        private void handleJoin(String[] parts) {
            if (parts.length < 3) {
                out.println("Error: Missing nickname or token"); // پیام خطا در صورت نبودن نام مستعار یا توکن
                return;
            }
            this.nickname = parts[1]; // دریافت نام مستعار
            String token = parts[2]; // دریافت توکن از کلاینت

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
                    out.println("Waiting for more friends to join..."); // پیام انتظار برای دوستان بیشتر
                }
            }
        }

        // پردازش دستور random برای پیوستن به گروه تصادفی
        private void handleRandom(String[] parts) {
            this.nickname = parts[1]; // دریافت نام مستعار
            clients.put(nickname, this); // اضافه کردن کلاینت به لیست
            joinRandomGroup(); // پیوستن به گروه تصادفی
        }

        // پیوستن به گروه تصادفی
        private void joinRandomGroup() {
            randomPlayers.add(this); // اضافه کردن کلاینت به لیست بازیکنان تصادفی
            if (randomPlayers.size() % 4 == 0) { // اگر تعداد بازیکنان به ۴ نفر رسید
                List<ClientHandler> newGroup = new ArrayList<>(); // ساخت گروه جدید
                for (int i = 0; i < 4; i++) {
                    newGroup.add(randomPlayers.remove(0)); // اضافه کردن ۴ کلاینت به گروه جدید
                }
                startGame(newGroup); // شروع بازی با گروه جدید
            } else {
                out.println("Waiting for more players to join..."); // پیام انتظار برای بازیکنان بیشتر
            }
        }

        // شروع بازی با گروه
        private void startGame(List<ClientHandler> group) {
            group.get(0).setPlayerIndex(0);
            group.get(1).setPlayerIndex(1);
            group.get(2).setPlayerIndex(2);
            group.get(3).setPlayerIndex(3);
            Game newGame = new Game(group);
            System.out.println("*****  " + group.size());
            AllGames.add(newGame);
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

    // ارسال پیام به اعضای گروه
    private void sendMessageToGroup(String message, List<ClientHandler> group) {
        for (ClientHandler player : group) {
            player.out.println(message);
        }
    }
    public static void sendMessageToOne(String message, List<ClientHandler> group, int index) {
        group.get(index).out.println(message);
    }


    // پردازش دستور message برای ارسال پیام به گروه
       /* private void handleMessage(String[] parts) {
            if (parts.length < 3) {
                out.println("Error: Missing message or token");
                return;
            }
            String token = parts[1];
            String message = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));

            synchronized (friendGroups) {
                if (friendGroups.containsKey(token)) {
                    List<ClientHandler> group = friendGroups.get(token);
                    sendMessageToGroup(nickname + ": " + message, group);
                } else {
                    out.println("Error: Invalid token");
                }
            }
        }*/
}