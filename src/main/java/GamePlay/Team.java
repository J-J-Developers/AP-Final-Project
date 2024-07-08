package GamePlay;

import Server.Server;
import Server.Server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public List<ClientHandler> teamMembers ;
    public ClientHandler p1;
    public ClientHandler p2;
    private int teamWinedRounds;
    private int teamWinedSets;

    public Team(ClientHandler p1,ClientHandler p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.teamWinedRounds = 0;
        this.teamWinedSets = 0;
        teamMembers = new ArrayList<>();
        teamMembers.add(p1);
        teamMembers.add(p2);
    }

    public void addWinedRounds(){
        this.teamWinedRounds ++;
    }

    public int getTeamWinedRounds() {
        return teamWinedRounds;
    }

    public void addTeamWinedSets() {
        this.teamWinedSets ++;
    }

    public int getTeamWinedSets() {
        return teamWinedSets;
    }
    public void teamZeroing(){
        teamWinedSets = 0;
    }

}

