package me.white.cascade.render.model;

import com.google.gson.*;
import me.white.cascade.ResourceManager;
import me.white.cascade.Resource;
import org.joml.Vector3f;

import java.io.IOException;

public class Model {
    private String path;
    private Transformation transformation = new Transformation();
    private Mesh mesh;

    public Model(Resource resource) {
        if (!resource.isOf(Resource.ASSET_MODEL)) {
            throw new IllegalStateException("Model resource should be of ASSET_MODEL.");
        }
        path = resource.getPath();
        JsonObject config;
        try {
            config = new Gson().fromJson(resource.readText(), JsonObject.class);
        } catch (IOException | JsonSyntaxException e) {
            throw new IllegalStateException("Could not file for model '" + path + "': " + e);
        }
        String texture = config.get("texture").getAsString();
        Resource textureResource = new Resource(Resource.ASSET_TEXTURE, texture);

        JsonArray vertices = config.get("vertices").getAsJsonArray();
        JsonArray faces = config.get("faces").getAsJsonArray();
        int facesCount = faces.size();
        int verticesCount = vertices.size();
        float[] positionArray = new float[verticesCount * 3];
        float[] uvArray = new float[verticesCount * 2];
        float[] normalArray = new float[verticesCount * 3];
        int[] facesArray = new int[facesCount * 3];
        for (int i = 0; i < verticesCount; ++i) {
            JsonObject vertex = vertices.get(i).getAsJsonObject();
            JsonArray pos = vertex.get("pos").getAsJsonArray();
            positionArray[3 * i] = pos.get(0).getAsFloat();
            positionArray[3 * i + 1] = pos.get(1).getAsFloat();
            positionArray[3 * i + 2] = pos.get(2).getAsFloat();
            JsonArray uv = vertex.get("UV").getAsJsonArray();
            uvArray[2 * i] = uv.get(0).getAsFloat();
            uvArray[2 * i + 1] = uv.get(1).getAsFloat();
        }
        for (int i = 0; i < facesCount; ++i) {
            JsonArray face = faces.get(i).getAsJsonArray();
            int first = face.get(0).getAsInt();
            int second = face.get(1).getAsInt();
            int third = face.get(2).getAsInt();
            facesArray[3 * i] = first;
            facesArray[3 * i + 1] = second;
            facesArray[3 * i + 2] = third;
            Vector3f firstPos = new Vector3f(
                    positionArray[3 * first],
                    positionArray[3 * first + 1],
                    positionArray[3 * first + 2]
            );
            Vector3f secondPos = new Vector3f(
                    positionArray[3 * second],
                    positionArray[3 * second + 1],
                    positionArray[3 * second + 2]
            );
            Vector3f thirdPos = new Vector3f(
                    positionArray[3 * third],
                    positionArray[3 * third + 1],
                    positionArray[3 * third + 2]
            );
            Vector3f normal = secondPos.sub(firstPos).cross(thirdPos.sub(firstPos)).normalize();
            normalArray[3 * first] = normal.x;
            normalArray[3 * first + 1] = normal.y;
            normalArray[3 * first + 2] = normal.z;
            normalArray[3 * second] = normal.x;
            normalArray[3 * second + 1] = normal.y;
            normalArray[3 * second + 2] = normal.z;
            normalArray[3 * third] = normal.x;
            normalArray[3 * third + 1] = normal.y;
            normalArray[3 * third + 2] = normal.z;
        }
        mesh = new Mesh(ResourceManager.getTexture(textureResource), positionArray, uvArray, normalArray, facesArray);
    }

    public void cleanup() {
        mesh.cleanup();
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
