package com.kiri.hackjak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kiri.hackjak.R;

public class RouteMapActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
		if (id == R.id.action_settings) {
			return true;
		}
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

			try {
				// TODO retrieve from intent
				InputStream is = getActivity().getAssets().open(
						"kopaja_p19.json");
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		private SupportMapFragment mMapFragment;
		private GoogleMap mMap;
	}

}
