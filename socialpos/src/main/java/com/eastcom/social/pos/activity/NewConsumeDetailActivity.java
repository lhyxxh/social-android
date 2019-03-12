package com.eastcom.social.pos.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.eastcom.social.pos.adapter.NewConsumeDetailListAdapter;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.service.LocalDataFactory;

/**
 * 收费流水界面
 * 
 * @author Ljj 上午10:05:49
 */
public class NewConsumeDetailActivity extends BaseActivity implements OnClickListener {
	private TradeDetailService tradeDetailService;

	private ListView lv_consume;
	private NewConsumeDetailListAdapter mConsumeListAdapter;
	private ArrayList<TradeDetail> mTradeDetailList = new ArrayList<TradeDetail>();
	private String fk_trade_id;

	private GetDataTask getDataTask;
	private int operateType = 0;

	private RelativeLayout rl4;
	private RelativeLayout rl3;
	private RelativeLayout rl2;
	private RelativeLayout rl1;
	
	private String eid = "";
	private TextView tv_eid;
	private LocalDataFactory localDataFactory ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_consume_detail);
		tradeDetailService = TradeDetailService.getInstance(this);
		fk_trade_id = getIntent().getStringExtra("fk_trade_id");
		localDataFactory = LocalDataFactory.newInstance(this);
		eid = localDataFactory.getString(LocalDataFactory.WHOLE_EID, "");
		initView();
		initData();
		startGetDataTask();

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl4:
			intent.setClass(NewConsumeDetailActivity.this, PolicyWordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl3:
			intent.setClass(NewConsumeDetailActivity.this, ChartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl2:
			intent.setClass(NewConsumeDetailActivity.this, NewConsumeActivity.class);
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

	private void initData() {
	}

	private void initView() {
		rl4 = (RelativeLayout) findViewById(R.id.rl4);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		
		rl4.setOnClickListener(this);
		rl3.setOnClickListener(this);
		rl2.setOnClickListener(this);
		rl1.setOnClickListener(this);
		
		tv_eid = (TextView) findViewById(R.id.tv_eid);
		tv_eid.setText(getResources().getString(R.string.tv_eid)+eid);
	}

	public void startGetDataTask() {
		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute((Void) null);
		}
	}

	public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				if (operateType == 0) {
					mTradeDetailList = (ArrayList<TradeDetail>) tradeDetailService.queryByFkTradeId(fk_trade_id);
//					mTradeDetailList =  (ArrayList<TradeDetail>) tradeDetailService.loadAllTradeDetail();
				} else if (operateType == 1) {// 删除
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
					if (operateType == 0) {
						showConsumeDetailList(mTradeDetailList);

					} else if (operateType == 1) {
					} else if (operateType == 2) {
					}

				} catch (Exception e) {
				}
			}

		}
	}

	private void showConsumeDetailList(List<TradeDetail> list) {
		try {

			if (mConsumeListAdapter == null) {
				lv_consume = (ListView) findViewById(R.id.lv_consume);
				mConsumeListAdapter = new NewConsumeDetailListAdapter(this, list);
				lv_consume.setAdapter(mConsumeListAdapter);
				lv_consume.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
					}

				});
			} else {
				mConsumeListAdapter.notifyDataSetChanged();
			}

		} catch (Exception e) {
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
  
        }  
          
        return false;  
	}

	@Override
	public void Nodata(boolean nodata) {
		
	}

	@Override
	public void setTime(String time) {
		// TODO Auto-generated method stub
		
	}

}
