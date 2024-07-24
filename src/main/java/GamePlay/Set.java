package GamePlay;

import Server.Server.ClientHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Set {
    //Attributes
    public Round round;
    public ClientHandler firstPlayer;
    public ClientHandler nextFirstPlayer;
    public String bordType;
    public final Object lock2 = new Object();
    public boolean isPlayerSelected = false;
    public boolean isSetFinished = false;
    //to make a turn
    public ArrayList<Card> bordCards = new ArrayList<>();
    //For scoring
    public HashMap<Integer, Card> bordMap = new HashMap<>();

    //******************************************************************************************************************
    //Getter and Setters
    public String getBordType() {
        return bordType;
    }

    public void setBordType(String bordType) {
        this.bordType = bordType;
    }

    public ClientHandler getFirstPlayer() {
        return firstPlayer;
    }

    public ClientHandler getNextFirstPlayer() {
        return nextFirstPlayer;
    }

    public void setNextFirstPlayer(ClientHandler firstPlayer) {
        nextFirstPlayer = firstPlayer;
    }

    public ArrayList<Card> getBordCards() {
        return bordCards;
    }

    public HashMap<Integer, Card> getBordMap() {
        return bordMap;
    }

    public Round getRound() {
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
    public Set(Round round, ClientHandler firstPlayer) {
        this.round = round;
        this.firstPlayer = firstPlayer;
    }
    //******************************************************************************************************************
    //Starting set method
    public void startSet() {
        puttingCard();
        scoring();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        cleaningBord();
        setNextFirstPlayer(round.getGame().roomPlayers.get(winner()));
        setIsSetFinished();
    }
    //******************************************************************************************************************
    //Main methods
    public void puttingCard() {
        round.getGame().roomPlayers.get(firstPlayer.getPlayerIndex()).sendMessage("YOUR TURN." + "FREE");
        waitForPlayerCardSelection();
        round.getGame().roomPlayers.get(firstPlayer.getPlayerIndex()).sendMessage("NOT TURN.");
        isPlayerSelected = false;
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex() + 1) % 4).sendMessage("YOUR TURN." + bordType);
        waitForPlayerCardSelection();
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex() + 1) % 4).sendMessage("NOT TURN.");
        isPlayerSelected = false;
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex() + 2) % 4).sendMessage("YOUR TURN." + bordType);
        waitForPlayerCardSelection();
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex() + 2) % 4).sendMessage("NOT TURN.");
        isPlayerSelected = false;
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex() + 3) % 4).sendMessage("YOUR TURN." + bordType);
        waitForPlayerCardSelection();
        round.getGame().roomPlayers.get((firstPlayer.getPlayerIndex() + 3) % 4).sendMessage("NOT TURN.");
        isPlayerSelected = false;
    }
    public void updateBordCards(String putCard, int puterIndex) {
        round.getGame().roomPlayers.get(puterIndex).sendMessage("NOT TURN.");
        round.getGame().roomPlayers.get(puterIndex).sendMessage("YOUR CARD:" + putCard);
        round.getGame().roomPlayers.get((puterIndex + 1) % 4).sendMessage("LEFT CARD:" + putCard);
        round.getGame().roomPlayers.get((puterIndex + 2) % 4).sendMessage("FRONT CARD:" + putCard);
        round.getGame().roomPlayers.get((puterIndex + 3) % 4).sendMessage("RIGHT CARD:" + putCard);
    }
    public void scoring() {
        int x = winner();
        switch (x) {
            case 0:
            case 2:
                if (getRound().getGame().roomTeams.get(0).getTeamWinedSets() == 7 && (getRound().getGame().roomTeams.get(1).getTeamWinedSets() == 0)) {
                    if ((getRound().getRuler() == getRound().getGame().roomTeams.get(0).p1) || (getRound().getRuler() == getRound().getGame().roomTeams.get(0).p2)) {
                        for (int i = 0; i < 3; i++) {
                            getRound().getGame().roomTeams.get(0).addTeamWinedSets();
                            getRound().getGame().roomPlayers.get(0).sendMessage("YOU WINED THE SET.");
                            getRound().getGame().roomPlayers.get(2).sendMessage("YOU WINED THE SET.");
                            getRound().getGame().roomPlayers.get(1).sendMessage("YOU LOST THE SET.");
                            getRound().getGame().roomPlayers.get(3).sendMessage("YOU LOST THE SET.");
                        }
                    }else {
                        for (int i = 0; i <2 ; i++) {
                            getRound().getGame().roomTeams.get(0).addTeamWinedSets();
                            getRound().getGame().roomPlayers.get(0).sendMessage("YOU WINED THE SET.");
                            getRound().getGame().roomPlayers.get(2).sendMessage("YOU WINED THE SET.");
                            getRound().getGame().roomPlayers.get(1).sendMessage("YOU LOST THE SET.");
                            getRound().getGame().roomPlayers.get(3).sendMessage("YOU LOST THE SET.");
                        }
                    }
                }else {
                    getRound().getGame().roomTeams.get(0).addTeamWinedSets();
                    getRound().getGame().roomPlayers.get(0).sendMessage("YOU WINED THE SET.");
                    getRound().getGame().roomPlayers.get(2).sendMessage("YOU WINED THE SET.");
                    getRound().getGame().roomPlayers.get(1).sendMessage("YOU LOST THE SET.");
                    getRound().getGame().roomPlayers.get(3).sendMessage("YOU LOST THE SET.");
                }
                break;
            case 1:
            case 3:
                if (getRound().getGame().roomTeams.get(1).getTeamWinedSets() == 7 && (getRound().getGame().roomTeams.get(0).getTeamWinedSets() == 0)) {
                    if ((getRound().getRuler() == getRound().getGame().roomTeams.get(1).p1) || (getRound().getRuler() == getRound().getGame().roomTeams.get(1).p2)) {
                        for (int i = 0; i < 3; i++) {
                            getRound().getGame().roomTeams.get(1).addTeamWinedSets();
                            getRound().getGame().roomPlayers.get(1).sendMessage("YOU WINED THE SET.");
                            getRound().getGame().roomPlayers.get(3).sendMessage("YOU WINED THE SET.");
                            getRound().getGame().roomPlayers.get(0).sendMessage("YOU LOST THE SET.");
                            getRound().getGame().roomPlayers.get(2).sendMessage("YOU LOST THE SET.");
                        }
                    } else {
                        for (int i = 0; i < 2; i++) {
                            getRound().getGame().roomTeams.get(1).addTeamWinedSets();
                            getRound().getGame().roomPlayers.get(1).sendMessage("YOU WINED THE SET.");
                            getRound().getGame().roomPlayers.get(3).sendMessage("YOU WINED THE SET.");
                            getRound().getGame().roomPlayers.get(0).sendMessage("YOU LOST THE SET.");
                            getRound().getGame().roomPlayers.get(2).sendMessage("YOU LOST THE SET.");
                        }
                    }
                } else {
                    getRound().getGame().roomTeams.get(1).addTeamWinedSets();
                    getRound().getGame().roomPlayers.get(1).sendMessage("YOU WINED THE SET.");
                    getRound().getGame().roomPlayers.get(3).sendMessage("YOU WINED THE SET.");
                    getRound().getGame().roomPlayers.get(0).sendMessage("YOU LOST THE SET.");
                    getRound().getGame().roomPlayers.get(2).sendMessage("YOU LOST THE SET.");
                }
                break;
            default:
                System.out.println("ERROR in team scoring!");
        }
        switch (x) {
            case 0:
                getRound().getGame().roomPlayers.get(0).addToPlayerWinedSets();
                break;
            case 2:
                getRound().getGame().roomPlayers.get(2).addToPlayerWinedSets();
                break;
            case 1:
                getRound().getGame().roomPlayers.get(1).addToPlayerWinedSets();
                break;
            case 3:
                getRound().getGame().roomPlayers.get(3).addToPlayerWinedSets();
                break;
            default:
                System.out.println("ERROR in player scoring!" + x);
        }
    }
    public void cleaningBord() {
        for (int i = 0; i < 4; i++) {
            getRound().getGame().roomPlayers.get(i).sendMessage("CLEANING BORD.");
        }
    }
    //******************************************************************************************************************
    //Helping methods
    public int winner() {
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
        // if no one has the Rul card, check for the firstPlayer's card type
        if (winner == -1) {
            max = 0;
            for (Map.Entry<Integer, Card> entry : bordMap.entrySet()) {
                int indexOfPerson = entry.getKey();
                if (entry.getValue().getType().equals(bordType)) {
                    // if someone has the same type as the firstPlayer's card, check if they have the highest number
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
    private void waitForPlayerCardSelection() {
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
