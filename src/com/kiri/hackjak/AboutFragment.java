package com.kiri.hackjak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";
	private static final String ARG_FILENAME = "filename";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static AboutFragment newInstance(int sectionNumber, String fileName) {
		AboutFragment fragment = new AboutFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		args.putString(ARG_FILENAME, fileName);
		fragment.setArguments(args);
		return fragment;
	}

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_webview, container,
				false);
		WebView webView = (WebView) rootView.findViewById(R.id.aboutWebView);
		try {
			String fileName = getArguments().getString(ARG_FILENAME);
			InputStream is = getActivity().getAssets().open(
					fileName + "_en.html");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			webView.loadData(sb.toString(), "text/html", "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((KiriActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}