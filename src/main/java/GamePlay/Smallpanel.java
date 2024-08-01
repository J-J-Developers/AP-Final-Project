package GamePlay;

import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Smallpanel extends JPanel {
   ArrayList<JButton> buttons = new ArrayList<>();
   private ImageIcon backgroundImage;

   public Smallpanel() {
      this.setLayout((LayoutManager)null);
      backgroundImage = new ImageIcon("src/main/java/Images/smallPage.jpg");
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;

      Image image = backgroundImage.getImage();
      g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
   }
   


}
