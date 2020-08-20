#version 330 core

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct Light {
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

in vec3 outNormal;
in vec3 outFragPos;

uniform vec3 viewPosition;
uniform vec3 objectColor;
uniform Material material;
uniform Light light;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(outNormal);

    vec3 ambient = light.ambient * material.ambient;

    vec3 lightDirection = normalize(light.position - outFragPos);
    float diff = max(dot(normal, lightDirection), 0);
    vec3 diffuse = light.diffuse * diff * material.diffuse;

    vec3 viewDirection = normalize(viewPosition - outFragPos);
    vec3 reflectedLightDirection = reflect(-lightDirection, normal);
    float spec = pow(max(dot(viewDirection, reflectedLightDirection), 0), 32);
    vec3 specular = light.specular * spec * material.specular;

    FragColor = vec4(objectColor * (ambient + diffuse + specular), 1.0f);
}
