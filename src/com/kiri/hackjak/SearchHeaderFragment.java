package com.kiri.hackjak;

import com.kiri.hackjak.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SearchHeaderFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_header, container, false);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Bundle args = getArguments();
		EditText editFrom = (EditText) getActivity().findViewById(R.id.editFrom);
		EditText editTo = (EditText) getActivity().findViewById(R.id.editTo);
		
		editFrom.setText(args.getString("FROM"));
		editTo.setText(args.getString("TO"));
	}
}
