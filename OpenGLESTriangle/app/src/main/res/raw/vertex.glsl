

/*#if defined(GL_ARB_explicit_attrib_location)
layout(location = 0) in vec3 vertex;
layout(location = 1) in vec3 color;
#else

#endif*/

attribute vec3 vPosition;
attribute vec3 color;
precision mediump float;
/*	Check if mobile OpenGL is used.	*/
/*
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif
*/

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
