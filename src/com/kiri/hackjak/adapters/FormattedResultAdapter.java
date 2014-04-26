package com.kiri.hackjak.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiri.hackjak.R;
import com.kiri.hackjak.RouteMapActivity;

public class FormattedResultAdapter extends ArrayAdapter<FormattedResult> {
	private List<FormattedResult> mList;
	private Context mContext;
	private int mResource;
	private LayoutInflater mInflater;

	public FormattedResultAdapter(Context context, int resource,
			List<FormattedResult> list) {
		super(context, resource, list);
		this.mContext = context;
		this.mResource = resource;
		this.mList = list;
		this.mInflater = ((Activity) mContext).getLayoutInflater();
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			this.mInflater = ((Activity) mContext).getLayoutInflater();
			convertView = mInflater.inflate(mResource, null);
		}

		FormattedResult step = mList.get(position);

		TextView textNoTrayek = (TextView) convertView
				.findViewById(R.id.textNoTrayek);
		TextView textResult = (TextView) convertView
				.findViewById(R.id.textStep);
		ImageView imageMap = (ImageView) convertView
				.findViewById(R.id.imageMap);

		textNoTrayek.setText(step.getTitle() );
		textResult.setText(step.getDetail());

		final Intent intent = new Intent(mContext, RouteMapActivity.class);
		intent.putExtra(RouteMapActivity.ARG_JENIS_ANGKUTAN, step.getJenisAngkutanString());
		intent.putExtra(RouteMapActivity.ARG_JENIS_TRAYEK, step.getJenisTrayekString());
		intent.putExtra(RouteMapActivity.ARG_NO_TRAYEK, step.getNoTrayekString());
		if (RouteMapActivity.checkAvailability(this.getContext(), intent)) {
			imageMap.setVisibility(View.VISIBLE);
			// Note: the imageMap is to small to click -_- any alternative?
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mContext.startActivity(intent);
				}
			});
		} else {
			imageMap.setVisibility(View.GONE);
		}

		return convertView;
	}

}
