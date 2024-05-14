package me.white.cascade;

import me.white.cascade.render.SceneRender;

public class Engine {
    public static final int TARGET_UPS = 30;
    private final App app;
    private final Window window;
    private boolean running;
    private SceneRender render;
    private Scene scene;
    private int targetFps;
    private int targetUps;

    public Engine(String windowTitle, Window.WindowOptions opts, App app) {
        window = new Window(windowTitle, opts, () -> {
            resize();
            return null;
        });
        targetFps = opts.fps;
        targetUps = opts.ups;
        this.app = app;
        render = new SceneRender();
        scene = new Scene(window.getWidth(), window.getHeight());
        app.init(window, scene);
        running = true;
    }

    private void cleanup() {
        app.cleanup();
        window.cleanup();
        AssetManager.cleanup();
    }

    private void resize() {
        scene.resize(window.getWidth(), window.getHeight());
    }

    private void run() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                app.input(window, scene, now - initialTime);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                app.update(window, scene, diffTimeMillis);
                updateTime = now;
                deltaUpdate--;
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                render.render(window, scene);
                deltaFps--;
                window.update();
                window.getMouseListener().input();
                app.input(window, scene, now - initialTime);
            }
            initialTime = now;
        }

        cleanup();
    }

    public void start() {
        running = true;
        run();
    }

    public void stop() {
        running = false;
    }
}
