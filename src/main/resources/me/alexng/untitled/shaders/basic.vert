#version 330 core

in layout (location = 0) vec3 vector;
in layout (location = 1) vec2 texCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    gl_Position = projection * view * model * vec4(vector, 1.0);
}
