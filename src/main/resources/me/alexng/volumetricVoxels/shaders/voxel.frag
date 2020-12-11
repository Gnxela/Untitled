#version 330 core

#include "me/alexng/volumetricVoxels/shaders/shared/lighting.sfrag"
#include "me/alexng/volumetricVoxels/shaders/shared/material.sfrag"

in vec3 outNormal;
in vec3 outFragPos;

uniform vec3 voxel_color;
uniform vec3 viewPosition;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(outNormal);

    FragColor = defaultLighting(normal, voxel_color, vec3(0.2), outFragPos, viewPosition);
}
