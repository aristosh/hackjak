package com.kiri.hackjak;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kiri.hackjak.apis.ApiGrabberHelper;

public class KiriActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	public static final int SECTION_TRAYEK = 0;
	public static final int SECTION_CONTRIBUTE = 1;
	public static final int SECTION_ABOUT = 2;

	ApiGrabberHelper hackJakApiHelper;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kiri);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		switch (position) {
		case SECTION_TRAYEK:
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							TrayekFragment.newInstance(SECTION_TRAYEK))
					.commit();
			break;
		case SECTION_ABOUT:
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							AboutFragment.newInstance(SECTION_ABOUT, "about"))
					.commit();
			break;
		case SECTION_CONTRIBUTE:
			// TODO contribute fragment
			fragmentManager
					.beginTransaction()
					.replace(
							R.id.container,
							AboutFragment.newInstance(SECTION_CONTRIBUTE,
									"contribute")).commit();
			break;

		default:
			break;
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case SECTION_TRAYEK:
			mTitle = getString(R.string.title_section_trayek);
			break;
		case SECTION_CONTRIBUTE:
			mTitle = getString(R.string.title_section_contribute);
			break;
		case SECTION_ABOUT:
			mTitle = getString(R.string.title_section_about);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.kiri, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_update) {
			// get data from API
			hackJakApiHelper = new ApiGrabberHelper(this);
			hackJakApiHelper.grabDataFromApi();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
