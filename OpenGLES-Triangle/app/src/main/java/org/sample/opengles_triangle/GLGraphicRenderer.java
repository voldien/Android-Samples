package org.sample.opengles_triangle;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLGraphicRenderer implements GLSurfaceView.Renderer {
	public int triangle_vbo;
	public int program;
	Context context;
	private int vba;
	private boolean useOpenGL20 = false;

	final String vertexName = "vPosition";
	final String colorName = "color";

	public GLGraphicRenderer(Context context) {
		this.context = context;
	}

	private void checkShaderError(int shader) {
		int[] lstatus = new int[1];
		/*  */
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, lstatus, 0);
		/*	*/
		if (lstatus[0] != GLES20.GL_TRUE) {
			String log = GLES20.glGetShaderInfoLog(shader);
			throw new RuntimeException(String.format("Shader compilation error\n%s", log));
		}
	}

	public int loadShader(String vertexSource, String fragmentSource) {

		int vshader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		int fshader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		GLES20.glShaderSource(vshader, vertexSource);
		GLES20.glCompileShader(vshader);
		checkShaderError(vshader);
		GLES20.glShaderSource(fshader, fragmentSource);
		GLES20.glCompileShader(fshader);
		checkShaderError(vshader);

		int program = GLES20.glCreateProgram();

		GLES20.glBindAttribLocation(program, 0, vertexName);
		GLES20.glBindAttribLocation(program, 1, colorName);

		GLES20.glAttachShader(program, vshader);
		GLES20.glAttachShader(program, fshader);

		/*  */
		GLES20.glLinkProgram(program);

		int[] lstatus = new int[1];
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, lstatus, 0);
		if (lstatus[0] != GLES20.GL_TRUE) {
			String log = GLES20.glGetProgramInfoLog(program);
			throw new RuntimeException(String.format("Shader link error\n%s", log));
		}

		/*  */
		GLES20.glDetachShader(program, vshader);
		GLES20.glDeleteShader(vshader);
		GLES20.glDetachShader(program, fshader);
		GLES20.glDeleteShader(fshader);

		return program;
	}

	public int createBuffer(float[] data) {

		Buffer buffer = FloatBuffer.wrap(data);
		int[] buf = new int[1];
		GLES20.glGenBuffers(1, buf, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buf[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.length * 4, buffer, GLES20.GL_STATIC_DRAW);

		int vertexLocation = GLES20.glGetAttribLocation(program, vertexName);
		int colorLocation = GLES20.glGetAttribLocation(program, colorName);

		GLES20.glEnableVertexAttribArray(vertexLocation);
		GLES20.glEnableVertexAttribArray(colorLocation);
		GLES20.glVertexAttribPointer(vertexLocation, 3, GLES20.GL_FLOAT, false, 24, 0);
		GLES20.glVertexAttribPointer(colorLocation, 3, GLES20.GL_FLOAT, false, 24, 12);
		return buf[0];
	}

	public String readRawStringFile(Context context, int id) {
		InputStream inputStream = context.getResources().openRawResource(id);
		String str = "";
		StringBuffer buf = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			if (inputStream != null) {
				while ((str = reader.readLine()) != null) {
					buf.append(str + "\n");
				}
			}
		} catch (IOException e) {
		} finally {
			try {
				inputStream.close();
			} catch (Throwable ignore) {
			}
		}
		return buf.toString();
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color
		GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);

		String vertexSource = readRawStringFile(context, R.raw.vertex_es_20);
		String fragmentSource = readRawStringFile(context, R.raw.fragment_es_20);

		float[] triangleGeometryData = new float[]{
				-1.0f, -1.0f, 0.0f, /*  Vertex0 location. */
				1.0f, 0.0f, 0.0f,   /*  Vertex0 color. */
				-0.0f, 1.0f, 0.0f,  /*  Vertex1 location. */
				0.0f, 1.0f, 0.0f,   /*  Vertex1 color. */
				1.0f, -1.0f, 0.0f,  /*  Vertex2 location. */
				0.0f, 0.0f, 1.0f,   /*  Vertex2 color. */
		};
		program = loadShader(vertexSource, fragmentSource);

		IntBuffer buf = IntBuffer.allocate(1);
		GLES30.glGenVertexArrays(1, buf);
		GLES30.glBindVertexArray(buf.get(0));
		triangle_vbo = createBuffer(triangleGeometryData);
		GLES30.glBindVertexArray(0);
		vba = buf.get(0);
	}

	private void drawOpenGL20() {
		/*  Draw Triangle.  */
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, triangle_vbo);
		GLES20.glUseProgram(program);
		int vertexLocation = GLES20.glGetAttribLocation(program, vertexName);
		int colorLocation = GLES20.glGetAttribLocation(program, colorName);
		GLES20.glEnableVertexAttribArray(vertexLocation);
		GLES20.glEnableVertexAttribArray(colorLocation);
		GLES20.glVertexAttribPointer(vertexLocation, 3, GLES20.GL_FLOAT, false, 24, 0);
		GLES20.glVertexAttribPointer(colorLocation, 3, GLES20.GL_FLOAT, false, 24, 12);
		GLES10.glDrawArrays(GLES10.GL_TRIANGLES, 0, 3);
	}

	private void drawOpenGL30() {
		GLES20.glUseProgram(program);
		GLES30.glBindVertexArray(vba);
		GLES10.glDrawArrays(GLES10.GL_TRIANGLES, 0, 3);
	}

	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		/*  Draw Triangle - Using OpenGLES 2.0  */
		//drawOpenGL20();

		/*  Draw Triangle - Using OpenGLES 3.0  */
		drawOpenGL30();
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}
}
