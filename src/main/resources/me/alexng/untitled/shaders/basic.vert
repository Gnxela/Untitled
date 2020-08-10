#version 330 core

in layout (location = 0) vec3 vecPos;
in layout (location = 1) vec3 vecColor;
in layout (location = 2) vec2 texCoord;

out vec3 outColor;
out vec2 outTexCoord;

void main() {
    gl_Position = vec4(vecPos.x, vecPos.y, vecPos.z, 1.0);
    outColor = vecColor;
    outTexCoord = texCoord;
}
