precision mediump float;

out block {
	vec3 vColor;
} In;

void main(void) {
	gl_FragColor = vec4(In.vColor, 1.0f);
}
