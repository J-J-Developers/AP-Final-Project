package GamePlay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GamePage extends JPanel {
   ArrayList<JButton> buttons = new ArrayList<>();
   public GamePage() {
//
      this.setLayout((LayoutManager)null);


//      setLayout(new GridLayout(3, 5, 10, 10)); // 3 rows, 5 columns, gap of 10 pixels
//      int d=0;
//      // Create and add 13 buttons
//      for (int i = 1; i <= 13; i++) {
//         JButton button = new JButton("Button " + i);
//         buttons.add(button);
//         button.setBounds(20+d,420,90,90);
//         d+=30;
//         button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//               String text = button.getText();
//               remove(button);
//               JButton button = new JButton(text);
//               button.setBounds(210,250,90,90);
//               add(button);
//            }
//         });
//         add(button);
//      }



   }

   @Override
   protected void paintComponent(Graphics g) {
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

   // Optional: Override getPreferredSize to set preferred size of the panel
   @Override
   public Dimension getPreferredSize() {
      return new Dimension(500, 500); // Example size
   }
}