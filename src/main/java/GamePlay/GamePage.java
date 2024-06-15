package GamePlay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GamePage extends JPanel {
   ArrayList<JButton> buttons = new ArrayList<>();
   public GamePage() {
      setLayout(new GridLayout(3, 5, 10, 10)); // 3 rows, 5 columns, gap of 10 pixels
      int d=0;
      // Create and add 13 buttons
      for (int i = 1; i <= 13; i++) {
         JButton button = new JButton("Button " + i);
         buttons.add(button);
         button.setBounds(20+d,420,90,90);
         d+=30;
         button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String text = button.getText();
               remove(button);
               JButton button = new JButton(text);
               button.setBounds(210,250,90,90);
               add(button);
            }
         });
         add(button);
      }



   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;

      g2.setColor(new Color(97, 150, 134));
      g2.fillRect(0, 0, getWidth(), getHeight());

      g2.setColor(Color.WHITE);
      g2.drawRect(75, 70, 350, 350);

      g2.setColor(Color.WHITE);
      g2.drawRect(140, 140, 215, 215);

      g.setColor(Color.cyan);
      g.drawOval(225 , 80, 55, 55);

      g.setColor(Color.cyan);
      g.drawOval(80 , 225, 55, 55);

      g.setColor(Color.cyan);
      g.drawOval(225 , 360, 55, 55);

      g.setColor(Color.cyan);
      g.drawOval(360 , 220, 55, 55);
   }

   // Optional: Override getPreferredSize to set preferred size of the panel
   @Override
   public Dimension getPreferredSize() {
      return new Dimension(500, 500); // Example size
   }
}