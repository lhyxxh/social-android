package com.eastcom.social.pos.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.fragment.AppManagerFragment;
import com.eastcom.social.pos.fragment.AppStoreFragment;
import com.eastcom.social.pos.fragment.MyAppFragment;

public class AppStoreActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "MainActivity";
	// private FragmentManager fragment_manager;
	// private FragmentTransaction transaction;
	private MyAppFragment my_app_fragment;
	private AppStoreFragment app_store_fragment;
	private AppManagerFragment app_manager_fragment;
	private RelativeLayout title_my_app, title_app_store, title_app_manager,
			title_back;
	private LinearLayout rootview;// 设置点击编辑框之外的地方，就隐藏软键盘

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_store);

		title_my_app = (RelativeLayout) this.findViewById(R.id.title_my_app);
		title_app_store = (RelativeLayout) this
				.findViewById(R.id.title_app_store);
		title_app_manager = (RelativeLayout) this
				.findViewById(R.id.title_app_manager);
		title_back = (RelativeLayout) this.findViewById(R.id.title_back);
		rootview = (LinearLayout) this.findViewById(R.id.rootview);
		title_my_app.setOnClickListener(this);
		title_app_store.setOnClickListener(this);
		title_app_manager.setOnClickListener(this);
		title_back.setOnClickListener(this);
		rootview.setOnClickListener(this);

		// 初始化fragment界面 一个transaction只能Commit一次
		FragmentManager fragment_manager = getFragmentManager();
		FragmentTransaction transaction = fragment_manager.beginTransaction();
		my_app_fragment = new MyAppFragment();
		transaction.replace(R.id.fragment_main, my_app_fragment);
		transaction.addToBackStack(null);
		transaction.commit();

	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.title_my_app:
				FragmentManager fragment_manager1 = getFragmentManager();
				FragmentTransaction transaction1 = fragment_manager1
						.beginTransaction();
				my_app_fragment = new MyAppFragment();
				transaction1.replace(R.id.fragment_main, my_app_fragment);
				// transaction1.addToBackStack(null);
				transaction1.commit();
				title_my_app.setBackgroundColor(getResources().getColor(
						R.color.title_dark_blue));
				title_app_store.setBackgroundColor(getResources().getColor(
						R.color.title_white_blue));
				title_app_manager.setBackgroundColor(getResources().getColor(
						R.color.title_white_blue));
				break;
			case R.id.title_app_store:
				FragmentManager fragment_manager2 = getFragmentManager();
				FragmentTransaction transaction2 = fragment_manager2
						.beginTransaction();
				app_store_fragment = new AppStoreFragment();
				transaction2.replace(R.id.fragment_main, app_store_fragment);
				// transaction2.addToBackStack(null);
				transaction2.commit();
				title_my_app.setBackgroundColor(getResources().getColor(
						R.color.title_white_blue));
				title_app_store.setBackgroundColor(getResources().getColor(
						R.color.title_dark_blue));
				title_app_manager.setBackgroundColor(getResources().getColor(
						R.color.title_white_blue));
				break;
			case R.id.title_app_manager:
				FragmentManager fragment_manager3 = getFragmentManager();
				FragmentTransaction transaction3 = fragment_manager3
						.beginTransaction();
				app_manager_fragment = new AppManagerFragment();
				transaction3.replace(R.id.fragment_main, app_manager_fragment);
				// transaction3.addToBackStack(null);
				transaction3.commit();
				title_my_app.setBackgroundColor(getResources().getColor(
						R.color.title_white_blue));
				title_app_store.setBackgroundColor(getResources().getColor(
						R.color.title_white_blue));
				title_app_manager.setBackgroundColor(getResources().getColor(
						R.color.title_dark_blue));
				break;
			case R.id.title_back:
				finish();
				break;
			case R.id.rootview:
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "--错误->>>" + e.toString());
		}
	}

	@Override
	public void Nodata(boolean nodata) {

	}

	@Override
	public void setTime(String time) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return false;
	}
}
