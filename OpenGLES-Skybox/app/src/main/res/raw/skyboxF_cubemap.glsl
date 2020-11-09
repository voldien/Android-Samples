FRAGLOCATION(0) vec4 fragColor;
SMOOTH_IN vec3 vVertex;

uniform samplerCube ReflectionTexture;
uniform vec4 DiffuseColor;

void main(void){
	fragColor = texture(ReflectionTexture, vVertex) * DiffuseColor;
	fragColor.a = 1.0f;
}
