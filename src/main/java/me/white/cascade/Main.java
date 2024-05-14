package me.white.cascade;

import me.white.cascade.listener.KeyListener;
import me.white.cascade.listener.MouseListener;
import me.white.cascade.render.Camera;
import me.white.cascade.render.model.Model;
import me.white.cascade.render.model.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Main implements App {
    private static final float MOUSE_SENSITIVITY = 0.001f;
    private static final float MOVEMENT_SPEED = 0.05f;
    private Model model;
    private boolean isCursorLocked = false;
    private boolean cursorKeybind = false;

    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEng = new Engine("ARCAn", new Window.WindowOptions(), main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene) {
        model = AssetManager.getModel("cube");
        scene.addModel(model);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        Camera camera = scene.getCamera();
        KeyListener keyListener = window.getKeyListener();
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
                    GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                } else {
                    GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                }
                isCursorLocked = !isCursorLocked;
            }
            cursorKeybind = true;
        } else {
            cursorKeybind = false;
        }
        MouseListener mouseListener = window.getMouseListener();
        if (mouseListener.isInWindow() && (isCursorLocked || mouseListener.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT))) {
            Vector2f diff = mouseListener.getPosDifference();
            camera.addRotation(diff.y * MOUSE_SENSITIVITY, diff.x * MOUSE_SENSITIVITY);
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        MouseListener mouseListener = window.getMouseListener();
        if (mouseListener.isInWindow() && mouseListener.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            Vector2f diff = mouseListener.getPosDifference();
            float angle = diff.length() * 0.01f;
            if (Math.abs(angle) > 1e-6) {
                Vector3f cameraForward = scene.getCamera().getDirection();
                Vector3f cameraUp = scene.getCamera().getUp();
                Vector3f cameraRight = scene.getCamera().getRight();
                Vector3f delta = new Vector3f(diff.x, -diff.y, 0);
                Vector3f rotationAxis = cameraForward.cross(delta, new Vector3f()).normalize();
                float halfAngle = angle / 2;
                float sin = (float) Math.sin(halfAngle);
                float cos = (float) Math.cos(halfAngle);
                Quaternionf rotation = new Quaternionf(rotationAxis.x * sin, rotationAxis.y * sin, rotationAxis.z * sin, cos);
                Transformation transformation = model.getTransformation();
                Quaternionf oldOrientation = transformation.getOrientation();
                transformation.setOrientation(oldOrientation.add(rotation));
            }
        }
    }
}
