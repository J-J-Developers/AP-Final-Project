package GamePlay;

import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
//        myHandPanelSetting();
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
        // باید با کد buttons.getLast به اخرین کارتی که دست بازیکن اومده دسترسی پیدا کنین
        // بعدش کار های گرافیکی رو مثل سایز و جانمایی و... بر کارت انجام بدین
        // و در نهایت به پنل myHand افزوده میشود
        myHand.add(buttons.getLast());
        myHand.revalidate();
        myHand.repaint();
    }
    public static JPanel myHandPanelSetting(){
        // این قسمت هم برای انجام کار های گرافیکی بر پنل دست بازیکن
        // این متد در کانستراکتور فراخوانی میشود
        // هر بار که یک player ایجاد شود یک panel با ویژگی های گرافیکی تعریف شده براش ساخته میشود
        // کارت ها هم با متد بالایی به پنل دست بازیکن افزوده میشود

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        GamePlay.GamePage mainPanel = new GamePlay.GamePage();
        mainPanel.setBounds(0, 0, screenSize.width, 900);

        JPanel myHandPanel = new JPanel();
        myHandPanel.setBackground(new Color(119, 62, 62));
        myHandPanel.setBounds(0, 650, screenSize.width, 700);
        Dimension buttonSize = new Dimension(100, 120);




        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(1070, 400));
        centerPanel.setBounds(190, 120, 1070, 400);
        centerPanel.setBackground(new Color(50, 87, 80));
        centerPanel.setVisible(true);

        for(int i = 1; i <= 13; ++i) {

            JButton button = new JButton("Button " + i);
            button.setPreferredSize(buttonSize);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    myHandPanel.remove(button);

                    String text = button.getText();

                    JButton button = new JButton(text);
                    button.setBounds(480,270,90,90);




                    centerPanel.add(button);
                    myHandPanel.repaint();
                    mainPanel.repaint();
                }
            });
            myHandPanel.add(button);
        }
        mainPanel.add(centerPanel);
        mainPanel.add(myHandPanel);





        return mainPanel;
    }

    public JButton cardButton(String btnName, ActionListener actionListener){
        JButton btn = new JButton(btnName);
        btn.addActionListener(actionListener);
        return btn;
    }


    //سیستم پیشنهادی بازی
        /*private JButton recommendCard(){
        //الگوریتم سیستم پیشنهادی که نوشته شد این بخش پر میشه
            //این متد کارت پیشنهادی را بر میگرداند
            //بعدش حاشیه کارت مورد نظر سبز رنگ میشود که اون کاربر خنگ کارتشو بندازه
            return buttons.get(i);
        }*/

}

