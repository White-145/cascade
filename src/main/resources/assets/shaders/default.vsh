#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 normal;

out vec2 outTextCoord;
out vec3 outNormal;
out float lightness;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform float minLightness;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
    outTextCoord = texCoord;
    outNormal = normalize(modelMatrix * vec4(normal, 0.0)).xyz;
    lightness = dot(outNormal, vec3(-1, 1, 1));
    lightness = clamp(lightness, minLightness, 0.9);
}