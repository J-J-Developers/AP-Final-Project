package GamePlay;

import Server.Server;
import com.google.gson.Gson;
import Server.Server.ClientHandler;


import java.util.ArrayList;
import java.util.HashMap;

public class Set {
    //Attributes
    private static Round round;
    private int setNumber;
    private static ClientHandler firstPlayer;
    private ClientHandler nextFirstPlayer;
    private static String bordType = "Heart";
    private static final Object lock2 = new Object();
    private static boolean isPlayerSelected = false;
    //to make a turn
    public static ArrayList<Card> bordCards = new ArrayList<>();
    //For scoring
    HashMap<Integer,Card> bordMap = new HashMap<>();
    Gson gson = new Gson();
    //******************************************************************************************************************
    //Getter and Setters
    public int getSetNumber(){
        return setNumber;
    }

    public String getBordType() {
        return bordType;
    }

    public void setBordType(String bordType) {
        this.bordType = bordType;
    }

    public boolean isPlayerSelected() {
        return isPlayerSelected;
    }

    public void playerCardSelected() {
        synchronized (lock2) {
            isPlayerSelected = true;
            lock2.notifyAll();
        }
    }

    public static void addToBordCards(Card newCard) {
        getBordCards().add(newCard);
    }

    public ClientHandler getFirstPlayer() {
        return firstPlayer;
    }

    public ClientHandler getNextFirstPlayer() {
        return nextFirstPlayer;
    }

    public void setNextFirstPlayer(ClientHandler firstPlayer) {
        this.nextFirstPlayer = nextFirstPlayer;
    }

    public static ArrayList<Card> getBordCards() {
        return bordCards;
    }
    //******************************************************************************************************************
    //Constructor
    public Set(Round round,int setNumber,ClientHandler firstPlayer){
        this.round = round;
        this.setNumber = setNumber;
        this.firstPlayer = firstPlayer;
    }
    //******************************************************************************************************************
    //Main methods
    public static void startSet(){
        puttingCard();
    }
    public static void puttingCard(){
        round.getGame().roomPlayers.get(firstPlayer.getPlayerIndex()).sendMessage("YOUR TURN." + "FREE");
        waitForPlayerCardSelection();
        round.getGame().roomPlayers.get(firstPlayer.getPlayerIndex()).sendMessage("NOT TURN.");
        isPlayerSelected = false;
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex()+1)%4).sendMessage("YOUR TURN." + bordType);
        waitForPlayerCardSelection();
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex()+1)%4).sendMessage("NOT TURN.");
        isPlayerSelected =false;
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex()+2)%4).sendMessage("YOUR TURN." + bordType);
        waitForPlayerCardSelection();
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex()+2)%4).sendMessage("NOT TURN.");
        isPlayerSelected = false;
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex()+3)%4).sendMessage("YOUR TURN." + bordType);
        waitForPlayerCardSelection();
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex()+3)%4).sendMessage("NOT TURN.");
        isPlayerSelected = false;
    }
    public static void updateBordCards(Card putCard, int puterIndex) {
        bordCards.add(putCard);
        round.getGame().roomPlayers.get(puterIndex).sendMessage("NOT TURN.");
        round.getGame().roomPlayers.get(puterIndex).sendMessage("YOUR CARD:" );
        round.getGame().roomPlayers.get((puterIndex + 1) % 4).sendMessage("LEFT CARD:" );
        round.getGame().roomPlayers.get((puterIndex + 2) % 4).sendMessage("FRONT CARD:" );
        round.getGame().roomPlayers.get((puterIndex + 3) % 4).sendMessage("RIGHT CARD:" );
    }












    //******************************************************************************************************************
    //Helping methods
    private static void waitForPlayerCardSelection() {
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


}