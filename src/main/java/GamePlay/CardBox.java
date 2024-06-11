package GamePlay;

import java.util.ArrayList;
import java.util.List;

public class CardBox {
    public List<Card> cards = new ArrayList<>();
    public final String poshtPath = "poshtPath";
    public CardBox(){
        initializeFullDeck();
    }

    // متد برای پر کردن جعبه کارت با 52 کارت
    public void initializeFullDeck() {
        // اضافه کردن کارت‌های قلب
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Hearts", "roopath" + i + "hearts", "poshtPath"));
        }
        // اضافه کردن کارت‌های خشت
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Diamonds", "roopath" + i + "diamonds", "poshtPath"));
        }
        // اضافه کردن کارت‌های گشنیز
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Clubs", "roopath" + i + "clubs", "poshtPath"));
        }
        // اضافه کردن کارت‌های پیک
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Spades", "roopath" + i + "spades", "poshtPath"));
        }
    }


    public List<Card> getCards() {
        return cards;
    }


}

