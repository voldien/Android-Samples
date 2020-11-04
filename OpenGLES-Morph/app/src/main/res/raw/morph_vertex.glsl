#version 300 es


layout(location = 0) in vec3 vVertex0;
layout(location = 1) in vec2 TextureCoordinate0;
layout(location = 2) in vec3 vNormal0;
layout(location = 3) in vec3 vTangent0;
layout(location = 4) in vec3 vVertex1;
layout(location = 5) in vec2 TextureCoordinate1;
layout(location = 6) in vec3 vNormal1;
layout(location = 7) in vec3 vTangent1;


uniform float t[2];
/*#if defined(GL_ARB_explicit_attrib_location)

#else

#endif*/

attribute vec3 vPosition;
attribute vec3 color;
precision mediump float;

/*out gl_PerVertex {
	vec4 gl_Position;
	float gl_PointSize;
	float gl_ClipDistance[];
};*/

varying	vec3 vColor;

void main(void) {
	gl_Position = vec4(vPosition, 1.0);
	vColor = color;
}
