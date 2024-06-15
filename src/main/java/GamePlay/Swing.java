package GamePlay;
import GamePlay.Token;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Swing {
    public static void main(String[] args) {
        Token TOKEN = new Token();
        JFrame frame = new JFrame("Hokm");
        frame.setBounds(350, 0, 700, 700);
        frame.setLayout(null);
        frame.getContentPane();

        Color customColor = new Color(97, 150, 134);
        Color customColor1 = new Color(50, 87, 80);

        frame.getContentPane().setBackground(customColor);

        JButton btn1 = new JButton("Random");
        JButton btn2 = new JButton("Friends");
        btn1.setBounds(260, 200, 150, 70);
        btn2.setBounds(260, 350, 150, 70);

        btn1.setBackground(customColor1);
        btn1.setOpaque(true);
        btn1.setForeground(new Color(131, 75, 166));

        btn2.setBackground(customColor1);
        btn2.setOpaque(true);
        btn2.setForeground(new Color(131, 75, 166));

        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(btn1);
                frame.remove(btn2);
                frame.setTitle("Game Room");

                JLabel lbl2 = new JLabel("Nickname:");
                JTextField txt2 = new JTextField();
                JButton btn5 = new JButton("Go");
                JButton exit1 = new JButton("Back");

                btn5.setBackground(customColor1);
                btn5.setOpaque(true);
                btn5.setForeground(new Color(131, 75, 166));

                exit1.setBackground(customColor1);
                exit1.setOpaque(true);
                exit1.setForeground(new Color(131, 75, 166));

                lbl2.setBounds(140, 200, 110, 30);
                txt2.setBounds(260, 200, 200, 30);
                btn5.setBounds(360, 300, 50, 30);
                exit1.setBounds(270, 300, 80, 30);

                exit1.addActionListener(new ActionListener() {
                    @Override
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
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String text1 = txt2.getText();
                        if (!text1.isEmpty()) {
                            frame.remove(btn5);
                            frame.remove(lbl2);
                            frame.remove(txt2);
                            frame.remove(exit1);
                            frame.repaint();
                            frame.revalidate();


                            GamePage panel = new GamePage();
                            panel.setBounds(100,60 , 500,500);
                            frame.add(panel);



                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "please sure you write the nickname",
                                    "Error", JOptionPane.WARNING_MESSAGE);
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
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(btn1);
                frame.remove(btn2);
                frame.setTitle("Friendly Game");
                JButton btn3 = new JButton("Join");
                JButton btn4 = new JButton("Create");
                JButton exit = new JButton("Back");

                btn3.setBounds(260, 200, 150, 70);
                btn4.setBounds(260, 300, 150, 70);
                exit.setBounds(260, 400, 150, 70);

                btn3.setBackground(customColor1);
                btn3.setOpaque(true);
                btn3.setForeground(new Color(131, 75, 166));

                btn4.setBackground(customColor1);
                btn4.setOpaque(true);
                btn4.setForeground(new Color(131, 75, 166));

                exit.setBackground(customColor1);
                exit.setOpaque(true);
                exit.setForeground(new Color(131, 75, 166));

                frame.add(exit);
                frame.add(btn3);
                frame.add(btn4);

                btn3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(exit);
                        frame.remove(btn3);
                        frame.remove(btn4);
                        frame.setTitle("Join By Token");

                        JLabel lbl1 = new JLabel("GamePlay.Token:");
                        JTextField txt1 = new JTextField();
                        JLabel lbl2 = new JLabel("Nickname:");
                        JTextField txt2 = new JTextField();
                        JButton btn5 = new JButton("Go");
                        JButton exit1 = new JButton("Back");

                        btn5.setBackground(customColor1);
                        btn5.setOpaque(true);
                        btn5.setForeground(new Color(131, 75, 166));

                        exit1.setBackground(customColor1);
                        exit1.setOpaque(true);
                        exit1.setForeground(new Color(131, 75, 166));

                        lbl2.setBounds(160, 200, 110, 30);
                        txt2.setBounds(280, 200, 200, 30);
                        lbl1.setBounds(160, 300, 110, 30);
                        txt1.setBounds(280, 300, 200, 30);
                        btn5.setBounds(360, 400, 50, 30);
                        exit1.setBounds(270, 400, 80, 30);
                        btn5.addActionListener(new ActionListener() {
                            @Override
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
                                    frame.repaint();
                                    frame.revalidate();
                                    frame.setTitle("Game Room");

                                    GamePage panel = new GamePage();
                                    panel.setBounds(100,60 , 700,700);
                                    frame.add(panel);


                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "please sure you write the nickname and token",
                                            "Error", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        });
                        exit1.addActionListener(new ActionListener() {
                            @Override
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
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(btn3);
                        frame.remove(btn4);
                        frame.remove(exit);
                        frame.repaint();
                        frame.revalidate();
                        frame.setTitle("Game Room");

                        GamePage panel = new GamePage();
                        panel.setBounds(100,60 , 700,700);


                        JLabel lbl2 = new JLabel("Token:");
                        JTextField txt3 = new JTextField(TOKEN.getTokenId());
                        txt3.setEditable(false);
                        txt3.setBackground(null);
                        txt3.setBorder(null);


                        lbl2.setBounds(185, 30, 55, 30);
                        txt3.setBounds(250, 30, 240, 30);

                        frame.add(lbl2);
                        frame.add(txt3);
                        frame.add(panel);


                    }
                });

                exit.addActionListener(new ActionListener() {
                    @Override
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