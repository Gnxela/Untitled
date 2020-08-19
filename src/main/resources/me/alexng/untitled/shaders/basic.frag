#version 330 core

in vec3 outNormal;
in vec3 outFragPos;

uniform vec3 objectColor;
uniform vec3 lightColor;
uniform vec3 lightPosition;

out vec4 FragColor;

void main() {
    float ambientStrength = 0.1;
    vec3 ambient = ambientStrength * lightColor;

    vec3 lightDirection = normalize(lightPosition - outFragPos);
    float diff = max(dot(normalize(outNormal), lightDirection), 0);
    vec3 diffuse = diff * lightColor;

    FragColor = vec4(objectColor * (ambient + diffuse), 1.0f);
}
