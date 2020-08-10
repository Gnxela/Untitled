#version 330 core

in vec3 outColor;
in vec2 outTexCoord;

uniform sampler2D inputTexture1;
uniform sampler2D inputTexture2;

out vec4 FragColor;

void main() {
    vec4 color = vec4(outColor, 1.0f);
    vec4 texColor = mix(texture(inputTexture1, outTexCoord), texture(inputTexture2, outTexCoord), 0.3);
    FragColor = texColor * color;
}
