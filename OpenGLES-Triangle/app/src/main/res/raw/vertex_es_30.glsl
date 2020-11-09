layout(location = 0) vec3 vPosition;
layout(location = 1) vec3 color;
precision mediump float;

out block {
	vec3 vColor;
} Out;

void main(void) {
	gl_Position = vec4(vPosition, 1.0);
	Out.vColor = color;
}
