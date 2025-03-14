package com.fps;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private float pitch;
    private float yaw;
    private Matrix4f viewMatrix;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;

    public Camera() {
        position = new Vector3f(0, 0, 0);
        viewMatrix = new Matrix4f();
        pitch = 0;
        yaw = -90f; // Look along negative Z axis
        front = new Vector3f(0, 0, -1);
        up = new Vector3f(0, 1, 0);
        right = new Vector3f(1, 0, 0);
        updateVectors();
    }

    private void updateVectors() {
        front.x = (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));
        front.normalize();

        right.set(front).cross(new Vector3f(0, 1, 0)).normalize();
        up.set(right).cross(front).normalize();
    }

    public void move(float dx, float dy, float dz) {
        if (dz != 0) {
            position.add(new Vector3f(front).mul(dz));
        }
        if (dx != 0) {
            position.add(new Vector3f(right).mul(dx));
        }
        position.y += dy;
    }

    public void rotate(float dx, float dy) {
        yaw += dx * 0.1f;
        pitch += dy * 0.1f;

        if (pitch > 89.0f) {
            pitch = 89.0f;
        }
        if (pitch < -89.0f) {
            pitch = -89.0f;
        }

        updateVectors();
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().lookAt(
                position,
                new Vector3f(position).add(front),
                up);
    }

    public Vector3f getPosition() {
        return position;
    }
}