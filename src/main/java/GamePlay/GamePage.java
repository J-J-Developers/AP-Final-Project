package GamePlay;

import javax.swing.*;
import java.awt.*;

public class GamePage extends JPanel {
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
}