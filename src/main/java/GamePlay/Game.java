package GamePlay;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Game{
    private String token;
    private CardBox cardBox;
    private int round;
    private Player king;
    private Card hokm;
    Random rand = new Random();
    private final Object lock = new Object();
    private boolean isRulerCardSelected = false;

    public ArrayList<Team> roomTeams = new ArrayList<>(2);
    public ArrayList<Card> roomCards = new ArrayList<>(getCardBox().cards);
    public ArrayList<Player> roomPlayers = new ArrayList<>();
    // Constructor
    public Game(Player p1, Player p2, Player p3, Player p4) {
        roomPlayers.add(new Player(p1.getName(),p1.getId()));
        roomPlayers.add(new Player(p2.getName(),p2.getId()));
        roomPlayers.add(new Player(p3.getName(),p3.getId()));
        roomPlayers.add(new Player(p4.getName(),p4.getId()));
        roomTeams.add(new Team(p1,p3));
        roomTeams.add(new Team(p2,p4));
        this.cardBox = new CardBox();
        this.king = roomPlayers.get(rand.nextInt(roomPlayers.size()));
    }

    // Getter method for CardBox
    public CardBox getCardBox() {
        return this.cardBox;
    }

    public Card getHokm() {
        return hokm;
    }

    public void setHokm(Card hokm) {
        this.hokm = hokm;
    }

    public Player getKing() {
        return king;
    }

    public void setKing(Player king) {
        this.king = king;
    }

    int kingIndex = 0;
    public void preGame(){
        // پیدا کردن حاکم
        for (int i = 0; i < roomPlayers.size(); i++) {
            if (this.king == roomPlayers.get(i)){
                kingIndex = i;
            }
        }
        // دادن 5 کارت به حاکم و نفر بعدیش
            for (int j = 0; j < 5; j++) {
                roomPlayers.get(kingIndex).getMyCards().add(roomCards.get(rand.nextInt(roomCards.size())));
                roomPlayers.get(kingIndex).getMyButtons().add(new JButton());
                roomPlayers.get(kingIndex).getMyButtons().getLast().setIcon(new ImageIcon(roomPlayers.get(kingIndex).getMyCards().getLast().getRoo().getImage()));
            }
            for (int j = 0; j < 5; j++) {
                roomPlayers.get((kingIndex+1)%4).getMyCards().add(roomCards.get(rand.nextInt(roomCards.size())));
                roomPlayers.get((kingIndex+1)%4).getMyButtons().add(new JButton());
                roomPlayers.get((kingIndex+1)%4).getMyButtons().getLast().setIcon(new ImageIcon(roomPlayers.get((kingIndex+1)%4).getMyCards().getLast().getRoo().getImage()));
            }
            waitForRulerCardSelection();
            divideCards();
            // start game method...


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

    public void divideCards(){
        // دادن 5 کارت به 2 نفر بعدی
        for (int j = 0; j < 5; j++) {
            roomPlayers.get((kingIndex+2)%4).getMyCards().add(roomCards.get(rand.nextInt(roomCards.size())));
            roomPlayers.get((kingIndex+2)%4).getMyButtons().add(new JButton());
            roomPlayers.get((kingIndex+2)%4).getMyButtons().getLast().setIcon(new ImageIcon(roomPlayers.get((kingIndex+2)%4).getMyCards().getLast().getRoo().getImage()));
        }
        for (int j = 0; j < 5; j++) {
            roomPlayers.get((kingIndex+3)%4).getMyCards().add(roomCards.get(rand.nextInt(roomCards.size())));
            roomPlayers.get((kingIndex+3)%4).getMyButtons().add(new JButton());
            roomPlayers.get((kingIndex+3)%4).getMyButtons().getLast().setIcon(new ImageIcon(roomPlayers.get((kingIndex+3)%4).getMyCards().getLast().getRoo().getImage()));
        }
        // دادن 2 دور 4 کارت به هر 4 نفر
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
            int playerIndex = (kingIndex + j) % 4;
                for (int k = 0; k < 4; k++) {
                    roomPlayers.get(playerIndex).getMyCards().add(roomCards.get(rand.nextInt(roomCards.size())));
                    roomPlayers.get(playerIndex).getMyButtons().add(new JButton());
                    roomPlayers.get(playerIndex).getMyButtons().getLast().setIcon(new ImageIcon(roomPlayers.get(playerIndex).getMyCards().getLast().getRoo().getImage()));
                }
             }

        }
    }
}
