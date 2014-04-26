package com.kiri.hackjak;

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

import com.kiri.hackjak.adapters.WaypointAdapter;
import com.kiri.hackjak.apis.ApiGrabberHelper;
import com.kiri.hackjak.db.TrayekRouteDetail;
import com.kiri.hackjak.db.TrayekWaypoint;
import com.kiri.hackjak.db.TrayekWaypointDao;
import com.kiri.hackjak.model.RoutingEngine;

import de.greenrobot.dao.query.QueryBuilder;

public class SearchFragment extends ListFragment implements OnClickListener,
		OnScrollListener {
	// private static final String[] TEXT_AUTOCOMPLETE = new String[] {
	// "Belgium",
	// "France", "Italy", "Germany", "Spain" };

	AutoCompleteTextView acFrom;
	AutoCompleteTextView acTo;
	Button btnSearch;

	ApiGrabberHelper hackJakApiHelper;
	RoutingEngine router;

	TrayekFragment mTrayekFragment;

	// ROUTELIST
	// data for adapter
	// private List<TrayekRouteDetail> mListTrayekRouteDetail = new
	// ArrayList<TrayekRouteDetail>();

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
	private ArrayAdapter<TrayekRouteDetail> mAdapter;
	private ListView mListView;

	public void setParentFragment(TrayekFragment fragment) {
		mTrayekFragment = fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);

		mButtonNavigate = (ImageButton) rootView
				.findViewById(R.id.buttonNavigate);
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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mListView = getListView();

		// mRouteList = TrayekFragment.mRouteList;

		mButtonNavigate.setVisibility(View.GONE);
		// if (mRouteList.size() > 0) {
		mButtonNavigate.setOnClickListener(this);
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			mButtonNavigate.setVisibility(View.VISIBLE);
		}

		// mAdapter = new ResultAdapter(getActivity(), R.layout.cell_route_list,
		// mRouteList);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(this);
		// } else {
		// Toast.makeText(getActivity(), "No result", Toast.LENGTH_LONG)
		// .show();
		// }

		mQuickReturnState = QuickReturnState.DISPLAY;
		mTouchingState = ToucingState.IDLE;
	}

	@Override
	public void onClick(View v) {

		if (validateSearchParameter(acFrom.getText().toString())
				&& validateSearchParameter(acTo.getText().toString())) {
			// WaypointModel from =
			// router.getWaypointFromString(acFrom.getText()
			// .toString());
			// WaypointModel to = router.getWaypointFromString(acTo.getText()
			// .toString());
			TrayekWaypoint waypointFrom = KiriApp
					.getTrayekWaypointDao()
					.queryBuilder()
					.where(TrayekWaypointDao.Properties.Point.eq(acFrom
							.getText().toString())).unique();
			TrayekWaypoint waypointTo = KiriApp
					.getTrayekWaypointDao()
					.queryBuilder()
					.where(TrayekWaypointDao.Properties.Point.eq(acTo.getText()
							.toString())).unique();

			// TrayekFragment.mRouteList = router.findRoute(from, to);
			mTrayekFragment.doSearch(waypointFrom, waypointTo);
			// mTrayekFragment.displayRouteList();
		} else {
			Toast.makeText(getActivity(), "Invalid input parameter",
					Toast.LENGTH_LONG).show();
		}
	}

	private boolean validateSearchParameter(String param) {
		QueryBuilder<TrayekWaypoint> qb = KiriApp.getTrayekWaypointDao()
				.queryBuilder();
		qb.where(TrayekWaypointDao.Properties.Point.like(param));
		return qb.list().size() > 0;
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
