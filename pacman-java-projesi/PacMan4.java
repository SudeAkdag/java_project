import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Iterator;
import javax.swing.*;

public class PacMan4 extends JPanel implements ActionListener, KeyListener {
    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'U'; // U D L R
        int velocityX = 0;
        int velocityY = 0;
        char ghostType;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize/4;
            }
            else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if (this.direction == 'L') {
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }
            else if (this.direction == 'R') {
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    public String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XXXXXX X XXXXXX X",
            "XCX             X X",
            "X X XX XXXXX XX X X",
            "X    X       X    X",
            "XXXX X XX XX X XXXX",
            "X    X bopr  X    X",
            "X X XX XX XX XX X X",
            "X X             X X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "X XX XXX X XXX XX X",
            "X    X   X   X   CX",
            "XX X X XXXXX X X XX",
            "X  X     P     X  X",
            "X XX XXX X XXX XX X",
            "X    X   X   X    X",
            "X XX X XXXXX X XX X",
            "X C  X       X    X",
            "XXXXXXXXXXXXXXXXXXX"
    };


    static HashSet<Block> walls;
    static HashSet<Block> foods;
    static HashSet<Block> ghosts;
    static Block pacman;
    private int ghostCount = 4; // Varsayılan hayalet sayısı
    public void setGhostCount(int count) {
        ghostCount = count; // Yeni hayalet sayısını ayarla
        loadMap();          // Haritayı yeni hayalet sayısıyla yeniden yükle
    }
    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'}; //up down left right
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    private Image scaredGhostImage;
    private Image cherryImage;
    private HashSet<PacMan4.Block> powerUps;
    private boolean isPoweredUp = false;
    private int powerUpTimer = 0;
    private static final int POWER_UP_DURATION = 300; // 15 saniye (50ms * 300)
    private static final int CHERRY_SCORE = 300; // Kiraz yeme puanı

    PacMan4() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("./wall4.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();


        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        scaredGhostImage = new ImageIcon(getClass().getResource("./scaredGhost.png")).getImage();
        cherryImage = new ImageIcon(getClass().getResource("./cherry.png")).getImage();
        powerUps = new HashSet<>();

        loadMap();
        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        //how long it takes to start timer, milliseconds gone between frames
        gameLoop = new Timer(28, this); //20fps (1000/50)
        gameLoop.start();

    }

    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();
        powerUps.clear();

        int currentGhostCount = 0; // Şu an eklenen hayaletlerin sayısını tutar

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') { // Duvar
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (isGhost(tileMapChar) && currentGhostCount < ghostCount) {
                    // Hayalet ekleme kontrolü
                    Block ghost = createGhost(tileMapChar, x, y);
                    ghosts.add(ghost);
                    currentGhostCount++; // Eklenen hayalet sayısını artır
                } else if (tileMapChar == 'P') { // Pac-Man
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == ' ') { // Yem
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                } else if (tileMapChar == 'C') { // Güç yemi
                    PacMan4.Block cherry = new PacMan4.Block(cherryImage, x, y, tileSize, tileSize);
                    powerUps.add(cherry);
                }
            }
        }
    }


    // Hayalet mi? Kontrol fonksiyonu
    private boolean isGhost(char tile) {
        return tile == 'b' || tile == 'o' || tile == 'p' || tile == 'r';
    }

    // Hayalet yaratma fonksiyonu
    private PacMan4.Block createGhost(char tile, int x, int y) {
        PacMan4.Block ghost = new PacMan4.Block(getGhostImage(tile), x, y, tileSize, tileSize);
        ghost.ghostType = tile; // Hayalet tipini kaydet
        return ghost;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void setGameSpeed(int fps) {
        int delay = 1000 / fps; // Frame başına milisaniye
        gameLoop.setDelay(delay);
    }

    public void draw(Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        //score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }

        // Güç yemlerini çiz
        for (PacMan4.Block powerUp : powerUps) {
            g.drawImage(powerUp.image, powerUp.x, powerUp.y, powerUp.width, powerUp.height, null);
        }
    }

    public void move() {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        //check wall collisions
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        //check ghost collisions
        for (PacMan4.Block ghost : ghosts) {
            if (collision(ghost, pacman)) {
                if (isPoweredUp) {
                    // Hayaleti ye
                    ghost.reset();
                    score += 200;
                } else {
                    lives--;
                    if (lives <= 0) {
                        gameOver = true;
                    }
                    resetPositions();
                }
            }

            if (ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls) {
                if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }

        //check food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }

        // Güç yemi kontrolü
        Iterator<PacMan4.Block> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PacMan4.Block powerUp = powerUpIterator.next();
            if (collision(pacman, powerUp)) {
                powerUpIterator.remove();
                isPoweredUp = true;
                powerUpTimer = POWER_UP_DURATION;
                // Hayaletleri korkmuş moda geçir
                for (PacMan4.Block ghost : ghosts) {
                    ghost.image = scaredGhostImage;
                }
            }
        }


        // Güç modu süresi kontrolü
        if (isPoweredUp) {
            powerUpTimer--;
            if (powerUpTimer <= 0) {
                isPoweredUp = false;
                // Hayaletleri normal görünüme döndür
                for (PacMan4.Block ghost : ghosts) {
                    ghost.image = getGhostImage(ghost.ghostType);
                }
            }

            PacMan4.Block cherryEaten = null;
            for (PacMan4.Block powerUp : powerUps) {
                if (collision(pacman, powerUp)) {
                    cherryEaten = powerUp;
                    score += 300;
                }
            }
            powerUps.remove(cherryEaten);

        }
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }

    // Hayalet görüntüsünü döndüren yardımcı metod
    private Image getGhostImage(char type) {
        return switch (type) {
            case 'b' -> blueGhostImage;
            case 'o' -> orangeGhostImage;
            case 'p' -> pinkGhostImage;
            case 'r' -> redGhostImage;
            default -> blueGhostImage;
        };
    }
}