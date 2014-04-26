package com.kiri.hackjak;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.kiri.hackjak.adapters.WaypointAdapter;
import com.kiri.hackjak.apis.ApiGrabberHelper;
import com.kiri.hackjak.db.TrayekWaypoint;
import com.kiri.hackjak.db.TrayekWaypointDao;
import com.kiri.hackjak.model.RoutingEngine;
import com.kiri.hackjak.model.WaypointModel;

import de.greenrobot.dao.query.QueryBuilder;

public class SearchFragment extends Fragment implements OnClickListener {
	AutoCompleteTextView acFrom;
	AutoCompleteTextView acTo;
	Button btnSearch;

	ApiGrabberHelper hackJakApiHelper;
	RoutingEngine router;

	TrayekFragment mTrayekFragment;

	public void setParentFragment(TrayekFragment fragment) {
		mTrayekFragment = fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);

		acFrom = (AutoCompleteTextView) rootView.findViewById(R.id.acEditFrom);
		acTo = (AutoCompleteTextView) rootView.findViewById(R.id.acEditTo);
		btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

		// get data from API
		hackJakApiHelper = new ApiGrabberHelper(getActivity());
		hackJakApiHelper.grabDataFromApi();

		// TODO lambat di bagian ini , mohon dituning. Mungkin object bisa
		// dipindahkan ke KiriActivity
		// get data either from CSV or SQLite
		// RoutingEngineFactory ref = new RoutingEngineFactory(getActivity());
		// router = ref.createRoutingEngine();

		WaypointAdapter adapter = new WaypointAdapter(getActivity(),
				android.R.layout.simple_dropdown_item_1line);
		acFrom.setAdapter(adapter);
		acTo.setAdapter(adapter);

		btnSearch.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {

		if (validateSearchParameter(acFrom.getText().toString())
				&& validateSearchParameter(acTo.getText().toString())) {
			WaypointModel from = router.getWaypointFromString(acFrom.getText()
					.toString());
			WaypointModel to = router.getWaypointFromString(acTo.getText()
					.toString());

			TrayekFragment.mRouteList = router.findRoute(from, to);

			mTrayekFragment.displayRouteList();
		} else {
			Toast.makeText(getActivity(), "Invalid input parameter. Select from the list",
					Toast.LENGTH_LONG).show();
		}
	}

	private boolean validateSearchParameter(String param) {
		QueryBuilder<TrayekWaypoint> qb = KiriApp.getTrayekWaypointDao()
				.queryBuilder();
		qb.where(TrayekWaypointDao.Properties.Point.like(param));
		return qb.list().size() > 0;
	}

}
