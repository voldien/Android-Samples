package org.sample.startactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(this, MyActivity.class);

		/*  */
		Bundle bundle = new Bundle();   /*  Optional, same method can be used with the intent.  */
		bundle.putInt("myInt", 42);
		bundle.putString("myString", "Hello World");

		/*  */
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
