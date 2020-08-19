#version 330 core

in layout (location = 0) vec3 vector;
in layout (location = 1) vec2 texCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 outColor;
out vec2 outTexCoord;

void main() {
    gl_Position = projection * view * model * vec4(vector, 1.0);
    outColor = vec3(1, 1, 1);
    outTexCoord = texCoord;
}
