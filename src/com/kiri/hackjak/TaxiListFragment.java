package com.kiri.hackjak;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kiri.hackjak.db.Taxi;
import com.kiri.hackjak.db.TaxiDao;
import com.kiri.hackjak.db.TaxiPhone;

/**
 * A placeholder fragment containing a simple view.
 */
public class TaxiListFragment extends ListFragment {
	private static final String ARG_SECTION_NUMBER = "section_number";
	List<Taxi> listTaxi;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static TaxiListFragment newInstance(int sectionNumber) {
		TaxiListFragment fragment = new TaxiListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public TaxiListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_taxi, container,
				false);

		List<Taxi> listData = KiriApp.getTaxiDao().queryBuilder()
				.orderAsc(TaxiDao.Properties.Name).list();
		listTaxi = listData;

		MyAdapter adapter = new MyAdapter();
		setListAdapter(adapter);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((KiriActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}

	private class MyAdapter extends BaseAdapter {
		LayoutInflater inflater;

		public MyAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			return listTaxi.size();
		}

		@Override
		public Taxi getItem(int arg0) {
			return listTaxi.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return listTaxi.get(arg0).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			View v = inflater.inflate(R.layout.row_taxi, null);
			TextView tv1 = (TextView) v.findViewById(R.id.text1);
			TextView tv2 = (TextView) v.findViewById(R.id.text2);

			Taxi item = getItem(position);
			tv1.setText(item.getName());
			List<String> arrPhone = new ArrayList<String>();
			for (TaxiPhone phone : item.getPhones()) {
				arrPhone.add(phone.getPhone());
			}
			tv2.setText(KiriApp.implodeArray(arrPhone, ", "));
			return v;
		}

	}
}