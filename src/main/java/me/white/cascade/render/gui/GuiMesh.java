package me.white.cascade.render.gui;

import imgui.ImDrawData;
import org.lwjgl.opengl.GL46;

public class GuiMesh {
    private int indicesVbo;
    private int vaoId;
    private int verticesVbo;

    public GuiMesh() {
        vaoId = GL46.glGenVertexArrays();
        GL46.glBindVertexArray(vaoId);

        verticesVbo = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, verticesVbo);
        GL46.glEnableVertexAttribArray(0);
        GL46.glVertexAttribPointer(0, 2, GL46.GL_FLOAT, false, ImDrawData.SIZEOF_IM_DRAW_VERT, 0);
        GL46.glEnableVertexAttribArray(1);
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, ImDrawData.SIZEOF_IM_DRAW_VERT, 8);
        GL46.glEnableVertexAttribArray(2);
        GL46.glVertexAttribPointer(2, 4, GL46.GL_UNSIGNED_BYTE, true, ImDrawData.SIZEOF_IM_DRAW_VERT, 16);

        indicesVbo = GL46.glGenBuffers();

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindVertexArray(0);
    }

    public void cleanup() {
        GL46.glDeleteBuffers(indicesVbo);
        GL46.glDeleteBuffers(verticesVbo);
        GL46.glDeleteVertexArrays(vaoId);
    }

    public int getIndicesVbo() {
        return indicesVbo;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVerticesVbo() {
        return verticesVbo;
    }
}
