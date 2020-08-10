#version 330 core

layout (location = 0) in vec3 vecPos;
layout (location = 1) in vec3 vecColor;
layout (location = 2) in vec2 texCoord;

uniform bool whiteout;

out vec3 outColor;
out vec2 outTexCoord;

void main() {
    gl_Position = vec4(vecPos.x, vecPos.y, vecPos.z, 1.0);
    if (whiteout) {
        outColor = vec3(1, 1, 1);
    } else {
        outColor = vecColor;
    }
    outTexCoord = texCoord;
}
