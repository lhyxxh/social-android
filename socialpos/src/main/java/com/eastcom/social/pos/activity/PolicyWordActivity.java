package com.eastcom.social.pos.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.adapter.PolicyListAdapter;
import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.service.LocalDataFactory;

/**
 * 政策文件界面
 * 
 * @author Ljj 上午10:07:41
 */
public class PolicyWordActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl4;
	private RelativeLayout rl3;
	private RelativeLayout rl2;
	private RelativeLayout rl1;
	private ListView listView;
	private PolicyListAdapter mAdapter;
	private ArrayList<String> mData = new ArrayList<String>();
	private String eid = "";
	private TextView tv_eid;
	private LocalDataFactory localDataFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ativity_policy_word);
		localDataFactory = LocalDataFactory.newInstance(this);
		eid = localDataFactory.getString(LocalDataFactory.WHOLE_EID, "");
		initView();
		initData();
	}

	private void initData() {
		String fileAbsolutePath = Constance.PolicyFolderPath;
		String type1 = ".pdf";
		String type2 = ".txt";
		File file = new File(fileAbsolutePath);
		if (file.exists()) {
			GetFileNameBytype(mData,fileAbsolutePath, type1,type2);
			showList(mData);
		}
	}

	/**
	 * 遍历指定路径文件夹下的文件
	 * 
	 * @param fileAbsolutePath
	 * @param type
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static ArrayList<String> GetFileNameBytype(ArrayList<String> vecFile,String fileAbsolutePath,
			String type1,String type2) {
		File file = new File(fileAbsolutePath);
		File[] subFile = file.listFiles();

		for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
			if (!subFile[iFileLength].isDirectory()) {
				String filename = subFile[iFileLength].getName();
				if (filename.trim().toLowerCase().endsWith(type1)||filename.trim().toLowerCase().endsWith(type2)) {
					vecFile.add(filename);
				}
			}
		}
		return vecFile;
	}

	private void initView() {
		tv_eid = (TextView) findViewById(R.id.tv_eid);
		tv_eid.setText(getResources().getString(R.string.tv_eid) + eid);
		rl4 = (RelativeLayout) findViewById(R.id.rl4);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);

		rl4.setOnClickListener(this);
		rl3.setOnClickListener(this);
		rl2.setOnClickListener(this);
		rl1.setOnClickListener(this);
	}

	private void showList(ArrayList<String> list) {
		try {
			if (mAdapter == null) {
				listView = (ListView) findViewById(R.id.listView);
				mAdapter = new PolicyListAdapter(this, list);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String fileName = (String) arg0.getItemAtPosition(arg2);
						Intent intent = new Intent();
						intent.setClass(PolicyWordActivity.this,
								ReadPDFActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Bundle bundle = new Bundle();
						ArrayList<String> mFileNmaes = new ArrayList<String>();
						mFileNmaes.add(fileName);
						bundle.putSerializable("pathList", mFileNmaes);
						intent.putExtras(bundle);
						startActivity(intent);
					}

				});
			} else {
				mAdapter.notifyDataSetChanged();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl4:
			intent.setClass(PolicyWordActivity.this, PolicyWordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl3:
			intent.setClass(PolicyWordActivity.this, ChartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl2:
			intent.setClass(PolicyWordActivity.this, NewConsumeActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl1:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}

		return false;
	}

	@Override
	public void Nodata(boolean nodata) {

	}

	@Override
	public void setTime(String time) {
		
	}

}
