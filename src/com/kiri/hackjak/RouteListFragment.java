package com.kiri.hackjak;

import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.kiri.hackjak.adapters.ResultAdapter;
import com.kiri.hackjak.model.SearchResult;

public class RouteListFragment extends ListFragment implements AbsListView.OnScrollListener, View.OnClickListener {	
	private enum QuickReturnState {
		HIDE,
		DISPLAY,
	}
	
	private enum ToucingState {
		IDLE,
		TOUCHING,
		SCROLLING_UP,
		SCROLLING_DOWN,
	}
	
	private final int ANIMATION_DURATION = 200;
	
	private QuickReturnState mQuickReturnState;
	private ToucingState mTouchingState;
	private int mLastFirstVisibleItem = 0;
	private int mTopOfLastFirstVisibleItem = 0;
	
	private ImageButton mButtonNavigate;
	private ArrayAdapter<SearchResult.Step> mAdapter;
	private ListView mListView;
	private List<SearchResult> mRouteList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_route_list, container, false);
		mButtonNavigate = (ImageButton)rootView.findViewById(R.id.buttonNavigate);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mListView = getListView();
		
		mRouteList = TrayekFragment.mRouteList;
		
		mButtonNavigate.setVisibility(View.GONE);
		if(mRouteList.size() > 0) {
			mButtonNavigate.setOnClickListener(this);
			if(android.os.Build.VERSION.SDK_INT >= 11) {
				mButtonNavigate.setVisibility(View.VISIBLE);
			}
			
			mAdapter = new ResultAdapter(getActivity(), R.layout.cell_route_list, mRouteList.get(0).steps);
			mListView.setAdapter(mAdapter);
			mListView.setOnScrollListener(this);
		} else {
			Toast.makeText(getActivity(), "No result", Toast.LENGTH_LONG).show();
		}
		
		
		
		
		mQuickReturnState = QuickReturnState.DISPLAY;
		mTouchingState = ToucingState.IDLE;
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		View topVisibleView = view.getChildAt(0);
		int topOfFirstVisibleItem = topVisibleView == null ? 0 : topVisibleView.getTop();
		
		
		if( (mLastFirstVisibleItem < firstVisibleItem ||
				(mLastFirstVisibleItem == firstVisibleItem && mTopOfLastFirstVisibleItem > topOfFirstVisibleItem)) &&
			(mTouchingState == ToucingState.TOUCHING || mTouchingState == ToucingState.SCROLLING_UP) && mQuickReturnState == QuickReturnState.DISPLAY) {
			
			// scroll down
			AnimatorSet mAnimationSet = new AnimatorSet();
			mTouchingState = ToucingState.SCROLLING_DOWN;
			
			ObjectAnimator mButtonNavigateAnimation = ObjectAnimator.ofFloat(mButtonNavigate, View.TRANSLATION_Y,
					((MarginLayoutParams) mButtonNavigate.getLayoutParams()).bottomMargin + mButtonNavigate.getHeight());
			mButtonNavigateAnimation.setDuration(ANIMATION_DURATION);
						
			mAnimationSet.playTogether(mButtonNavigateAnimation);

			mAnimationSet.start();
			mQuickReturnState = QuickReturnState.HIDE;
			
		} else if(
				(mLastFirstVisibleItem > firstVisibleItem ||
						(mLastFirstVisibleItem == firstVisibleItem && mTopOfLastFirstVisibleItem < topOfFirstVisibleItem)) &&
				(mTouchingState == ToucingState.TOUCHING || mTouchingState == ToucingState.SCROLLING_DOWN) && mQuickReturnState == QuickReturnState.HIDE) {
			
			// scroll up
			AnimatorSet mAnimationSet = new AnimatorSet();
			mTouchingState = ToucingState.SCROLLING_UP;
			
			ObjectAnimator mButtonNavigateAnimation = ObjectAnimator.ofFloat(mButtonNavigate, View.TRANSLATION_Y, 0);
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
		if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) mTouchingState = ToucingState.TOUCHING;
	}
	
	@Override
	public void onClick(View v) {
		Toast.makeText(getActivity(), "Navigation Mode", Toast.LENGTH_SHORT).show();
		
		Navigation navigation = Navigation.getInstance();
		navigation.initiateRoutes(getActivity(), mRouteList.get(0).steps);
		navigation.redraw();
		
		getActivity().moveTaskToBack(true);	
	}
}
