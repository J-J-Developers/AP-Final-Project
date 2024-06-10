import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Swing {
    public static void main(String[] args) {
        String TOKEN = null;
        JFrame frame = new JFrame("Hokm");
        frame.setBounds(500, 150, 500, 500);
        frame.setLayout(null);
        frame.getContentPane();

        Color customColor = new Color(97, 150, 134);
        Color customColor1 = new Color(50, 87, 80);

        frame.getContentPane().setBackground(customColor);

        JButton btn1 = new JButton("Random");
        JButton btn2 = new JButton("Friends");

        btn1.setBounds(175, 150, 150, 70);
        btn2.setBounds(175, 250, 150, 70);

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

                btn3.setBounds(175, 150, 150, 70);
                btn4.setBounds(175, 250, 150, 70);

                btn3.setBackground(customColor1);
                btn3.setOpaque(true);
                btn3.setForeground(new Color(131, 75, 166));

                btn4.setBackground(customColor1);
                btn4.setOpaque(true);
                btn4.setForeground(new Color(131, 75, 166));

                frame.add(btn3);
                frame.add(btn4);

                btn3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(btn3);
                        frame.remove(btn4);
                        frame.setTitle("Join By Token");

                        JLabel lbl1 = new JLabel("Token:");
                        JTextField txt1 = new JTextField();
                        JButton btn5 = new JButton("Go");

                        btn5.setBackground(customColor1);
                        btn5.setOpaque(true);
                        btn5.setForeground(new Color(131, 75, 166));

                        lbl1.setBounds(135,200,55,30);
                        txt1.setBounds(190,200,150,30);
                        btn5.setBounds(345,200,30,30);

                        btn5.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                frame.remove(lbl1);
                                frame.remove(txt1);
                                frame.remove(btn5);
                                frame.setTitle("Game Room");

                                frame.revalidate();
                                frame.repaint();
                            }
                        });

                        frame.add(lbl1);
                        frame.add(txt1);
                        frame.add(btn5);

                        frame.revalidate();
                        frame.repaint();
                    }
                });

                btn4.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(btn3);
                        frame.remove(btn4);
                        frame.setTitle("Game Room");

                        JLabel lbl2 = new JLabel("Token:");
                        JTextField txt3 = new JTextField(TOKEN);
                        txt3.setEditable(false);
                        txt3.setBackground(null);
                        txt3.setBorder(null);

                        lbl2.setBounds(135,30,55,30);
                        txt3.setBounds(190,30,150,30);

                        frame.add(lbl2);
                        frame.add(txt3);

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

        frame.setVisible(true);
    }
}


