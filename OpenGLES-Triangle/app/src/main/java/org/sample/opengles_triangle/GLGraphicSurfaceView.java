package org.sample.opengles_triangle;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GLGraphicSurfaceView extends GLSurfaceView {
	private GLGraphicRenderer renderer;

	public GLGraphicSurfaceView(Context context) {
		super(context);
		init(context);

	}

	public GLGraphicSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	protected void init(Context context) {
		detectedGLESVersion(context);
		/*  */
		renderer = new GLGraphicRenderer(context);

		/* Set the Renderer for drawing on the GLSurfaceView    */
		setRenderer(renderer);
	}

	protected void detectedGLESVersion(Context context){
		ActivityManager am =
				(ActivityManager) context.getSystemService ( Context.ACTIVITY_SERVICE );
		ConfigurationInfo info = am.getDeviceConfigurationInfo();

		/*  Set the OpenGL version */
		if( info.reqGlEsVersion >= 0x30000) {
			setEGLContextClientVersion(3);
		}else{
			setEGLContextClientVersion(2);
		}
	}
}
