package GamePlay;

import java.util.ArrayList;

public class Team {
    public ArrayList<Player> teamMembers;
    public Player p1;
    public Player p2;
    public int winedRounds;
    public Team(Player p1,Player p2){
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
}
