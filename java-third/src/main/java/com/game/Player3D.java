package com.game;

import org.joml.Vector3f;
import com.game.utils.ResourceManager;

public class Player3D extends GameObject3D {
    private static final float MOVEMENT_SPEED = 0.1f;
    private static final float ROTATION_SPEED = 2.0f;
    private float health;
    private float maxHealth;

    public Player3D() {
        super(ResourceManager.getMesh("pyramid"));
        health = 100;
        maxHealth = 100;
        setScale(0.5f);
    }

    public void moveForward() {
        Vector3f position = getPosition();
        float rotationY = (float) Math.toRadians(getRotation().y);
        position.x -= (float) Math.sin(rotationY) * MOVEMENT_SPEED;
        position.z -= (float) Math.cos(rotationY) * MOVEMENT_SPEED;
        setPosition(position.x, position.y, position.z);
    }

    public void moveBackward() {
        Vector3f position = getPosition();
        float rotationY = (float) Math.toRadians(getRotation().y);
        position.x += (float) Math.sin(rotationY) * MOVEMENT_SPEED;
        position.z += (float) Math.cos(rotationY) * MOVEMENT_SPEED;
        setPosition(position.x, position.y, position.z);
    }

    public void turnLeft() {
        Vector3f rotation = getRotation();
        rotation.y += ROTATION_SPEED;
        setRotation(rotation.x, rotation.y, rotation.z);
    }

    public void turnRight() {
        Vector3f rotation = getRotation();
        rotation.y -= ROTATION_SPEED;
        setRotation(rotation.x, rotation.y, rotation.z);
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public void heal(float amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public boolean isAlive() {
        return health > 0;
    }
}