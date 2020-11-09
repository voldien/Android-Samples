ATTRIBUTE_IN(0) vec3 vertex;
SMOOTH_OUT vec3 vVertex;

uniform vec4 DiffuseColor;
uniform mat4 ModelViewProjection;

void main (void){
	vec4 MVPPos  = ModelViewProjection * vec4(vertex, 1.0);
	gl_Position = MVPPos.xyww;
	vVertex = vertex;
}
