package GamePlay;

import Client.Client;

import javax.swing.*;
import java.util.ArrayList;

public class Player extends Client {
    private int winSets;
    private boolean turn;
    private JPanel myHand;
    private ArrayList<Card> myCards ;
    private ArrayList<JButton> buttons ;
    public Player(String name,String id){
        super(name,id);
        this.winSets = 0;
        this.turn = false;
        myHand = new JPanel();
        this.myCards = new ArrayList<>(13);
        this.buttons = new ArrayList<>(13);
    }

    public ArrayList<Card> getMyCards() {
        return myCards;
    }

    public ArrayList<JButton> getMyButtons() {
        return buttons;
    }

    public JPanel getMyHand() {
        return myHand;
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

    public String putCard(String rooPath){
        String output = "";
        for (int i = 0; i < myCards.size(); i++) {
            if (myCards.get(i).getPoshtPath().equalsIgnoreCase(rooPath)){
                //فراخوانی متد ارسال پیام اینکه چی زده
                // پاک شدن عکس کارت از صفحه بازی
                // حذف کارت از اری لیست
                output += myCards.get(i).getType();
                myCards.remove(i);
                break;
            }
        }
        return output;
    }

        public void showHandCards() {
            for (int i = 0; i < myCards.size(); i++) {
                myHand.add(getMyButtons().get(i));
                // این متد با همفکری کامل خواهد شد...
            }
            myHand.revalidate();
            myHand.repaint();
        }


        //سیستم پیشنهادی بازی
        /*private JButton recommendCard(){
        //الگوریتم سیستم پیشنهادی که نوشته شد این بخش پر میشه
            //این متد کارت پیشنهادی را بر میگرداند
            //بعدش حاشیه کارت مورد نظر سبز رنگ میشود که اون کاربر خنگ کارتشو بندازه
            return buttons.get(i);
        }*/

}
