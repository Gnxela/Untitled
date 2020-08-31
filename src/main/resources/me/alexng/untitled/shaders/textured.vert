#version 330 core

in layout (location = 0) vec3 position;
in layout (location = 0) vec2 textureCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec2 outTextureCoords;

void main() {
    gl_Position = projection * view * model * vec4(position, 1.0);
    outTextureCoords = textureCoords;
}
