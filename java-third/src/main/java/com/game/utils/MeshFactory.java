package com.game.utils;

import com.game.Mesh;

public class MeshFactory {
    public static Mesh createCube() {
        float[] positions = new float[] {
                // Front face
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                // Back face
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
        };

        float[] colors = new float[] {
                // Front face (red)
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                // Back face (green)
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
        };

        int[] indices = new int[] {
                // Front face
                0, 1, 2,
                2, 3, 0,
                // Top face
                0, 4, 7,
                7, 3, 0,
                // Right face
                3, 7, 6,
                6, 2, 3,
                // Left face
                4, 0, 1,
                1, 5, 4,
                // Bottom face
                1, 2, 6,
                6, 5, 1,
                // Back face
                7, 4, 5,
                5, 6, 7
        };

        return new Mesh(positions, colors, indices);
    }

    public static Mesh createPyramid() {
        float[] positions = new float[] {
                // Base
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                // Top
                0.0f, 0.5f, 0.0f,
        };

        float[] colors = new float[] {
                // Base (blue)
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                // Top (yellow)
                1.0f, 1.0f, 0.0f,
        };

        int[] indices = new int[] {
                // Base
                0, 1, 2,
                2, 3, 0,
                // Front
                0, 1, 4,
                // Right
                1, 2, 4,
                // Back
                2, 3, 4,
                // Left
                3, 0, 4
        };

        return new Mesh(positions, colors, indices);
    }
}