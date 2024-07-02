package GamePlay;
import Server.Server.ClientHandler;
import Server.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

import com.google.gson.Gson;

import static Server.Server.sendMessageToOne;

public class Game {
    private String token; //saving tokens
    private static CardBox cardBox = new CardBox();
    private int round;

    //    ===========================================================================================
    private int input ;
    private int set ;
    private String typeHokm = "Heart" ;
    private String bordType ;
    //    ===========================================================================================

    private ClientHandler ruler;
    private Card hokm;
    Random rand = new Random();
    Gson gson = new Gson();
    private final Object lock = new Object();
    private final Object lock2 = new Object();
    private boolean isRulerCardSelected = false;
    private boolean isPlayerSelected = false;

    public ArrayList<Team> roomTeams = new ArrayList<>(2);//making 2 team
    public static ArrayList<Card> roomCards = new ArrayList<>(getCardBox().cards);
    public List<ClientHandler> roomPlayers;//list of players
    public static ArrayList<Card> bordCards = new ArrayList<>();//list of cards
    HashMap<Integer,Card> bordMap = new HashMap<>();
    //    ========================================================================
    private static ArrayList<String> scoreCards = new ArrayList<>();//list of having point cards
//    ========================================================================

    //***********************************


    public void setBordType(String bordType) {
        this.bordType = bordType;
    }

    public String getBordType() {
        return bordType;
    }

    public static void setBordCards(Card card) {
        bordCards.add(card);
    }

    public static CardBox getCardBox() {
        return cardBox;
    }

    public ArrayList<Card> getRoomCards() {
        return roomCards;
    }

    public ArrayList<Card> getBordCards() {
        return bordCards;
    }

    public static void addToBordCards(Card putCard) {
        bordCards.add(putCard);
    }

    public boolean getIsRulerCardSelected() {
        return isRulerCardSelected;
    }

    public void setRulerCardSelected(boolean rulerCardSelected) {
        isRulerCardSelected = rulerCardSelected;
    }

    public boolean isPlayerSelected() {
        return isPlayerSelected;
    }

    public void setPlayerSelected(boolean playerSelected) {
        isPlayerSelected = playerSelected;
    }

    // Constructor
    public Game(List<ClientHandler> GameMembers) {//getting members list
        this.roomPlayers = GameMembers;
        roomTeams.add(new Team(roomPlayers.get(0), roomPlayers.get(2)));//creating team 1 with member 0 , 2
        roomTeams.add(new Team(roomPlayers.get(1), roomPlayers.get(3)));//creating team 2 with member 1 , 3
        this.ruler = roomPlayers.get(rand.nextInt(roomPlayers.size()));//choosing the king randomly
    }

    // Getter method for CardBox
    public Card getHokm() {
        return hokm;
    }//returning the hokm

    public void setHokm(Card hokm) {
        this.hokm = hokm;
    }

    public ClientHandler getKing() {
        return ruler;
    }//returning the ruler

    public void setKing(ClientHandler king) {
        this.ruler = king;
    }


    //===============================================================================
    public void setTypeHokm(String hokm) {
        this.typeHokm = hokm;
    }

    public void setTypeCard(String typeCard) {
        this.bordType = typeCard;
    }
    //===============================================================================


    public void initializingNames() {//showing members name to other members
        for (int i = 0; i < 4; i++) {
            roomPlayers.get(i).sendMessage("YOUR NAME:" + roomPlayers.get(i).getNickname());
            roomPlayers.get(i).sendMessage("LEFT NAME:" + roomPlayers.get((i + 1) % 4).getNickname());
            roomPlayers.get(i).sendMessage("FRONT NAME:" + roomPlayers.get((i + 2) % 4).getNickname());
            roomPlayers.get(i).sendMessage("RIGHT NAME:" + roomPlayers.get((i + 3) % 4).getNickname());
        }
    }

