package com.example.djprototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayFragment extends Fragment implements OnClickListener {
	// View
	ImageButton				mModeChange;

	TextView				mSound1;

	// Variables
	ArrayAdapter<String>	adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_play, container, false);

		createView(v);

		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.button_modeChange:
			break;
		case R.id.textView_sound1:
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			MainFragment fragment = new MainFragment();
			ft.replace(R.id.LinearLayout2, fragment);
			ft.addToBackStack(null);
			ft.commit();
			break;
		default:
			break;
		}
	}

	private void createView(View v) {
		mModeChange = (ImageButton) v.findViewById(R.id.button_modeChange);
		mSound1 = (TextView) v.findViewById(R.id.textView_sound1);
		mModeChange.setOnClickListener(this);
		mSound1.setOnClickListener(this);
	}
}
