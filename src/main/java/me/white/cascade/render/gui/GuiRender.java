package me.white.cascade.render.gui;

import imgui.ImDrawData;
import imgui.ImFontAtlas;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.type.ImInt;
import me.white.cascade.Resource;
import me.white.cascade.ResourceManager;
import me.white.cascade.Scene;
import me.white.cascade.Window;
import me.white.cascade.render.Texture;
import me.white.cascade.render.shader.ShaderProgram;
import me.white.cascade.render.shader.UniformsMap;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL46;

import java.nio.ByteBuffer;

public class GuiRender {
    private static final Resource GUI_SHADER_RESOURCE = new Resource(Resource.ASSET_SHADER, "gui");
    private GuiMesh guiMesh;
    private Vector2f scale;
    private ShaderProgram shaderProgram;
    private Texture texture;
    private UniformsMap uniformsMap;

    public GuiRender(Window window) {
        shaderProgram = ResourceManager.getShader(GUI_SHADER_RESOURCE);
        uniformsMap = shaderProgram.getUniformsMap();
        scale = new Vector2f();
        createUniforms();
        createUIResources(window);
    }

    private void createUniforms() {
        uniformsMap.createUniform("scale");
    }

    public void render(Window window, Scene scene) {
        Gui gui = scene.getGuiInstance();
        if (gui == null) {
            return;
        }
        gui.drawGui();

        shaderProgram.bind();

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendEquation(GL46.GL_FUNC_ADD);
        GL46.glBlendFuncSeparate(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA, GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        GL46.glDisable(GL46.GL_DEPTH_TEST);
        GL46.glDisable(GL46.GL_CULL_FACE);

        GL46.glBindVertexArray(guiMesh.getVaoId());

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, guiMesh.getVerticesVbo());
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, guiMesh.getIndicesVbo());

        ImGuiIO io = ImGui.getIO();
        scale.x = 2.0f / io.getDisplaySizeX();
        scale.y = -2.0f / io.getDisplaySizeY();
        uniformsMap.setUniform("scale", scale);

        ImDrawData drawData = ImGui.getDrawData();
        int numLists = drawData.getCmdListsCount();
        for (int i = 0; i < numLists; i++) {
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, drawData.getCmdListVtxBufferData(i), GL46.GL_STREAM_DRAW);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, drawData.getCmdListIdxBufferData(i), GL46.GL_STREAM_DRAW);

            int numCmds = drawData.getCmdListCmdBufferSize(i);
            for (int j = 0; j < numCmds; j++) {
                final int elemCount = drawData.getCmdListCmdBufferElemCount(i, j);
                final int idxBufferOffset = drawData.getCmdListCmdBufferIdxOffset(i, j);
                final int indices = idxBufferOffset * ImDrawData.SIZEOF_IM_DRAW_IDX;

                texture.bind();
                GL46.glDrawElements(GL46.GL_TRIANGLES, elemCount, GL46.GL_UNSIGNED_SHORT, indices);
            }
        }

        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glDisable(GL46.GL_BLEND);
    }

    private void createUIResources(Window window) {
        ImGui.createContext();

        ImGuiIO imGuiIO = ImGui.getIO();
        imGuiIO.setIniFilename(null);
        imGuiIO.setDisplaySize(window.getWidth(), window.getHeight());

        ImFontAtlas fontAtlas = ImGui.getIO().getFonts();
        ImInt width = new ImInt();
        ImInt height = new ImInt();
        ByteBuffer buf = fontAtlas.getTexDataAsRGBA32(width, height);
        texture = new Texture(width.get(), height.get(), buf);

        guiMesh = new GuiMesh();
    }

    public void resize(int width, int height) {
        ImGuiIO imGuiIO = ImGui.getIO();
        imGuiIO.setDisplaySize(width, height);
    }
}
