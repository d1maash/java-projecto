package com.game;

import org.joml.Vector3f;
import com.game.utils.ResourceManager;

public class Bullet3D extends GameObject3D {
    private static final float SPEED = 0.3f;
    private static final float DAMAGE = 20;
    private Vector3f direction;
    private boolean active;

    public Bullet3D(Vector3f position, Vector3f direction) {
        super(ResourceManager.getMesh("cube"));
        this.direction = new Vector3f(direction).normalize();
        this.active = true;

        setPosition(position.x, position.y, position.z);
        setScale(0.1f);

        // Вычисляем углы поворота для направления пули
        float yaw = (float) Math.toDegrees(Math.atan2(direction.x, direction.z));
        float pitch = (float) Math.toDegrees(Math.asin(-direction.y));
        setRotation(pitch, yaw, 0);
    }

    public void update() {
        if (!active)
            return;

        Vector3f position = getPosition();
        position.x += direction.x * SPEED;
        position.y += direction.y * SPEED;
        position.z += direction.z * SPEED;
        setPosition(position.x, position.y, position.z);
    }

    public float getDamage() {
        return DAMAGE;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public boolean checkCollision(GameObject3D other) {
        // Простая проверка столкновения по сфере
        Vector3f position = getPosition();
        Vector3f otherPosition = other.getPosition();
        float distance = position.distance(otherPosition);
        float combinedRadius = getScale() + other.getScale();
        return distance < combinedRadius;
    }
}