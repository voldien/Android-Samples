package org.sample.skybox;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLGraphicSurfaceView surfaceView = new GLGraphicSurfaceView(this);
		setContentView(surfaceView);
		//TODO compute
	}
}
