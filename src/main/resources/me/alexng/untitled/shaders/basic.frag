#version 330 core

in vec3 outNormal;
in vec3 outFragPos;

uniform vec3 viewPosition;
uniform vec3 objectColor;
uniform vec3 lightColor;
uniform vec3 lightPosition;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(outNormal);

    float ambientStrength = 0.1;
    vec3 ambient = ambientStrength * lightColor;

    vec3 lightDirection = normalize(lightPosition - outFragPos);
    float diff = max(dot(normal, lightDirection), 0);
    vec3 diffuse = diff * lightColor;

    float specularStrength = 0.5;
    vec3 viewDirection = normalize(viewPosition - outFragPos);
    vec3 reflectedLightDirection = reflect(-lightDirection, normal);
    float spec = pow(max(dot(viewDirection, reflectedLightDirection), 0), 32);
    vec3 specular = specularStrength * spec * lightColor;

    FragColor = vec4(objectColor * (ambient + diffuse + specular), 1.0f);
}
