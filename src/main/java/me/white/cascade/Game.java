package me.white.cascade;

public abstract class Game {
    public Window window;
    public Scene scene;

    public void init(Window window, Scene scene) {
        this.window = window;
        this.scene = scene;
    }

    public void cleanup() { }

    public void input(boolean inputConsumed) { }

    public void update() { }
}
