package com.kiri.hackjak.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.sqlite.TrayekWaypoint;
import com.kiri.hackjak.sqlite.TrayekWaypointDao;

import de.greenrobot.dao.query.QueryBuilder;

public class WaypointAdapter extends ArrayAdapter<TrayekWaypoint> implements
		Filterable {

	private List<TrayekWaypoint> fullList;
	private List<TrayekWaypoint> mOriginalValues;

	private ArrayFilter mFilter;

	public WaypointAdapter(Context context, int resource) {
		super(context, resource);
		mOriginalValues = new ArrayList<TrayekWaypoint>();
		fullList = mOriginalValues;
		// this.fullList = listTrayekWaypoint;
		// mOriginalValues = new ArrayList<TrayekWaypoint>(fullList);
	}

	@Override
	public int getCount() {
		return fullList.size();
	}

	@Override
	public TrayekWaypoint getItem(int arg0) {
		return fullList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		tv.setText(fullList.get(position).getPoint());
		return v;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {
		private Object lock;

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (lock) {
					mOriginalValues = new ArrayList<TrayekWaypoint>();
				}
			}

			if (prefix == null || prefix.length() == 0) {
				synchronized (lock) {
					ArrayList<TrayekWaypoint> list = new ArrayList<TrayekWaypoint>(
							mOriginalValues);
					results.values = list;
					results.count = list.size();
				}
			} else {
				final String prefixString = prefix.toString().toLowerCase();

				QueryBuilder<TrayekWaypoint> qb = KiriApp
						.getTrayekWaypointDao().queryBuilder();
				qb.where(TrayekWaypointDao.Properties.Point.like("%" + prefix
						+ "%"));
				mOriginalValues = qb.list();

				List<TrayekWaypoint> values = mOriginalValues;

				results.values = values;
				results.count = values.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			if (results.values != null) {
				fullList = (ArrayList<TrayekWaypoint>) results.values;
			} else {
				fullList = new ArrayList<TrayekWaypoint>();
			}
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}

}