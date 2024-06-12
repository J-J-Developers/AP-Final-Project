package GamePlay;

import java.util.ArrayList;
import java.util.Random;

public class Game extends Swing{
    private String token;
    private CardBox cardBox;
    private int round;
    public Player king;
    public Card hokm;
    Random rand = new Random();
    public ArrayList<Team> roomTeams = new ArrayList<>(2);
    public ArrayList<Card> roomCards = new ArrayList<>(getCardBox().cards);
    public ArrayList<Player> roomPlayers = new ArrayList<>();
    //این اری لیستو فقط برای پخش کارت زدم نه ارتباط با سرور!!
    // Constructor
    public Game(Player p1, Player p2, Player p3, Player p4) {
        roomPlayers.add(new Player(p1.getName(),p1.getId()));
        roomPlayers.add(new Player(p2.getName(),p2.getId()));
        roomPlayers.add(new Player(p3.getName(),p3.getId()));
        roomPlayers.add(new Player(p4.getName(),p4.getId()));
        roomTeams.add(new Team(p1,p3));
        roomTeams.add(new Team(p2,p4));
        this.cardBox = new CardBox();
    }

    // Getter method for CardBox
    public CardBox getCardBox() {
        return this.cardBox;
    }
    public void divideCards(){
        while (!roomCards.isEmpty()){
            for (int i=0 ; i < 4 ; i++){
                for (int j = 0; j < 5; j++) {
                  roomPlayers.get(i).getMyCards().add(roomCards.get(rand.nextInt(roomCards.size())));
                }
            }
            //ایجاد وقفه برای تعیین حکم
            for (int i=0 ; i < 4 ; i++){
                for (int j = 0; j < 4; j++) {
                    roomPlayers.get(i).getMyCards().add(roomCards.get(rand.nextInt(roomCards.size())));
                }
            }
            for (int i=0 ; i < 4 ; i++){
                for (int j = 0; j < 4; j++) {
                    roomPlayers.get(i).getMyCards().add(roomCards.get(rand.nextInt(roomCards.size())));
                }
            }
        }
    }
}
