package me.white.cascade.render;

import me.white.cascade.Scene;
import me.white.cascade.Window;
import me.white.cascade.render.gui.GuiRender;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

public class Render {
    private SceneRender sceneRender;
    private GuiRender guiRender;
    private Window window;
    private Scene scene;

    public Render(Window window, Scene scene) {
        GL.createCapabilities();
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        this.window = window;
        this.scene = scene;
        sceneRender = new SceneRender();
        guiRender = new GuiRender(window);
    }

    public void render() {
        sceneRender.render(window, scene);
        guiRender.render(window, scene);
    }

    public void resize(int width, int height) {
        guiRender.resize(width, height);
    }
}
