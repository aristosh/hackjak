package com.kiri.hackjak;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiri.hackjak.model.SearchResult;

public class TrayekFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";

	// sorry for doing this
	public static List<SearchResult> mRouteList;

	private FragmentManager mFragmentManager;
	private SearchFragment mSearchFragment;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static TrayekFragment newInstance(int sectionNumber) {
		TrayekFragment fragment = new TrayekFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public TrayekFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_trayek, container,
				false);

		mFragmentManager = getFragmentManager();
		mSearchFragment = new SearchFragment();
		if (savedInstanceState == null) {
			mFragmentManager.beginTransaction()
					.add(R.id.header, mSearchFragment).commit();
			mSearchFragment.setParentFragment(this);
		}

		return rootView;
	}

	public void displayRouteList() {
		RouteListFragment fragmentContainer = new RouteListFragment();
		mFragmentManager.beginTransaction()
				.replace(R.id.container, fragmentContainer).commit();
	}
}
