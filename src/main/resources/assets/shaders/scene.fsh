#version 330

in vec2 outTextCoord;
in vec3 outNormal;
in float lightness;

out vec4 fragColor;

uniform sampler2D txtSampler;

void main() {
    fragColor = texture(txtSampler, outTextCoord);
    fragColor *= lightness;
}