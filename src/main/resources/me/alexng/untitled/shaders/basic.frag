#version 330 core

in vec3 outColor;
in vec2 outTexCoord;

uniform vec3 lightColor;
uniform vec3 objectColor;

out vec4 FragColor;

void main() {
    FragColor = vec4(lightColor * objectColor, 1.0f);
}
