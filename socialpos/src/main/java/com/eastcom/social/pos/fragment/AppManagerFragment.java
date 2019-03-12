package com.eastcom.social.pos.fragment;

import android.app.Fragment;//要求  level 11
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.activity.AppStoreActivity;




 public class AppManagerFragment extends Fragment {
	 
	 public AppStoreActivity mActivity;
	 
	 @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		 
		 @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.fragment_app_manager,container, false);
			return view;
		}
		 
		 @Override
		public void onPause() {
			super.onPause();
		}


}
