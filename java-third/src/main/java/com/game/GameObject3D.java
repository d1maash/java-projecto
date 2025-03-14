package com.game;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameObject3D {
    private Mesh mesh;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;
    private Matrix4f modelMatrix;

    public GameObject3D(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        scale = 1.0f;
        modelMatrix = new Matrix4f();
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Matrix4f getModelMatrix() {
        modelMatrix.identity()
                .translate(position)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
        return modelMatrix;
    }

    public void render() {
        mesh.render();
    }

    public void cleanup() {
        mesh.cleanup();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }
}