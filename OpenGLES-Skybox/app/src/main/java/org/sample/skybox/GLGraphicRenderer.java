package org.sample.skybox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

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
	static final float g_vertex_buffer_data[] = {
			-1.0f, -1.0f, -1.0f, // triangle 1 : begin
			-1.0f, -1.0f, 1.0f,
			-1.0f, 1.0f, 1.0f, // triangle 1 : end
			1.0f, 1.0f, -1.0f, // triangle 2 : begin
			-1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, -1.0f, // triangle 2 : end
			1.0f, -1.0f, 1.0f,
			-1.0f, -1.0f, -1.0f,
			1.0f, -1.0f, -1.0f,
			1.0f, 1.0f, -1.0f,
			1.0f, -1.0f, -1.0f,
			-1.0f, -1.0f, -1.0f,
			-1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, 1.0f,
			-1.0f, 1.0f, -1.0f,
			1.0f, -1.0f, 1.0f,
			-1.0f, -1.0f, 1.0f,
			-1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, 1.0f,
			-1.0f, -1.0f, 1.0f,
			1.0f, -1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, -1.0f, -1.0f,
			1.0f, 1.0f, -1.0f,
			1.0f, -1.0f, -1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, -1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, -1.0f,
			-1.0f, 1.0f, -1.0f,
			1.0f, 1.0f, 1.0f,
			-1.0f, 1.0f, -1.0f,
			-1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			-1.0f, 1.0f, 1.0f,
			1.0f, -1.0f, 1.0f
	};
	final String vertexName = "vPosition";
	final String colorName = "color";
	public int triangle_vbo;
	public int skybox_texture;
	public int program;
	Context context;
	float[] mvp = new float[16];
	private int vba;
	private int uniform_location_mvp;

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

	public int createCubemapTexture(byte[] data, int width, int height, int internalformat, int format) {
		GLES20.glGenTextures(1, null);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);

/*		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + 0, 0, internalformat, width, height, 0,
				format,
				type, desc->cubepixel[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + 1, 0, internalformat, width, height, 0,
				format,
				type, desc->cubepixel[1]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + 2, 0, internalformat, width, height, 0,
				format,
				type, desc->cubepixel[2]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + 3, 0, internalformat, width, height, 0,
				format,
				type, desc->cubepixel[3]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + 4, 0, internalformat, width, height, 0,
				format,
				type, desc->cubepixel[4]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + 5, 0, internalformat, width, height, 0,
				format,
				type, desc->cubepixel[5]);*/

		return 0;
	}

	public int createTexture(Buffer data, int width, int height, int internalformat, int format) {
		IntBuffer buf = IntBuffer.allocate(1);
		GLES20.glGenTextures(1, buf);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);

		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, internalformat, width, height, 0,
				format,
				GLES20.GL_UNSIGNED_BYTE, data);
		return buf.array()[0];
	}

	public int[] loadImageFromRawFile(Bitmap bm) {
		int w = bm.getWidth();
		int h = bm.getHeight();
		int[] data = new int[w * h];

		bm.getPixels(data, 0, w, 0, 0, w, h);
		return data;
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color
		GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);

		String vertexSource = readRawStringFile(context, R.raw.skybox);
		String fragmentSource = readRawStringFile(context, R.raw.skybox_panoramic);

		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.skybox_cloud100001);
		int[] skybox_panoramic = loadImageFromRawFile(bm);

		skybox_texture = createTexture(IntBuffer.wrap(skybox_panoramic), bm.getWidth(), bm.getHeight(), GLES20.GL_RGB, GLES20.GL_RGB);

		program = loadShader(vertexSource, fragmentSource);

		/*	Setup program.	*/
		GLES20.glUseProgram(this.program);
		this.uniform_location_mvp = GLES20.glGetUniformLocation(this.program, "MVP");
		GLES20.glUniform1i(GLES20.glGetUniformLocation(this.program, "panorama"), 0);
		GLES20.glUseProgram(0);

		IntBuffer buf = IntBuffer.allocate(1);
		GLES30.glGenVertexArrays(1, buf);
		GLES30.glBindVertexArray(buf.get(0));
		triangle_vbo = createBuffer(g_vertex_buffer_data);
		GLES30.glBindVertexArray(0);
		vba = buf.get(0);
	}

	private void drawOpenGL30() {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.skybox_texture);
		GLES20.glUseProgram(program);

		GLES20.glUniformMatrix4fv(this.uniform_location_mvp, 1, false, mvp, 0);

		GLES30.glBindVertexArray(vba);
		GLES10.glDrawArrays(GLES10.GL_TRIANGLES, 0, g_vertex_buffer_data.length / 3);
	}

	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		Matrix.perspectiveM(mvp,0, 75.0f, 1.333f, 0.5f, 1000.0f);

		/*  Draw Triangle - Using OpenGLES 3.0  */
		drawOpenGL30();
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}
}
