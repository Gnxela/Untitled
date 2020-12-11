#version 330 core

in layout (location = 0) vec3 position;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vec4 position4f = vec4(position, 1);
    gl_Position = projection * view * model * position4f;
}
