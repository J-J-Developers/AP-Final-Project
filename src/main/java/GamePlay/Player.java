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
    JPanel centerPanel;
    GamePlay.GamePage mainPanel;
    private ArrayList<Card> myCards ;
    private ArrayList<JButton> buttons ;
    public Player(String name,String id){
        super(name,id);
        this.winSets = 0;
        this.turn = false;
        this.myHand = new JPanel();
        this.centerPanel = new JPanel();
        this.mainPanel = new GamePlay.GamePage();
//        myHandPanelSetting();
        this.myCards = new ArrayList<>(13);
        this.buttons = new ArrayList<>(13);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        mainPanel.setBounds(0, 0, screenSize.width, 900);


        myHand.setBackground(new Color(119, 62, 62));
        myHand.setBounds(0, 650, screenSize.width, 700);


        centerPanel.setPreferredSize(new Dimension(1070, 400));
        centerPanel.setBounds(190, 120, 1070, 400);
        centerPanel.setBackground(new Color(50, 87, 80));
        centerPanel.setVisible(true);

        mainPanel.add(centerPanel);
        mainPanel.add(myHand);
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


    public void showHandCards() {
        // باید با کد buttons.getLast به اخرین کارتی که دست بازیکن اومده دسترسی پیدا کنین
        // بعدش کار های گرافیکی رو مثل سایز و جانمایی و... بر کارت انجام بدین
        // و در نهایت به پنل myHand افزوده میشود
        Dimension buttonSize = new Dimension(100, 120);
        buttons.getLast().setPreferredSize(buttonSize);
        buttons.getLast().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myHand.remove(buttons.getLast());

                String text = buttons.getLast().getText();

                JButton button = new JButton(text);
                button.setBounds(480,270,90,90);




                centerPanel.add(button);
                myHand.repaint();
                mainPanel.repaint();
            }
        });
        myHand.add(buttons.getLast());
    }

    }
    /*public void myHandPanelSetting() {
        // این قسمت هم برای انجام کار های گرافیکی بر پنل دست بازیکن
        // این متد در کانستراکتور فراخوانی میشود
        // هر بار که یک player ایجاد شود یک panel با ویژگی های گرافیکی تعریف شده براش ساخته میشود
        // کارت ها هم با متد بالایی به پنل دست بازیکن افزوده می
    }

    //سیستم پیشنهادی بازی
        /*private JButton recommendCard(){
        //الگوریتم سیستم پیشنهادی که نوشته شد این بخش پر میشه
            //این متد کارت پیشنهادی را بر میگرداند
            //بعدش حاشیه کارت مورد نظر سبز رنگ میشود که اون کاربر خنگ کارتشو بندازه
            return buttons.get(i);
        }*/
/*/
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
 */


