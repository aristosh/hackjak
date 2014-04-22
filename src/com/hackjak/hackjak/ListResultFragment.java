package com.hackjak.hackjak;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListResultFragment extends ListFragment {
	private ArrayAdapter<String> mAdapter;
	private ListView mListView;
	private List<String> mList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list_result, container, false);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mListView = getListView();
		mList = new ArrayList<String>();
		
		// TTDO : dummy data
		for(int i = 0; i < 10; i++)
			mList.add("");
		
		mAdapter = new ResultAdapter(getActivity(), R.layout.cell_list_result, mList);
		mListView.setAdapter(mAdapter);
	}
}
