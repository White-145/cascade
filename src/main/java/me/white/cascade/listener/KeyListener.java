package me.white.cascade.listener;

import org.lwjgl.glfw.GLFW;

public class KeyListener {
    private boolean[] keysPressed = new boolean[GLFW.GLFW_KEY_LAST + 1];

    public KeyListener(long windowHandle) {
        GLFW.glfwSetKeyCallback(windowHandle, (handle, key, scancode, action, mods) ->
                keysPressed[key] = action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT
        );
    }

    public boolean isKeyPressed(int key) {
        if (key < 0 || key > GLFW.GLFW_KEY_LAST) {
            return false;
        }
        return keysPressed[key];
    }
}
