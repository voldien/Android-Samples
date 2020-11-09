attribute vec3 vPosition;
attribute vec3 color;
precision mediump float;

varying	vec3 vColor;

void main(void) {
	gl_Position = vec4(vPosition, 1.0);
	vColor = color;
}
