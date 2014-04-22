package com.hackjak.hackjak;

import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultAdapter extends ArrayAdapter<String> {
	private List<String> mList;
	private Context mContext;
	private int mResource;
	private LayoutInflater mInflater;
	
	public ResultAdapter(Context context, int resource, List<String> list) {
		super(context, resource, list);
		this.mContext = context;
		this.mResource = resource; 
		this.mList = list;		
		this.mInflater = ((Activity)mContext).getLayoutInflater();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mInflater.inflate(mResource, null);			
		}

		// TODO : fill data
		//String route = mList.get(position);
		//TextView textResult = (TextView) convertView.findViewById(R.id.textView1);
		//textResult.setText(route);
		
		return convertView;
	}

}
