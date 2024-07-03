package GamePlay;

import Server.Server;
import Server.Server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public List<ClientHandler> teamMembers ;
    public ClientHandler p1;
    public ClientHandler p2;
    private int winedRounds;
    private int winedSets;

    public Team(ClientHandler p1,ClientHandler p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.winedRounds = 0;
        this.winedSets = 0;
        teamMembers = new ArrayList<>();
        teamMembers.add(p1);
        teamMembers.add(p2);
    }

    public void addWinedRounds(){
        this.winedRounds ++;
    }

    public int getWinedRounds() {
        return winedRounds;
    }

    public void addWinedSets() {
        this.winedSets ++;
    }

    public int getWinedSets() {
        return winedSets;
    }

}

