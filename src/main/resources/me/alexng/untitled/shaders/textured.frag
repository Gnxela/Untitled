#version 330 core

struct Material {
    sampler2D texture_diffuse1;
};

in vec2 outTextureCoords;

uniform Material material;

out vec4 FragColor;

void main() {
    vec3 sampleColor = texture(material.texture_diffuse1, outTextureCoords).rgb;
    FragColor = vec4(sampleColor, 1f);
}
