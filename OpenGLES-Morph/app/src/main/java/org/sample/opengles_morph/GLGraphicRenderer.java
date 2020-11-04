package org.sample.opengles_morph;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLGraphicRenderer implements GLSurfaceView.Renderer  {
	Context context;

	public GLGraphicRenderer(Context context){
		this.context = context;
	}

	private void checkShaderError(int shader){
		int[] lstatus = new int[1];
		/*  */
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, lstatus, 0);
		/*	*/
		if (lstatus[0] != GLES20.GL_TRUE) {
			String log = GLES20.glGetShaderInfoLog(shader);
			throw new RuntimeException(String.format("Shader compilation error\n%s", log));
		}
	}

	public int loadShader(String vertexSource, String fragmentSource){

		int vshader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		int fshader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		GLES20.glShaderSource(vshader, vertexSource);
		GLES20.glCompileShader(vshader);
		checkShaderError(vshader);
		GLES20.glShaderSource(fshader, fragmentSource);
		GLES20.glCompileShader(fshader);
		checkShaderError(vshader);

		int program = GLES20.glCreateProgram();

		GLES20.glBindAttribLocation(program, 0, "vPosition");
		GLES20.glBindAttribLocation(program, 1, "color");

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
		GLES20.glDetachShader(program, vshader);
		GLES20.glDeleteShader(fshader);

		return program;
	}


	public int createBuffer(float[] data){

		Buffer buffer = FloatBuffer.wrap(data);
		int[] buf = new int[1];
		GLES20.glGenBuffers(1, buf, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buf[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.length * 4 ,buffer, GLES20.GL_STATIC_DRAW);
		return buf[0];
	}

	public int trianglebuffer;
	public int shader;

	public String readRawStringFile(Context context, int id){
		InputStream inputStream = context.getResources().openRawResource(id);
		String str = "";
		StringBuffer buf = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			if (inputStream != null) {
				while ((str = reader.readLine()) != null) {
					buf.append(str + "\n" );
				}
			}
		} catch (IOException e) {
		} finally {
			try { inputStream.close(); } catch (Throwable ignore) {}
		}
		return buf.toString();
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color
		GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);

		String vertexSource = null; //readRawStringFile(context, R.raw.vertex);
		String fragmentSource = null;//readRawStringFile(context, R.raw.fragment);

		float[] geomtry = new float[]{
				-1.0f, -1.0f, 0.0f,
				1.0f, 0.0f, 0.0f,
				-1.0f,  1.0f, 0.0f,
				0.0f, 1.0f, 0.0f,
				1.0f, -1.0f, 0.0f,
				0.0f, 0.0f, 1.0f,
		};
		shader = loadShader(vertexSource, fragmentSource);
		trianglebuffer = createBuffer(geomtry);
	}

	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		/*  Draw Triangle.  */
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, trianglebuffer);
		GLES20.glUseProgram(shader);
		GLES20.glEnableVertexAttribArray(0);
		GLES20.glEnableVertexAttribArray(1);
		GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(shader, "vPosition"), 3, GLES20.GL_FLOAT, false, 24, 0);
		GLES20.glVertexAttribPointer(1, 3, GLES20.GL_FLOAT, false, 24, 12);
		GLES10.glDrawArrays(GLES10.GL_TRIANGLES, 0, 3);
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}
}
