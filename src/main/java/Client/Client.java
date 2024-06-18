package Client;

import GamePlay.Card;
import GamePlay.GamePage;
import GamePlay.Token;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    // آدرس سرور چت
    private static final String SERVER_ADDRESS = "127.0.0.1";
    // پورتی که سرور چت بر روی آن گوش می‌دهد
    private static final int SERVER_PORT = 1234;

    private String name;
    private String id;
    private JPanel myHand;
    JPanel centerPanel;
    GamePlay.GamePage mainPanel;
    private ArrayList<Card> myCards ;
    private ArrayList<JButton> buttons ;

    public GamePage getMainPanel() {
        return mainPanel;
    }

    public Client(String name, String id){
        this.name = name;
        this.id = id;
        this.myCards = new ArrayList<>(13);
        this.buttons = new ArrayList<>(13);
        this.myHand = new JPanel();
        this.centerPanel = new JPanel();
        this.mainPanel = new GamePlay.GamePage();
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
        initializeUI();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    public JPanel getMyHand() {
        return myHand;
    }
    public ArrayList<Card> getMyCards() {
        return myCards;
    }

    public ArrayList<JButton> getMyButtons() {
        return buttons;
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

    public static void main(String[] args) throws Exception {
        new Client(" " , " ").startClient();
    }

    // متد startClient برای شروع اتصال به سرور چت
    public void startClient() throws Exception {
        // ایجاد یک سوکت برای اتصال به سرور
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        // برای خواندن پیام‌های دریافتی از سرور
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // برای نوشتن پیام‌ها به سرور
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        // برای خواندن ورودی از کاربر
        Scanner scanner = new Scanner(System.in);
        System.out.println("Connected to chat server");

        // ایجاد یک رشته جدید برای خواندن ورودی کاربر و ارسال آن به سرور
        new Thread(() -> {
            while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());
            }
        }).start();

        // خواندن پیام‌های دریافتی از سرور و چاپ آن‌ها
        String message;
        while ((message = in.readLine()) != null) {
            System.out.println(message);
        }
    }
    public void initializeUI(){
        final Token TOKEN = new Token();
        final JFrame frame = new JFrame("Hokm");
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout((LayoutManager)null);
        frame.getContentPane();
        Color customColor = new Color(97, 150, 134);
        final Color customColor1 = new Color(50, 87, 80);
        frame.getContentPane().setBackground(customColor);
        final JButton btn1 = new JButton("Random");
        final JButton btn2 = new JButton("Friends");
        btn1.setBounds(625, 250, 150, 70);
        btn2.setBounds(625, 400, 150, 70);
        btn1.setBackground(customColor1);
        btn1.setOpaque(true);
        btn1.setForeground(new Color(131, 75, 166));
        btn2.setBackground(customColor1);
        btn2.setOpaque(true);
        btn2.setForeground(new Color(131, 75, 166));
        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(btn1);
                frame.remove(btn2);
                frame.setTitle("Game Room");
                final JLabel lbl2 = new JLabel("Nickname:");
                final JTextField txt2 = new JTextField();
                final JButton btn5 = new JButton("Go");
                final JButton exit1 = new JButton("Back");
                btn5.setBackground(customColor1);
                btn5.setOpaque(true);
                btn5.setForeground(new Color(131, 75, 166));
                exit1.setBackground(customColor1);
                exit1.setOpaque(true);
                exit1.setForeground(new Color(131, 75, 166));
                lbl2.setBounds(520, 300, 110, 50);
                txt2.setBounds(650, 300, 250, 50);
                btn5.setBounds(800, 400, 90, 50);
                exit1.setBounds(670, 400, 90, 50);
                exit1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(btn5);
                        frame.remove(lbl2);
                        frame.remove(txt2);
                        frame.remove(exit1);
                        frame.add(btn1);
                        frame.add(btn2);
                        frame.revalidate();
                        frame.repaint();
                    }
                });
                btn5.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String text1 = txt2.getText();
                        if (!text1.isEmpty()) {
                            frame.remove(btn5);
                            frame.remove(lbl2);
                            frame.remove(txt2);
                            frame.remove(exit1);
                           frame.add(getMainPanel());

                            frame.revalidate();
                            frame.repaint();
                        } else {
                            JOptionPane.showMessageDialog((Component)null, "please sure you write the nickname", "Error", 2);
                        }

                    }
                });
                frame.add(lbl2);
                frame.add(txt2);
                frame.add(btn5);
                frame.add(exit1);
                frame.setVisible(true);
                frame.revalidate();
                frame.repaint();
            }
        });
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(btn1);
                frame.remove(btn2);
                frame.setTitle("Friendly Game");
                final JButton btn3 = new JButton("Join");
                final JButton btn4 = new JButton("Create");
                final JButton exit = new JButton("Back");
                btn3.setBounds(625, 200, 150, 70);
                btn4.setBounds(625, 300, 150, 70);
                exit.setBounds(625, 400, 150, 70);
                btn3.setBackground(customColor1);
                btn3.setOpaque(true);
                btn3.setForeground(new Color(131, 75, 166));
                btn4.setBackground(customColor1);
                btn4.setOpaque(true);
                btn4.setForeground(new Color(131, 75, 166));
                exit.setBackground(customColor1);
                exit.setOpaque(true);
                btn3.setOpaque(true);
                btn3.setForeground(new Color(131, 75, 166));
                btn4.setOpaque(true);
                btn4.setForeground(new Color(131, 75, 166));
                exit.setOpaque(true);
                exit.setForeground(new Color(131, 75, 166));
                frame.add(exit);
                frame.add(btn3);
                frame.add(btn4);
                btn3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(exit);
                        frame.remove(btn3);
                        frame.remove(btn4);
                        frame.setTitle("Join By Token");
                        final JLabel lbl1 = new JLabel("GamePlay.Token:");
                        final JTextField txt1 = new JTextField();
                        final JLabel lbl2 = new JLabel("Nickname:");
                        final JTextField txt2 = new JTextField();
                        final JButton btn5 = new JButton("Go");
                        final JButton exit1 = new JButton("Back");
                        btn5.setBackground(customColor1);
                        btn5.setOpaque(true);
                        btn5.setForeground(new Color(131, 75, 166));
                        exit1.setBackground(customColor1);
                        exit1.setOpaque(true);
                        exit1.setForeground(new Color(131, 75, 166));
                        lbl2.setBounds(520, 200, 110, 50);
                        txt2.setBounds(650, 200, 250, 50);
                        lbl1.setBounds(520, 300, 110, 50);
                        txt1.setBounds(650, 300, 250, 50);
                        btn5.setBounds(800, 400, 90, 50);
                        exit1.setBounds(670, 400, 90, 50);
                        btn5.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String text1 = txt2.getText();
                                String text = txt1.getText();
                                if (!text.isEmpty() && !text1.isEmpty()) {
                                    frame.remove(lbl1);
                                    frame.remove(txt1);
                                    frame.remove(btn5);
                                    frame.remove(lbl2);
                                    frame.remove(txt2);
                                    frame.remove(exit1);
                                    frame.setTitle("Game Room");


                                    frame.add(getMainPanel());
                                    frame.revalidate();
                                    frame.repaint();
                                } else {
                                    JOptionPane.showMessageDialog((Component)null, "please sure you write the nickname and token", "Error", 2);
                                }

                            }
                        });
                        exit1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                frame.remove(lbl1);
                                frame.remove(txt1);
                                frame.remove(btn5);
                                frame.remove(lbl2);
                                frame.remove(txt2);
                                frame.remove(exit1);
                                frame.add(btn3);
                                frame.add(btn4);
                                frame.add(exit);
                                frame.revalidate();
                                frame.repaint();
                            }
                        });
                        frame.add(lbl2);
                        frame.add(txt2);
                        frame.add(lbl1);
                        frame.add(txt1);
                        frame.add(btn5);
                        frame.add(exit1);
                        frame.revalidate();
                        frame.repaint();
                    }
                });
                btn4.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(btn3);
                        frame.remove(btn4);
                        frame.remove(exit);
                        frame.setTitle("Game Room");
                        Token TOKEN = new Token();

                        final JLabel lbl2 = new JLabel("Nickname:");
                        final JLabel lbl1 = new JLabel("Token:");
                        final JTextField txt1 = new JTextField(String.valueOf(TOKEN));
                        final JTextField txt2 = new JTextField();
                        final JButton btn5 = new JButton("Go");
                        final JButton exit1 = new JButton("Back");

                        txt1.setEditable(false);

                        btn5.setBackground(customColor1);
                        btn5.setOpaque(true);
                        btn5.setForeground(new Color(131, 75, 166));
                        exit1.setBackground(customColor1);
                        exit1.setOpaque(true);
                        exit1.setForeground(new Color(131, 75, 166));
                        lbl2.setBounds(520, 300, 110, 50);
                        txt2.setBounds(650, 300, 250, 50);
                        lbl1.setBounds(520, 200, 110, 50);
                        txt1.setBounds(650, 200, 250, 50);
                        btn5.setBounds(800, 400, 90, 50);
                        exit1.setBounds(670, 400, 90, 50);

                        exit1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                frame.remove(btn5);
                                frame.remove(lbl2);
                                frame.remove(txt2);
                                frame.remove(lbl1);
                                frame.remove(txt1);
                                frame.remove(exit1);
                                frame.add(btn1);
                                frame.add(btn2);
                                frame.revalidate();
                                frame.repaint();
                            }
                        });
                        btn5.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String text1 = txt2.getText();
                                if (!text1.isEmpty()) {
                                    frame.remove(btn5);
                                    frame.remove(lbl2);
                                    frame.remove(txt2);
                                    frame.remove(exit1);
                                    frame.remove(lbl1);
                                    frame.remove(txt1);
                                    frame.add(getMainPanel());

                                    frame.revalidate();
                                    frame.repaint();
                                } else {
                                    JOptionPane.showMessageDialog((Component)null, "please sure you write the nickname", "Error", 2);
                                }

                            }
                        });
                        frame.add(lbl1);
                        frame.add(txt1);
                        frame.add(lbl2);
                        frame.add(txt2);
                        frame.add(btn5);
                        frame.add(exit1);
                        frame.setVisible(true);
                        frame.revalidate();
                        frame.repaint();
                    }

                });
                exit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(btn3);
                        frame.remove(btn4);
                        frame.remove(exit);
                        frame.add(btn1);
                        frame.add(btn2);
                        frame.revalidate();
                        frame.repaint();
                    }
                });
                frame.revalidate();
                frame.repaint();
            }
        });
        frame.add(btn1);
        frame.add(btn2);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
