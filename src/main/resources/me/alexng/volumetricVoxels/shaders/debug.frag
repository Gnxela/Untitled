#version 330 core

uniform vec3 debug_color;

out vec4 FragColor;

void main() {
    FragColor = vec4(debug_color, 1);
}
