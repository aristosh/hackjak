package com.kiri.hackjak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ListResultActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_result);

		if (savedInstanceState == null) {
			Intent intent = getIntent();
			Bundle args = new Bundle();
			args.putString("FROM", intent.getStringExtra("FROM"));
			args.putString("TO", intent.getStringExtra("TO"));
			
			SearchHeaderFragment searchHeaderFragment = new SearchHeaderFragment();
			searchHeaderFragment.setArguments(args);
			
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft	.add(R.id.search_header, searchHeaderFragment)
				.add(R.id.container, new RouteListFragment())
				.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_result, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClickNavigate(View v) {
		Toast.makeText(this, "Mode Navigasi", Toast.LENGTH_SHORT).show();
		
		Navigation navigation = Navigation.getInstance();
		navigation.initiateRoutes(this, RouteListFragment.mDummyList);
		navigation.redraw();
		
		moveTaskToBack(true);
	}
	
	public void onClickButtonFrom(View v) {
		Toast.makeText(this, "onClickButtonFrom", Toast.LENGTH_SHORT).show();
		// TODO
		// create navigation in notification
	}
	
	public void onClickButtonTo(View v) {
		Toast.makeText(this, "onClickButtonTo", Toast.LENGTH_SHORT).show();
		// TODO
		// create navigation in notification
	}
}
