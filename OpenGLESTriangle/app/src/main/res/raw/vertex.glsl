#version 120

#if defined(GL_ARB_explicit_attrib_location)
layout(location = 0) in vec3 vertex;
layout(location = 1) in vec3 color;
#else
attribute vec3 vertex;
attribute vec3 color;
#endif

/*	Check if mobile OpenGL is used.	*/
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

out gl_PerVertex {
	vec4 gl_Position;
	float gl_PointSize;
	float gl_ClipDistance[];
};

out block {
	vec4 color;
} Out ;

void main() {

	gl_Position = vec4(vertex, 0.0);
	Out.color = color;
}
