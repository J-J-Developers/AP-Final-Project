package GamePlay;

import Server.Server;
import com.google.gson.Gson;
import Server.Server.ClientHandler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Set {
    //Attributes
    private static Round round;
    private int setNumber;
    private static ClientHandler firstPlayer;
    private static ClientHandler nextFirstPlayer;
    private static String bordType;
    private static final Object lock2 = new Object();
    private static boolean isPlayerSelected = false;
    private boolean isSetFinished = false;
    //to make a turn
    public static ArrayList<Card> bordCards = new ArrayList<>();
    //For scoring
    static HashMap<Integer,Card> bordMap = new HashMap<>();
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

    public static void addToBordCards(Card newCard) {
        getBordCards().add(newCard);
    }

    public ClientHandler getFirstPlayer() {
        return firstPlayer;
    }

    public ClientHandler getNextFirstPlayer() {
        return nextFirstPlayer;
    }

    public static void setNextFirstPlayer(ClientHandler firstPlayer) {
        nextFirstPlayer = firstPlayer;
    }

    public static ArrayList<Card> getBordCards() {
        return bordCards;
    }

    public HashMap<Integer, Card> getBordMap() {
        return bordMap;
    }

    public static Round getRound() {
        return round;
    }

    public boolean isIsSetFinished() {
        return isSetFinished;
    }

    public void setIsSetFinished() {
        isSetFinished = true;
    }
    //******************************************************************************************************************
    //Constructor
    public Set(Round round,int setNumber,ClientHandler firstPlayer){
        this.round = round;
        this.setNumber = setNumber;
        this.firstPlayer = firstPlayer;
    }
    //******************************************************************************************************************
    //Starting set method
    public void startSet(){
        puttingCard();
        scoring();
        cleaningBord();
        setNextFirstPlayer(round.getGame().roomPlayers.get(winner()));
        setIsSetFinished();
    }
    //******************************************************************************************************************
    //Main methods
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
    public static void scoring(){
        switch (winner()) {
            case 0:
            case 2:
                getRound().getGame().roomTeams.get(0).addTeamWinedSets();
                System.out.println("team 0 wined sets is:" + getRound().getGame().roomTeams.get(0).getTeamWinedSets());
                getRound().getGame().roomPlayers.get(0).addToPlayerWinedSets();
                System.out.println( "player 0 wined sets is:" + getRound().getGame().roomPlayers.get(2).getPlayerWinedSets());
                getRound().getGame().roomPlayers.get(2).addToPlayerWinedSets();
                System.out.println( "player 2 wined sets is:" + getRound().getGame().roomPlayers.get(2).getPlayerWinedSets());
                getRound().getGame().roomPlayers.get(0).sendMessage("YOU WINED THE SET.");
                getRound().getGame().roomPlayers.get(2).sendMessage("YOU WINED THE SET.");
                break;

            case 1:
            case 3:
                getRound().getGame().roomTeams.get(1).addTeamWinedSets();
                System.out.println("team 1 wined sets is:" + getRound().getGame().roomTeams.get(1).getTeamWinedSets());
                getRound().getGame().roomPlayers.get(1).addToPlayerWinedSets();
                System.out.println( "player 1 wined sets is:" + getRound().getGame().roomPlayers.get(1).getPlayerWinedSets());
                getRound().getGame().roomPlayers.get(3).addToPlayerWinedSets();
                System.out.println( "player 3 wined sets is:" + getRound().getGame().roomPlayers.get(3).getPlayerWinedSets());
                getRound().getGame().roomPlayers.get(1).sendMessage("YOU WINED THE SET.");
                getRound().getGame().roomPlayers.get(3).sendMessage("YOU WINED THE SET.");
                break;
            default:
                System.out.println("ERROR in winning set");
        }
    }
    public static void cleaningBord(){
        for (int i = 0; i < 4; i++) {
            getRound().getGame().roomPlayers.get(i).sendMessage("CLEANING BORD.");
        }
    }
    //******************************************************************************************************************
    //Helping methods
    public static int winner() {
        int winner = -1;
        int max = 0;
        // first, check if anyone has the Rul card
        for (Map.Entry<Integer, Card> entry : bordMap.entrySet()) {
            int indexOfPerson = entry.getKey();
            if (entry.getValue().getType().equals(round.getRulType())) {
                // if someone has the Rul card, check if they have the highest number
                int personScore = entry.getValue().getNumber();
                if (personScore > max) {
                    max = personScore;
                    winner = indexOfPerson;
                }
            }
        }
        // if no one has the Rul card, check for the ruler's card type
        if (winner == -1) {
            max = 0;
            for (Map.Entry<Integer, Card> entry : bordMap.entrySet()) {
                int indexOfPerson = entry.getKey();
                if (entry.getValue().getType().equals(bordType)) {
                    // if someone has the same type as the ruler's card, check if they have the highest number
                    int personScore = entry.getValue().getNumber();
                    if (personScore > max) {
                        max = personScore;
                        winner = indexOfPerson;
                    }
                }
            }
        }
        return winner;
    }
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
    public void playerCardSelected() {
        synchronized (lock2) {
            isPlayerSelected = true;
            lock2.notifyAll();
        }
    }

}
