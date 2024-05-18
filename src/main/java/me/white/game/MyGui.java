package me.white.game;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import me.white.cascade.Scene;
import me.white.cascade.Window;
import me.white.cascade.listener.MouseListener;
import me.white.cascade.render.gui.Gui;
import org.joml.Vector2f;

public class MyGui extends Gui {
    public float[] position = new float[3];
    public float[] rotation = new float[3];

    public MyGui(Window window, Scene scene) {
        super(window, scene);
    }

    @Override
    public void drawGui() {
        ImGui.newFrame();
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.begin("Model controls");
        ImGui.dragFloat3("Position", position, 0.1f, -10, 10);
        ImGui.dragFloat3("Rotation", rotation, 0.1f);
        ImGui.end();
        ImGui.endFrame();
        ImGui.render();
    }

    @Override
    public boolean handleGuiInput(Scene scene) {
        ImGuiIO imGuiIO = ImGui.getIO();
        MouseListener mouseListener = window.getMouseListener();
        Vector2f mousePos = mouseListener.getCurrentPos();
        imGuiIO.setMousePos(mousePos.x, mousePos.y);
        imGuiIO.setMouseDown(0, mouseListener.isMouseButtonPressed(0));
        imGuiIO.setMouseDown(1, mouseListener.isMouseButtonPressed(1));
        imGuiIO.setMouseDown(2, mouseListener.isMouseButtonPressed(2));
        Vector2f mouseScroll = mouseListener.getCurrentScroll();
        imGuiIO.setMouseWheelH(mouseScroll.x);
        imGuiIO.setMouseWheel(mouseScroll.y);

        return imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
    }
}
