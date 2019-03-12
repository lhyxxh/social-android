package com.eastcom.social.pos.activity;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.artifex.mupdf.MuPDFCore;
import com.artifex.mupdf.MuPDFPageAdapter;
import com.eastcom.social.pos.R;
import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.core.service.DocumentService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.message.SoFollowCommad;
import com.eastcom.social.pos.core.socket.message.confirm.ConfirmMssage;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.service.DataFactory;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.FileUtils;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.ToastUtil;

public class ReadPDFActivity extends BaseActivity {
	private int mListViewHeight;
	private TextView tv_name;
	private TextView tv_contents;
	private LinearLayout ll;
	private ListView mListView;
	private Button btn_commit;
	private MuPDFPageAdapter adapter;
	private MuPDFCore core = null;
	private ArrayList<String> mFileNmaes = new ArrayList<String>();
	private int fileIndex = 0;

	private GetDataTask getDataTask;
	private DocumentService documentService;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_pdf);
		Intent intent = this.getIntent();
		mFileNmaes = (ArrayList<String>) intent
				.getSerializableExtra("pathList");
		MyLog.i("ReadPDFActivity",
				"onCreate,mFileNmaes size = " + mFileNmaes.size());
		documentService = DocumentService.getInstance(this);
		initView();
		initData();
	}

	private void initData() {
		fileIndex = 0;
		tv_name.setText(mFileNmaes.get(0));
		if (mFileNmaes.size() > 1) {
			btn_commit.setText("下一页");
		} else {
			btn_commit.setText("已阅读");
		}
		showPdf();
	}

	private void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_contents = (TextView) findViewById(R.id.tv_contents);
		ll = (LinearLayout) findViewById(R.id.ll);
		mListView = (ListView) findViewById(R.id.listview);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		btn_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (fileIndex + 1 == mFileNmaes.size()) {
						MyLog.i("ReadPDFActivity", "fileIndex = "+fileIndex+",mFileNmaes = "+mFileNmaes.size());
						documentService.saveReadDocument(mFileNmaes);
						finish();
					} else {
						btn_commit.setVisibility(View.GONE);
						fileIndex++;
						tv_name.setText(mFileNmaes.get(fileIndex));
						if (fileIndex != mFileNmaes.size() - 1) {
							btn_commit.setText("下一页");
						} else {
							btn_commit.setText("确定");
						}
						showPdf();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
	}

	private void showPdf() {
		try {
			String path = mFileNmaes.get(fileIndex);
			File file = new File(Constance.PolicyFolderPath + path);
			if (file.exists()) {
				if (path.contains(".pdf")) {
					ll.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					initPdfListView(Constance.PolicyFolderPath + path);
				}else {
					ll.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
					String contents = FileUtils.readFileSdcardFile(Constance.PolicyFolderPath + path);
					tv_contents.setText(contents);
					btn_commit.setVisibility(View.VISIBLE);
				}
			} else {
				ToastUtil.show(ReadPDFActivity.this, "不存在此文件" + path, 3000);
				btn_commit.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initPdfListView(String path) {
		try {
			core = new MuPDFCore(this, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		adapter = new MuPDFPageAdapter(this, core);
		mListView.setAdapter(adapter);
		mListView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mListViewHeight = mListView.getHeight();
						mListView.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == totalItemCount - 1) {
					btn_commit.setVisibility(View.VISIBLE);
				} else if (firstVisibleItem == 0) {
					View firstVisibleItemView = mListView.getChildAt(0);
					if (firstVisibleItemView != null
							&& firstVisibleItemView.getTop() == 0) {
						Log.d("ListView", "<----滚动到顶部----->");
					}
				} else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
					View lastVisibleItemView = mListView.getChildAt(mListView
							.getChildCount() - 1);
					if (lastVisibleItemView != null
							&& lastVisibleItemView.getBottom() == mListViewHeight) {
						Log.d("ListView", "#####滚动到底部######");
						btn_commit.setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}

	public void startGetDataTask() {
		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute((Void) null);
		}
	}

	public class GetDataTask extends AsyncTask<Void, Void, Boolean> {
		private ArrayList<String> fileIds = new ArrayList<String>();

		@Override
		protected Boolean doInBackground(Void... params) {
			try {

				for (int i = 0; i < mFileNmaes.size(); i++) {
					String fileId = mFileNmaes.get(i).split("\\.")[0];
					fileIds.add(fileId);
				}
				JSONObject json = new JSONObject(
						DataFactory.postDocument_Readfile(fileIds));
				if ("true".equals(json.getString("success"))) {
					documentService.saveReadDocument(fileIds);
				} else {
					return false;
				}

			} catch (Exception e) {
				getDataTask = null;
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
			if (result) {
				try {
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				finish();
			}

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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyLog.i("ReadPDFActivity", "onDestroy");
	}

}
