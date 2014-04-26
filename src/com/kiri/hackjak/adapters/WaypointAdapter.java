package com.kiri.hackjak.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.R;
import com.kiri.hackjak.db.TrayekRouteDetail;
import com.kiri.hackjak.db.TrayekWaypoint;
import com.kiri.hackjak.db.TrayekWaypointDao;

import de.greenrobot.dao.query.QueryBuilder;

public class WaypointAdapter extends BaseAdapter implements Filterable {

	private List<TrayekWaypoint> fullList;
	private List<TrayekWaypoint> mOriginalValues;

	LayoutInflater inflater;
	private ArrayFilter mFilter;
	private Context mContext;

	public WaypointAdapter(Context context) {
		mContext = context;
		mOriginalValues = new ArrayList<TrayekWaypoint>();
		fullList = mOriginalValues;
		// this.fullList = listTrayekWaypoint;
		// mOriginalValues = new ArrayList<TrayekWaypoint>(fullList);
		inflater = LayoutInflater.from(context);
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
		View v = inflater.inflate(R.layout.row_waypoint_dropdown, null);
		TextView tv1 = (TextView) v.findViewById(R.id.text1);
		TextView tv2 = (TextView) v.findViewById(R.id.text2);
		TrayekWaypoint waypoint = fullList.get(position);
		List<TrayekRouteDetail> listRouteDetail = waypoint.getRuteVia();
		List<String> trayekLewat = new ArrayList<String>();
		for (int i = 0; i < listRouteDetail.size(); i++) {
			TrayekRouteDetail detail = listRouteDetail.get(i);
			trayekLewat.add(detail.getTrayekRoute().getTrayek().getNoTrayek());
		}
		tv1.setText(waypoint.getPoint());
		tv2.setText(KiriApp.implodeArray(trayekLewat, ", "));
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