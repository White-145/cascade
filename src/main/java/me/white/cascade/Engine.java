package me.white.cascade;

import imgui.ImGui;
import imgui.ImGuiIO;
import me.white.cascade.render.Render;
import me.white.cascade.render.gui.Gui;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Engine {
    public static final int TARGET_UPS = 30;
    private final Game game;
    private final Window window;
    private boolean running;
    private Render render;
    private Scene scene;
    private int targetFps;
    private int targetUps;
    private GLFWKeyCallback prevKeyCallBack;

    public Engine(Game game, Window.WindowOptions opts) {
        this.game = game;
        targetFps = opts.fps;
        targetUps = opts.ups;
        window = new Window(this, opts);
        scene = new Scene(window);
        render = new Render(window, scene);
        setupKeyCallBack(window);
    }

    private void cleanup() {
        if (prevKeyCallBack != null) {
            prevKeyCallBack.free();
        }
        game.cleanup();
        window.cleanup();
        ResourceManager.cleanup();
    }

    private void setupKeyCallBack(Window window) {
        ImGuiIO io = ImGui.getIO();
        io.setKeyMap(new int[] {
                GLFW.GLFW_KEY_TAB,
                GLFW.GLFW_KEY_LEFT,
                GLFW.GLFW_KEY_RIGHT,
                GLFW.GLFW_KEY_UP,
                GLFW.GLFW_KEY_DOWN,
                GLFW.GLFW_KEY_PAGE_UP,
                GLFW.GLFW_KEY_PAGE_DOWN,
                GLFW.GLFW_KEY_HOME,
                GLFW.GLFW_KEY_END,
                GLFW.GLFW_KEY_INSERT,
                GLFW.GLFW_KEY_DELETE,
                GLFW.GLFW_KEY_BACKSPACE,
                GLFW.GLFW_KEY_SPACE,
                GLFW.GLFW_KEY_ENTER,
                GLFW.GLFW_KEY_ESCAPE,
                GLFW.GLFW_KEY_KP_ENTER,
                GLFW.GLFW_KEY_A,
                GLFW.GLFW_KEY_C,
                GLFW.GLFW_KEY_V,
                GLFW.GLFW_KEY_X,
                GLFW.GLFW_KEY_Y,
                GLFW.GLFW_KEY_Z
        });

        prevKeyCallBack = GLFW.glfwSetKeyCallback(window.getWindowHandle(), (handle, key, scancode, action, mods) -> {
            if (prevKeyCallBack != null) {
                prevKeyCallBack.invoke(handle, key, scancode, action, mods);
            }
            if (action == GLFW.GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW.GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }
            io.setKeyCtrl(io.getKeysDown(GLFW.GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW.GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW.GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW.GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_SUPER));
        });

        GLFW.glfwSetCharCallback(window.getWindowHandle(), (handle, c) -> {
            if (!io.getWantCaptureKeyboard()) {
                return;
            }
            io.addInputCharacter(c);
        });
    }

    public void resize() {
        int width = window.getWidth();
        int height = window.getHeight();
        scene.resize(width, height);
        render.resize(width, height);
    }

    private void run() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;
        Gui gui = scene.getGuiInstance();
        while (running && !window.windowShouldClose()) {
            window.pollEvents();
            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                boolean inputConsumed = gui != null && gui.handleGuiInput(scene);
                game.input(inputConsumed);
                window.getMouseListener().input();
            }
            if (deltaUpdate >= 1) {
                game.update();
                deltaUpdate--;
            }
            if (targetFps <= 0 || deltaFps >= 1) {
                render.render();
                deltaFps--;
                window.update();
            }
            initialTime = now;
        }

        cleanup();
    }

    public void start() {
        game.init(window, scene);
        running = true;
        run();
    }

    public void stop() {
        running = false;
    }
}
