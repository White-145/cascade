package me.white.cascade.listener;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseListener {
    private boolean isInWindow = false;
    private Vector2f previousPos = null;
    private Vector2f currentPos = new Vector2f();
    private Vector2f previousScroll = null;
    private Vector2f currentScroll = new Vector2f();
    private boolean[] mouseButtonsPressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

    public MouseListener(long windowHandle) {
        GLFW.glfwSetCursorPosCallback(windowHandle, (handle, posX, posY) -> {
            currentPos.x = (float) posX;
            currentPos.y = (float) posY;
        });
        GLFW.glfwSetCursorEnterCallback(windowHandle, (handle, entered) ->
                isInWindow = entered
        );
        GLFW.glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mode) ->
                mouseButtonsPressed[button] = action == GLFW.GLFW_PRESS
        );
        GLFW.glfwSetScrollCallback(windowHandle, (handle, scrollX, scrollY) -> {
            currentScroll.x = (float) scrollX;
            currentScroll.y = (float) scrollY;
        });
    }

    public void input() {
        if (previousPos == null) {
            previousPos = new Vector2f();
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
        if (previousScroll == null) {
            previousScroll = new Vector2f();
        }
        previousScroll.x = currentScroll.x;
        previousScroll.y = currentScroll.y;
    }

    public boolean isInWindow() {
        return isInWindow;
    }

    public Vector2f getPreviousPos() {
        return previousPos;
    }

    public Vector2f getCurrentPos() {
        return currentPos;
    }

    public Vector2f getPosDifference() {
        if (previousPos == null) {
            return new Vector2f(currentPos);
        }
        return new Vector2f(currentPos).sub(previousPos);
    }

    public Vector2f getPreviousScroll() {
        return previousScroll;
    }

    public Vector2f getCurrentScroll() {
        return currentScroll;
    }

    public boolean isMouseButtonPressed(int button) {
        if (button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) {
            return false;
        }
        return mouseButtonsPressed[button];
    }
}
