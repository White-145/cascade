package me.white.cascade;

import me.white.cascade.render.Camera;
import me.white.cascade.render.model.Model;
import me.white.cascade.render.shader.Projection;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Model> models = new ArrayList<>();
    private Projection projection;
    private Camera camera = new Camera();

    public Scene(int width, int height) {
        projection = new Projection(width, height);
    }

    public void addModel(Model model) {
        models.add(model);
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

    public void resize(int width, int height) {
        projection.updateProjMatrix(width, height);
    }
}