    public void CardDividing() {//dividing cards btw members

        int rulerIndex = 0;//king index
        for (int i = 0; i < roomPlayers.size(); i++) {//checking who is the ruler
            if (ruler == roomPlayers.get(i)) {
                rulerIndex = i;
                break;
            }
        }

        int randomCard;
        // دادن 5 کارت به حاکم و نفر بعدیش
        for (int j = 0; j < 5; j++) {
            randomCard = rand.nextInt(roomCards.size());
            String CodedRandomCard = gson.toJson(roomCards.get(randomCard));
            roomPlayers.get(rulerIndex).sendMessage("TAKE CARD:" + CodedRandomCard);
            roomCards.remove(randomCard);
        }
        for (int j = 0; j < 5; j++) {
            randomCard = rand.nextInt(roomCards.size());
            String CodedRandomCard = gson.toJson(roomCards.get(randomCard));
            roomPlayers.get((rulerIndex + 1) % 4).sendMessage("TAKE CARD:" + CodedRandomCard);
            roomCards.remove(randomCard);
        }

        roomPlayers.get(rulerIndex).sendMessage("YOU ARE RULER.");
        waitForRulerCardSelection();//stopping till ruler chose its card
        roomPlayers.get(rulerIndex).sendMessage("YOU RULED.");

        // دادن 5 کارت به 2 نفر بعدی
        for (int j = 0; j < 5; j++) {
            randomCard = rand.nextInt(roomCards.size());//choosing random card and make it gson
            String CodedRandomCard = gson.toJson(roomCards.get(randomCard));
            roomPlayers.get((rulerIndex + 2) % 4).sendMessage("TAKE CARD:" + CodedRandomCard);
            roomCards.remove(randomCard);
        }
        for (int j = 0; j < 5; j++) {
            randomCard = rand.nextInt(roomCards.size());
            String CodedRandomCard = gson.toJson(roomCards.get(randomCard));
            roomPlayers.get((rulerIndex + 3) % 4).sendMessage("TAKE CARD:" + CodedRandomCard);
            roomCards.remove(randomCard);
        }
        // دادن 2 دور 4 کارت به هر 4 نفر
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                int playerIndex = (rulerIndex + j) % 4;
                for (int k = 0; k < 4; k++) {
                    randomCard = rand.nextInt(roomCards.size());
                    String CodedRandomCard = gson.toJson(roomCards.get(randomCard));
                    roomPlayers.get(playerIndex).sendMessage("TAKE CARD:" + CodedRandomCard);
                    roomCards.remove(randomCard);
                }
            }
        }
    }


    private void waitForRulerCardSelection() {
        synchronized (lock) {
            while (!isRulerCardSelected) {
                try {
                    lock.wait(); // منتظر می‌ماند تا حاکم کارت را انتخاب کند
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // put this method in each actionListeners of Cards button to check hokm is selected or not...
    public void rulerCardSelected() {
        synchronized (lock) {
            isRulerCardSelected = true;
            lock.notifyAll(); // اطلاع به نخ منتظر که کارت انتخاب شده است
        }
    }
    public void updateBordCards(Card putCard,int puterIndex){//updating cards in desk
        bordCards.add(putCard);
        roomPlayers.get(puterIndex).sendMessage("NOT TURN.");
        roomPlayers.get(puterIndex).sendMessage("YOUR CARD:" + imageIconToString(putCard.getRooImage()));
        roomPlayers.get((puterIndex+1)%4).sendMessage("LEFT CARD:" + imageIconToString(putCard.getRooImage()));
        roomPlayers.get((puterIndex+2)%4).sendMessage("FRONT CARD:" + imageIconToString(putCard.getRooImage()));
        roomPlayers.get((puterIndex+3)%4).sendMessage("RIGHT CARD:" + imageIconToString(putCard.getRooImage()));
    }
    public static String imageIconToString(ImageIcon icon) {//writing string from images
        try {
            BufferedImage bufferedImage = (BufferedImage) icon.getImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void waitForPlayerCardSelection() {// waiting for players to choose their cards
        synchronized (lock2) {
            while (!isPlayerSelected) {
                try {
                    lock2.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public void playerCardSelected() {//when players choosing their cards
        synchronized (lock2) {
            isPlayerSelected = true;
            lock2.notifyAll();
        }
    }
    public void playing(){//telling all players in bord it's their turn
        while (true){
            roomPlayers.get(ruler.getPlayerIndex()).sendMessage("YOUR TURN." + "FREE");
            waitForPlayerCardSelection();
            roomPlayers.get(ruler.getPlayerIndex()).sendMessage("NOT TURN.");
            isPlayerSelected = false;
            roomPlayers.get((ruler.getPlayerIndex()+1)%4).sendMessage("YOUR TURN." + bordType);
            waitForPlayerCardSelection();
            roomPlayers.get((ruler.getPlayerIndex()+1)%4).sendMessage("NOT TURN.");
            isPlayerSelected =false;
            roomPlayers.get((ruler.getPlayerIndex()+2)%4).sendMessage("YOUR TURN." + bordType);
            waitForPlayerCardSelection();
            roomPlayers.get((ruler.getPlayerIndex()+2)%4).sendMessage("NOT TURN.");
            isPlayerSelected = false;
            roomPlayers.get((ruler.getPlayerIndex()+3)%4).sendMessage("YOUR TURN." + bordType);
            waitForPlayerCardSelection();
            roomPlayers.get((ruler.getPlayerIndex()+3)%4).sendMessage("NOT TURN.");
            isPlayerSelected = false;
        }
    }


    //=========================================================================================
    public void checkCards(String card) {
        String[] score = card.split(" ");
        scoreCards.addAll(Arrays.asList(score));
        input++;
        if (input == 4) {//checking if all players adding their cards or not
            judge();//getting score
        }

    }

    public int judge() {
        int winner = -1;
        int max = 0;
        int rulerIndex = 0;
        for (int i = 0; i < roomPlayers.size(); i++) {
            if (ruler == roomPlayers.get(i)) {
                rulerIndex = i;
                break;
            }
        }
        Card rulerCard = bordMap.get(rulerIndex);
        String typeRuler = rulerCard.getType(); // get the type of the ruler's card

        // first, check if anyone has the Hokm card
        for (Map.Entry<Integer, Card> entry : bordMap.entrySet()) {
            int indexOfPerson = entry.getKey();
            if (entry.getValue().getType().equals(typeHokm)) {
                // if someone has the Hokm card, check if they have the highest number
                int personScore = entry.getValue().getNumber();
                if (personScore > max) {
                    max = personScore;
                    winner = indexOfPerson;
                }
            }
        }

        // if no one has the Hokm card, check for the ruler's card type
        if (winner == -1) {
            max = 0;
            for (Map.Entry<Integer, Card> entry : bordMap.entrySet()) {
                int indexOfPerson = entry.getKey();
                if (entry.getValue().getType().equals(typeRuler)) {
                    // if someone has the same type as the ruler's card, check if they have the highest number
                    int personScore = entry.getValue().getNumber();
                    if (personScore > max) {
                        max = personScore;
                        winner = indexOfPerson;
                    }
                }
            }
        }

        //System.out.println("Winner is player " + winner);
        return  winner;
    }



}























/*
int governingNumber = 0;
        for (ClientHandler player : roomPlayers) {
            player.sendMessage("Players " + roomPlayers.get(0).getNickname() + " 0 " + roomPlayers.get(1).getNickname() + " 1 " + roomPlayers.get(2).getNickname() + " 2 " + roomPlayers.get(3).getNickname() + " 3");
            if (governingNumber == rulerIndex) {
                player.sendMessage("You are ruler ");
            } else {
                player.sendMessage("Ruler is " + roomPlayers.get(rulerIndex).getNickname()); // ارسال پیام حاکم به کل اعضای گروه
            }
            governingNumber++;

        }
 */



//    private void scoreCalculation() {//counting scores
//        int max = 0;
//        int similarity = 0;
//        int finalScore = 0;
//        Card winningCard = null;
//        String winner = "";
//
//        for(Map.Entry<Integer,Card> entry : scoreMap.entrySet()){
//            int score = entry.getKey();
//            Card card = entry.getValue();
//            if(score > max){
//                max = score;
//                winningCard = card;
//            }
//        }
//        if(winningCard != null){
//            String Winner = "";
//            for (ClientHandler player : roomPlayers){
//                player.sendMessage("WINNER IS SET IS :" + Winner);
//            }
//
//        }
//
//        // اصلاح شرط برای جلوگیری از IndexOutOfBoundsExceptiongt
//        for (int i = 0; i < scoreCards.size() - 1; i += 2) {
//            if (scoreCards.get(i + 1).equals(bordType)) {//checking if card is similar to bord card
//                similarity++;
//            } else if (scoreCards.get(i + 1).equals(typeHokm)) {//checking if card is similar to hokm type
//                int score = Integer.parseInt(scoreCards.get(i + 2));//turning score to digit
//                if (finalScore < score) { //check its more han the last score or not
//                    finalScore = score;
//                    winner = scoreCards.get(i);
//
//                }
//            }
//        }
//
//        if (similarity > 0 && finalScore == 0) {
//            for (int i = 0; i < scoreCards.size() - 1; i += 2) {//checking witch score is more
//                int score = Integer.parseInt(scoreCards.get(i + 2));
//                if (score > max) {
//                    max = score;
//                    winner = scoreCards.get(i);
//                }
//            }
//
//            for (int i = 0; i < scoreCards.size() - 1; i += 2) {
//                if (max == Integer.parseInt(scoreCards.get(i + 2))) {
//                    roomPlayers.get(i).sendMessage("WINNER IN SET IS :" + winner);
//
//                }
//            }
//        }
//    }
//=============================================================================================