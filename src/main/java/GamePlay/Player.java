package GamePlay;

import Client.Client;

import javax.swing.*;
import java.util.ArrayList;

public class Player extends Client {
    private int winSets;
    private boolean turn;
    private ArrayList<Card> myCards ;
    private ArrayList<JButton> buttons ;
    public Player(String name,String id){
        super(name,id);
        this.winSets = 0;
        this.turn = false;
        this.myCards = new ArrayList<>(13);
        this.buttons = new ArrayList<>(13);
    }

    public ArrayList<Card> getMyCards() {
        return myCards;
    }

    public int getWinSets() {
        return winSets;
    }
    public boolean isTurn(){
        return turn;
    }

    public void changeTurn() {
        this.turn = ! this.turn;
    }

    public void addWinSets() {
        this.winSets ++;
    }

    public void putCard(String path){
        for (int i = 0; i < myCards.size(); i++) {
            if (myCards.get(i).getPoshtPath().equalsIgnoreCase(path)){
                //فراخوانی متد ارسال پیام اینکه چی زده
                // پاک شدن عکس کارت از صفحه بازی
                // حذف کارت از اری لیست
                myCards.remove(i);
                break;
            }
        }
    }

    //وظیفه این متد فقط فقط ساخت دکمه که همون کارت بازیکنا هستش
    //یه متد میخوایم برای اینکه هر کارتی که  اندخته شد setvisible اون دکمه false بشه
        public void showCards(JPanel panel) {
            // ایجاد و اضافه کردن دکمه‌ها به لیست و پنل
            for (int i = 0; i < myCards.size(); i++) {
                JButton button = new JButton();
                button.setIcon(new ImageIcon(myCards.get(i).getRoo().getImage()));
                buttons.add(button); // اضافه کردن دکمه به لیست دکمه‌ها
                panel.add(button); // اضافه کردن دکمه به پنل
            }
            panel.revalidate();
            panel.repaint();
        }
        //سیستم پیشنهادی بازی
        /*private JButton recommendCard(){
        //الگوریتم سیستم پیشنهادی که نوشته شد این بخش پر میشه
            //این متد کارت پیشنهادی را بر میگرداند
            //بعدش حاشیه کارت مورد نظر سبز رنگ میشود که اون کاربر خنگ کارتشو بندازه
            return buttons.get(i);
        }*/

}
