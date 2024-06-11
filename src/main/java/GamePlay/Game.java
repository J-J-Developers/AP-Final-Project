package GamePlay;

import java.util.ArrayList;

public class Game {
    private String token;
    private CardBox cardBox;
    private int round;
    public Player king;
    public Card hokm;
    public ArrayList<Team> roomTeams = new ArrayList<>(2);
    public ArrayList<Card> roomCards = new ArrayList<>(getCardBox().cards);

    // Constructor
    public Game(Player p1, Player p2, Player p3, Player p4) {
        this.cardBox = new CardBox(); // ایجاد یک نمونه جدید از CardBox
        // ... سایر کدهای مورد نیاز برای تکمیل کنستراکتور
    }

    // Getter method for CardBox
    public CardBox getCardBox() {
        return this.cardBox;
    }



    // ... سایر متدها و کدهای مورد نیاز
}
