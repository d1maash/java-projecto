package com.game;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import com.game.utils.ResourceManager;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game3D {
    private long window;
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private GameState gameState;
    private ShaderProgram shaderProgram;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Vector3f cameraPosition;
    private float cameraRotation;

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "3D Space Shooter", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.1f, 0.0f);
        glEnable(GL_DEPTH_TEST);

        // Инициализация матриц
        projectionMatrix = new Matrix4f().perspective(
                (float) Math.toRadians(45.0f),
                (float) WIDTH / HEIGHT,
                0.01f,
                100.0f);

        cameraPosition = new Vector3f(0.0f, 0.0f, 3.0f);
        viewMatrix = new Matrix4f().lookAt(
                cameraPosition,
                new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f));

        // Инициализация игрового состояния и ресурсов
        try {
            ResourceManager.init();
            gameState = new GameState();
            shaderProgram = ResourceManager.getShader("basic");
            if (shaderProgram == null) {
                throw new RuntimeException("Failed to load basic shader");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Настройка обработки ввода
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Обновление игровой логики
            update();

            // Рендеринг
            render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void update() {
        // Обновление позиции камеры и игровых объектов
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            gameState.getPlayer().turnLeft();
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            gameState.getPlayer().turnRight();
        }
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            gameState.getPlayer().moveForward();
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            gameState.getPlayer().moveBackward();
        }
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            gameState.fireBullet();
        }

        gameState.update();

        // Обновление матрицы вида
        Vector3f playerPos = gameState.getPlayer().getPosition();
        Vector3f playerRot = gameState.getPlayer().getRotation();
        viewMatrix = new Matrix4f().lookAt(
                new Vector3f(playerPos.x, playerPos.y + 1.0f, playerPos.z + 2.0f),
                new Vector3f(playerPos.x, playerPos.y, playerPos.z),
                new Vector3f(0.0f, 1.0f, 0.0f));
    }

    private void render() {
        shaderProgram.bind();
        shaderProgram.setUniform("projection", projectionMatrix);
        shaderProgram.setUniform("view", viewMatrix);

        // Рендеринг игрока
        shaderProgram.setUniform("model", gameState.getPlayer().getModelMatrix());
        gameState.getPlayer().render();

        // Рендеринг врагов
        for (Enemy3D enemy : gameState.getEnemies()) {
            shaderProgram.setUniform("model", enemy.getModelMatrix());
            enemy.render();
        }

        // Рендеринг пуль
        for (Bullet3D bullet : gameState.getBullets()) {
            shaderProgram.setUniform("model", bullet.getModelMatrix());
            bullet.render();
        }

        shaderProgram.unbind();
    }

    public static void main(String[] args) {
        new Game3D().run();
    }
}