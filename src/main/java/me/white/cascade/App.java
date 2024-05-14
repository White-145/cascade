package me.white.cascade;

public interface App {
    void cleanup();

    void init(Window window, Scene scene);

    void input(Window window, Scene scene, long diffTimeMillis);

    void update(Window window, Scene scene, long diffTimeMillis);
}
