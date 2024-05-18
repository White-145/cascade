package me.white.cascade;

import me.white.cascade.listener.KeyListener;
import me.white.cascade.listener.MouseListener;
import me.white.cascade.render.Camera;
import me.white.cascade.render.gui.Gui;
import me.white.cascade.render.model.Model;
import me.white.cascade.render.shader.Projection;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Model> models = new ArrayList<>();
    private Projection projection;
    private Camera camera = new Camera();
    private Window window;
    private Gui gui;

    public Scene(Window window) {
        this.window = window;
        projection = new Projection(window.getWidth(), window.getHeight());
    }

    public List<Model> getModels() {
        return models;
    }

    public Projection getProjection() {
        return projection;
    }

    public Camera getCamera() {
        return camera;
    }

    public Window getWindow() {
        return window;
    }

    public KeyListener getKeyListener() {
        return window.getKeyListener();
    }

    public MouseListener getMouseListener() {
        return window.getMouseListener();
    }

    public Gui getGuiInstance() {
        return gui;
    }

    public void resize(int width, int height) {
        projection.updateProjMatrix(width, height);
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public void setGuiInstance(Gui gui) {
        this.gui = gui;
    }
}
