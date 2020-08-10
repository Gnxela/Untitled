#version 330 core

in vec3 outColor;
in vec2 outTexCoord;

uniform sampler2D inputTexture;

out vec4 FragColor;

void main() {
    FragColor = texture(inputTexture, outTexCoord) * vec4(outColor, 1.0f);
}
