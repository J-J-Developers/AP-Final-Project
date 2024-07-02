package GamePlay;

import Server.Server;
import Server.Server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public List<ClientHandler> teamMembers ;
    public ClientHandler p1;
    public ClientHandler p2;
    public int winedRounds;
    public Team(ClientHandler p1,ClientHandler p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.winedRounds = 0;
        teamMembers = new ArrayList<>();
        teamMembers.add(p1);
        teamMembers.add(p2);
    }

    public void addPoint(){
        this.winedRounds ++;
    }

    public int getWinedRounds() {
        return winedRounds;
    }
}

