package me.white.cascade.render.model;

import me.white.cascade.AssetManager;
import me.white.cascade.render.Texture;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private Texture texture;
    private int numVertices;
    private int vaoId;
    private List<Integer> vboIdList = new ArrayList<>();

    public Mesh(Texture texture, float[] positions, float[] uvs, float[] normals, int[] indices) {
        this.texture = texture;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            numVertices = indices.length;

            vaoId = GL46.glGenVertexArrays();
            GL46.glBindVertexArray(vaoId);

            // Positions VBO
            int vboId = GL46.glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer positionsBuffer = stack.callocFloat(positions.length);
            positionsBuffer.put(0, positions);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, positionsBuffer, GL46.GL_STATIC_DRAW);
            GL46.glEnableVertexAttribArray(0);
            GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

            // UVs VBO
            vboId = GL46.glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer uvsBuffer = stack.callocFloat(uvs.length);
            uvsBuffer.put(0, uvs);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, uvsBuffer, GL46.GL_STATIC_DRAW);
            GL46.glEnableVertexAttribArray(1);
            GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 0, 0);

            // Normals VBO
            vboId = GL46.glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer normalsBuffer = stack.callocFloat(normals.length);
            normalsBuffer.put(0, normals);
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, normalsBuffer, GL46.GL_STATIC_DRAW);
            GL46.glEnableVertexAttribArray(2);
            GL46.glVertexAttribPointer(2, 3, GL46.GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = GL46.glGenBuffers();
            vboIdList.add(vboId);
            IntBuffer indicesBuffer = stack.callocInt(indices.length);
            indicesBuffer.put(0, indices);
            GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, vboId);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);

            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
            GL46.glBindVertexArray(0);
        }
    }

    public void cleanup() {
        for (int vboId : vboIdList) {
            GL46.glDeleteBuffers(vboId);
        }
        GL46.glDeleteVertexArrays(vaoId);
        if (!AssetManager.has(texture)) {
            texture.cleanup();
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public final int getVaoId() {
        return vaoId;
    }
}
