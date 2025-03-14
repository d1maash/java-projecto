import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Track {
    private List<Point> waypoints;
    private List<Point> trackPoints;
    private static final int TRACK_WIDTH = 100;
    private Rectangle startLine;
    private boolean playerCrossedStart = false;
    private boolean aiCrossedStart = false;

    public Track() {
        waypoints = new ArrayList<>();
        trackPoints = new ArrayList<>();

        // Добавляем точки пути для более сложной трассы
        waypoints.add(new Point(200, 200)); // Старт
        waypoints.add(new Point(600, 200)); // Прямая
        waypoints.add(new Point(700, 300)); // Поворот
        waypoints.add(new Point(600, 400)); // Нижняя прямая
        waypoints.add(new Point(400, 500)); // Крутой поворот
        waypoints.add(new Point(200, 400)); // Возврат
        waypoints.add(new Point(100, 300)); // Финальный поворот

        // Создаем стартовую линию
        startLine = new Rectangle(190, 180, 20, 40);

        // Создаем точки для отрисовки трассы
        generateTrackPoints();
    }

    private void generateTrackPoints() {
        for (int i = 0; i < waypoints.size(); i++) {
            Point current = waypoints.get(i);
            Point next = waypoints.get((i + 1) % waypoints.size());

            // Добавляем точки для создания границ трассы
            trackPoints.add(new Point(current.x, current.y));

            // Добавляем дополнительные точки между waypoints для сглаживания
            int steps = 10;
            for (int j = 1; j < steps; j++) {
                float t = j / (float) steps;
                int x = (int) (current.x + (next.x - current.x) * t);
                int y = (int) (current.y + (next.y - current.y) * t);
                trackPoints.add(new Point(x, y));
            }
        }
    }

    public boolean isOnTrack(double x, double y) {
        for (int i = 0; i < trackPoints.size(); i++) {
            Point current = trackPoints.get(i);
            Point next = trackPoints.get((i + 1) % trackPoints.size());

            // Проверяем расстояние от точки до линии трассы
            double distance = pointToLineDistance(x, y, current.x, current.y, next.x, next.y);
            if (distance <= TRACK_WIDTH / 2) {
                return true;
            }
        }
        return false;
    }

    private double pointToLineDistance(double px, double py, double x1, double y1, double x2, double y2) {
        double A = px - x1;
        double B = py - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;

        if (len_sq != 0) {
            param = dot / len_sq;
        }

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = px - xx;
        double dy = py - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean hasCompletedLap(double x, double y, boolean isPlayer) {
        boolean crossedNow = startLine.contains(x, y);
        if (isPlayer) {
            if (crossedNow && !playerCrossedStart) {
                playerCrossedStart = true;
                return true;
            } else if (!crossedNow) {
                playerCrossedStart = false;
            }
        } else {
            if (crossedNow && !aiCrossedStart) {
                aiCrossedStart = true;
                return true;
            } else if (!crossedNow) {
                aiCrossedStart = false;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Рисуем основную трассу
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < trackPoints.size(); i++) {
            Point current = trackPoints.get(i);
            Point next = trackPoints.get((i + 1) % trackPoints.size());

            g2d.setStroke(new BasicStroke(TRACK_WIDTH));
            g2d.drawLine(current.x, current.y, next.x, next.y);
        }

        // Рисуем границы трассы
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < trackPoints.size(); i++) {
            Point current = trackPoints.get(i);
            Point next = trackPoints.get((i + 1) % trackPoints.size());
            g2d.drawLine(current.x - TRACK_WIDTH / 2, current.y, next.x - TRACK_WIDTH / 2, next.y);
            g2d.drawLine(current.x + TRACK_WIDTH / 2, current.y, next.x + TRACK_WIDTH / 2, next.y);
        }

        // Рисуем стартовую линию
        g2d.setColor(Color.WHITE);
        g2d.fill(startLine);

        // Рисуем контрольные точки
        g2d.setColor(Color.YELLOW);
        for (Point p : waypoints) {
            g2d.fillOval(p.x - 5, p.y - 5, 10, 10);
        }
    }

    public Point getWaypoint(int index) {
        return waypoints.get(index);
    }

    public int getWaypointCount() {
        return waypoints.size();
    }
}