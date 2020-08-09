#version 330 core

layout (location = 0) in vec3 vecPos;
layout (location = 1) in vec3 vecColor;

out vec3 outColor;

void main() {
    gl_Position = vec4(vecPos.x, vecPos.y, vecPos.z, 1.0);
    outColor = vecColor;
}
