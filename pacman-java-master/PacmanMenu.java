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
        for (int i = 1; i <= 3; i++) {
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

        // PacMan oyununu başlat
        JFrame gameFrame = new JFrame("PacMan - Level " + level);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Her level için farklı harita kullanılabilir
        PacMan game = new PacMan();
        game.tileMap = getTileMapForLevel(level); // Seçilen seviyeye uygun haritayı al

        gameFrame.add(game);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }

    private String[] getTileMapForLevel(int level) {
        // Haritalar
        String[] level1Map = {
                "XXXXXXXXXXXXXXXXXXX",
                "XXXXX    X        X",
                "X XX XXX X XXX XX X",
                "X                 X",
                "X XX X XXXXX X XX X",
                "X    X       X    X",
                "XXXX XXXX XXXX XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXrXX X XXXX",
                "O       bpo       O",
                "XXXX X XXXXX X XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXXXX X XXXX",
                "X        X        X",
                "X XX XXX X XXX XX X",
                "X  X     P     X  X",
                "XX X X XXXXX X X XX",
                "X    X   X   X    X",
                "X XXXXXX X XXXXXX X",
                "X                 X",
                "XXXXXXXXXXXXXXXXXXX"
        };

        String[] level2Map = {
                "XXXXXXXXXXXXXXXXXXX",
                "X        X        X",
                "X XX XXX X XXX XX X",
                "X                 X",
                "X XX X XXXXX X XX X",
                "X    X       X    X",
                "XXXX XXXX XXXX XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXrXX X XXXX",
                "O       bpo       O",
                "XXXX X XXXXX X XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXXXX X XXXX",
                "X        X        X",
                "X XX XXX X XXX XX X",
                "X  X     P     X  X",
                "XX X X XXXXX X X XX",
                "X    X   X   X    X",
                "X XXXXXX X XXXXXX X",
                "X X               X",
                "XXXXXXXXXXXXXXXXXXX"
        };

        String[] level3Map = {
                "XXXXXXXXXXXXXXXXXXX",
                "XX       X        X",
                "X XX XXX X XXX XX X",
                "X                 X",
                "X XX X XXXXX X XX X",
                "X    X       X    X",
                "XXXX XXXX XXXX XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXrXX X XXXX",
                "O       bpo       O",
                "XXXX X XXXXX X XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXXXX X XXXX",
                "X        X        X",
                "X XX XXX X XXX XX X",
                "X  X     P     X  X",
                "XX X X XXXXX X X XX",
                "X    X   X   X    X",
                "X XXXXXX X XXXXXX X",
                "X                 X",
                "XXXXXXXXXXXXXXXXXXX"
        };

        // Seviye numarasına göre uygun haritayı döndür
        if (level == 1) {
            return level1Map;
        } else if (level == 2) {
            return level2Map;
        } else if (level == 3) {
            return level3Map;
        } else {
            return level1Map; // Varsayılan olarak level 1
        }
    }

    public static void main(String[] args) {
        new PacmanMenu();
    }
}