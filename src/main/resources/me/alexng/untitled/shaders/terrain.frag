#version 330 core

in vec3 outFragPos;

uniform vec3 terrainColor;

out vec4 FragColor;

void main() {
    // TODO: Calculate lighting. Need normals
    FragColor = vec4(terrainColor, 1.0);
}
