import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class Player {
    private double x, y;
    private double speed;
    private double angle;
    private double acceleration;
    private double turnSpeed;
    private boolean accelerating;
    private boolean turning;
    private int turnDirection; // -1 влево, 1 вправо
    private boolean damaged = false;
    private long damageTime = 0;
    private static final long DAMAGE_DURATION = 1000; // 1 секунда

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 0;
        this.angle = 0;
        this.acceleration = 0.2;
        this.turnSpeed = 0.1;
        this.accelerating = false;
        this.turning = false;
    }

    public void handleCollision() {
        if (!damaged) {
            damaged = true;
            damageTime = System.currentTimeMillis();
            speed *= -0.5; // Отскок при столкновении
        }
    }

    public void update() {
        // Проверяем состояние повреждения
        if (damaged && System.currentTimeMillis() - damageTime > DAMAGE_DURATION) {
            damaged = false;
        }

        if (!damaged) {
            if (accelerating) {
                speed += acceleration;
            } else {
                speed *= 0.95; // трение
            }

            speed = Math.min(10, Math.max(-5, speed));

            if (turning) {
                angle += turnSpeed * turnDirection;
            }
        } else {
            // Замедление при повреждении
            speed *= 0.9;
        }

        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;

        // Ограничиваем движение в пределах экрана
        x = Math.max(0, Math.min(x, 800));
        y = Math.max(0, Math.min(y, 600));
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(damaged ? Color.RED : Color.BLUE);

        // Сохраняем текущую трансформацию
        AffineTransform old = g2d.getTransform();

        // Перемещаем и поворачиваем машину
        g2d.translate(x, y);
        g2d.rotate(angle);

        // Рисуем машину с улучшенной графикой
        if (!damaged) {
            // Основной корпус
            g2d.setColor(Color.BLUE);
            g2d.fillRect(-20, -10, 40, 20);

            // Кабина
            g2d.setColor(Color.CYAN);
            g2d.fillRect(-10, -8, 15, 16);

            // Колеса
            g2d.setColor(Color.BLACK);
            g2d.fillRect(-15, -12, 8, 4); // Левое переднее
            g2d.fillRect(-15, 8, 8, 4); // Левое заднее
            g2d.fillRect(7, -12, 8, 4); // Правое переднее
            g2d.fillRect(7, 8, 8, 4); // Правое заднее
        } else {
            // Поврежденная машина
            g2d.setColor(Color.RED);
            g2d.fillRect(-20, -10, 40, 20);
            g2d.setColor(Color.ORANGE);
            g2d.drawLine(-20, -10, 20, 10);
            g2d.drawLine(-20, 10, 20, -10);
        }

        // Восстанавливаем трансформацию
        g2d.setTransform(old);
    }

    public void keyPressed(KeyEvent e) {
        if (!damaged) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    accelerating = true;
                    break;
                case KeyEvent.VK_DOWN:
                    speed = -2;
                    break;
                case KeyEvent.VK_LEFT:
                    turning = true;
                    turnDirection = -1;
                    break;
                case KeyEvent.VK_RIGHT:
                    turning = true;
                    turnDirection = 1;
                    break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                accelerating = false;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                turning = false;
                break;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }
}