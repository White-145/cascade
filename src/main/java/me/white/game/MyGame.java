package me.white.game;

import me.white.cascade.*;
import me.white.cascade.listener.KeyListener;
import me.white.cascade.listener.MouseListener;
import me.white.cascade.render.Camera;
import me.white.cascade.render.model.Model;
import me.white.cascade.render.model.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MyGame extends Game {
    private static final float MOUSE_SENSITIVITY = 0.001f;
    private static final float MOVEMENT_SPEED = 0.1f;
    private boolean isCursorLocked = false;
    private boolean cursorKeybind = false;
    private MyGui gui;
    private Model model;

    @Override
    public void init(Window window, Scene scene) {
        super.init(window, scene);
        gui = new MyGui(window, scene);
        scene.setGuiInstance(gui);
        model = ResourceManager.getModel("cube");
        scene.addModel(model);
    }

    @Override
    public void update() {
        Transformation transformation = model.getTransformation();
        transformation.setPosition(transformation.getPosition().set(gui.position));
        float cr = (float) Math.cos(gui.rotation[2] * 0.5);
        float sr = (float) Math.sin(gui.rotation[2] * 0.5);
        float cp = (float) Math.cos(gui.rotation[1] * 0.5);
        float sp = (float) Math.sin(gui.rotation[1] * 0.5);
        float cy = (float) Math.cos(gui.rotation[0] * 0.5);
        float sy = (float) Math.sin(gui.rotation[0] * 0.5);

        Quaternionf orientation = new Quaternionf();
        orientation.w = cr * cp * cy + sr * sp * sy;
        orientation.x = sr * cp * cy - cr * sp * sy;
        orientation.y = cr * sp * cy + sr * cp * sy;
        orientation.z = cr * cp * sy - sr * sp * cy;
        transformation.setOrientation(orientation);
    }

    @Override
    public void input(boolean inputConsumed) {
        if (inputConsumed) {
            return;
        }
        Camera camera = scene.getCamera();
        KeyListener keyListener = scene.getKeyListener();
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_W)) {
            camera.moveForward(MOVEMENT_SPEED);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
            camera.moveBackwards(MOVEMENT_SPEED);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
            camera.moveLeft(MOVEMENT_SPEED);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
            camera.moveRight(MOVEMENT_SPEED);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            camera.moveUp(MOVEMENT_SPEED);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            camera.moveDown(MOVEMENT_SPEED);
        }

        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_T)) {
            if (!cursorKeybind) {
                if (isCursorLocked) {
                    GLFW.glfwSetInputMode(scene.getWindow().getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                } else {
                    GLFW.glfwSetInputMode(scene.getWindow().getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                }
                isCursorLocked = !isCursorLocked;
            }
            cursorKeybind = true;
        } else {
            cursorKeybind = false;
        }
        MouseListener mouseListener = scene.getMouseListener();
        if (mouseListener.isInWindow() && (isCursorLocked || mouseListener.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT))) {
            Vector2f diff = mouseListener.getPosDifference();
            camera.addRotation(diff.y * MOUSE_SENSITIVITY, diff.x * MOUSE_SENSITIVITY);
        }
    }
}
