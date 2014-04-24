package com.kiri.hackjak;

import java.util.ArrayList;
import java.util.List;

import com.kiri.hackjak.adapters.ResultAdapter;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RouteListFragment extends ListFragment {
	private ArrayAdapter<String> mAdapter;
	private ListView mListView;
	private List<String> mRouteList;
	
	public static List<String> mDummyList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_route_list, container, false);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mListView = getListView();
		mDummyList = new ArrayList<String>();
		
		// TTDO : dummy data
		for(int i = 0; i < 10; i++)
			mDummyList.add(Integer.toString(i) + "Turun di JALAN PAJAJARAN\nKemudian naik CIBOGO - ELANG");
		mDummyList.add("Turun di JALAN PAJAJARAN");
		
		mRouteList = mDummyList;
		mAdapter = new ResultAdapter(getActivity(), R.layout.cell_route_list, mRouteList);
		mListView.setAdapter(mAdapter);
	}
}
