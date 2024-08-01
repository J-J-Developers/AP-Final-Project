package GamePlay;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StartPages extends JPanel {
    ImageIcon bgIcon = new ImageIcon("src/main/java/Images/farsh.jpg");
    Image backimg = bgIcon.getImage();
    private Image backgroundImage=backimg;
    private List<Card> cards;
    private boolean allCardsReached = false;
    private boolean allCardsRotated = false;
    private boolean allCardsTilted = false;
    private long allCardsReachTime;
    private long rotationStartTime;

    // Define card size
    private final int cardWidth = 80;  // Width of each card
    private final int cardHeight = 120; // Height of each card

    // Define margins and spacing
    private final int margin = 20; // Margin from the edges of the panel
    private final int spacing = 13; // Spacing between cards

    // Constructor to initialize the panel with a background image and cards
    public StartPages() {

        this.cards = new ArrayList<>();

        // Create 52 cards
        for (int i = 0; i <= 52; i++) {
            cards.add(new Card("src/main/java/Images/" + i + ".jpg", 0, 900, i)); // Initial position (0,0) will be updated
        }

        // Timer for animation
        Timer timer = new Timer(10, e -> {
            boolean allReached = true;
            for (Card card : cards) {
                card.updatePosition(getWidth(), getHeight());
                if (!card.reachedTarget) {
                    allReached = false;
                }
            }
            if (allReached && !allCardsReached) {
                allCardsReached = true;
                allCardsReachTime = System.currentTimeMillis();
                rotationStartTime = allCardsReachTime;
            }
            if (allCardsReached && !allCardsRotated) {
                long elapsedTime = System.currentTimeMillis() - rotationStartTime;
                if (elapsedTime < 1000) {
                    for (Card card : cards) {
                        card.slowDownRotation();
                    }
                } else {
                    allCardsRotated = true;
                }
            }
            if (allCardsRotated && !allCardsTilted) {
                for (Card card : cards) {
                    card.startTilting();
                }
                allCardsTilted = true;
            }
            repaint();
        });
        timer.start();
    }

    // Override paintComponent to draw the background image and cards
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        // Draw the cards
        for (Card card : cards) {
            card.draw(g, this);
        }
    }

    // Inner class for Card
    private class Card {
        private Image image;
        private int x;
        private int y;
        private int targetX;
        private int targetY;
        private double angle;
        private int index;
        public boolean reachedTarget;
        private double rotateAngle;
        private boolean rotating = true;
        private boolean tilting = false;
        private long tiltStartTime;
        private double tiltAngle = 0.0;
        private final double initialTiltSpeed = 0.1; // Increased initial speed of tilting effect
        private double tiltSpeed = initialTiltSpeed; // Variable speed for tilting effect
        private final double minTiltSpeed = 0.01; // Minimum speed for tilting effect
        private final double tiltAmplitude = 0.1; // Amplitude of tilting effect
        private final double finalTiltSpeed = 0.05; // Increased final speed for tilting effect

        public Card(String imagePath, int startX, int startY, int index) {
            this.image = new ImageIcon(imagePath).getImage();
            this.x = startX;
            this.y = startY;
            this.angle = 0;
            this.index = index;
            this.reachedTarget = false;
            this.rotateAngle = 0;
            setTargetPosition(index);
        }

        private void setTargetPosition(int index) {
            // Calculate the target position around the panel border
            int cardsPerSide = 14; // Number of cards per side

            if (index < cardsPerSide) {
                // Top border
                targetX = margin + index * (cardWidth + spacing);
                targetY = margin;
            } else if (index < 2 * cardsPerSide) {
                // Right border
                targetX = 1425 - cardWidth - margin;
                targetY = margin + (index - cardsPerSide) * (cardHeight + spacing);
            } else if (index < 3 * cardsPerSide-1) {
                // Bottom border
                targetX = margin + (index - 2 * cardsPerSide) * (cardWidth + spacing)+100;
                targetY = 800 - cardHeight - margin;
            } else {
                // Left border
                targetX = margin;
                targetY = margin + (index - 3 * cardsPerSide) * (cardHeight + spacing)+129;
            }
        }

        public void updatePosition(int panelWidth, int panelHeight) {
            // حرکت کارت به موقعیت هدف
            if (!reachedTarget) {
                if (x < targetX) x += 3; // افزایش اندازه گام برای حرکت سریع‌تر
                if (x > targetX) x -= 3; // افزایش اندازه گام برای حرکت سریع‌تر
                if (y < targetY) y += 3; // افزایش اندازه گام برای حرکت سریع‌تر
                if (y > targetY) y -= 3; // افزایش اندازه گام برای حرکت سریع‌تر
                if (Math.abs(x - targetX) < 3 && Math.abs(y - targetY) < 3) {
                    x = targetX; // تنظیم به موقعیت دقیق وقتی نزدیک است
                    y = targetY; // تنظیم به موقعیت دقیق وقتی نزدیک است
                    reachedTarget = true;
                }

                // ادامه چرخش در حالی که حرکت می‌کند
                if (rotating) {
                    rotateAngle += 0.1;
                    if (rotateAngle >= Math.PI * 2) {
                        rotateAngle = 0;
                    }
                }
            } else if (rotating) {
                // کاهش سرعت چرخش پس از رسیدن همه کارت‌ها به هدف
                rotateAngle += 0.05;
                if (rotateAngle >= Math.PI * 2) {
                    rotateAngle = 0;
                    rotating = false;
                    tiltStartTime = System.currentTimeMillis();
                }
            } else if (tilting) {
                // کج کردن کارت‌ها به چپ و راست با سرعت کم‌شونده تدریجی
                long elapsedTime = System.currentTimeMillis() - tiltStartTime;
                tiltAngle += tiltSpeed;
                angle = (index % 2 == 0 ? 1 : -1) * tiltAmplitude * Math.sin(tiltAngle);

                // کاهش تدریجی سرعت کج شدن، اما اطمینان از باقی ماندن آن بالاتر از سرعت نهایی
                if (tiltSpeed > finalTiltSpeed) {
                    tiltSpeed *= 0.99; // کاهش سرعت تدریجی
                } else {
                    tiltSpeed = finalTiltSpeed; // تنظیم به سرعت نهایی زمانی که به آستانه برسد
                }
            }
        }

        public void slowDownRotation() {
            rotating = true;
        }

        public void startTilting() {
            tilting = true;
            tiltStartTime = System.currentTimeMillis(); // Initialize tilt start time
        }

        public void draw(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();
            if (rotating || tilting) {
                g2d.rotate(rotateAngle, x + cardWidth / 2, y + cardHeight / 2);
            }
            g2d.rotate(angle, x + cardWidth / 2, y + cardHeight / 2); // Apply the tilting effect
            g2d.drawImage(image, x, y, cardWidth, cardHeight, c);
            g2d.dispose();
        }
    }


}