package me.white.cascade;

import me.white.cascade.listener.KeyListener;
import me.white.cascade.listener.MouseListener;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.util.concurrent.Callable;

public class Window {
    private final long windowHandle;
    private int height;
    private Callable<Void> resizeFunc;
    private int width;
    private MouseListener mouseListener;
    private KeyListener keyListener;

    public Window(String title, WindowOptions opts, Callable<Void> resizeFunc) {
        this.resizeFunc = resizeFunc;
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL30.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL30.GL_TRUE);

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
        if (opts.compatibleProfile) {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL30.GL_TRUE);
        }

        if (opts.width > 0 && opts.height > 0) {
            this.width = opts.width;
            this.height = opts.height;
        } else {
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            width = vidMode.width();
            height = vidMode.height();
        }

        windowHandle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> resized(w, h));

        GLFW.glfwSetErrorCallback((int errorCode, long msgPtr) ->
                System.err.println("Error code " + errorCode + ", msg " + MemoryUtil.memUTF8(msgPtr))
        );

        GLFW.glfwMakeContextCurrent(windowHandle);

        if (opts.fps > 0) {
            GLFW.glfwSwapInterval(0);
        } else {
            GLFW.glfwSwapInterval(1);
        }

        GLFW.glfwShowWindow(windowHandle);

        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];
        GLFW.glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight);
        width = arrWidth[0];
        height = arrHeight[0];
        mouseListener = new MouseListener(windowHandle);
        keyListener = new KeyListener(windowHandle);
    }

    public void cleanup() {
        Callbacks.glfwFreeCallbacks(windowHandle);
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
        GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    protected void resized(int width, int height) {
        this.width = width;
        this.height = height;
        try {
            resizeFunc.call();
        } catch (Exception e) {
            System.err.println("Error calling resize callback: " + e);
        }
    }

    public void update() {
        GLFW.glfwSwapBuffers(windowHandle);
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public static class WindowOptions {
        public boolean compatibleProfile;
        public int fps;
        public int height;
        public int ups = Engine.TARGET_UPS;
        public int width;
    }
}
