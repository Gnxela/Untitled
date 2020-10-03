#version 330 core

#include "me/alexng/untitled/shaders/shared/lighting.sfrag"
#include "me/alexng/untitled/shaders/shared/material.sfrag"

in vec3 outNormal;
in vec3 outFragPos;
in vec2 outTexCoord;

uniform vec3 viewPosition;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(outNormal);
    vec3 diffuseSample = vec3(texture(material.texture_diffuse1, outTexCoord));
    vec3 specularSample = vec3(texture(material.texture_specular1, outTexCoord));

    FragColor = defaultLighting(normal, diffuseSample, specularSample, outFragPos, viewPosition);
}
