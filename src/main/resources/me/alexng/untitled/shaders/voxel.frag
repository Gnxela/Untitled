#version 330 core

#include "me/alexng/untitled/shaders/shared/lighting.sfrag"
#include "me/alexng/untitled/shaders/shared/material.sfrag"

in vec3 outNormal;
in vec3 outFragPos;

uniform vec3 viewPosition;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(outNormal);

    FragColor = defaultLighting(normal, vec3(1, 0, 0), vec3(0.2), outFragPos, viewPosition);
}
