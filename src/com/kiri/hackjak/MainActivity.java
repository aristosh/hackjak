package com.kiri.hackjak;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kiri.hackjak.model.SearchResult;
import com.kiri.hackjak.sqlite.Trayek;

import de.greenrobot.dao.query.QueryBuilder;

public class MainActivity extends ActionBarActivity {
	// sorry for doing this
	public static List<SearchResult> mRouteList;
	
	private FragmentManager mFragmentManager;
	private SearchFragment mSearchFragment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mFragmentManager = getSupportFragmentManager();
		mSearchFragment = new SearchFragment();
		if (savedInstanceState == null) {
			mFragmentManager.beginTransaction().add(R.id.header, mSearchFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void displayRouteList() {
		RouteListFragment fragmentContainer = new RouteListFragment();
		mFragmentManager.beginTransaction()
			.replace(R.id.container, fragmentContainer)
			.commit();		
	}
}
