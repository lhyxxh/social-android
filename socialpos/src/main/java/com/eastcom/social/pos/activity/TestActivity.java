package com.eastcom.social.pos.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.orm.entity.TradeFile;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.core.service.TradeFileService;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.service.DataFactory;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.service.UploadTradeFileService;
import com.eastcom.social.pos.util.AppUtils;
import com.eastcom.social.pos.util.MD5;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.TimeUtil;
import com.eastcom.social.pos.util.ToastUtil;
import com.example.usbcciddrv.jniUsbCcid;

public class TestActivity extends Activity {
	private EditText editText1;
	private EditText editText2;
	private EditText editText3;
	private EditText editText4;
	private EditText editText5;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private GetDataTask getDataTask;

	private TradeService tradeService;
	private SoClient client;
	private MyBroadcastReceiver mBroadcastReceiver;
	private MySoClient mySoClient;

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		initView();
		initData();
		mBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("POLICY");
		registerReceiver(mBroadcastReceiver, intentFilter);
		mySoClient = MySoClient.newInstance();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (client != null && client.isAvtive()) {
			MyLog.i("TestActivity", "client stop");
			client.stop();
			client = null;
		}
		boolean timeServiceRunning = AppUtils.isServiceRunning(
				MyApplicationLike.getContext(),
				"com.eastcom.social.pos.service.UploadTradeFileService");
		if (!timeServiceRunning) {
			stopService(intent);
		}
	}

	private void ConnectTcp(final String eid, final String ip, final String port) {
		try {
			final LocalDataFactory localDataFactory = LocalDataFactory
					.newInstance(this);
			localDataFactory.putString(LocalDataFactory.EID, eid);
			localDataFactory.putString(LocalDataFactory.HOST, ip);
			localDataFactory.putInt(LocalDataFactory.POST,
					Integer.valueOf(port));
			new Thread() {
				public void run() {
					if (client == null) {
						client = mySoClient.getClient();
						mySoClient.run();
					} else {
						int p = Integer.valueOf(port);
						MySoClient.newInstance().setClient(ip, p, eid);
					}

				}

			}.start();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("POLICY")) {
				String stringExtra = intent.getStringExtra("flag");
				if (stringExtra.equals("1")) {
					String fileName = intent.getStringExtra("fileName");
					textView1.setText("下载完成，文件名为" + fileName);

				} else {
					textView1.setText("下载失败");
				}

			}

		}

	}

	private void initView() {
		editText1 = (EditText) findViewById(R.id.editText1);
		editText2 = (EditText) findViewById(R.id.editText2);
		editText3 = (EditText) findViewById(R.id.editText3);
		editText4 = (EditText) findViewById(R.id.editText4);
		editText5 = (EditText) findViewById(R.id.editText5);
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String getPsamCardNo = jniUsbCcid.GetPsamCardNo();
				textView1.setText(getPsamCardNo);
			}
		});
		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startGetDataTask();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String appid = editText1.getText().toString();
				intent = new Intent(TestActivity.this,
						UploadTradeFileService.class);
				intent.putExtra("appid", appid.equals("") ? "16091013" : appid);
				boolean timeServiceRunning = AppUtils.isServiceRunning(
						MyApplicationLike.getContext(),
						"com.eastcom.social.pos.service.UploadTradeFileService");
				if (timeServiceRunning) {
					stopService(intent);
				}
				startService(intent);
			}
		});
		button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 1; i < 36; i++) {
					String fk_trade_id = UUID.randomUUID().toString();
					Trade trade = new Trade();
					trade.setAmount(1);
					trade.setBank_card_no("06191910292819");
					trade.setEncrypt("");
					trade.setId(fk_trade_id);
					trade.setId_card_no("12121212121");
					trade.setIndividual_pay(1);
					trade.setIs_revoke(0);
					trade.setPre_trade_no(1);
					trade.setPsam_no("37161616161000" + i);
					trade.setRef_no("123123123000" + i);
					trade.setRfsam_no("55667788");
					trade.setSend_count(0);
					trade.setSign_board_no("16091013");
					trade.setSocial_card_no("M12345678");
					trade.setSs_pay(1);
					trade.setTerminal_code("22223333");
					trade.setTrace("00000" + i);
					trade.setTrade_money(100);
					trade.setTrade_no(i);
					trade.setIs_upload(0);
					trade.setTrade_state(70);
					trade.setTrade_time(TimeUtil.getDate("2017-11-08"));
					trade.setTrade_type(14);
					TradeService.getInstance(TestActivity.this)
							.saveTrade(trade);

					TradeDetail tradeDetail = new TradeDetail();
					tradeDetail.setActual_price(100);
					tradeDetail.setAmount(1);
					tradeDetail.setBar_code("657578585000" + i);
					tradeDetail.setDetail_trade(i);
					tradeDetail.setFk_trade_id(fk_trade_id);
					tradeDetail.setId(UUID.randomUUID().toString());
					tradeDetail.setIs_upload(1);
					tradeDetail.setProduct_name("keyle");
					tradeDetail.setSocial_category(1);
					tradeDetail.setSuper_vision_code("");
					tradeDetail.setTrade_type(14);

					TradeDetailService.getInstance(TestActivity.this)
							.saveTradeDetail(tradeDetail);

				}
				// for (int i = 1; i < 10; i++) {
				// String fk_trade_id = UUID.randomUUID().toString();
				// Trade trade = new Trade();
				// trade.setAmount(1);
				// trade.setBank_card_no("06191910292819");
				// trade.setEncrypt("");
				// trade.setId(fk_trade_id);
				// trade.setId_card_no("12121212121");
				// trade.setIndividual_pay(1);
				// trade.setIs_revoke(0);
				// trade.setPre_trade_no(1);
				// trade.setPsam_no("37161616161000" + i);
				// trade.setRef_no("123123123000" + i);
				// trade.setRfsam_no("55667788");
				// trade.setSend_count(0);
				// trade.setSign_board_no("16091013");
				// trade.setSocial_card_no("M12345678");
				// trade.setSs_pay(1);
				// trade.setTerminal_code("22223333");
				// trade.setTrace("00000" + i);
				// trade.setTrade_money(100);
				// trade.setTrade_no(i);
				// trade.setIs_upload(0);
				// trade.setTrade_state(70);
				// trade.setTrade_time(TimeUtil.getDate("2017-11-07"));
				// trade.setTrade_type(14);
				// TradeService.getInstance(TestActivity.this)
				// .saveTrade(trade);
				//
				// TradeDetail tradeDetail = new TradeDetail();
				// tradeDetail.setActual_price(100);
				// tradeDetail.setAmount(1);
				// tradeDetail.setBar_code("657578585000" + i);
				// tradeDetail.setDetail_trade(i);
				// tradeDetail.setFk_trade_id(fk_trade_id);
				// tradeDetail.setId(UUID.randomUUID().toString());
				// tradeDetail.setIs_upload(1);
				// tradeDetail.setProduct_name("keyle");
				// tradeDetail.setSocial_category(1);
				// tradeDetail.setSuper_vision_code("");
				// tradeDetail.setTrade_type(14);
				//
				// TradeDetailService.getInstance(TestActivity.this)
				// .saveTradeDetail(tradeDetail);
				//
				// }
			}
		});
		button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<Trade> list = TradeService.getInstance(TestActivity.this)
						.loadAllTrade();
				List<TradeFile> list2 = TradeFileService.getInstance(
						TestActivity.this).loadAllTradeFile();
				ToastUtil.show(TestActivity.this, "size:" + list.size() + "-"
						+ list2.size(), 5000);
			}
		});
		button6 = (Button) findViewById(R.id.button6);
		button6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);

		textView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		textView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<Trade> loadAllTrade = tradeService.loadAllTrade();
				Intent intent = new Intent(TestActivity.this,
						ChartActivity.class);
				startActivity(intent);
			}
		});
		textView3 = (TextView) findViewById(R.id.tv_1);
		textView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	private void initData() {
		tradeService = TradeService.getInstance(this);
	}

	public void startGetDataTask() {
		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute((Void) null);
		}
	}

	private int type = 3;

	public class GetDataTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				String desFilePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/Monitor/test.jpg";
				File desFile = new File(desFilePath);
				if (!desFile.exists()) {
					return false;
				}

				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				String appid = "16091013";
				String secret = "pdsm12K0prj7SpoRfGy32DswwX098aXa";
				String timestamp = format.format(new Date());
				String sign = appid + secret + timestamp;
				String jsonString = DataFactory.uploadMonitorPic(desFile,
						appid, timestamp, MD5.getMD5Code(sign).toUpperCase(),
						TimeUtil.getWholeDateString(new Date()));
				JSONObject jsonObject = new JSONObject(jsonString);
				String code = jsonObject.getString("code");
				Log.i("----", code);
				// String desFilePath =
				// Environment.getExternalStorageDirectory()
				// .getAbsolutePath() + "/social-pos/20170101_01.zip";
				// File desFile = new File(desFilePath);
				// if (!desFile.exists()) {
				// return false;
				// }
				//
				// SimpleDateFormat format = new
				// SimpleDateFormat("yyyyMMddHHmmss");
				// String appid = "16091013";
				// String secret = "pdsm12K0prj7SpoRfGy32DswwX098aXa";
				// String timestamp = format.format(new Date());
				// String sign = appid + secret + timestamp;
				// int tradeType = 1;
				// String jsonString = DataFactory.uploadTradeFile(desFile,
				// appid,
				// timestamp, MD5.getMD5Code(sign).toUpperCase(),
				// TimeUtil.getDateString(new Date()), tradeType);
				// JSONObject jsonObject = new JSONObject(jsonString);
				// String code = jsonObject.getString("code");
				// Log.i("----", code);
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
			try {
				if (result) {

				} else {
					ToastUtil.show(TestActivity.this, "not ok", 3000);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

	}

}
