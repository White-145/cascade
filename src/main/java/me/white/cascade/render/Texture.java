package me.white.cascade.render;

import me.white.cascade.Resource;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture  {
    private final int textureId;
    private final String path;

    public Texture(String resource) {
        this(new Resource(Resource.Type.ASSET_TEXTURE, resource));
    }

    public Texture(Resource resource) {
        if (!resource.isOf(Resource.Type.ASSET_TEXTURE)) {
            throw new IllegalStateException("Texture should be of type ASSET_TEXTURE.");
        }
        try (MemoryStack stack = MemoryStack.stackPush()) {
            path = resource.getPath();
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            byte[] bytes;
            try {
                bytes = resource.readBytes();
            } catch (IOException ignored) {
                throw new IllegalStateException("Could not read texture '" + path + "'.");
            }
            ByteBuffer buffer = STBImage.stbi_load_from_memory(stack.bytes(bytes), width, height, channels, 4);
            if (buffer == null) {
                throw new IllegalStateException("Could not load texture '" + path + "': " + STBImage.stbi_failure_reason());
            }

            textureId = genTexture(width.get(), height.get(), buffer);
        }
    }

    public Texture(int width, int height, ByteBuffer buffer) {
        path = "...";
        textureId = genTexture(width, height, buffer);
    }

    public int genTexture(int width, int height, ByteBuffer buffer) {
        int textureId = GL46.glGenTextures();
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);
        GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, GL46.GL_TRUE);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buffer);
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);

        STBImage.stbi_image_free(buffer);
        return textureId;
    }

    public void bind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);
    }

    public void cleanup() {
        GL46.glDeleteTextures(textureId);
    }

    public String getPath() {
        return path;
    }
}
