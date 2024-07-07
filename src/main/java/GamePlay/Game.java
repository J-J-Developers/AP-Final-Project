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

    //Attributes
    public List<ClientHandler> roomPlayers;
    public ArrayList<Team> roomTeams = new ArrayList<>(2);
    public ArrayList<Round> gameRounds = new ArrayList<>();
    Random rand = new Random();
    //******************************************************************************************************************
    //Getter and Setters
    public ArrayList<Round> getGameRounds() {
        return gameRounds;
    }

    //******************************************************************************************************************
    // Constructor
    public Game(List<ClientHandler> GameMembers) {
        this.roomPlayers = GameMembers;
        roomTeams.add(new Team(roomPlayers.get(0), roomPlayers.get(2)));
        roomTeams.add(new Team(roomPlayers.get(1), roomPlayers.get(3)));
        initializingNames();
    }
    //******************************************************************************************************************
    //Starting game methods
    public void startMatch() {
        int roundNumber = gameRounds.size() + 1;
        Round round = new Round(this, roundNumber,roomPlayers.get(rand.nextInt(roomPlayers.size())));
        gameRounds.add(round);
        round.startRound();
        while (true){
            while ((roomTeams.get(0).getTeamWinedRounds() < 7) && (roomTeams.get(1).getTeamWinedRounds() < 7) && (gameRounds.getLast().isIsRoundFinished())) {
            Round newRound = new Round(this, roundNumber,gameRounds.getLast().getNextRuler());
            gameRounds.add(newRound);
            newRound.startRound();
            roundNumber++;
            }
            if ((roomTeams.get(0).getTeamWinedRounds()== 7) || (roomTeams.get(1).getTeamWinedRounds() == 7))
                break;
        }
        if (roomTeams.get(0).getTeamWinedRounds()==7){
            roomTeams.get(0).p1.sendMessage("YOU WINED THE GAME.");
            roomTeams.get(0).p2.sendMessage("YOU WINED THE GAME.");
        }else{
            roomTeams.get(1).p1.sendMessage("YOU WINED THE GAME.");
            roomTeams.get(1).p2.sendMessage("YOU WINED THE GAME.");
        }
    }
    //******************************************************************************************************************
    //Main methods
    public void initializingNames() {
        for (int i = 0; i < 4; i++) {
            roomPlayers.get(i).sendMessage("YOUR NAME:" + roomPlayers.get(i).getNickname());
            roomPlayers.get(i).sendMessage("LEFT NAME:" + roomPlayers.get((i + 1) % 4).getNickname());
            roomPlayers.get(i).sendMessage("FRONT NAME:" + roomPlayers.get((i + 2) % 4).getNickname());
            roomPlayers.get(i).sendMessage("RIGHT NAME:" + roomPlayers.get((i + 3) % 4).getNickname());
        }
    }
}