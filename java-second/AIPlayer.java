import java.awt.*;
import java.awt.geom.AffineTransform;

public class AIPlayer {
    private double x, y;
    private double speed;
    private double angle;
    private double acceleration;
    private double turnSpeed;
    private int currentWaypoint;
    private boolean damaged = false;
    private long damageTime = 0;
    private static final long DAMAGE_DURATION = 1000; // 1 секунда

    public AIPlayer(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 0;
        this.angle = 0;
        this.acceleration = 0.15; // Немного медленнее игрока
        this.turnSpeed = 0.08;
        this.currentWaypoint = 0;
    }

    public void handleCollision() {
        if (!damaged) {
            damaged = true;
            damageTime = System.currentTimeMillis();
            speed *= -0.5; // Отскок при столкновении
        }
    }

    public void update(Track track) {
        // Проверяем состояние повреждения
        if (damaged && System.currentTimeMillis() - damageTime > DAMAGE_DURATION) {
            damaged = false;
        }

        if (!damaged) {
            // Получаем следующую точку пути
            Point target = track.getWaypoint(currentWaypoint);

            // Вычисляем угол к цели
            double targetAngle = Math.atan2(target.y - y, target.x - x);

            // Поворачиваем к цели
            double angleDiff = targetAngle - angle;

            // Нормализуем угол
            while (angleDiff > Math.PI)
                angleDiff -= 2 * Math.PI;
            while (angleDiff < -Math.PI)
                angleDiff += 2 * Math.PI;

            // Поворачиваем
            if (Math.abs(angleDiff) > 0.1) {
                angle += turnSpeed * Math.signum(angleDiff);
            }

            // Ускоряемся
            speed += acceleration;
            speed = Math.min(8, Math.max(-4, speed)); // Максимальная скорость немного ниже игрока
        } else {
            // Замедление при повреждении
            speed *= 0.9;
        }

        // Обновляем позицию
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;

        // Ограничиваем движение в пределах экрана
        x = Math.max(0, Math.min(x, 800));
        y = Math.max(0, Math.min(y, 600));

        // Проверяем достижение текущей точки
        if (!damaged) {
            Point target = track.getWaypoint(currentWaypoint);
            if (distanceTo(target) < 50) {
                currentWaypoint = (currentWaypoint + 1) % track.getWaypointCount();
            }
        }
    }

    private double distanceTo(Point target) {
        return Math.sqrt(Math.pow(target.x - x, 2) + Math.pow(target.y - y, 2));
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(damaged ? Color.ORANGE : Color.RED);

        AffineTransform old = g2d.getTransform();

        g2d.translate(x, y);
        g2d.rotate(angle);

        // Рисуем ИИ машину с улучшенной графикой
        if (!damaged) {
            // Основной корпус
            g2d.setColor(Color.RED);
            g2d.fillRect(-20, -10, 40, 20);

            // Кабина
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(-10, -8, 15, 16);

            // Колеса
            g2d.setColor(Color.BLACK);
            g2d.fillRect(-15, -12, 8, 4); // Левое переднее
            g2d.fillRect(-15, 8, 8, 4); // Левое заднее
            g2d.fillRect(7, -12, 8, 4); // Правое переднее
            g2d.fillRect(7, 8, 8, 4); // Правое заднее
        } else {
            // Поврежденная машина
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(-20, -10, 40, 20);
            g2d.setColor(Color.RED);
            g2d.drawLine(-20, -10, 20, 10);
            g2d.drawLine(-20, 10, 20, -10);
        }

        g2d.setTransform(old);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}