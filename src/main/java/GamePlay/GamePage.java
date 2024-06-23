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
      g2.setColor(new Color(97, 150, 134));
      g2.fillRect(0, 0, 1500, 650);

      g2.setColor(Color.WHITE);
      g2.drawRect(100, 30, 1250, 600);
      g2.setColor(new Color(50, 87, 80));
      g2.fillRect(190, 120, 1070, 400);
   }
}