import java.awt.*;
import java.awt.geom.AffineTransform;

public class PowerUp {
    private int x, y;
    private int width = 20;
    private int height = 20;
    private int speed = 3;
    private boolean active = true;
    private PowerUpType type;
    private float rotation = 0;

    public enum PowerUpType {
        HEALTH,
        SPEED,
        WEAPON
    }

    public PowerUp(int x, int y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        y += speed;
        rotation += 0.1f; // Вращение бонуса
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Сохраняем текущую трансформацию
        AffineTransform old = g2d.getTransform();

        // Устанавливаем точку вращения в центр бонуса
        g2d.rotate(rotation, x + width / 2, y + height / 2);

        switch (type) {
            case HEALTH:
                drawHealthPowerUp(g2d);
                break;
            case SPEED:
                drawSpeedPowerUp(g2d);
                break;
            case WEAPON:
                drawWeaponPowerUp(g2d);
                break;
        }

        // Восстанавливаем трансформацию
        g2d.setTransform(old);

        // Добавляем свечение
        drawGlow(g2d);
    }

    private void drawHealthPowerUp(Graphics2D g2d) {
        // Красный крест
        g2d.setColor(new Color(220, 20, 60));
        g2d.fillRect(x + width / 4, y + 2, width / 2, height - 4); // Вертикальная часть
        g2d.fillRect(x + 2, y + height / 4, width - 4, height / 2); // Горизонтальная часть

        // Белая окантовка
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x + width / 4, y + 2, width / 2, height - 4);
        g2d.drawRect(x + 2, y + height / 4, width - 4, height / 2);
    }

    private void drawSpeedPowerUp(Graphics2D g2d) {
        // Молния
        g2d.setColor(new Color(255, 215, 0));
        int[] xPoints = {
                x + width / 2, x + width / 4, x + width / 2,
                x + 3 * width / 4
        };
        int[] yPoints = {
                y + 2, y + height / 2, y + height / 2,
                y + height - 2
        };
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Белая окантовка
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawPolygon(xPoints, yPoints, 4);
    }

    private void drawWeaponPowerUp(Graphics2D g2d) {
        // Звезда
        g2d.setColor(new Color(255, 69, 0));
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int outerRadius = width / 2;
        int innerRadius = width / 4;

        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 2 + (Math.PI * 2 * i) / 10;
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = centerX + (int) (radius * Math.cos(angle));
            yPoints[i] = centerY - (int) (radius * Math.sin(angle));
        }
        g2d.fillPolygon(xPoints, yPoints, 10);

        // Белая окантовка
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawPolygon(xPoints, yPoints, 10);
    }

    private void drawGlow(Graphics2D g2d) {
        int glowRadius = 3;
        Color glowColor = getGlowColor();

        for (int i = glowRadius; i > 0; i--) {
            float alpha = 0.3f - (i * 0.1f);
            if (alpha > 0) {
                g2d.setColor(new Color(
                        glowColor.getRed() / 255f,
                        glowColor.getGreen() / 255f,
                        glowColor.getBlue() / 255f,
                        alpha));
                g2d.setStroke(new BasicStroke(i * 2));
                g2d.drawRect(x - i, y - i, width + i * 2, height + i * 2);
            }
        }
    }

    private Color getGlowColor() {
        switch (type) {
            case HEALTH:
                return new Color(220, 20, 60);
            case SPEED:
                return new Color(255, 215, 0);
            case WEAPON:
                return new Color(255, 69, 0);
            default:
                return Color.WHITE;
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

    public PowerUpType getType() {
        return type;
    }

    public int getY() {
        return y;
    }
}