package GamePlay;

import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GamePage extends JPanel {
   ArrayList<JButton> buttons = new ArrayList<>();
   private ImageIcon backgroundImage;

   public GamePage() {
      this.setLayout((LayoutManager)null);
      backgroundImage = new ImageIcon("src/main/java/Images/background.jpg");
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;


      Image image = backgroundImage.getImage();
      g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);

    

      g2.setColor(Color.WHITE);
      g2.drawRect(100, 30, 1250, 600);
      g2.setColor(new Color(50, 87, 80));
      g2.fillRect(190, 120, 1070, 400);
   }
   


}