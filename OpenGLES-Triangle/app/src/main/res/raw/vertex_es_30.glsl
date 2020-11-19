#version 300 es

layout(location = 0) in vec3 vertex;
layout(location = 1) in vec3 color;
precision mediump float;

out vec3 vColor;

void main(void) {
	gl_Position = vec4(vertex, 1.0);
	vColor = color;
}
