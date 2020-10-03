#version 330 core

#include "me/alexng/untitled/shaders/shared/lighting.sfrag"

struct Material {
    float shininess;
};

in vec3 outNormal;
in vec3 outFragPos;
in vec3 outColor;

uniform vec3 viewPosition;
uniform Material material;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(outNormal);
    vec3 diffuseSample = outColor;
    vec3 specularSample = vec3(0.1);

    FragColor = defaultLighting(normal, diffuseSample, specularSample, outFragPos, viewPosition);
}
