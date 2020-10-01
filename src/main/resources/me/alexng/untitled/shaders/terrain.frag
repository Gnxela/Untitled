#version 330 core

in vec3 outFragPos;
in vec3 outColor;

out vec4 FragColor;

void main() {
    // TODO: Calculate lighting. Need normals
    FragColor = vec4(outColor, 1.0);
}
