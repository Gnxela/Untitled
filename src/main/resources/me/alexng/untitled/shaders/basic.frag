#version 330 core

#include "me/alexng/untitled/shaders/shared/lighting.sfrag"

struct Material {
    sampler2D texture_diffuse1;
    sampler2D texture_specular1;
    float shininess;
};

in vec3 outNormal;
in vec3 outFragPos;
in vec2 outTexCoord;

uniform vec3 viewPosition;
uniform Material material;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(outNormal);
    vec3 diffuseSample = vec3(texture(material.texture_diffuse1, outTexCoord));
    vec3 specularSample = vec3(texture(material.texture_specular1, outTexCoord));

    FragColor = defaultLighting(normal, diffuseSample, specularSample, outFragPos, viewPosition);
}
