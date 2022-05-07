package org.sample.listview;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
	List<String> items;
	public ListViewAdapter(List<String> items) {
		this.items = items;
	}


	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater.from(parent.getContext()));
			convertView = inflater.inflate(R.layout.item_text_view, parent, false);
		}

		((TextView) convertView.findViewById(R.id.textview_item))
				.setText((CharSequence) getItem(position));
		return convertView;
	}
}
