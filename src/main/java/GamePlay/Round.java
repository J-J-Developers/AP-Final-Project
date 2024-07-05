package GamePlay;

import Server.Server.ClientHandler;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Round  {
    //Attributes
    private static Game game;
    private int roundNumber;
    private static CardBox cardBox = new CardBox();
    private ClientHandler ruler;
    private ClientHandler nextRuler;
    private String rulType = "Heart";
    Random rand = new Random();
    Gson gson = new Gson();
    private final Object lock = new Object();
    private boolean isRulerCardSelected = false;
    public static ArrayList<Card> roundCards = new ArrayList<>(getCardBox().cards);
    public ArrayList<Set> gameSets = new ArrayList<>();
    //******************************************************************************************************************
    //Getter and Setters

    public static Game getGame() {
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

    public ClientHandler getRuler() {
        return ruler;
    }

    public void setRuler(ClientHandler ruler) {
        this.ruler = ruler;
    }

    public ClientHandler getNextRuler() {
        return nextRuler;
    }

    public void setNextRuler(ClientHandler nextRuler) {
        this.nextRuler = nextRuler;
    }

    public boolean isRulerCardSelected() {
        return isRulerCardSelected;
    }

    public void setRulerCardSelected(boolean rulerCardSelected) {
        isRulerCardSelected = rulerCardSelected;
    }

    public static ArrayList<Card> getRoomCards() {
        return roundCards;
    }

    public ArrayList<Set> getGameSets() {
        return gameSets;
    }
    //******************************************************************************************************************
    //Constructor
    public Round(Game game,int roundNumber,ClientHandler ruler){
        this.game = game;
        this.roundNumber = roundNumber;
        this.ruler = ruler;
    }
    //******************************************************************************************************************
    //Starting round method
    public void startRound(){
        CardDividing();
        int setNumber = gameSets.size() + 1;
        Set set = new Set(this,setNumber,ruler);
        gameSets.add(set);
        Set.startSet();
        while (game.roomTeams.get(0).getTeamWinedSets() < 7 && game.roomTeams.get(1).getTeamWinedSets() < 7) {
            Set newSet = new Set(this,setNumber+1,gameSets.getLast().getNextFirstPlayer());
            gameSets.add(newSet);
            newSet.startSet();
            setNumber ++;
        }
        if (game.roomTeams.get(0).getTeamWinedSets()==7){
            game.roomTeams.get(0).addWinedRounds();
            game.roomTeams.get(0).p1.sendMessage("YOU WINED THE ROUND.");
            game.roomTeams.get(0).p2.sendMessage("YOU WINED THE ROUND.");
        }else{
            game.roomTeams.get(1).addWinedRounds();
            game.roomTeams.get(1).p1.sendMessage("YOU WINED THE ROUND.");
            game.roomTeams.get(1).p2.sendMessage("YOU WINED THE ROUND.");
        }
        setNextRuler(whoIsNextRuler());
        //0 کردن ست بازیکنان برای راند بعدی بازی

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
    public static ClientHandler whoIsNextRuler(){
        ClientHandler nextRuler = getGame().roomPlayers.get(0);
        for (int i = 1; i < 4; i++) {
            if (getGame().roomPlayers.get(i).getPlayerWinedSets() > nextRuler.getPlayerWinedSets() ){
                nextRuler = getGame().roomPlayers.get(i);
            }
        }
        return nextRuler;
    }
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
}
