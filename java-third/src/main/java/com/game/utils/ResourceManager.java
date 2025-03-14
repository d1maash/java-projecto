package com.game.utils;

import com.game.ShaderProgram;
import com.game.Mesh;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static Map<String, ShaderProgram> shaders = new HashMap<>();
    private static Map<String, Mesh> meshes = new HashMap<>();

    public static void init() throws Exception {
        // Загрузка шейдеров
        String vertexShaderCode = FileUtils.loadResource("/shaders/vertex.glsl");
        String fragmentShaderCode = FileUtils.loadResource("/shaders/fragment.glsl");

        if (vertexShaderCode == null || fragmentShaderCode == null) {
            throw new RuntimeException("Failed to load shader files");
        }

        ShaderProgram shader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
        shaders.put("basic", shader);

        // Создание базовых мешей
        meshes.put("cube", MeshFactory.createCube());
        meshes.put("pyramid", MeshFactory.createPyramid());
    }

    public static ShaderProgram getShader(String name) {
        return shaders.get(name);
    }

    public static Mesh getMesh(String name) {
        return meshes.get(name);
    }

    public static void cleanup() {
        for (ShaderProgram shader : shaders.values()) {
            if (shader != null) {
                shader.cleanup();
            }
        }
        for (Mesh mesh : meshes.values()) {
            if (mesh != null) {
                mesh.cleanup();
            }
        }
    }
}