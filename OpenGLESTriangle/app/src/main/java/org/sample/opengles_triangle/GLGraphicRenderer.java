package org.sample.opengles_triangle;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLGraphicRenderer implements GLSurfaceView.Renderer  {
	Context context;

	public GLGraphicRenderer(Context context){
		this.context = context;
	}

	public int loadShader(String vertexSource, String fragmentSource){
		int program = GLES20.glCreateProgram();
		int vshader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		int fshader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		GLES20.glShaderSource(vshader, vertexSource);
		GLES20.glShaderSource(fshader, fragmentSource);
		GLES20.glCompileShader(vshader);
		GLES20.glCompileShader(fshader);
		//GLES20.glGetShaderInfoLog()

		GLES20.glAttachShader(program, vshader);
		GLES20.glAttachShader(program, fshader);
		GLES20.glLinkProgram(program);

		GLES20.glDeleteShader(vshader);
		GLES20.glDeleteShader(fshader);


		GLES20.glValidateProgram(program);
		return program;
	}


	public int createBuffer(float[] data){

		Buffer buffer = FloatBuffer.wrap(data);
		int[] buf = new int[1];
		GLES20.glGenBuffers(1, buf, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buf[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffer.position(),buffer, GLES20.GL_STATIC_DRAW);
		return buf[0];
	}

	public int trianglebuffer;
	public int shader;

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color
		GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);

		InputStream inputStream = context.getResources().openRawResource(R.raw.vertex);
		//creating an InputStreamReader object
		InputStreamReader isReader = new InputStreamReader(inputStream);
		//Creating a BufferedReader object
		BufferedReader reader = new BufferedReader(isReader);
		StringBuffer sb = new StringBuffer();
		String vertexSource = sb.toString();
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		inputStream = context.getResources().openRawResource(R.raw.fragment);
		//creating an InputStreamReader object
		 isReader = new InputStreamReader(inputStream);
		//Creating a BufferedReader object
		 reader = new BufferedReader(isReader);
		 sb = new StringBuffer();
		String fragmentSource = sb.toString();
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		float[] geomtry = new float[]{
				0.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 1.0f, 0.0f,
				0.0f, 0.5f, 0.5f,
				0.0f, 0.0f, 1.0f,
		};
		shader = loadShader(vertexSource, fragmentSource);
		trianglebuffer = createBuffer(geomtry);
	}

	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		/*  Draw Triangle.  */
		GLES20.glUseProgram(shader);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, trianglebuffer);
		GLES20.glEnableVertexAttribArray(0);
		GLES20.glEnableVertexAttribArray(1);
		GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 24, 0);
		GLES20.glVertexAttribPointer(1, 3, GLES20.GL_FLOAT, false, 24, 12);
		GLES10.glDrawArrays(GLES10.GL_TRIANGLES, 0, 3);
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}
}
