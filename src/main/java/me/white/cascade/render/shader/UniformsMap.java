package me.white.cascade.render.shader;

import org.joml.*;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public class UniformsMap {
    private ShaderProgram shaderProgram;
    private Map<String, Integer> uniformLocations = new HashMap<>();

    public UniformsMap(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public void createUniform(String name) {
        int location = GL46.glGetUniformLocation(shaderProgram.getProgramId(), name);
        if (location < 0) {
            throw new IllegalStateException("Could not find uniform '" + name + "' in shader '" + shaderProgram.getPath() + "'.");
        }
        uniformLocations.put(name, location);
    }

    private int getUniformLocation(String name) {
        if (!uniformLocations.containsKey(name)) {
            throw new IllegalStateException("Could not find uniform '" + name + "'.");
        }
        return uniformLocations.get(name);
    }

    public void setUniform(String name, int value) {
        int location = getUniformLocation(name);
        GL46.glUniform1i(location, value);
    }

    public void setUniform(String name, float value) {
        int location = getUniformLocation(name);
        GL46.glUniform1f(location, value);
    }

    public void setUniform(String name, Vector2f value) {
        int location = getUniformLocation(name);
        GL46.glUniform2f(location, value.x, value.y);
    }

    public void setUniform(String name, Vector3f value) {
        int location = getUniformLocation(name);
        GL46.glUniform3f(location, value.x, value.y, value.z);
    }

    public void setUniform(String name, Vector4f value) {
        int location = getUniformLocation(name);
        GL46.glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, Matrix3f value) {
        int location = getUniformLocation(name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL46.glUniformMatrix3fv(location, false, value.get(stack.mallocFloat(9)));
        }
    }

    public void setUniform(String name, Matrix4f value) {
        int location = getUniformLocation(name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL46.glUniformMatrix4fv(location, false, value.get(stack.mallocFloat(16)));
        }
    }
}
