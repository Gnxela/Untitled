#version 330 core

in layout (location = 0) vec3 position;
in layout (location = 1) vec3 normal;
in layout (location = 2) vec3 color;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 outFragPos;
out vec3 outNormal;
out vec3 outColor;

void main() {
    vec4 position4f = vec4(position, 1);
    gl_Position = projection * view * model * position4f;
    outFragPos = vec3(model * position4f);
    outNormal = mat3(transpose(inverse(model))) * normal;
    outColor = color;
}
