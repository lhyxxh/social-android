package com.eastcom.social.pos.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.adapter.SettingListAdapter;
import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.listener.MyBank;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.mispos.NewPos;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.AppUtils;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.TimeUtil;
import com.eastcom.social.pos.util.ToastUtil;
import com.eastcom.social.pos.util.dialog.SettingDialog;
import com.example.usbcciddrv.jniUsbCcid;
import com.landicorp.android.mispos.MisPosClient;
import com.lidroid.xutils.util.LogUtils;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private Spinner spinner1;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private Button button7;
	private Button button8;
	private Button button9;
	private Button button10;
	private Button button11;
	private Button button12;
	private Button button13;
	private Button button_check_heartbeat;
	private TextView tv_check_heartbeat;
	private Button bt_so_reset;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;
	private TextView textView5;
	private TextView textView6;
	private TextView textView7;

	private EditText et_cmd;
	private Button bt_exec_cmd;
	private Button bt_exec_cmd_test;

	private Button btn_check_increment;
	private Button btn_update_increment;

	private ListView listView;
	private TradeService tradeService;
	private SoClient client;
	private String host;
	private int post;

	private SettingListAdapter mAdapter;
	private ArrayList<Trade> mData = new ArrayList<Trade>();
	private MisPosClient bank = null;
	private LocalDataFactory localDataFactory;

	private AlertDialog.Builder builder;

	public static final String BACKUP = "SocialBackup";
	private int operateType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		tradeService = TradeService.getInstance(SettingActivity.this);
		localDataFactory = LocalDataFactory.newInstance(SettingActivity.this);
		client = MySoClient.newInstance().getClient();
		initView();
		builder = new AlertDialog.Builder(SettingActivity.this,
				R.style.AlertDialogCustom);

	}

	private void initView() {
		bank = MyBank.newInstance().getBank();
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		textView5 = (TextView) findViewById(R.id.textView5);
		textView6 = (TextView) findViewById(R.id.textView6);
		textView7 = (TextView) findViewById(R.id.textView7);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		button5 = (Button) findViewById(R.id.button5);
		button6 = (Button) findViewById(R.id.button6);
		button7 = (Button) findViewById(R.id.button7);
		button8 = (Button) findViewById(R.id.button8);
		button9 = (Button) findViewById(R.id.button9);
		button10 = (Button) findViewById(R.id.button10);
		button11 = (Button) findViewById(R.id.button11);
		button12 = (Button) findViewById(R.id.button12);
		button13 = (Button) findViewById(R.id.button13);
		bt_so_reset = (Button) findViewById(R.id.bt_so_reset);
		button_check_heartbeat = (Button) findViewById(R.id.button_check_heartbeat);
		btn_check_increment = (Button) findViewById(R.id.btn_check_increment);
		btn_update_increment = (Button) findViewById(R.id.btn_update_increment);
		tv_check_heartbeat = (TextView) findViewById(R.id.tv_check_heartbeat);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		button7.setOnClickListener(this);
		button8.setOnClickListener(this);
		button9.setOnClickListener(this);
		button10.setOnClickListener(this);
		button11.setOnClickListener(this);
		button12.setOnClickListener(this);
		button13.setOnClickListener(this);
		bt_so_reset.setOnClickListener(this);
		btn_check_increment.setOnClickListener(this);
		btn_update_increment.setOnClickListener(this);
		button_check_heartbeat.setOnClickListener(this);
		SoClient soClient = MySoClient.newInstance().getClient();
		textView4.setText("tcp地址:" + soClient.getConfig().getHost() + ":"
				+ soClient.getConfig().getPort());
		textView5.setText("app更新地址:" + Constance.API_HOST);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		final String[] mItems1 = getResources().getStringArray(R.array.host);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems1);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定 Adapter到控件
		spinner1.setAdapter(adapter1);

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String url = mItems1[pos];
				String postString = url.split("\\:")[1].split("\\(")[0];
				host = url.split("\\:")[0];
				post = Integer.valueOf(postString);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		et_cmd = (EditText) findViewById(R.id.et_cmd);
		bt_exec_cmd = (Button) findViewById(R.id.bt_exec_cmd);
		bt_exec_cmd_test = (Button) findViewById(R.id.bt_exec_cmd_test);
		bt_exec_cmd.setOnClickListener(this);
		bt_exec_cmd_test.setOnClickListener(this);

	}

	@Override
	public void Nodata(boolean nodata) {
		if (nodata) {
			textView2.setText("设备未绑定");
		} else {
			textView2.setText("设备已绑定");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			localDataFactory.putString(LocalDataFactory.HOST, host);
			localDataFactory.putInt(LocalDataFactory.POST, Integer.valueOf(post));
			String eid = localDataFactory.getString(LocalDataFactory.EID, "");
			MySoClient.newInstance().setClient(host, post, eid);
			textView4.setText(host + ":" + post);
			break;
		case R.id.button2:
			mData.clear();
			ArrayList<Trade> temp1 = (ArrayList<Trade>) tradeService
					.queryByIsUpload();
			for (int i = 0; i < temp1.size(); i++) {
				mData.add(temp1.get(i));
			}
			textView1.setText("未上传交易数量为：" + mData.size());
			showList(mData);
			break;
		case R.id.button3:
			if (client.isAvtive()) {
				textView3.setText("tcp通道已开启");
			} else {
				textView3.setText("tcp通道未开启");
			}
			break;
		case R.id.button4:
			mData.clear();
			Date today = TimeUtil.getDate(TimeUtil.getDateStringZeroTime(
					new Date(), 0));
			ArrayList<Trade> temp2 = (ArrayList<Trade>) tradeService
					.queryByStartTime(today);
			for (int i = 0; i < temp2.size(); i++) {
				mData.add(temp2.get(i));
			}
			textView1.setText("今天交易数量为：" + mData.size());
			showList(mData);
			break;
		case R.id.button5:
			mData.clear();
			ArrayList<Trade> temp3 = (ArrayList<Trade>) tradeService
					.loadAllTrade();
			for (int i = 0; i < temp3.size(); i++) {
				mData.add(temp3.get(i));
			}
			textView1.setText("所有交易数量为：" + mData.size());
			showList(mData);
			break;
		case R.id.button6:
			jniUsbCcid.GetRfsamCardNo();
			jniUsbCcid.GetPsamCardNo();
			if (jniUsbCcid.GetAuthorization() == 1) {
				String insideMsg = bank.GetOutsideInfo();
				if (insideMsg==null || insideMsg.equals(""))
					Toast.makeText(SettingActivity.this, "内部认证信息为空", Toast.LENGTH_SHORT).show();
				else {
					String insideAuth = NewPos.outsideAuth(insideMsg);
					String cardInfoString = bank.ReadCardInfo(insideAuth);
					textView6.setText("读卡信息:" + cardInfoString);
				}
			} else {
				ToastUtil.show(SettingActivity.this, "授权失败，请先授权", 5000);
			}

			break;
		case R.id.button7:
			clearEid();
			break;
		case R.id.button8:
			showLogoutDialog();
			break;
		case R.id.button9:
			finish();
			break;
		case R.id.button10:
			textView7.setText("稍等。。。");
			dataBackup();
			break;
		case R.id.button11:
			textView7.setText("稍等。。。");
			// Intent intent = new Intent();
			// intent.setClass(SettingActivity.this, FSExplorerActivity.class);
			// startActivityForResult(intent, 1000);
			dataRecover("sdcard/SocialBackup/social_pos");
			break;
		case R.id.button12:
			Intent intent2 = new Intent();
			intent2.setClass(SettingActivity.this, ConfigActivity.class);
			startActivity(intent2);
			break;
		case R.id.button13:
			Intent intent3 = new Intent();
			intent3.setClass(SettingActivity.this, TestActivity.class);
			startActivity(intent3);
			break;
		case R.id.button_check_heartbeat:
			boolean checkHeartbeat = localDataFactory.getBoolean(LocalDataFactory.CHECK_HEARTBEAT, false);
			localDataFactory.putBoolean(LocalDataFactory.CHECK_HEARTBEAT,!checkHeartbeat);
			if (!localDataFactory.getBoolean(LocalDataFactory.CHECK_HEARTBEAT, false)) {
				tv_check_heartbeat.setText("启用闲时维持心跳");
			} else {
				tv_check_heartbeat.setText("启用闲时取消心跳");
			}
			break;
		case R.id.bt_exec_cmd:
			String cmd = et_cmd.getText().toString();
			AppUtils.execCmd(cmd);
			break;
		case R.id.bt_exec_cmd_test:
			try {
				String systemSupplierSoName = AppUtils.getSystemSupplierSoName();
				ToastUtil.show(SettingActivity.this, systemSupplierSoName, 5000);
				AppUtils.soCopyLib(SettingActivity.this, systemSupplierSoName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		case R.id.bt_so_reset:
			localDataFactory.putBoolean(LocalDataFactory.SOCOPYLIB,false);
			break;

		case R.id.btn_check_increment:
			MyApplicationLike.getContext().stopService(MyApplicationLike.updateIncrementService);
			MyApplicationLike.getContext().startService(MyApplicationLike.updateIncrementService);
			break;

		case R.id.btn_update_increment:
			String versionName = null;
			try {
//				versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//				final int versionMain = Integer.valueOf(versionName
//						.split("\\.")[0]);
//				final int versionSub = Integer
//						.valueOf(versionName.split("\\.")[1]);
				int versionMain = localDataFactory.getInt(LocalDataFactory.INCREMENT_MAIN_UPDATE,0);
				int versionSub = localDataFactory.getInt(LocalDataFactory.INCREMENT_SUB_UPDATE, 0);
				String realFilePath = Constance.ApkIncrementFile
						+ versionMain + "_" + versionSub
						+ ".apk";
				TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), realFilePath);
			}catch (Exception e){
				LogUtils.e("Tinker更新失败" + e.toString());
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 清除eid
	 */
	private void clearEid() {

		LocalDataFactory localDataFactory = LocalDataFactory.newInstance(this);
		localDataFactory.putString(LocalDataFactory.WHOLE_EID, "");
	}

	private void showList(ArrayList<Trade> list) {
		try {

			if (mAdapter == null) {
				listView = (ListView) findViewById(R.id.listView1);
				mAdapter = new SettingListAdapter(this, list);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

					}

				});
			} else {
				mAdapter.notifyDataSetChanged();
			}

		} catch (Exception e) {
		}
	}

	/**
	 * 退出程序输入框
	 */
	private void showLogoutDialog() {

		final SettingDialog.Builder builder = new SettingDialog.Builder(this);
		builder.setTitle("请输入管理密码");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (!"888888".equals(builder.GetSum())) {
					AlertDialog("密码错误");
					dialog.dismiss();
				} else {
					dialog.cancel();
					Intent intent = new Intent();
					setResult(1000, intent);
					finish();
				}

			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();

	}

	private void AlertDialog(String title) {
		builder.setTitle("温馨提示");
		builder.setMessage(title);
		builder.setPositiveButton("确定", null);
		builder.create().show();
	}

	// 数据恢复
	private void dataRecover(String path) {
		operateType = 1;
		new BackupTask().execute(path);
	}

	// 数据备份
	private void dataBackup() {
		File file = dbOk(Dao.DB_NAME);
		if (file != null) {
			operateType = 0;
			new BackupTask().execute("");
		} else {
			textView7.setText("没有对应的数据库");
		}

	}

	/**
	 * 数据库文件是否存在，并可以使用
	 * 
	 * @return
	 */
	private File dbOk(String dbName) {
		String sp = File.separator;
		String absPath = Environment.getDataDirectory().getAbsolutePath();
		String pakName = getPackageName();
		String dbPath = absPath + sp + "data" + sp + pakName + sp + "databases"
				+ sp + dbName;
		File file = new File(dbPath);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	@SuppressWarnings("resource")
	private void fileCopy(File dbFile, File backup) throws IOException {
		FileChannel inChannel = new FileInputStream(dbFile).getChannel();
		FileChannel outChannel = new FileOutputStream(backup).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		try {
			if (resultCode == 1001) {
				String path = intent.getStringExtra("path");
				String text = "Path:" + path;
				textView7.setText(text);
				dataRecover(path);
			}
		} catch (Exception e) {
			MyLog.e("Setting", "backup error");
		}

	}

	public class BackupTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// 获得正在使用的数据库路径，我的是 sdcard 目录下的 /dlion/db_dlion.db
			// 默认路径是 /data/data/(包名)/databases/*.db
			File dbFile = dbOk(Dao.DB_NAME);
			if (operateType == 0) {
				File exportDir = new File(
						Environment.getExternalStorageDirectory(), BACKUP);
				if (!exportDir.exists()) {
					exportDir.mkdirs();
				}
				File backup = new File(exportDir, dbFile.getName());
				try {
					backup.createNewFile();
					fileCopy(dbFile, backup);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

			} else if (operateType == 1) {
				String path = params[0];
				File backup = new File(path);
				try {
					fileCopy(backup, dbFile);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				textView7.setText("操作成功");
			} else {
				textView7.setText("操作失败");
			}
		}
	}

	@Override
	public void setTime(String time) {

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return false;
	}

}
