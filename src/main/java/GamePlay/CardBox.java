package GamePlay;

import java.util.ArrayList;
import java.util.List;

public class CardBox {
    public List<Card> cards = new ArrayList<>();
    public CardBox(){
        initializeFullDeck();
    }

    // متد برای پر کردن جعبه کارت با 52 کارت
    public void initializeFullDeck() {
        // اضافه کردن کارت‌های قلب
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Hearts", "C:\\Users\\IR-TECH\\Desktop\\Hokm\\QD.png"));
        }
        // اضافه کردن کارت‌های خشت
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Diamonds", "C:\\Users\\IR-TECH\\Desktop\\Hokm\\QG.png"));
        }
        // اضافه کردن کارت‌های گشنیز
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Clubs", "C:\\Users\\IR-TECH\\Desktop\\Hokm\\QG.png"));
        }
        // اضافه کردن کارت‌های پیک
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Spades", "C:\\Users\\IR-TECH\\Desktop\\Hokm\\QG.png"));
        }
    }

    public List<Card> getCards() {
        return cards;
    }


}

