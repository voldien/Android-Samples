FRAGLOCATION(0) vec4 fragColor;
SMOOTH_IN vec3 vVertex;

uniform samplerCube ReflectionTexture;
uniform vec4 DiffuseColor;

void main(void) {
	gl_FragColor = vec4(vColor, 1.0f);
}
