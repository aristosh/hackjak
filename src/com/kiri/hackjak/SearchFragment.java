package com.kiri.hackjak;

import com.kiri.hackjak.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class SearchFragment extends Fragment {
    private static final String[] TEXT_AUTOCOMPLETE = new String[] {
        "Belgium", "France", "Italy", "Germany", "Spain"
    };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, TEXT_AUTOCOMPLETE);
        AutoCompleteTextView editFrom = (AutoCompleteTextView)
                getActivity().findViewById(R.id.editFrom);
        AutoCompleteTextView editTo = (AutoCompleteTextView)
                getActivity().findViewById(R.id.editTo);
        editFrom.setAdapter(adapter);
        editTo.setAdapter(adapter);
	}

}
