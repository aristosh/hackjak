package com.kiri.hackjak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteMapActivity extends ActionBarActivity {

	public static final String ARG_JENIS_TRAYEK = "jenisTrayek";
	public static final String ARG_JENIS_ANGKUTAN = "jenisAngkutan";
	public static final String ARG_NO_TRAYEK = "noAngkutan";
	
	
	public static boolean checkAvailability(Context context, Intent intent) {
		String jenisAngkutan = intent.getStringExtra(ARG_JENIS_ANGKUTAN);
		String jenisTrayek = intent.getStringExtra(ARG_JENIS_TRAYEK);
		String noTrayek = intent.getStringExtra(ARG_NO_TRAYEK);
		String fileName = (jenisTrayek.equals("-") ? normalize(jenisAngkutan) : normalize(jenisTrayek)) + "_" + normalize(noTrayek) + ".json";
		try {
			InputStream stream = context.getAssets().open(fileName);
			stream.close();
		} catch (IOException ioe) {
			return false;
		}
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		setTitle(getIntent().getStringExtra(ARG_NO_TRAYEK));
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_route_map,
					container, false);
			return rootView;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			mMapFragment = (SupportMapFragment) getFragmentManager()
					.findFragmentById(R.id.google_map);
			mMap = mMapFragment.getMap();
			mMap.setMyLocationEnabled(true);
			
			Intent intent = getActivity().getIntent();
			String jenisAngkutan = intent.getStringExtra(ARG_JENIS_ANGKUTAN);
			String jenisTrayek = intent.getStringExtra(ARG_JENIS_TRAYEK);
			String noTrayek = intent.getStringExtra(ARG_NO_TRAYEK);
			String fileName = (jenisTrayek.equals("-") ? normalize(jenisAngkutan) : normalize(jenisTrayek)) + "_" + normalize(noTrayek) + ".json";
			
			
			try {
				InputStream is = getActivity().getAssets().open(fileName);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				reader.close();
				JSONArray jsonArray = new JSONArray(sb.toString());
				PolylineOptions options = new PolylineOptions();
				LatLngBounds bounds = null;
				for (int i = 0, size = jsonArray.length(); i < size; i++) {
					JSONArray latLngJson = jsonArray.getJSONArray(i);
					LatLng latLng = new LatLng(latLngJson.getDouble(1), latLngJson.getDouble(0));
					options.add(latLng);
					bounds = bounds == null ? new LatLngBounds(latLng, latLng) : bounds.including(latLng);
				}
				options.color(0xaa00ff00);
				mMap.addPolyline(options);
				final LatLngBounds finalizedBounds = bounds;
				mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
					@Override
					public void onCameraChange(CameraPosition arg0) {
						mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(finalizedBounds, 10));
						mMap.setOnCameraChangeListener(null);
					}
				});
			} catch (Exception e) {
				Toast toast = new Toast(getActivity());
				toast.setText(R.string.path_not_available);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show();
			}

		}

		private SupportMapFragment mMapFragment;
		private GoogleMap mMap;
	}

	private static String normalize(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			if ((input.charAt(i) >= 'A' && input.charAt(i) <= 'Z') || (input.charAt(i) >= 'a' && input.charAt(i) <= 'z') || (input.charAt(i) >= '0' && input.charAt(i) <= '9')){
				sb.append(Character.toLowerCase(input.charAt(i)));
			}
		}
		return sb.toString();
	}
	
}
