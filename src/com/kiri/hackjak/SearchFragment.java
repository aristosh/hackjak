package com.kiri.hackjak;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.kiri.hackjak.adapters.WaypointAdapter;
import com.kiri.hackjak.apis.ApiGrabberHelper;
import com.kiri.hackjak.model.RoutingEngine;
import com.kiri.hackjak.model.RoutingEngineFactory;
import com.kiri.hackjak.model.SQLiteRouteWorldFactory;
import com.kiri.hackjak.model.SearchResult;
import com.kiri.hackjak.model.WaypointModel;

public class SearchFragment extends Fragment implements OnClickListener {
//	private static final String[] TEXT_AUTOCOMPLETE = new String[] { "Belgium",
//			"France", "Italy", "Germany", "Spain" };

	AutoCompleteTextView acFrom;
	AutoCompleteTextView acTo;
	Button btnSearch;

	ApiGrabberHelper hackJakApiHelper;
	RoutingEngine router;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);

		acFrom = (AutoCompleteTextView) rootView.findViewById(R.id.acEditFrom);
		acTo = (AutoCompleteTextView) rootView.findViewById(R.id.acEditTo);
		btnSearch = (Button)rootView.findViewById(R.id.btnSearch);

		// get data from API
		//hackJakApiHelper = new ApiGrabberHelper(getActivity());
		//hackJakApiHelper.grabDataFromApi();
		
		// get data either from CSV or SQLite
		RoutingEngineFactory ref = new RoutingEngineFactory(getActivity());
		router = ref.createRoutingEngine(hackJakApiHelper);

		WaypointAdapter adapter = new WaypointAdapter(getActivity(),
				android.R.layout.simple_dropdown_item_1line);
		acFrom.setAdapter(adapter);
		acTo.setAdapter(adapter);

		btnSearch.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		WaypointModel from = new WaypointModel(acFrom.getText().toString());
		WaypointModel to = new WaypointModel(acTo.getText().toString());
		
		MainActivity.mRouteList = router.findRoute(from, to);
		
		((MainActivity)getActivity()).displayRouteList();
	}

}
