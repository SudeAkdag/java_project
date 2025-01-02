import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class PacmanMenu extends JFrame {
    // Oyun nesnelerini sınıf seviyesinde tanımla
    private PacMan  game;
    private PacMan2 game2;
    private PacMan3 game3;
    private PacMan4 game4;
    private PacMan5 game5;
    private Font arcadeFont;

    public PacmanMenu() {
        // Font yükleme
        try {
            // Font dosyasının projedeki tam konumunu belirtin
            String fontPath = "pacman-java-master/PressStart2P-Regular.ttf"; // veya fontun bulunduğu tam konum
            arcadeFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            arcadeFont = arcadeFont.deriveFont(34f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(arcadeFont);
        } catch (Exception e) {
            arcadeFont = new Font("SansSerif", Font.BOLD, 24); // Yedek font
            System.out.println("Font yüklenemedi: " + e.getMessage());
            e.printStackTrace(); // Hata detayını görmek için
        }

        // Oyun nesnelerini başlat
        game = new PacMan();    // Level 1: Normal hız, 8 can
        game2 = new PacMan2();  // Level 2: Daha hızlı, 6 can
        game3 = new PacMan3();  // Level 3: Daha da hızlı, 4 can
        game4 = new PacMan4();  // Level 4: En hızlı, 4 can
        game5 = new PacMan5();  // Level 5: Özel güçler ve teleport

        // Pencere Ayarları
        setTitle("PacMan - Ana Menü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 850);
        setResizable(false);
        setLayout(new BorderLayout());

        // Ana panel - özel arka plan paneli
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Arka plan rengi
                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, 0, getWidth(), getHeight());

                try {
                    // Logo yükleme
                    ImageIcon logoIcon = new ImageIcon(getClass().getResource("./pacman_logo2.jpeg"));
                    if (logoIcon.getImage() != null) {
                        Image logo = logoIcon.getImage();
                        // Logoyu üst kısma yerleştir
                        int logoWidth = 904; // Sabit logo genişliği
                        int logoHeight = 258; // Sabit logo yüksekliği
                        int x = (getWidth() - logoWidth) / 2; // Yatayda ortala
                        g.drawImage(logo, x, 50, logoWidth, logoHeight, this);
                    } else {
                        // Logo yüklenemezse başlık yaz
                        g.setColor(Color.YELLOW);
                        g.setFont(arcadeFont.deriveFont(48f));
                        String title = "PAC-MAN";
                        FontMetrics fm = g.getFontMetrics();
                        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
                        g.drawString(title, titleX, 100);
                    }
                } catch (Exception e) {
                    // Hata durumunda başlık yaz
                    g.setColor(Color.YELLOW);
                    g.setFont(arcadeFont.deriveFont(48f));
                    String title = "PAC-MAN";
                    FontMetrics fm = g.getFontMetrics();
                    int titleX = (getWidth() - fm.stringWidth(title)) / 2;
                    g.drawString(title, titleX, 100);
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Level butonları paneli
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayout(5, 1, 30, 30));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(330, 500, 20, 500));
        levelPanel.setOpaque(false);

        // Level butonları
        for (int i = 1; i <= 5; i++) {
            Color buttonColor = new Color(255, 215, 0);
            JButton levelButton = createStyledButton("LEVEL " + i, buttonColor);
            levelButton.setFont(arcadeFont.deriveFont(20f));
            levelButton.setPreferredSize(new Dimension(180, 50));
            int level = i;
            levelButton.addActionListener(e -> startGame(level));
            levelPanel.add(levelButton);
        }
        mainPanel.add(levelPanel, BorderLayout.CENTER);

        // Alt panel için container
        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BorderLayout());
        bottomContainer.setOpaque(false);

        // Çıkış butonu paneli
        JPanel exitPanel = new JPanel();
        exitPanel.setOpaque(false);
        exitPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Çıkış butonu
        JButton exitButton = createStyledButton("Çıkış", new Color(255, 69, 0));
        exitButton.setFont(arcadeFont.deriveFont(20f));
        exitButton.setPreferredSize(new Dimension(150, 40));
        exitButton.addActionListener(e -> System.exit(0));

        exitPanel.add(exitButton);
        bottomContainer.add(exitPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomContainer, BorderLayout.SOUTH);

        // Pencere ayarları
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startGame(int level) {
        dispose();
        JFrame gameFrame = new JFrame("PacMan - Level " + level);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        switch (level) {
            case 1:
                gameFrame.add(game);
                break;
            case 2:
                gameFrame.add(game2);
                break;
            case 3:
                gameFrame.add(game3);
                break;
            case 4:
                gameFrame.add(game4);
                break;
            case 5:
                gameFrame.add(game5);
                break;
            default:
                gameFrame.add(game);
                break;
        }

        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }

    // Özel stil buton oluşturucu - güncellendi
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient arka plan
                GradientPaint gradient = new GradientPaint(
                        0, 0, baseColor,
                        0, getHeight(), baseColor.darker()
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);

                // Parlak kenar efekti
                g2.setColor(new Color(255, 255, 255, 70));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 23, 23);

                // Gölge efekti
                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);

                g2.dispose();

                // Metin çizimi
                Graphics2D textG = (Graphics2D) g.create();
                textG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                textG.setFont(getFont());
                textG.setColor(getForeground());
                FontMetrics fm = textG.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                textG.drawString(text, x, y);
                textG.dispose();
            }
        };

        // Buton özellikleri
        button.setFont(arcadeFont.deriveFont(20f));
        button.setForeground(new Color(33, 33, 33));
        button.setPreferredSize(new Dimension(200, 60));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Hover efektleri
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setFont(arcadeFont.deriveFont(22f));
                button.setForeground(new Color(0, 0, 0));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setFont(arcadeFont.deriveFont(20f));
                button.setForeground(new Color(33, 33, 33));
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PacmanMenu());
    }
}