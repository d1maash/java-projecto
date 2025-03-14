package com.game;

import org.joml.Vector3f;
import com.game.utils.ResourceManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameState {
    private Player3D player;
    private List<Enemy3D> enemies;
    private List<Bullet3D> bullets;
    private int score;
    private int wave;
    private Random random;
    private long lastEnemySpawnTime;
    private static final long ENEMY_SPAWN_DELAY = 2000; // 2 секунды

    public GameState() {
        player = new Player3D();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        score = 0;
        wave = 1;
        random = new Random();
        lastEnemySpawnTime = System.currentTimeMillis();
    }

    public void update() {
        // Обновление игрока
        handlePlayerInput();

        // Спавн врагов
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEnemySpawnTime > ENEMY_SPAWN_DELAY && enemies.size() < wave * 3) {
            spawnEnemy();
            lastEnemySpawnTime = currentTime;
        }

        // Обновление врагов
        for (Enemy3D enemy : enemies) {
            enemy.update(player.getPosition());
        }

        // Обновление пуль
        Iterator<Bullet3D> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet3D bullet = bulletIterator.next();
            bullet.update();

            // Проверка столкновений пули с врагами
            Iterator<Enemy3D> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy3D enemy = enemyIterator.next();
                if (bullet.checkCollision(enemy)) {
                    enemy.takeDamage(bullet.getDamage());
                    bullet.deactivate();
                    if (!enemy.isAlive()) {
                        enemyIterator.remove();
                        score += 100;
                    }
                    break;
                }
            }

            // Удаление неактивных пуль
            if (!bullet.isActive()) {
                bulletIterator.remove();
            }
        }

        // Проверка столкновений игрока с врагами
        for (Enemy3D enemy : enemies) {
            if (isCollision(player, enemy)) {
                player.takeDamage(enemy.getDamage());
            }
        }

        // Проверка условий новой волны
        if (enemies.isEmpty()) {
            startNewWave();
        }
    }

    private void spawnEnemy() {
        float angle = random.nextFloat() * 360;
        float distance = 10.0f;
        float x = (float) (Math.cos(Math.toRadians(angle)) * distance);
        float z = (float) (Math.sin(Math.toRadians(angle)) * distance);

        Enemy3D.EnemyType type;
        float typeRoll = random.nextFloat();
        if (typeRoll < 0.6f) {
            type = Enemy3D.EnemyType.BASIC;
        } else if (typeRoll < 0.9f) {
            type = Enemy3D.EnemyType.FAST;
        } else {
            type = Enemy3D.EnemyType.TANK;
        }

        enemies.add(new Enemy3D(type, x, 0, z));
    }

    private void startNewWave() {
        wave++;
        for (int i = 0; i < wave * 2; i++) {
            spawnEnemy();
        }
    }

    public void fireBullet() {
        Vector3f position = new Vector3f(player.getPosition());
        Vector3f direction = new Vector3f(
                -(float) Math.sin(Math.toRadians(player.getRotation().y)),
                0,
                -(float) Math.cos(Math.toRadians(player.getRotation().y)));
        bullets.add(new Bullet3D(position, direction));
    }

    private boolean isCollision(GameObject3D obj1, GameObject3D obj2) {
        Vector3f pos1 = obj1.getPosition();
        Vector3f pos2 = obj2.getPosition();
        float distance = pos1.distance(pos2);
        float combinedRadius = obj1.getScale() + obj2.getScale();
        return distance < combinedRadius;
    }

    public Player3D getPlayer() {
        return player;
    }

    public List<Enemy3D> getEnemies() {
        return enemies;
    }

    public List<Bullet3D> getBullets() {
        return bullets;
    }

    public int getScore() {
        return score;
    }

    public int getWave() {
        return wave;
    }

    private void handlePlayerInput() {
        // Обработка ввода будет реализована в основном игровом цикле
    }
}