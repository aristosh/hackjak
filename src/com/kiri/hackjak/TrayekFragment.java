package com.kiri.hackjak;

import java.util.ArrayList;
import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.kiri.hackjak.adapters.FormattedResult;
import com.kiri.hackjak.adapters.FormattedResultAdapter;
import com.kiri.hackjak.adapters.WaypointAdapter;
import com.kiri.hackjak.db.TrayekRouteDetail;
import com.kiri.hackjak.db.TrayekWaypoint;
import com.kiri.hackjak.db.TrayekWaypointDao;
import com.kiri.hackjak.utils.Routing;

import de.greenrobot.dao.query.QueryBuilder;

public class TrayekFragment extends ListFragment implements OnClickListener,
		OnScrollListener {
	private static final String ARG_SECTION_NUMBER = "section_number";

	AutoCompleteTextView acFrom;
	AutoCompleteTextView acTo;
	Button btnSearch;

	// sorry for doing this
	// it's okay
	public List<FormattedResult> mRouteList = new ArrayList<FormattedResult>();

	// FROM ROUTE LIST
	private enum QuickReturnState {
		HIDE, DISPLAY,
	}

	private enum ToucingState {
		IDLE, TOUCHING, SCROLLING_UP, SCROLLING_DOWN,
	}

	private final int ANIMATION_DURATION = 200;

	private QuickReturnState mQuickReturnState;
	private ToucingState mTouchingState;
	private int mLastFirstVisibleItem = 0;
	private int mTopOfLastFirstVisibleItem = 0;

	private ImageButton mButtonNavigate;
	private ArrayAdapter<FormattedResult> mAdapter;
	private ListView mListView;

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

		mButtonNavigate = (ImageButton) rootView
				.findViewById(R.id.buttonNavigate);
		acFrom = (AutoCompleteTextView) rootView.findViewById(R.id.acEditFrom);
		acTo = (AutoCompleteTextView) rootView.findViewById(R.id.acEditTo);
		btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

		WaypointAdapter adapter = new WaypointAdapter(getActivity(),
				android.R.layout.simple_dropdown_item_1line);
		acFrom.setAdapter(adapter);
		acTo.setAdapter(adapter);

		btnSearch.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mListView = getListView();

		// mRouteList = TrayekFragment.mRouteList;

		mButtonNavigate.setVisibility(View.GONE);
		// if (mRouteList.size() > 0) {
		mButtonNavigate.setOnClickListener(this);
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			mButtonNavigate.setVisibility(View.VISIBLE);
		}

		mAdapter = new FormattedResultAdapter(getActivity(),
				R.layout.cell_route_list, mRouteList);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(this);

		mQuickReturnState = QuickReturnState.DISPLAY;
		mTouchingState = ToucingState.IDLE;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSearch:
			if (validateSearchParameter(acFrom.getText().toString())
					&& validateSearchParameter(acTo.getText().toString())) {
				TrayekWaypoint waypointFrom = KiriApp
						.getTrayekWaypointDao()
						.queryBuilder()
						.where(TrayekWaypointDao.Properties.Point.eq(acFrom
								.getText().toString())).unique();
				TrayekWaypoint waypointTo = KiriApp
						.getTrayekWaypointDao()
						.queryBuilder()
						.where(TrayekWaypointDao.Properties.Point.eq(acTo
								.getText().toString())).unique();

				doSearch(waypointFrom, waypointTo);
			} else {
				Toast.makeText(getActivity(), "Invalid input parameter",
						Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

	private boolean validateSearchParameter(String param) {
		QueryBuilder<TrayekWaypoint> qb = KiriApp.getTrayekWaypointDao()
				.queryBuilder();
		qb.where(TrayekWaypointDao.Properties.Point.like(param));
		return qb.list().size() > 0;
	}

	public void doSearch(TrayekWaypoint from, TrayekWaypoint to) {
		List<TrayekRouteDetail> listRouteDetails = Routing.getRouteOne(
				from.getId(), to.getId());
		if (listRouteDetails.size() == 0) {
			// try route 2
			listRouteDetails = Routing
					.getRouteTwoIbun(from.getId(), to.getId());
		}
		mRouteList.clear();
		FormattedResult currentResult = null;
		for (TrayekRouteDetail routeDetail : listRouteDetails) {
			if (currentResult != null
					&& routeDetail.getIdRuteTrayek() == currentResult.idRuteTrayek) {
				currentResult.turun = routeDetail.getIdWaypoint();
			} else {
				currentResult = new FormattedResult();
				currentResult.idRuteTrayek = routeDetail.getIdRuteTrayek();
				currentResult.naik = routeDetail.getIdWaypoint();
				currentResult.turun = routeDetail.getIdWaypoint();
				mRouteList.add(currentResult);
			}
		}
		for (FormattedResult result: mRouteList) {
			result.updateStringValues();
		}
		mAdapter.notifyDataSetChanged();
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		View topVisibleView = view.getChildAt(0);
		int topOfFirstVisibleItem = topVisibleView == null ? 0 : topVisibleView
				.getTop();

		if ((mLastFirstVisibleItem < firstVisibleItem || (mLastFirstVisibleItem == firstVisibleItem && mTopOfLastFirstVisibleItem > topOfFirstVisibleItem))
				&& (mTouchingState == ToucingState.TOUCHING || mTouchingState == ToucingState.SCROLLING_UP)
				&& mQuickReturnState == QuickReturnState.DISPLAY) {

			// scroll down
			AnimatorSet mAnimationSet = new AnimatorSet();
			mTouchingState = ToucingState.SCROLLING_DOWN;

			ObjectAnimator mButtonNavigateAnimation = ObjectAnimator
					.ofFloat(
							mButtonNavigate,
							View.TRANSLATION_Y,
							((MarginLayoutParams) mButtonNavigate
									.getLayoutParams()).bottomMargin
									+ mButtonNavigate.getHeight());
			mButtonNavigateAnimation.setDuration(ANIMATION_DURATION);

			mAnimationSet.playTogether(mButtonNavigateAnimation);

			mAnimationSet.start();
			mQuickReturnState = QuickReturnState.HIDE;

		} else if ((mLastFirstVisibleItem > firstVisibleItem || (mLastFirstVisibleItem == firstVisibleItem && mTopOfLastFirstVisibleItem < topOfFirstVisibleItem))
				&& (mTouchingState == ToucingState.TOUCHING || mTouchingState == ToucingState.SCROLLING_DOWN)
				&& mQuickReturnState == QuickReturnState.HIDE) {

			// scroll up
			AnimatorSet mAnimationSet = new AnimatorSet();
			mTouchingState = ToucingState.SCROLLING_UP;

			ObjectAnimator mButtonNavigateAnimation = ObjectAnimator.ofFloat(
					mButtonNavigate, View.TRANSLATION_Y, 0);
			mButtonNavigateAnimation.setDuration(ANIMATION_DURATION);

			mAnimationSet.playTogether(mButtonNavigateAnimation);

			mAnimationSet.start();
			mQuickReturnState = QuickReturnState.DISPLAY;
		}

		mLastFirstVisibleItem = firstVisibleItem;
		mTopOfLastFirstVisibleItem = topOfFirstVisibleItem;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
			mTouchingState = ToucingState.TOUCHING;
	}
}
