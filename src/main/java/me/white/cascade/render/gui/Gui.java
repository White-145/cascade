package me.white.cascade.render.gui;

import me.white.cascade.Scene;
import me.white.cascade.Window;

public abstract class Gui {
    public Window window;
    public Scene scene;

    public Gui(Window window, Scene scene) {
        this.window = window;
        this.scene = scene;
    }

    public abstract void drawGui();

    public abstract boolean handleGuiInput(Scene scene);
}
