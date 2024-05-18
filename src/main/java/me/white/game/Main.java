package me.white.game;

import me.white.cascade.Engine;
import me.white.cascade.Window;

public class Main {
    public static void main(String[] args) {
        MyGame game = new MyGame();
        Window.WindowOptions options = new Window.WindowOptions();
        options.title = "Cascade Demo";
        options.fps = 30;
        Engine engine = new Engine(game, options);
        engine.start();
    }
}
