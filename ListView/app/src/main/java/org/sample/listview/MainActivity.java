package org.sample.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*  Find List View object.  */
		ListView listView = findViewById(R.id.list_view);
		/*  Create list objects.    */
		List<String> items = new ArrayList<>(Arrays.asList("Rem", "Ram", "Tanya", "Saber", "Morgiana", "Misaka", "Mikasa", "Nagisa", "Megumin", "Mayo"));
		/*  Create adapter and assigned to list view object.    */
		ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.item_text_view,R.id.textview_item,items);
		/*  Create adapter and assigned to list view object.    */
		ListViewAdapter listViewAdapter = new ListViewAdapter(items);

		listView.setAdapter(listViewAdapter);
	}
}
