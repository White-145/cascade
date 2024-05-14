package me.white.cascade.render;

import me.white.cascade.Resource;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {
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

            textureId = GL30.glGenTextures();

            GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureId);
            GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
            GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, width.get(), height.get(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);

            STBImage.stbi_image_free(buffer);
        }
    }

    public void bind() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureId);
    }

    public void cleanup() {
        GL30.glDeleteTextures(textureId);
    }

    public String getPath() {
        return path;
    }
}
