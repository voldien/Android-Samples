#version 120

in block {
	vec3 color;
} In ;

void main() {

	gl_FragColor = vec4(In.color, 1.0f);
}
