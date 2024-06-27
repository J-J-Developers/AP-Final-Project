package GamePlay;

import java.util.ArrayList;
import java.util.List;

public class CardBox {
    public List<Card> cards = new ArrayList<>();
    final static String basicDirectory = "C:\\Program Files\\AP-Final-Project\\src\\main\\java\\Images\\";
    public CardBox(){
        initializeFullDeck();
    }

    // متد برای پر کردن جعبه کارت با 52 کارت
    public void initializeFullDeck() {
        // اضافه کردن کارت‌های قلب
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Heart", basicDirectory + i + "Heart.jpg"));
        }
        // اضافه کردن کارت‌های خشت
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Diamond", basicDirectory + i + "Diamond.jpg"));
        }
        // اضافه کردن کارت‌های گشنیز
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Club", basicDirectory + i + "Club.jpg"));
        }
        // اضافه کردن کارت‌های پیک
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(i, "Spade",basicDirectory + i + "Spade.jpg" ));
        }
    }
    public List<Card> getCards() {
        return cards;
    }


}

