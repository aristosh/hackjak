package com.kiri.hackjak;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.kiri.hackjak.adapters.WaypointAdapter;
import com.kiri.hackjak.apis.ApiGrabberHelper;

public class SearchFragment extends Fragment {
//	private static final String[] TEXT_AUTOCOMPLETE = new String[] { "Belgium",
//			"France", "Italy", "Germany", "Spain" };

	AutoCompleteTextView acFrom;
	AutoCompleteTextView acTo;

	ApiGrabberHelper hackJakApiHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);

		acFrom = (AutoCompleteTextView) rootView.findViewById(R.id.acEditFrom);
		acTo = (AutoCompleteTextView) rootView.findViewById(R.id.acEditTo);

		hackJakApiHelper = new ApiGrabberHelper(getActivity());
		hackJakApiHelper.grabDataFromApi();

		WaypointAdapter adapter = new WaypointAdapter(getActivity(),
				android.R.layout.simple_dropdown_item_1line,
				KiriApp.getTrayekWaypointDao());
		acFrom.setAdapter(adapter);
		acTo.setAdapter(adapter);

		return rootView;
	}

}
