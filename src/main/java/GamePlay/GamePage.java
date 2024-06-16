package GamePlay;
import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GamePage extends JPanel {
   ArrayList<JButton> buttons = new ArrayList<>();
   public GamePage() {
      this.setLayout((LayoutManager)null);
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      g2.setColor(new Color(97, 150, 134));
      g2.fillRect(0, 0, screenSize.width, 650);
      g2.setColor(Color.WHITE);
      g2.drawRect(100, 30, 1250, 600);
      g2.setColor(Color.WHITE);

      g2.drawRect(190, 120, 1070, 400);
   }
}