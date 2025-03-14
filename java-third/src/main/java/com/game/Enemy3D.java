package com.game;

import org.joml.Vector3f;
import com.game.utils.ResourceManager;

public class Enemy3D extends GameObject3D {
    public enum EnemyType {
        BASIC(100, 0.05f, 10),
        FAST(50, 0.1f, 5),
        TANK(200, 0.03f, 20);

        private final float maxHealth;
        private final float speed;
        private final float damage;

        EnemyType(float maxHealth, float speed, float damage) {
            this.maxHealth = maxHealth;
            this.speed = speed;
            this.damage = damage;
        }
    }

    private EnemyType type;
    private float health;
    private float speed;
    private float damage;

    public Enemy3D(EnemyType type, float x, float y, float z) {
        super(ResourceManager.getMesh("cube"));
        this.type = type;
        this.health = type.maxHealth;
        this.speed = type.speed;
        this.damage = type.damage;

        setPosition(x, y, z);
        setScale(type == EnemyType.TANK ? 0.8f : 0.5f);

        // Установка цвета в зависимости от типа
        switch (type) {
            case BASIC:
                // Базовый враг остается красным (цвет по умолчанию)
                break;
            case FAST:
                // Быстрый враг будет синим
                break;
            case TANK:
                // Танк будет зеленым
                break;
        }
    }

    public void update(Vector3f playerPosition) {
        // Вычисляем направление к игроку
        Vector3f direction = new Vector3f(playerPosition).sub(getPosition()).normalize();

        // Обновляем позицию
        Vector3f position = getPosition();
        position.x += direction.x * speed;
        position.y += direction.y * speed;
        position.z += direction.z * speed;
        setPosition(position.x, position.y, position.z);

        // Поворачиваем врага в сторону игрока
        float angle = (float) Math.toDegrees(Math.atan2(direction.x, direction.z));
        setRotation(0, angle, 0);
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public float getDamage() {
        return damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public EnemyType getType() {
        return type;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return type.maxHealth;
    }
}