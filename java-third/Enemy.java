import java.awt.*;

public class Enemy {
    private int x, y;
    private int width = 30;
    private int height = 30;
    private int speed;
    private int health;
    private boolean active = true;
    private EnemyType type;
    private int points;

    public enum EnemyType {
        BASIC(2, 1, 10),
        FAST(3, 1, 15),
        TANK(1, 3, 20);

        private final int speed;
        private final int health;
        private final int points;

        EnemyType(int speed, int health, int points) {
            this.speed = speed;
            this.health = health;
            this.points = points;
        }
    }

    public Enemy(int x, int y, EnemyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.speed = type.speed;
        this.health = type.health;
        this.points = type.points;
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (type) {
            case BASIC:
                drawBasicEnemy(g2d);
                break;
            case FAST:
                drawFastEnemy(g2d);
                break;
            case TANK:
                drawTankEnemy(g2d);
                break;
        }

        // Отрисовка полоски здоровья
        drawHealthBar(g2d);
    }

    private void drawBasicEnemy(Graphics2D g2d) {
        // Основной корпус
        g2d.setColor(new Color(50, 205, 50));
        g2d.fillOval(x, y, width, height);

        // Детали
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillOval(x + width / 4, y + height / 4, width / 2, height / 2);

        // Антенны
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(x + width / 3, y, x + width / 3, y - 5);
        g2d.drawLine(x + 2 * width / 3, y, x + 2 * width / 3, y - 5);
    }

    private void drawFastEnemy(Graphics2D g2d) {
        // Основной корпус - треугольная форма
        g2d.setColor(new Color(255, 215, 0));
        int[] xPoints = { x + width / 2, x, x + width };
        int[] yPoints = { y, y + height, y + height };
        g2d.fillPolygon(xPoints, yPoints, 3);

        // Детали
        g2d.setColor(new Color(218, 165, 32));
        g2d.fillOval(x + width / 4, y + height / 2, width / 2, height / 3);

        // Реактивный след
        g2d.setColor(new Color(255, 140, 0, 150));
        int[] xTrail = { x + width / 3, x + width / 2, x + 2 * width / 3 };
        int[] yTrail = { y + height, y + height + 10, y + height };
        g2d.fillPolygon(xTrail, yTrail, 3);
    }

    private void drawTankEnemy(Graphics2D g2d) {
        // Основной корпус - прямоугольная форма
        g2d.setColor(new Color(178, 34, 34));
        g2d.fillRect(x, y, width, height);

        // Броня
        g2d.setColor(new Color(139, 0, 0));
        g2d.fillRect(x + 3, y + 3, width - 6, height - 6);

        // Пушка
        g2d.setColor(new Color(178, 34, 34));
        g2d.fillRect(x + width / 2 - 3, y + height - 5, 6, 10);

        // Детали брони
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(x + 5, y + 5, x + width - 5, y + 5);
        g2d.drawLine(x + 5, y + height - 5, x + width - 5, y + height - 5);
    }

    private void drawHealthBar(Graphics2D g2d) {
        int barWidth = width;
        int barHeight = 3;
        int currentWidth = (int) ((float) health / type.health * barWidth);

        // Фон полоски здоровья
        g2d.setColor(new Color(169, 169, 169));
        g2d.fillRect(x, y - 5, barWidth, barHeight);

        // Текущее здоровье
        g2d.setColor(new Color(255, 0, 0));
        g2d.fillRect(x, y - 5, currentWidth, barHeight);
    }

    public void hit() {
        health--;
        if (health <= 0) {
            active = false;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getY() {
        return y;
    }

    public int getPoints() {
        return points;
    }
}