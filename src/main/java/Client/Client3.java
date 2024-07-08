package Client;


import GamePlay.Card;
import GamePlay.GamePage;
import GamePlay.Token;
import com.google.gson.Gson;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class Client3 {
    // آدرس سرور چت
    private static final String SERVER_ADDRESS = "127.0.0.1";
    // پورتی که سرور چت بر روی آن گوش می‌دهد
    private static final int SERVER_PORT = 6666;
    static Gson gson = new Gson();

    private String name;
    private String id;
    private int winedSets = 0;
    private int winedRounds = 0;
    private JPanel myHand;
    JPanel centerPanel;
    GamePlay.GamePage mainPanel;
    private ArrayList<Card> myCards;
    private ArrayList<JButton> buttons;
    private PrintWriter out;
    JLabel lblNik1;
    JLabel lblNik2;
    JLabel lblNik3;
    JLabel lblNik4;

    static JButton pan1;
    static JButton pan2;
    static JButton pan3;
    static JButton pan4;
    JPanel hokmPan;

    JButton Heart;
    JButton Diamonds;
    JButton Clubs;
    JButton Spades;
    JTable resultTlb;
    JScrollPane scrollPane;
    JButton HokmButton;
    public GamePage getMainPanel() {
        return mainPanel;
    }

    public String getName() {
        return name;
    }

    public int getPlayerWinedSets() {
        return winedSets;
    }
    public void addToPlayerWinedSets(){
        winedSets ++;
    }
    public void startNewRound() {
        winedSets=0;
    }
    public int getPlayerWinedRounds() {
        return winedRounds;
    }
    public void addToPlayerWinedRounds(){
        winedRounds++;
    }

    public String getId() {
        return id;
    }
    public JPanel getMyHand() {
        return myHand;
    }
    public ArrayList<Card> getMyCards() {
        return myCards;
    }

    public ArrayList<JButton> getMyButtons() {
        return buttons;
    }
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            int clickedButtonIndex = 0;
            for (int i = 0; i < buttons.size(); i++) {
                if (buttons.get(i) == clickedButton){
                    clickedButtonIndex = i;
                    break;
                }
            }

            String CodedPutCard = gson.toJson(myCards.get(clickedButtonIndex));
            sendMessageToServer("I PUT:" + CodedPutCard);

            myCards.remove(clickedButtonIndex);
            buttons.remove(clickedButtonIndex);
            myHand.remove(clickedButton);

            myHand.repaint();
            mainPanel.repaint();
        }
    };
    public void showHandCards() {
        Dimension buttonSize = new Dimension(100, 120);
        buttons.getLast().setPreferredSize(buttonSize);
        myHand.add(buttons.getLast());
    }

    public static void main(String[] args) throws Exception {
        Client3 client = new Client3(" ", " ");
        client.startClient();
    }

    // متد startClient برای شروع اتصال به سرور چت
    public void startClient() throws Exception {
        // ایجاد یک سوکت برای اتصال به سرور
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        // برای خواندن پیام‌های دریافتی از سرور
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // برای نوشتن پیام‌ها به سرور
        out = new PrintWriter(socket.getOutputStream(), true);

        // Start a thread to read messages from the server
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("TAKE CARD:") ){
                        String jsonCardString = message.substring(10);
                        getMyCards().add(gson.fromJson(jsonCardString, Card.class));
                        getMyButtons().add(new JButton());
                        getMyButtons().getLast().setIcon(new ImageIcon(getMyCards().getLast().getRooImage().getImage()));
                        getMyButtons().getLast().addActionListener(actionListener);
                        getMyButtons().getLast().setEnabled(false);
                        showHandCards();
                    }
                    if (message.startsWith("YOUR NAME:")){
                        lblNik1.setText(message.substring(10));
                    }
                    if (message.startsWith("LEFT NAME:")){
                        lblNik4.setText(message.substring(10));
                    }
                    if (message.startsWith("FRONT NAME:")){
                        lblNik3.setText(message.substring(11));
                    }
                    if (message.startsWith("RIGHT NAME:")){
                        lblNik2.setText(message.substring(11));
                    }
                    if (message.startsWith("YOU ARE RULER.")){
                        ActionListener al = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JButton clicked = (JButton) e.getSource();
                                sendMessageToServer("RUL IS:" + clicked.getText());
                            }
                        };
                        Heart.addActionListener(al);
                        Diamonds.addActionListener(al);
                        Spades.addActionListener(al);
                        Clubs.addActionListener(al);
                        hokmPan.setVisible(true);
                    }
                    if (message.startsWith("YOU RULED.")){
                        hokmPan.setVisible(false);
                    }

                    if (message.startsWith("YOUR TURN.")){
                        if (message.equalsIgnoreCase("YOUR TURN.FREE")){
                            for (int i = 0; i < getMyButtons().size(); i++) {
                                getMyButtons().get(i).setEnabled(true);
                            }
                        } else {
                            boolean found = false;
                            String correctType = message.substring(10);
                            for (int i = 0; i < getMyCards().size(); i++) {
                                if (getMyCards().get(i).getType().equalsIgnoreCase(correctType)){
                                    getMyButtons().get(i).setEnabled(true);
                                    found = true;
                                }
                            }
                            if (!found){
                                for (int i = 0; i < getMyButtons().size(); i++) {
                                    getMyButtons().get(i).setEnabled(true);
                                }
                            }
                        }
                    }

                    if (message.startsWith("NOT TURN.")){
                        for (int i = 0; i < getMyButtons().size(); i++) {
                            getMyButtons().get(i).setEnabled(false);
                        }
                    }

                    if (message.startsWith("YOUR CARD:")){
                        String puttedCard = message.substring(10);
                        Card card =gson.fromJson(puttedCard, Card.class);
                        pan1.setIcon(new ImageIcon(card.getRooImage().getImage()));
                    }
                    if (message.startsWith("LEFT CARD:")){
                        String puttedCard = message.substring(10);
                        Card card =gson.fromJson(puttedCard, Card.class);
                        pan4.setIcon(new ImageIcon(card.getRooImage().getImage()));
                    }
                    if (message.startsWith("FRONT CARD:")){
                        String puttedCard = message.substring(11);
                        Card card =gson.fromJson(puttedCard, Card.class);
                        pan3.setIcon(new ImageIcon(card.getRooImage().getImage()));
                    }
                    if (message.startsWith("RIGHT CARD:")){
                        String puttedCard = message.substring(11);
                        Card card =gson.fromJson(puttedCard, Card.class);
                        pan2.setIcon(new ImageIcon(card.getRooImage().getImage()));
                    }

                    if (message.startsWith("RUL IS:")){
                        String rul = message.substring(7);
                        if(rul.equals("Spade")){
                            HokmButton.setIcon(new ImageIcon(new ImageIcon("src/main/java/Images/Hokm1.png").getImage().getScaledInstance(80,-1,Image.SCALE_SMOOTH)));
                        }
                        if (rul.equals("Heart")){
                            HokmButton.setIcon(new ImageIcon(new ImageIcon("src/main/java/Images/Hokm2.png").getImage().getScaledInstance(80,-1,Image.SCALE_SMOOTH)));
                        }
                        if(rul.equals("Diamond")){
                            HokmButton.setIcon(new ImageIcon(new ImageIcon("src/main/java/Images/Hokm3.png").getImage().getScaledInstance(80,-1,Image.SCALE_SMOOTH)));
                        }
                        if(rul.equals("Club")){
                            HokmButton.setIcon(new ImageIcon(new ImageIcon("src/main/java/Images/Hokm4.png").getImage().getScaledInstance(80,-1,Image.SCALE_SMOOTH)));
                        }
                    }
                    if (message.startsWith("CLEANING BORD.")){
                        cleaningBord();
                    }
                    if (message.startsWith("NEW ROUND IS STARTING.")){
                        for (int i = 0; i < getMyButtons().size(); i++) {
                            myHand.remove(getMyButtons().get(i));
                        }
                        getMyButtons().clear();
                        myCards.clear();
                        //۰ شدن گرافیکی شماره ست های برده
                    }
                    if (message.startsWith("YOU LOST THE SET.")){
                        //enemy set +1 in table
                    }
                    if (message.startsWith("YOU WINED THE SET.")){
                        addToPlayerWinedSets();
                        //your set +1 in table
                    }
                    if (message.startsWith("YOU LOST THE ROUND.")){
                        //enemy round +1 in table
                    }
                    if (message.startsWith("YOU WINED THE ROUND.")){
                        addToPlayerWinedRounds();
                        //your round +1 in table
                    }

                    if (message.startsWith("YOU WINED THE GAME.")){
                        JOptionPane.showMessageDialog(null,
                                "YOU WINED THE GAME.",
                                "WINNER",
                                JOptionPane.ERROR_MESSAGE);

                        // بستن برنامه
                        System.exit(0);
                    }
                    if (message.startsWith("YOU LOST THE GAME.")){
                        JOptionPane.showMessageDialog(null,
                                "YOU LOSE THE GAME.",
                                "LOSER",
                                JOptionPane.ERROR_MESSAGE);

                        // بستن برنامه
                        System.exit(0);
                    }


                    // **************************************************************************************************************************


                    if (message.contains("Players")){
                        String[]nameOfPlayer = message.split(" ");
                        for (int i = 0 ; i< nameOfPlayer.length ; i++) {
                            //nameOfPlayers.add(nameOfPlayer[i]);
                        }
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
            }
        }).start();

        // برای خواندن ورودی از کاربر
        Scanner scanner = new Scanner(System.in);
        System.out.println("Connected to chat server");

        // Thread to read user input and send it to server
        new Thread(() -> {
            while (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();
                sendMessageToServer(userInput);
            }
        }).start();
    }

    public void sendMessageToServer(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public static void cleaningBord(){

        if (pan1.getIcon() != null){
            pan1.setIcon(null);
        }
        if (pan2.getIcon() != null){
            pan2.setIcon(null);
        }
        if (pan3.getIcon() != null){
            pan3.setIcon(null);
        }
        if (pan4.getIcon() != null){
            pan4.setIcon(null);
        }
    }
    public Client3(String name, String id){
        initializeUI();
    }

    public void initializeUI(){
        final Token TOKEN = new Token();
        final JFrame frame = new JFrame("Hokm");
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(1500, 900);
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
                        String nickname = txt2.getText();
                        if (!nickname.isEmpty()) {
                            sendMessageToServer("random " + nickname);
                            frame.remove(btn5);
                            frame.remove(lbl2);
                            frame.remove(txt2);
                            frame.remove(exit1);
                            frame.add(getMainPanel());
                            lblNik1.setText(txt2.getText());
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
                                String nickname= txt2.getText();
                                String token = txt1.getText();
                                if (!nickname.isEmpty() && !token.isEmpty()) {
                                    sendMessageToServer("join " + nickname + " " + token);
                                    frame.remove(lbl1);
                                    frame.remove(txt1);
                                    frame.remove(btn5);
                                    frame.remove(lbl2);
                                    frame.remove(txt2);
                                    frame.remove(exit1);
                                    frame.setTitle("Game Room");
                                    frame.add(getMainPanel());
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
                                String nickname= txt2.getText();
                                String token = txt1.getText() ;
                                if (!nickname.isEmpty()) {
                                    sendMessageToServer("create " + nickname + " " + token);
                                    frame.remove(btn5);
                                    frame.remove(lbl2);
                                    frame.remove(txt2);
                                    frame.remove(exit1);
                                    frame.remove(lbl1);
                                    frame.remove(txt1);
                                    frame.add(getMainPanel());

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
        frame.repaint();
        frame.revalidate();

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // Not worth my time
            }
        }

        this.name = name;
        this.id = id;
        this.myCards = new ArrayList<>(13);
        this.buttons = new ArrayList<>(13);
        this.myHand = new JPanel();
        this.centerPanel = new JPanel();
        this.mainPanel = new GamePlay.GamePage();

        lblNik1 = new JLabel("player1");
        lblNik2 = new JLabel("player2");
        lblNik3 = new JLabel("player3");
        lblNik4 = new JLabel("player4");

        pan1=new JButton();
        pan2=new JButton();
        pan3=new JButton();
        pan4=new JButton();


        // داده‌های جدول: نام تیم و امتیاز
        Object[][] data = {
                {"Team1", getPlayerWinedRounds(),getPlayerWinedSets()},
                {"Team2", getPlayerWinedRounds(),getPlayerWinedSets()}
        };

        // نام ستون‌ها
        String[] columnNames = {"Team", "round","set"};

        // مدل جدول سفارشی با داده‌ها و نام ستون‌ها
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // همه سلول‌ها غیرقابل ویرایش هستند
                return false;
            }
        };


        resultTlb = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component cell = super.prepareRenderer(renderer, row, column);

                // تنظیم رنگ پس‌زمینه سلول با استفاده از RGB
                cell.setBackground(new Color(50, 87, 80)); // به عنوان مثال رنگ آبی روشن

                // تنظیم رنگ متن سلول با استفاده از RGB
                cell.setForeground(new Color(255, 255, 255)); // رنگ سیاه برای متن

                return cell;
            }
        };

        // تنظیم رندر سفارشی برای سربرگ جدول
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(119, 62, 62)); // رنگ پس‌زمینه سربرگ
        headerRenderer.setForeground(Color.WHITE); // رنگ متن سربرگ
        headerRenderer.setHorizontalAlignment(JLabel.CENTER); // تنظیم تراز متن به وسط

        for (int i = 0; i < resultTlb.getColumnModel().getColumnCount(); i++) {
            resultTlb.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }


        scrollPane = new JScrollPane(resultTlb);
        scrollPane.getViewport().setBackground(new Color(50, 87, 80)); // رنگ پس‌زمینه ویوپورت
        scrollPane.setBackground(new Color(50, 87, 80)); // رنگ پس‌زمینه خود ScrollPane

        // تنظیم رنگ نوارهای اسکرول
        scrollPane.getHorizontalScrollBar().setBackground(new Color(33, 56, 44));
        scrollPane.getVerticalScrollBar().setBackground(new Color(33, 56, 44));

        scrollPane.setBounds(1150, 28, 200, 55);

        mainPanel.add(scrollPane);
        hokmPan=new JPanel();

        Heart = new JButton();
        Spades = new JButton();
        Diamonds = new JButton();
        Clubs = new JButton();
        HokmButton = new JButton();

        Heart.setBackground(new Color(50, 87, 80));
        Spades.setBackground(new Color(50, 87, 80));
        Diamonds.setBackground(new Color(50, 87, 80));
        Clubs.setBackground(new Color(50, 87, 80));

        Heart.setIcon(new ImageIcon(new ImageIcon("src/main/java/Images/Hokm2.png").getImage().getScaledInstance(80,-1,Image.SCALE_SMOOTH)));
        Spades.setIcon(new ImageIcon(new ImageIcon("src/main/java/Images/Hokm1.png").getImage().getScaledInstance(80,-1,Image.SCALE_SMOOTH)));
        Diamonds.setIcon(new ImageIcon(new ImageIcon("src/main/java/Images/Hokm3.png").getImage().getScaledInstance(80,-1,Image.SCALE_SMOOTH)));
        Clubs.setIcon(new ImageIcon(new ImageIcon("src/main/java/Images/Hokm4.png").getImage().getScaledInstance(80,-1,Image.SCALE_SMOOTH)));

        Heart.setText("Heart");
        Spades.setText("Spade");
        Diamonds.setText("Diamond");
        Clubs.setText("Club");

        mainPanel.setBounds(0, 0, 1500, 900);


        myHand.setBackground(new Color(119, 62, 62));
        myHand.setBounds(0, 650, 1500, 700);


        centerPanel.setPreferredSize(new Dimension(1070, 400));
        centerPanel.setBounds(190, 120, 1070, 400);
        centerPanel.setBackground(new Color(50, 87, 80));
        centerPanel.setVisible(true);


        pan1.setBackground(new Color(50, 87, 80));
        pan2.setBackground(new Color(50, 87, 80));
        pan3.setBackground(new Color(50, 87, 80));
        pan4.setBackground(new Color(50, 87, 80));
        HokmButton.setBackground(new Color(97, 150, 134));

        pan4.setBounds(200,250,100,120);
        pan2.setBounds(1150,250,100,120);
        pan1.setBounds(690,390,100,120);
        pan3.setBounds(690,140,100,120);

        pan1.setVisible(true);
        pan2.setVisible(true);
        pan3.setVisible(true);
        pan4.setVisible(true);

        mainPanel.add(pan1);
        mainPanel.add(pan2);
        mainPanel.add(pan3);
        mainPanel.add(pan4);



        lblNik1.setBounds(720,500,100,100);
        lblNik2.setBounds(1300,270,100,100);
        lblNik3.setBounds(720,50,100,100);
        lblNik4.setBounds(120,270,100,100);



        lblNik1.setVisible(true);
        lblNik2.setVisible(true);
        lblNik3.setVisible(true);
        lblNik4.setVisible(true);
        mainPanel.add(lblNik1);
        mainPanel.add(lblNik2);
        mainPanel.add(lblNik3);
        mainPanel.add(lblNik4);
        mainPanel.add(centerPanel);
        mainPanel.add(myHand);


        hokmPan.setBounds(10,120,80,400);
        hokmPan.setBackground(new Color(50, 87, 80));
        hokmPan.setLayout(null);
        hokmPan.setVisible(true);

        Clubs.setBounds(0,15,80,80);
        Spades.setBounds(0,105,80,80);
        Diamonds.setBounds(0,195,80,80);
        Heart.setBounds(0,285,80,80);
        HokmButton.setBounds(700,0,80,80);

        Diamonds.setVisible(true);
        Spades.setVisible(true);
        Clubs.setVisible(true);
        Heart.setVisible(true);
        HokmButton.setVisible(true);

        hokmPan.add(Diamonds);
        hokmPan.add(Clubs);
        hokmPan.add(Spades);
        hokmPan.add(Heart);
        hokmPan.setVisible(false);
        mainPanel.add(HokmButton);
        mainPanel.add(hokmPan);
    }
}