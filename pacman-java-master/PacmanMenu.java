import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PacmanMenu extends JFrame {

    public PacmanMenu() {
        // Pencere Ayarları
        setTitle("PacMan - Ana Menü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Başlık
        JLabel titleLabel = new JLabel("PacMan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Level Seçim Paneli
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // Level Düğmeleri
        for (int i = 1; i <= 5; i++) {  // 3 level için buton ekliyoruz
            JButton levelButton = new JButton("Level " + i);
            int level = i; // Lambda için final değişken
            levelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startGame(level); // Oyunu başlat
                }
            });
            levelPanel.add(levelButton);
        }
        add(levelPanel, BorderLayout.CENTER);

        // Çıkış Butonu
        JButton exitButton = new JButton("Çıkış");
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton, BorderLayout.SOUTH);

        // Görünür Yap
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startGame(int level) {
        // Ana menüyü kapat
        dispose();

        // Seçilen level'a göre doğru PacMan oyununu başlat
        JFrame gameFrame = new JFrame("PacMan - Level " + level);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan game = null;
        PacMan2 game2 = null;
        PacMan3 game3 = null;
        PacMan4 game4 = null; // game2'yi burada null başlatıyoruz
        PacMan5 game5 = null;

        switch (level) {
            case 1:
                game = new PacMan();
                game.tileMap = game.tileMap; // Level 1 haritasını kullan
                break;
            case 2:
                game2 = new PacMan2();  // Level 2'de game2'yi başlatıyoruz
                game2.tileMap = game2.tileMap; // Level 2 haritasını kullan
                break;
            case 3:
                game3 = new PacMan3();
                game3.tileMap = game3.tileMap; // Level 3 haritasını kullan
                break;
            case 4:
                game4 = new PacMan4();
                game4.tileMap = game4.tileMap; // Level 3 haritasını kullan
                break;
            case 5:
                game5 = new PacMan5();
                game5.tileMap = game5.tileMap; // Level 3 haritasını kullan
                break;
            default:
                game = new PacMan();
                game.tileMap = game.tileMap; // Varsayılan olarak level 1
                break;
        }

        // Eğer game2 null değilse, game2'yi JFrame'e ekle
        if (game != null) {
            gameFrame.add(game);
        }
        if (game2 != null) {
            gameFrame.add(game2);
        }
        if (game3 != null) {
            gameFrame.add(game3);
        }
        if (game4 != null) {
            gameFrame.add(game4);
        }
        if (game5 != null) {
            gameFrame.add(game5);
        }


        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
    public static void main(String[] args) {
        new PacmanMenu();
    }
}