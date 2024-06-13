package GamePlay;

import javax.swing.*;
import java.awt.*;

public class GamePage {
   public GamePage() {
      JFrame frame = new JFrame("Game Frame");
      Color customColor = new Color(97, 150, 134);
      frame.getContentPane().setBackground(customColor);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Squares squares = new Squares();
      frame.getContentPane().add(squares);

      squares.addSquare(50, 50, 400, 400);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(GamePage::new);

   }
}

class Squares extends JPanel {
   private static final int PREF_W = 500;
   private static final int PREF_H = PREF_W;

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

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }

   public void addSquare(int i, int i1, int i2, int i3) {
   }
}

