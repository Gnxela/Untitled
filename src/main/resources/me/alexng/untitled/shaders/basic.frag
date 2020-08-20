#version 330 core

struct Material {
    sampler2D texture_diffuse1;
    sampler2D texture_specular1;
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
in vec2 outTexCoord;

uniform vec3 viewPosition;
uniform Material material;
uniform Light light;

out vec4 FragColor;

void main() {
    vec3 normal = normalize(outNormal);
    vec3 diffuseSample = vec3(texture(material.texture_diffuse1, outTexCoord));
    vec3 specularSample = vec3(texture(material.texture_specular1, outTexCoord));

    vec3 ambient = light.ambient * diffuseSample;

    vec3 lightDirection = normalize(light.position - outFragPos);
    float diff = max(dot(normal, lightDirection), 0);
    vec3 diffuse = light.diffuse * diff * diffuseSample;

    vec3 viewDirection = normalize(viewPosition - outFragPos);
    vec3 reflectedLightDirection = reflect(-lightDirection, normal);
    float spec = pow(max(dot(viewDirection, reflectedLightDirection), 0), 32);
    vec3 specular = light.specular * spec * specularSample;

    FragColor = vec4(ambient + diffuse + specular, 1.0f);
}
