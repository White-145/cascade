package me.white.cascade.render;

import me.white.cascade.Resource;
import me.white.cascade.ResourceManager;
import me.white.cascade.Scene;
import me.white.cascade.Window;
import me.white.cascade.render.model.Mesh;
import me.white.cascade.render.model.Model;
import me.white.cascade.render.shader.ShaderProgram;
import me.white.cascade.render.shader.UniformsMap;
import org.lwjgl.opengl.GL46;

public class SceneRender {
    private static final Resource SCENE_SHADER_RESOURCE = new Resource(Resource.ASSET_SHADER, "scene");
    private ShaderProgram shaderProgram;
    private UniformsMap uniformsMap;

    public SceneRender() {
        shaderProgram = ResourceManager.getShader(SCENE_SHADER_RESOURCE);
        uniformsMap = shaderProgram.getUniformsMap();
        createUniforms();
    }

    private void createUniforms() {
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("txtSampler");
        uniformsMap.createUniform("viewMatrix");
        uniformsMap.createUniform("minLightness");
    }

    public void render(Window window, Scene scene) {
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
        GL46.glClearColor(0.1f, 0.075f, 0.05f, 1.0f);
        GL46.glViewport(0, 0, window.getWidth(), window.getHeight());

        shaderProgram.bind();

        uniformsMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
        uniformsMap.setUniform("viewMatrix", scene.getCamera().getViewMatrix());

        uniformsMap.setUniform("txtSampler", 0);
        uniformsMap.setUniform("minLightness", 0.1f);

        for (Model model : scene.getModels()) {
            uniformsMap.setUniform("modelMatrix", model.getTransformation().getMatrix());
            Mesh mesh = model.getMesh();
            Texture texture = mesh.getTexture();
            GL46.glActiveTexture(GL46.GL_TEXTURE0);
            texture.bind();

            GL46.glBindVertexArray(mesh.getVaoId());
            GL46.glDrawElements(GL46.GL_TRIANGLES, mesh.getNumVertices(), GL46.GL_UNSIGNED_INT, 0);
        }

        GL46.glBindVertexArray(0);

        shaderProgram.unbind();
    }
}
