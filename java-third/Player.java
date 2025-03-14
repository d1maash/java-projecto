import java.awt.*;

public class Player {
    private int x, y;
    private int width = 40;
    private int height = 40;
    private int baseSpeed = 5;
    private int speed;
    private int maxHealth = 3;
    private int health;
    private boolean movingLeft;
    private boolean movingRight;
    private PowerUp.PowerUpType activePowerUp = null;
    private int powerUpDuration = 0;
    private Color mainColor = new Color(30, 144, 255); // Dodger Blue
    private Color engineColor = new Color(255, 69, 0); // Red-Orange
    private Color cockpitColor = new Color(135, 206, 250); // Light Sky Blue

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = maxHealth;
        this.speed = baseSpeed;
    }

    public void update() {
        if (movingLeft && x > 0) {
            x -= speed;
        }
        if (movingRight && x < Game.WIDTH - width) {
            x += speed;
        }

        if (powerUpDuration > 0) {
            powerUpDuration--;
            if (powerUpDuration == 0) {
                deactivatePowerUp();
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Основной корпус корабля
        g2d.setColor(mainColor);
        Polygon ship = new Polygon();
        ship.addPoint(x + width / 2, y); // Нос корабля
        ship.addPoint(x, y + height); // Левый нижний угол
        ship.addPoint(x + width, y + height); // Правый нижний угол
        g2d.fillPolygon(ship);

        // Двигатели
        g2d.setColor(engineColor);
        g2d.fillRect(x + 5, y + height - 10, 10, 15); // Левый двигатель
        g2d.fillRect(x + width - 15, y + height - 10, 10, 15); // Правый двигатель

        // Кабина пилота
        g2d.setColor(cockpitColor);
        g2d.fillOval(x + width / 2 - 8, y + height / 3, 16, 16);

        // Если есть активный бонус, добавляем свечение
        if (activePowerUp != null) {
            g2d.setColor(getPowerUpColor());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawPolygon(ship);

            // Эффект свечения двигателей
            int engineGlow = (int) (Math.random() * 5);
            g2d.setColor(engineColor.brighter());
            g2d.fillRect(x + 5, y + height + engineGlow, 10, 5);
            g2d.fillRect(x + width - 15, y + height + engineGlow, 10, 5);
        }

        // Полоска здоровья
        drawHealthBar(g2d);
    }

    private void drawHealthBar(Graphics2D g2d) {
        int healthBarWidth = 50;
        int healthBarHeight = 5;
        int spacing = 2;

        for (int i = 0; i < maxHealth; i++) {
            if (i < health) {
                g2d.setColor(new Color(50, 205, 50)); // Зеленый
            } else {
                g2d.setColor(new Color(169, 169, 169)); // Серый
            }
            g2d.fillRect(10 + i * (healthBarWidth + spacing), 40, healthBarWidth, healthBarHeight);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(10 + i * (healthBarWidth + spacing), 40, healthBarWidth, healthBarHeight);
        }
    }

    private Color getPowerUpColor() {
        switch (activePowerUp) {
            case HEALTH:
                return new Color(50, 205, 50, 150); // Полупрозрачный зеленый
            case SPEED:
                return new Color(255, 215, 0, 150); // Полупрозрачный золотой
            case WEAPON:
                return new Color(255, 0, 0, 150); // Полупрозрачный красный
            default:
                return new Color(255, 255, 255, 150);
        }
    }

    public void applyPowerUp(PowerUp powerUp) {
        activePowerUp = powerUp.getType();
        powerUpDuration = 300; // 5 seconds at 60 FPS

        switch (powerUp.getType()) {
            case HEALTH:
                health = Math.min(health + 1, maxHealth);
                break;
            case SPEED:
                speed = baseSpeed * 2;
                break;
            case WEAPON:
                // Weapon upgrade logic will be handled in Game class
                break;
        }
    }

    private void deactivatePowerUp() {
        if (activePowerUp == PowerUp.PowerUpType.SPEED) {
            speed = baseSpeed;
        }
        activePowerUp = null;
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public PowerUp.PowerUpType getActivePowerUp() {
        return activePowerUp;
    }
}