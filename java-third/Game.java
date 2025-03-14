import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private Player player;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<PowerUp> powerUps;
    private Timer timer;
    private Random random;
    private boolean isRunning;
    private int score;
    private int waveNumber = 1;
    private int enemiesInWave = 5;
    private int enemiesSpawned = 0;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        player = new Player(WIDTH / 2, HEIGHT - 50);
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        powerUps = new ArrayList<>();
        random = new Random();
        score = 0;
        isRunning = true;

        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isRunning) {
            player.draw(g);

            for (Bullet bullet : bullets) {
                bullet.draw(g);
            }

            for (Enemy enemy : enemies) {
                enemy.draw(g);
            }

            for (PowerUp powerUp : powerUps) {
                powerUp.draw(g);
            }

            // Draw score and wave
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 10, 20);
            g.drawString("Wave: " + waveNumber, WIDTH - 100, 20);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Game Over!", WIDTH / 2 - 120, HEIGHT / 2);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Score: " + score, WIDTH / 2 - 50, HEIGHT / 2 + 40);
            g.drawString("Wave: " + waveNumber, WIDTH / 2 - 50, HEIGHT / 2 + 70);
            g.drawString("Press SPACE to restart", WIDTH / 2 - 100, HEIGHT / 2 + 100);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isRunning)
            return;

        // Update player
        player.update();

        // Update bullets
        bullets.removeIf(bullet -> !bullet.isActive());
        for (Bullet bullet : bullets) {
            bullet.update();
        }

        // Spawn enemies
        if (enemiesSpawned < enemiesInWave && random.nextInt(100) < 5) {
            Enemy.EnemyType type;
            int rand = random.nextInt(100);
            if (rand < 60) {
                type = Enemy.EnemyType.BASIC;
            } else if (rand < 85) {
                type = Enemy.EnemyType.FAST;
            } else {
                type = Enemy.EnemyType.TANK;
            }
            enemies.add(new Enemy(random.nextInt(WIDTH - 20), 0, type));
            enemiesSpawned++;
        }

        // Check if wave is complete
        if (enemiesSpawned >= enemiesInWave && enemies.isEmpty()) {
            waveNumber++;
            enemiesInWave += 2;
            enemiesSpawned = 0;
        }

        // Update enemies
        enemies.removeIf(enemy -> !enemy.isActive());
        for (Enemy enemy : enemies) {
            enemy.update();
            if (enemy.getY() > HEIGHT) {
                enemy.setActive(false);
            }
        }

        // Spawn power-ups
        if (random.nextInt(1000) < 5) {
            PowerUp.PowerUpType type = PowerUp.PowerUpType.values()[random
                    .nextInt(PowerUp.PowerUpType.values().length)];
            powerUps.add(new PowerUp(random.nextInt(WIDTH - 20), 0, type));
        }

        // Update power-ups
        powerUps.removeIf(powerUp -> !powerUp.isActive());
        for (PowerUp powerUp : powerUps) {
            powerUp.update();
            if (powerUp.getY() > HEIGHT) {
                powerUp.setActive(false);
            }
        }

        // Check collisions
        checkCollisions();

        // Check game over
        if (!player.isAlive()) {
            isRunning = false;
            timer.stop();
        }

        repaint();
    }

    private void checkCollisions() {
        // Check bullet-enemy collisions
        for (Bullet bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bullet.setActive(false);
                    enemy.hit();
                    if (!enemy.isActive()) {
                        score += enemy.getPoints();
                    }
                }
            }
        }

        // Check player-enemy collisions
        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                enemy.setActive(false);
                player.hit();
            }
        }

        // Check player-powerup collisions
        for (PowerUp powerUp : powerUps) {
            if (player.getBounds().intersects(powerUp.getBounds())) {
                powerUp.setActive(false);
                player.applyPowerUp(powerUp);
            }
        }
    }

    private void resetGame() {
        player = new Player(WIDTH / 2, HEIGHT - 50);
        bullets.clear();
        enemies.clear();
        powerUps.clear();
        score = 0;
        waveNumber = 1;
        enemiesInWave = 5;
        enemiesSpawned = 0;
        isRunning = true;
        timer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isRunning) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    player.setMovingLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setMovingRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    if (player.getActivePowerUp() == PowerUp.PowerUpType.WEAPON) {
                        // Triple shot
                        bullets.add(new Bullet(player.getX() + player.getWidth() / 2, player.getY()));
                        bullets.add(new Bullet(player.getX() + player.getWidth() / 2 - 20, player.getY()));
                        bullets.add(new Bullet(player.getX() + player.getWidth() / 2 + 20, player.getY()));
                    } else {
                        bullets.add(new Bullet(player.getX() + player.getWidth() / 2, player.getY()));
                    }
                    break;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.setMovingLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                player.setMovingRight(false);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Shooter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new Game());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}