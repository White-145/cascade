package me.white.cascade.render.shader;

import com.google.gson.*;
import me.white.cascade.Resource;
import org.lwjgl.opengl.GL46;

import java.io.IOException;

public class ShaderProgram {
    private static ShaderProgram boundShader = null;
    private final int programId;
    private final String path;
    private UniformsMap uniformsMap;

    public ShaderProgram(String resource) {
        this(new Resource(Resource.Type.ASSET_SHADER, resource));
    }

    public ShaderProgram(Resource resource) {
        if (!resource.isOf(Resource.Type.ASSET_SHADER)) {
            throw new IllegalStateException("Shader should be of type ASSET_SHADER.");
        }
        path = resource.getPath();

        // reading config
        JsonObject config;
        try {
            String configText = resource.readText();
            config = new Gson().fromJson(configText, JsonObject.class);
            if (config == null) {
                config = new JsonObject();
            }
        } catch (IOException | JsonSyntaxException e) {
            throw new IllegalStateException("Could not read config file for shader '" + path + "': " + e);
        }

        boolean hasFragmentShader = config.has("fragment");
        boolean hasVertexShader = config.has("vertex");
        boolean hasComputeShader = config.has("compute");
        if (!hasFragmentShader && !hasVertexShader && !hasComputeShader) {
            throw new IllegalStateException("Can not create shader program without any shaders '" + path + "'.");
        }

        // reading fragment shader
        String fragmentSource = null;
        if (hasFragmentShader) {
            String fragmentPath = config.get("fragment").getAsString();
            Resource fragmentResource = new Resource(Resource.Type.SHADER_FRAGMENT, fragmentPath);
            try {
                fragmentSource = fragmentResource.readText();
            } catch (IOException e) {
                throw new IllegalStateException("Could not read fragment shader source file '" + path + "': " + e);
            }
            if (fragmentSource == null) {
                throw new IllegalStateException("Could not read fragment shader source file '" + path + "'.");
            }
        }

        // reading vertex shader
        String vertexSource = null;
        if (hasVertexShader) {
            String vertexPath = config.get("vertex").getAsString();
            Resource vertexResource = new Resource(Resource.Type.SHADER_VERTEX, vertexPath);
            try {
                vertexSource = vertexResource.readText();
            } catch (IOException e) {
                throw new IllegalStateException("Could not read vertex shader source file '" + path + "': " + e);
            }
            if (vertexSource == null) {
                throw new IllegalStateException("Could not read vertex shader source file '" + path + "'.");
            }
        }

        // reading compute shader
        String computeSource = null;
        if (hasComputeShader) {
            String computePath = config.get("compute").getAsString();
            Resource computeResource = new Resource(Resource.Type.SHADER_COMPUTE, computePath);
            try {
                computeSource = computeResource.readText();
            } catch (IOException e) {
                throw new IllegalStateException("Could not read compute shader source file '" + path + "': " + e);
            }
            if (computeSource == null) {
                throw new IllegalStateException("Could not read compute shader source file '" + path + "'.");
            }
        }

        // creating program
        programId = GL46.glCreateProgram();
        if (programId == 0) {
            throw new IllegalStateException("Could not create shader '" + path + "'.");
        }

        // compiling fragment shader
        int fragmentId = 0;
        if (hasFragmentShader) {
            fragmentId = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER);
            if (fragmentId == 0) {
                throw new IllegalStateException("Could not create shader '" + path + "'.");
            }
            GL46.glShaderSource(fragmentId, fragmentSource);
            GL46.glCompileShader(fragmentId);
            if (GL46.glGetShaderi(fragmentId, GL46.GL_COMPILE_STATUS) == 0) {
                int length = GL46.glGetProgrami(fragmentId, GL46.GL_INFO_LOG_LENGTH);
                throw new IllegalStateException("Could not compile fragment shader '" + path + "': " + GL46.glGetShaderInfoLog(fragmentId, length));
            }
            GL46.glAttachShader(programId, fragmentId);
        }

        // compiling vertex program
        int vertexId = 0;
        if (hasVertexShader) {
            vertexId = GL46.glCreateShader(GL46.GL_VERTEX_SHADER);
            if (vertexId == 0) {
                throw new IllegalStateException("Could not create shader '" + path + "'.");
            }
            GL46.glShaderSource(vertexId, vertexSource);
            GL46.glCompileShader(vertexId);
            if (GL46.glGetShaderi(vertexId, GL46.GL_COMPILE_STATUS) == 0) {
                int length = GL46.glGetProgrami(vertexId, GL46.GL_INFO_LOG_LENGTH);
                throw new IllegalStateException("Could not compile vertex shader '" + path + "': " + GL46.glGetShaderInfoLog(vertexId, length));
            }
            GL46.glAttachShader(programId, vertexId);
        }

        // compiling compute program
        int computeId = 0;
        if (hasComputeShader) {
            computeId = GL46.glCreateShader(GL46.GL_COMPUTE_SHADER);
            if (computeId == 0) {
                throw new IllegalStateException("Could not create shader '" + path + "'.");
            }
            GL46.glShaderSource(computeId, computeSource);
            GL46.glCompileShader(computeId);
            if (GL46.glGetShaderi(computeId, GL46.GL_COMPILE_STATUS) == 0) {
                int length = GL46.glGetProgrami(computeId, GL46.GL_INFO_LOG_LENGTH);
                throw new IllegalStateException("Could not compile compute shader '" + path + "': " + GL46.glGetShaderInfoLog(computeId, length));
            }
            GL46.glAttachShader(programId, computeId);
        }

        // linking program
        GL46.glLinkProgram(programId);
        if (hasFragmentShader) {
            GL46.glDetachShader(programId, fragmentId);
            GL46.glDeleteShader(fragmentId);
        }
        if (hasVertexShader) {
            GL46.glDetachShader(programId, vertexId);
            GL46.glDeleteShader(vertexId);
        }
        if (hasComputeShader) {
            GL46.glDetachShader(programId, computeId);
            GL46.glDeleteShader(computeId);
        }
        if (GL46.glGetProgrami(programId, GL46.GL_LINK_STATUS) == 0) {
            int length = GL46.glGetProgrami(programId, GL46.GL_INFO_LOG_LENGTH);
            throw new IllegalStateException("Could not link shader '" + path + "': " + GL46.glGetProgramInfoLog(programId, length));
        }

        uniformsMap = new UniformsMap(this);
    }

    public void bind() {
        if (boundShader != null) {
            boundShader.unbind();
        }
        GL46.glUseProgram(programId);
        boundShader = this;
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            GL46.glDeleteProgram(programId);
        }
    }

    public int getProgramId() {
        return programId;
    }
    
    public String getPath() {
        return path;
    }

    public UniformsMap getUniformsMap() {
        return uniformsMap;
    }

    public void unbind() {
        if (boundShader != null) {
            if (boundShader != this) {
                throw new IllegalStateException("Can not unbind unbound shader.");
            }
            GL46.glUseProgram(0);
            boundShader = null;
        }
    }
}
