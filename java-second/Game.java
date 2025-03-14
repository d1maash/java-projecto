import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.sampled.*;
import java.io.File;

public class Game extends JPanel implements KeyListener, Runnable {
    private static final int GAME_WIDTH = 800;
    private static final int GAME_HEIGHT = 600;
    private static final int DELAY = 16; // ~60 FPS

    private Player player;
    private AIPlayer aiPlayer;
    private Track track;
    private boolean isRunning;
    private Thread gameThread;
    private JFrame frame;
    private JPanel mainContainer;
    private CardLayout cardLayout;

    // Новые поля
    private int playerLaps = 0;
    private int aiLaps = 0;
    private long startTime;
    private long currentTime;
    private boolean gameFinished = false;
    private Clip engineSound;
    private Clip collisionSound;
    private boolean soundEnabled = true;

    public Game(JFrame frame, JPanel mainContainer, CardLayout cardLayout) {
        this.frame = frame;
        this.mainContainer = mainContainer;
        this.cardLayout = cardLayout;

        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(new Color(34, 139, 34)); // Темно-зеленый цвет травы
        setFocusable(true);
        addKeyListener(this);

        player = new Player(100, GAME_HEIGHT / 2);
        aiPlayer = new AIPlayer(100, GAME_HEIGHT / 2 + 100);
        track = new Track();
        isRunning = true;
        startTime = System.currentTimeMillis();

        loadSounds();
    }

    private void loadSounds() {
        try {
            // Загрузка звука двигателя
            AudioInputStream engineStream = AudioSystem.getAudioInputStream(new File("sounds/engine.wav"));
            engineSound = AudioSystem.getClip();
            engineSound.open(engineStream);
            engineSound.loop(Clip.LOOP_CONTINUOUSLY);

            // Загрузка звука столкновения
            AudioInputStream collisionStream = AudioSystem.getAudioInputStream(new File("sounds/collision.wav"));
            collisionSound = AudioSystem.getClip();
            collisionSound.open(collisionStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
        if (soundEnabled && engineSound != null) {
            engineSound.start();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            update();
            checkCollisions();
            updateLapsAndTime();
            repaint();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkCollisions() {
        // Проверка столкновения с границами трассы
        if (!track.isOnTrack(player.getX(), player.getY())) {
            player.handleCollision();
            playCollisionSound();
        }

        // Проверка столкновения между машинами
        double distance = Math.sqrt(
                Math.pow(player.getX() - aiPlayer.getX(), 2) +
                        Math.pow(player.getY() - aiPlayer.getY(), 2));

        if (distance < 40) { // Размер машины примерно 40 пикселей
            player.handleCollision();
            aiPlayer.handleCollision();
            playCollisionSound();
        }
    }

    private void playCollisionSound() {
        if (soundEnabled && collisionSound != null) {
            collisionSound.setFramePosition(0);
            collisionSound.start();
        }
    }

    private void updateLapsAndTime() {
        currentTime = (System.currentTimeMillis() - startTime) / 1000;

        // Обновление кругов для игрока
        if (track.hasCompletedLap(player.getX(), player.getY(), true)) {
            playerLaps++;
            if (playerLaps >= 3) { // Игра заканчивается после 3 кругов
                gameFinished = true;
                handleGameFinish();
            }
        }

        // Обновление кругов для ИИ
        if (track.hasCompletedLap(aiPlayer.getX(), aiPlayer.getY(), false)) {
            aiLaps++;
        }
    }

    private void handleGameFinish() {
        isRunning = false;
        if (engineSound != null) {
            engineSound.stop();
        }
        ScoreManager.addTime(currentTime);

        // Показываем диалог с результатами
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "Игра завершена!\nВремя: " + currentTime + " секунд\nКруги: " + playerLaps,
                    "Результаты", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainContainer, "menu");
        });
    }

    private void update() {
        if (!gameFinished) {
            player.update();
            aiPlayer.update(track);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Рисуем фон
        drawBackground(g);

        // Рисуем игровые объекты
        track.draw(g);
        player.draw(g);
        aiPlayer.draw(g);

        // Рисуем интерфейс
        drawHUD(g);
    }

    private void drawBackground(Graphics g) {
        // Рисуем траву
        g.setColor(new Color(34, 139, 34));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Рисуем декоративные элементы (деревья, кусты и т.д.)
        g.setColor(new Color(0, 100, 0));
        for (int i = 0; i < 20; i++) {
            int x = (int) (Math.random() * GAME_WIDTH);
            int y = (int) (Math.random() * GAME_HEIGHT);
            if (!track.isOnTrack(x, y)) {
                g.fillOval(x, y, 20, 20);
            }
        }
    }

    private void drawHUD(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        // Отображаем круги
        g.drawString("Круг: " + playerLaps + "/3", 20, 30);

        // Отображаем время
        g.drawString("Время: " + currentTime + " сек", 20, 60);

        // Отображаем скорость
        g.drawString("Скорость: " + (int) (player.getSpeed() * 10) + " км/ч", 20, 90);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cardLayout.show(mainContainer, "menu");
            if (engineSound != null) {
                engineSound.stop();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("2D Racing Game");
        Game game = new Game(frame, null, null);
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.startGame();
    }
}