#version 330 core

#include "me/alexng/untitled/shaders/shared/lighting.sfrag"

in vec3 outNormal;
in vec3 outFragPos;
in vec3 outColor;

uniform vec3 viewPosition;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(outNormal);
    vec3 diffuseSample = outColor;
    vec3 specularSample = vec3(0.1);

    FragColor = defaultLighting(normal, diffuseSample, specularSample, outFragPos, viewPosition);
}
