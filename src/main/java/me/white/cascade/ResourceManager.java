package me.white.cascade;

import me.white.cascade.render.model.Model;
import me.white.cascade.render.shader.ShaderProgram;
import me.white.cascade.render.Texture;
import me.white.cascade.util.BiMap;

import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    private static BiMap<Resource, ShaderProgram> shaders = new BiMap<>();
    private static BiMap<Resource, Texture> textures = new BiMap<>();
    private static BiMap<Resource, Model> models = new BiMap<>();
    private static List<ShaderProgram> arbitraryShaders = new ArrayList<>();
    private static List<Texture> arbitraryTextures = new ArrayList<>();
    private static List<Model> arbitraryModels = new ArrayList<>();

    public static void get(Resource resource) {
        switch (resource.getType()) {
            case ASSET_SHADER -> getShader(resource);
            case ASSET_TEXTURE -> getTexture(resource);
            case ASSET_MODEL -> getModel(resource);
            default -> throw new IllegalStateException("Can not get unknown resource type '" + resource + "'.");
        }
    }

    public static void add(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof ShaderProgram shader) {
            if (!shaders.containsU(shader)) {
                arbitraryShaders.add(shader);
            }
        }
        if (obj instanceof Texture texture) {
            if (!textures.containsU(texture)) {
                arbitraryTextures.add(texture);
            }
        }
        if (obj instanceof Model model) {
            if (!models.containsU(model)) {
                arbitraryModels.add(model);
            }
        }
    }

    public static boolean has(Resource resource) {
        return shaders.containsT(resource) || textures.containsT(resource) || models.containsT(resource);
    }

    public static boolean has(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ShaderProgram shader) {
            return shaders.containsU(shader);
        }
        if (obj instanceof Texture texture) {
            return textures.containsU(texture);
        }
        if (obj instanceof Model model) {
            return models.containsU(model);
        }
        return false;
    }

    public static ShaderProgram getShader(String resource) {
        return getShader(new Resource(Resource.ASSET_SHADER, resource));
    }

    public static ShaderProgram getShader(Resource resource) {
        if (!resource.isOf(Resource.ASSET_SHADER)) {
            throw new IllegalStateException("Shader resource should be of ASSET_SHADER.");
        }
        if (shaders.containsT(resource)) {
            return shaders.getU(resource);
        }
        ShaderProgram shader = new ShaderProgram(resource);
        shaders.putTU(resource, shader);
        return shader;
    }

    public static Texture getTexture(String resource) {
        return getTexture(new Resource(Resource.ASSET_TEXTURE, resource));
    }

    public static Texture getTexture(Resource resource) {
        if (!resource.isOf(Resource.ASSET_TEXTURE)) {
            throw new IllegalStateException("Texture resource should be of ASSET_TEXTURE.");
        }
        if (textures.containsT(resource)) {
            return textures.getU(resource);
        }
        Texture texture = new Texture(resource);
        textures.putTU(resource, texture);
        return texture;
    }

    public static Model getModel(String resource) {
        return getModel(new Resource(Resource.ASSET_MODEL, resource));
    }

    public static Model getModel(Resource resource) {
        if (!resource.isOf(Resource.ASSET_MODEL)) {
            throw new IllegalStateException("Texture resource should be of ASSET_MODEL.");
        }
        if (models.containsT(resource)) {
            return models.getU(resource);
        }
        Model model = new Model(resource);
        models.putTU(resource, model);
        return model;
    }

    public static void cleanup() {
        for (ShaderProgram shader : shaders.getUSet()) {
            shader.cleanup();
        }
        shaders.clear();
        for (Texture texture : textures.getUSet()) {
            texture.cleanup();
        }
        textures.clear();
        for (Model model : models.getUSet()) {
            model.cleanup();
        }
        models.clear();
        for (ShaderProgram shader : arbitraryShaders) {
            shader.cleanup();
        }
        arbitraryShaders.clear();
        for (Texture texture : arbitraryTextures) {
            texture.cleanup();
        }
        arbitraryTextures.clear();
        for (Model model : arbitraryModels) {
            model.cleanup();
        }
        arbitraryModels.clear();
    }

    public static void cleanup(Resource resource) {
        switch (resource.getType()) {
            case ASSET_SHADER -> shaders.removeT(resource).cleanup();
            case ASSET_TEXTURE -> textures.removeT(resource).cleanup();
            case ASSET_MODEL -> models.removeT(resource).cleanup();
            default -> throw new IllegalStateException("Can not cleanup unknown resource type '" + resource + "'.");
        }
    }

    public static void cleanup(Object obj) {
        if (obj instanceof ShaderProgram shader) {
            shaders.removeU(shader);
            arbitraryShaders.remove(shader);
            shader.cleanup();
        }
        if (obj instanceof Texture texture) {
            textures.removeU(texture);
            arbitraryTextures.remove(texture);
            texture.cleanup();
        }
        if (obj instanceof Model model) {
            models.removeU(model);
            arbitraryModels.remove(model);
            model.cleanup();
        }
    }
}
