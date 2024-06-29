package GamePlay;
import Server.Server.ClientHandler;
import Server.Server;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.google.gson.Gson;

import static Server.Server.sendMessageToOne;

public class Game {
    private String token;
    private static CardBox cardBox = new CardBox();
    private int round;
    private ClientHandler ruler;
    private Card hokm;
    Random rand = new Random();
    Gson gson = new Gson();
    private final Object lock = new Object();
    private boolean isRulerCardSelected = false;

    public ArrayList<Team> roomTeams = new ArrayList<>(2);
    public static ArrayList<Card> roomCards = new ArrayList<>(getCardBox().cards);
    public List<ClientHandler> roomPlayers;

    public static CardBox getCardBox() {
        return cardBox;
    }

    public ArrayList<Card> getRoomCards() {
        return roomCards;
    }

    public boolean getIsRulerCardSelected() {
        return isRulerCardSelected;
    }

    public void setRulerCardSelected(boolean rulerCardSelected) {
        isRulerCardSelected = rulerCardSelected;
    }

    // Constructor
    public Game(List<ClientHandler> GameMembers) {
        this.roomPlayers = GameMembers;
        roomTeams.add(new Team(roomPlayers.get(0), roomPlayers.get(2)));
        roomTeams.add(new Team(roomPlayers.get(1), roomPlayers.get(3)));
        this.ruler = roomPlayers.get(rand.nextInt(roomPlayers.size()));
    }

    // Getter method for CardBox
    public Card getHokm() {
        return hokm;
    }

    public void setHokm(Card hokm) {
        this.hokm = hokm;
    }

    public ClientHandler getKing() {
        return ruler;
    }

    public void setKing(ClientHandler king) {
        this.ruler = king;
    }


    public void initializingNames() {
        for (int i = 0; i < 4; i++) {
            roomPlayers.get(i).sendMessage("LEFT NAME:" + roomPlayers.get(i).getNickname());
            roomPlayers.get(i).sendMessage("LEFT NAME:" + roomPlayers.get((i + 1) % 4).getNickname());
            roomPlayers.get(i).sendMessage("FRONT NAME:" + roomPlayers.get((i + 2) % 4).getNickname());
            roomPlayers.get(i).sendMessage("RIGHT NAME:" + roomPlayers.get((i + 3) % 4).getNickname());
        }
    }

    public void CardDividing() {

        int rulerIndex = 0;
        for (int i = 0; i < roomPlayers.size(); i++) {
            if (ruler == roomPlayers.get(i)) {
                rulerIndex = i;
                break;
            }
        }

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
        waitForRulerCardSelection();
        roomPlayers.get(rulerIndex).sendMessage("YOU RULED.");

        // دادن 5 کارت به 2 نفر بعدی
        for (int j = 0; j < 5; j++) {
            randomCard = rand.nextInt(roomCards.size());
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
}
