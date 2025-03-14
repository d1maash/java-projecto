import java.awt.*;

public class Bullet {
    private int x, y;
    private int width = 4;
    private int height = 10;
    private int speed = 10;
    private boolean active = true;
    private Color bulletColor = new Color(255, 69, 0); // Красно-оранжевый
    private Color glowColor = new Color(255, 140, 0, 100); // Полупрозрачный оранжевый

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= speed;
        if (y < 0) {
            active = false;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Свечение пули
        g2d.setColor(glowColor);
        g2d.fillOval(x - width, y - 2, width * 2, height + 4);

        // Основная часть пули
        g2d.setColor(bulletColor);
        g2d.fillOval(x - width / 2, y, width, height);

        // Яркий центр пули
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - width / 4, y + height / 4, width / 2, height / 2);

        // Эффект движения
        int trailLength = 5;
        for (int i = 0; i < trailLength; i++) {
            int alpha = 150 - (i * 30);
            if (alpha > 0) {
                g2d.setColor(new Color(255, 140, 0, alpha));
                g2d.fillOval(x - width / 2, y + height + i * 2, width, 2);
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}