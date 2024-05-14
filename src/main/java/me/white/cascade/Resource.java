package me.white.cascade;

import java.io.IOException;

public class Resource {
    public enum Type {
        ASSET_TEXTURE("assets/textures", "png"),
        ASSET_MODEL("assets/models", "json"),
        ASSET_SHADER("assets/shaders", "json"),
        SHADER_FRAGMENT("assets/shaders", "fsh"),
        SHADER_VERTEX("assets/shaders", "vsh"),
        SHADER_COMPUTE("assets/shaders", "csh");

        final String path;
        final String extension;

        Type(String path, String extension) {
            this.path = path;
            this.extension = extension;
        }
    }
    public static final Type ASSET_SHADER = Type.ASSET_SHADER;
    public static final Type ASSET_TEXTURE = Type.ASSET_TEXTURE;
    public static final Type SHADER_FRAGMENT = Type.SHADER_FRAGMENT;
    public static final Type SHADER_VERTEX = Type.SHADER_VERTEX;
    public static final Type SHADER_COMPUTE = Type.SHADER_COMPUTE;
    public static final Type ASSET_MODEL = Type.ASSET_MODEL;

    private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
    private byte[] data = null;
    private Type type;
    private String path;

    public Resource(Type type, String path) {
        this.type = type;
        this.path = path;
    }

    public String getPath() {
        return type.path + "/" + path + "." + type.extension;
    }

    public byte[] readBytes() throws IOException {
        if (data == null) {
            data = CLASS_LOADER.getResourceAsStream(getPath()).readAllBytes();
        }
        return data;
    }

    public String readText() throws IOException {
        byte[] bytes = readBytes();
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    public Type getType() {
        return type;
    }

    public boolean isOf(Type type) {
        return type == this.type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Resource resource) {
            return type == resource.type && path.equals(resource.path);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 37 * (37 * type.path.hashCode() + type.extension.hashCode()) + path.hashCode();
    }
}
