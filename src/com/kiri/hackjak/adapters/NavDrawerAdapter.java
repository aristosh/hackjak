package com.kiri.hackjak.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kiri.hackjak.R;

public class NavDrawerAdapter extends BaseAdapter {

	private String[] menus;
	private LayoutInflater inflater;
	private Context context;

	public NavDrawerAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		menus = new String[] {
				context.getString(R.string.title_section_trayek),
				context.getString(R.string.title_section_taxi),
				context.getString(R.string.title_section_commuter),
				context.getString(R.string.title_section_contribute),
				context.getString(R.string.title_section_about), };
	}

	@Override
	public int getCount() {
		return menus.length;
	}

	@Override
	public Object getItem(int position) {
		return menus[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_nav_drawer, parent, false);
		TextView tv = (TextView) v.findViewById(R.id.text);
		tv.setText(menus[position]);
		v.setContentDescription("Open " + menus[position]);
		return v;
	}
}