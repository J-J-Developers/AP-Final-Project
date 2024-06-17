package GamePlay;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

public class Swing {
    public Swing() {
    }

    public static void main(String[] args) {
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
                            //frame.add(Player.myHandPanelSetting());

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


                                    //frame.add(Player.myHandPanelSetting());
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
                                    //frame.add(Player.myHandPanelSetting());

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
