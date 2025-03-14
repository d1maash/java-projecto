#version 120

attribute vec3 position;
attribute vec3 color;

varying vec3 fragColor;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

void main() {
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0);
    fragColor = color;
} 