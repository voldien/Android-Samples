attribute vec3 vertex;
attribute vec3 color;
precision mediump float;

varying	vec3 vColor;

void main(void) {
	gl_Position = vec4(vertex, 1.0);
	vColor = color;
}
