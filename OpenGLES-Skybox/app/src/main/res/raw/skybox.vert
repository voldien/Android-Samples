#version 330

 in vec3 vertex;
out vec3 vVertex;

layout(location = 0) uniform mat4 MVP;

void main() {
	vec4 MVPPos = MVP * vec4(vertex, 1.0);
	gl_Position = MVPPos.xyww;
	vVertex = normalize(vertex);
}
