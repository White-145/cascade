package me.white.cascade;

import me.white.cascade.render.model.Model;
import me.white.cascade.render.shader.ShaderProgram;
import me.white.cascade.render.Texture;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static Map<Resource, ShaderProgram> shaders = new HashMap<>();
    private static Map<Resource, Texture> textures = new HashMap<>();
    private static Map<Resource, Model> models = new HashMap<>();

    public static void get(Resource resource) {
        switch (resource.getType()) {
            case ASSET_SHADER -> getShader(resource);
            case ASSET_TEXTURE -> getTexture(resource);
            case ASSET_MODEL -> getModel(resource);
            default -> throw new IllegalStateException("Can not get unknown resource type '" + resource + "'.");
        }
    }

    public static boolean has(Resource resource) {
        return shaders.containsKey(resource) || textures.containsKey(resource) || models.containsKey(resource);
    }

    public static boolean has(ShaderProgram shader) {
        return shaders.containsValue(shader);
    }

    public static boolean has(Texture texture) {
        return textures.containsValue(texture);
    }

    public static boolean has(Model model) {
        return models.containsValue(model);
    }

    public static ShaderProgram getShader(String resource) {
        return getShader(new Resource(Resource.ASSET_SHADER, resource));
    }

    public static ShaderProgram getShader(Resource resource) {
        if (!resource.isOf(Resource.ASSET_SHADER)) {
            throw new IllegalStateException("Shader resource should be of ASSET_SHADER.");
        }
        if (shaders.containsKey(resource)) {
            return shaders.get(resource);
        }
        ShaderProgram shader = new ShaderProgram(resource);
        shaders.put(resource, shader);
        return shader;
    }

    public static Texture getTexture(String resource) {
        return getTexture(new Resource(Resource.ASSET_TEXTURE, resource));
    }

    public static Texture getTexture(Resource resource) {
        if (!resource.isOf(Resource.ASSET_TEXTURE)) {
            throw new IllegalStateException("Texture resource should be of ASSET_TEXTURE.");
        }
        if (textures.containsKey(resource)) {
            return textures.get(resource);
        }
        Texture texture = new Texture(resource);
        textures.put(resource, texture);
        return texture;
    }

    public static Model getModel(String resource) {
        return getModel(new Resource(Resource.ASSET_MODEL, resource));
    }

    public static Model getModel(Resource resource) {
        if (!resource.isOf(Resource.ASSET_MODEL)) {
            throw new IllegalStateException("Texture resource should be of ASSET_MODEL.");
        }
        if (models.containsKey(resource)) {
            return models.get(resource);
        }
        Model model = new Model(resource);
        models.put(resource, model);
        return model;
    }

    public static void cleanup(Resource resource) {
        switch (resource.getType()) {
            case ASSET_SHADER -> shaders.remove(resource).cleanup();
            case ASSET_TEXTURE -> textures.remove(resource).cleanup();
            case ASSET_MODEL -> models.remove(resource).cleanup();
            default -> throw new IllegalStateException("Can not cleanup unknown resource type '" + resource + "'.");
        }
    }

    public static void cleanup() {
        for (ShaderProgram shader : shaders.values()) {
            shader.cleanup();
        }
        shaders.clear();
        for (Texture texture : textures.values()) {
            texture.cleanup();
        }
        textures.clear();
        for (Model model : models.values()) {
            model.cleanup();
        }
        models.clear();
    }
}
