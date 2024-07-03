package GamePlay;

import Server.Server.ClientHandler;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Round  {
    //Attributes
    private Game game;
    private int roundNumber;
    private static CardBox cardBox = new CardBox();
    private ClientHandler ruler;
    private String rulType = "Heart";
    //private String bordType = "Heart";
    Random rand = new Random();
    Gson gson = new Gson();
    private final Object lock = new Object();
    //private final Object lock2 = new Object();
    private boolean isRulerCardSelected = false;
    //private boolean isPlayerSelected = false;
    public static ArrayList<Card> roundCards = new ArrayList<>(getCardBox().cards);
    //public static ArrayList<Card> bordCards = new ArrayList<>();
    public ArrayList<Set> gameSets = new ArrayList<>();
    //******************************************************************************************************************
    //Getter and Setters

    public Game getGame() {
        return game;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public static CardBox getCardBox() {
        return cardBox;
    }
    public String getRulType(){
        return rulType;
    }

    /*public String getBordType() {
        return bordType;
    }

    public void setBordType(String bordType) {
        this.bordType = bordType;
    }*/

    public ClientHandler getRuler() {
        return ruler;
    }

    public void setRuler(ClientHandler ruler) {
        this.ruler = ruler;
    }

    public boolean isRulerCardSelected() {
        return isRulerCardSelected;
    }

    public void setRulerCardSelected(boolean rulerCardSelected) {
        isRulerCardSelected = rulerCardSelected;
    }

    /*public boolean isPlayerSelected() {
        return isPlayerSelected;
    }

    public void setPlayerSelected(boolean playerSelected) {
        isPlayerSelected = playerSelected;
    }*/

    public static ArrayList<Card> getRoomCards() {
        return roundCards;
    }

    /*public static void addToBordCards(Card newCard) {
        getBordCards().add(newCard);
    }

    public static ArrayList<Card> getBordCards() {
        return bordCards;
    }*/
    public ArrayList<Set> getGameSets() {
        return gameSets;
    }
    //******************************************************************************************************************
    //Constructor
    public Round(Game game,int roundNumber){
        this.game = game;
        this.roundNumber = roundNumber;
        this.ruler = game.roomPlayers.get(rand.nextInt(game.roomPlayers.size()));//choosing the king randomly
    }
    //******************************************************************************************************************
    //Starting round method
    public void startRound(){
        CardDividing();
        int setNumber = gameSets.size() + 1;
        Set set = new Set(this,setNumber,ruler);
        gameSets.add(set);
        Set.startSet();
        while (game.roomTeams.get(0).getWinedSets() < 7 && game.roomTeams.get(1).getWinedSets() < 7) {
            Set newSet = new Set(this,setNumber+1,gameSets.getLast().getNextFirstPlayer());
            gameSets.add(newSet);
            newSet.startSet();
            setNumber ++;
        }

    }
    //******************************************************************************************************************
    //Main methods
    public void CardDividing() {

        int rulerIndex = 0;
        for (int i = 0; i < game.roomPlayers.size(); i++) {
            if (ruler == game.roomPlayers.get(i)) {
                rulerIndex = i;
                break;
            }
        }

        int randomCard;
        // دادن 5 کارت به حاکم و نفر بعدیش
        for (int j = 0; j < 5; j++) {
            randomCard = rand.nextInt(roundCards.size());
            String CodedRandomCard = gson.toJson(roundCards.get(randomCard));
            game.roomPlayers.get(rulerIndex).sendMessage("TAKE CARD:" + CodedRandomCard);
            roundCards.remove(randomCard);
        }
        for (int j = 0; j < 5; j++) {
            randomCard = rand.nextInt(roundCards.size());
            String CodedRandomCard = gson.toJson(roundCards.get(randomCard));
            game.roomPlayers.get((rulerIndex + 1) % 4).sendMessage("TAKE CARD:" + CodedRandomCard);
            roundCards.remove(randomCard);
        }

        game.roomPlayers.get(rulerIndex).sendMessage("YOU ARE RULER.");
        waitForRulerCardSelection();
        game.roomPlayers.get(rulerIndex).sendMessage("YOU RULED.");

        // دادن 5 کارت به 2 نفر بعدی
        for (int j = 0; j < 5; j++) {
            randomCard = rand.nextInt(roundCards.size());
            String CodedRandomCard = gson.toJson(roundCards.get(randomCard));
            game.roomPlayers.get((rulerIndex + 2) % 4).sendMessage("TAKE CARD:" + CodedRandomCard);
            roundCards.remove(randomCard);
        }
        for (int j = 0; j < 5; j++) {
            randomCard = rand.nextInt(roundCards.size());
            String CodedRandomCard = gson.toJson(roundCards.get(randomCard));
            game.roomPlayers.get((rulerIndex + 3) % 4).sendMessage("TAKE CARD:" + CodedRandomCard);
            roundCards.remove(randomCard);
        }
        // دادن 2 دور 4 کارت به هر 4 نفر
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                int playerIndex = (rulerIndex + j) % 4;
                for (int k = 0; k < 4; k++) {
                    randomCard = rand.nextInt(roundCards.size());
                    String CodedRandomCard = gson.toJson(roundCards.get(randomCard));
                    game.roomPlayers.get(playerIndex).sendMessage("TAKE CARD:" + CodedRandomCard);
                    roundCards.remove(randomCard);
                }
            }
        }
    }


    /*public void playing(){
        game.roomPlayers.get(ruler.getPlayerIndex()).sendMessage("YOUR TURN." + "FREE");
        waitForPlayerCardSelection();
        game.roomPlayers.get(ruler.getPlayerIndex()).sendMessage("NOT TURN.");
        isPlayerSelected = false;
        game.roomPlayers.get((ruler.getPlayerIndex()+1)%4).sendMessage("YOUR TURN." + bordType);
        waitForPlayerCardSelection();
        game.roomPlayers.get((ruler.getPlayerIndex()+1)%4).sendMessage("NOT TURN.");
        isPlayerSelected =false;
        game.roomPlayers.get((ruler.getPlayerIndex()+2)%4).sendMessage("YOUR TURN." + bordType);
        waitForPlayerCardSelection();
        game.roomPlayers.get((ruler.getPlayerIndex()+2)%4).sendMessage("NOT TURN.");
        isPlayerSelected = false;
        game.roomPlayers.get((ruler.getPlayerIndex()+3)%4).sendMessage("YOUR TURN." + bordType);
        waitForPlayerCardSelection();
        game.roomPlayers.get((ruler.getPlayerIndex()+3)%4).sendMessage("NOT TURN.");
        isPlayerSelected = false;
    }*/
    //******************************************************************************************************************
    //Helping methods
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
    public void rulerCardSelected() {
        synchronized (lock) {
            isRulerCardSelected = true;
            lock.notifyAll(); // اطلاع به نخ منتظر که کارت انتخاب شده است
        }
    }
    /*private void waitForPlayerCardSelection() {
        synchronized (lock2) {
            while (!isPlayerSelected) {
                try {
                    lock2.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }*/
    /*public void playerCardSelected() {
        synchronized (lock2) {
            isPlayerSelected = true;
            lock2.notifyAll();
        }
    }*/
}
