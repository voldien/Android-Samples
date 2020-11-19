#version 300 es

precision mediump float;
layout(location = 0) out vec4 fragColor;
in vec3 vColor;

void main(void) {
	fragColor = vec4(vColor, 1.0f);
}
