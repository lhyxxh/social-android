package com.eastcom.social.pos.activity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.adapter.NewConsumeListAdapter;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.FileUtils;
import com.eastcom.social.pos.util.PrintUtils;
import com.eastcom.social.pos.util.TimeUtil;
import com.eastcom.social.pos.util.ToastUtil;
import com.eastcom.social.pos.util.dialog.DateTimePickDialogUtil;
import com.eastcom.social.pos.util.dialog.LoadingDialog;
import com.eastcom.social.pos.util.dialog.MyDialog;
import com.eastcom.social.pos.util.dialog.NumDialog;
import com.eastcompeace.printer.api.Printer;
import com.printer.sdk.api.PrinterType;

/**
 * 收费流水界面
 * 
 * @author Ljj 上午10:05:49
 */
@SuppressLint({ "SdCardPath", "SimpleDateFormat", "ClickableViewAccessibility" })
public class NewConsumeActivity extends BaseActivity implements OnClickListener {
	private TradeService tradeService;

	private ListView lv_consume;
	private NewConsumeListAdapter mConsumeListAdapter;
	private ArrayList<Trade> mTradeList = new ArrayList<Trade>();

	private GetDataTask getDataTask;
	private int operateType = 0;
	private String card_no;
	private String trade_no;

	private EditText et_trade_no;
	private EditText et_card_no;
	private EditText et_trade_start;
	private EditText et_trade_end;
	private Button btn_search;

	private RelativeLayout rl4;
	private RelativeLayout rl3;
	private RelativeLayout rl2;
	private RelativeLayout rl1;

	private String eid = "";
	private TextView tv_eid;

	private LoadingDialog dialog;
	private Builder builder;

	private boolean isConnected = false;// 是否连接到打印机
	public static Printer mPrinter;
	private UsbDevice mUsbDevice;
	private boolean is_58mm = true;
	private boolean is_thermal = true;
	private PendingIntent pendingIntent;
	private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
	private LocalDataFactory localDataFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_consume);
		tradeService = TradeService.getInstance(this);
		localDataFactory = LocalDataFactory.newInstance(this);
		eid = localDataFactory.getString(LocalDataFactory.WHOLE_EID, "");
		dialog = new LoadingDialog(this, "请稍等");
		dialog.setCanceledOnTouchOutside(false);
		builder = new AlertDialog.Builder(NewConsumeActivity.this,
				R.style.AlertDialogCustom);
		initView();
		initData();
		initReciever();
	}

	private void initReciever() {
		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);

		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

		registerReceiver(mUsbReceiver, filter);

		Printer printer = new Printer(getApplicationContext());
		printer.getDeviceList(mHandler);
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Printer.Handler_Get_Device_List_Found:
				mUsbDevice = (UsbDevice) msg.obj;
				break;
			case Printer.Handler_Get_Device_List_No_Device:
				ToastUtil.show(NewConsumeActivity.this, "没有正在连接的打印机", 5000);
				break;
			case Printer.Handler_Get_Device_List_Completed:
				if (mUsbDevice != null) {
					initPrinter(mUsbDevice);
				}
				break;
			case Printer.Handler_Get_Device_List_Error:
				ToastUtil.show(NewConsumeActivity.this, "打印机连接错误", 5000);
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 通知链接打印机
	 */
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice mUsbDevice = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openConnection(mUsbDevice);
					} else {
					}
				}
			} else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				UsbDevice device = (UsbDevice) intent
						.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				if (device != null && isConnected
						&& device.equals(mPrinter.getCurrentDevice())) {
					closeConnection();
				}
			}
		}
	};

	/**
	 * 初始化打印机
	 * 
	 * @param device
	 */
	private void initPrinter(UsbDevice device) {
		mUsbDevice = device;
		mPrinter = new Printer(getApplicationContext());
		if (is_thermal) {
			mPrinter.setCurrentPrintType(is_58mm ? PrinterType.TIII
					: PrinterType.T9);
		} else {
			mPrinter.setCurrentPrintType(PrinterType.T5);
		}
		mPrinter.setEncoding(Printer.Encoding_GBK);
		UsbManager mUsbManager = (UsbManager) getSystemService(USB_SERVICE);
		if (mUsbManager.hasPermission(mUsbDevice)) {
			openConnection(mUsbDevice);
		} else {
			mUsbManager.requestPermission(mUsbDevice, pendingIntent);
		}
	}

	/**
	 * 链接打印机
	 * 
	 * @param device
	 */
	public void openConnection(UsbDevice device) {
		if (mPrinter != null) {
			isConnected = mPrinter.openConnection(device);
		}

	}

	/**
	 * 关闭链接打印机
	 */
	public void closeConnection() {
		mPrinter.closeConnection();
		isConnected = false;
	}

	@Override
	protected void onStart() {
		super.onStart();
		initReciever();
		Log.e("------------", "onstart");
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl4:
			intent.setClass(NewConsumeActivity.this, PolicyWordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl3:
			intent.setClass(NewConsumeActivity.this, ChartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl2:
			intent.setClass(NewConsumeActivity.this, NewConsumeActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl1:
			finish();
			break;
		case R.id.btn_search:
			card_no = et_card_no.getText().toString();
			trade_no = et_trade_no.getText().toString();
			String end = et_trade_end.getText().toString();
			String start = et_trade_start.getText().toString();
			if ("".equals(card_no) && "".equals(trade_no) && "".equals(end)
					&& "".equals(start)) {
				operateType = 0;
				startGetDataTask();
			} else {
				if (!"".equals(card_no)) {
					card_no = "%" + card_no + "%";
				} else {
					card_no = "%%";
				}
				if (!"".equals(trade_no)) {
					trade_no = "%" + trade_no + "%";
				} else {
					trade_no = "%%";
				}
				operateType = 1;
				startGetDataTask();
			}

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

		et_trade_no = (EditText) findViewById(R.id.et_trade_no);
		et_card_no = (EditText) findViewById(R.id.et_card_no);
		et_trade_start = (EditText) findViewById(R.id.et_trade_start);
		et_trade_end = (EditText) findViewById(R.id.et_trade_end);
		et_card_no.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_trade_no.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_trade_no.setInputType(InputType.TYPE_NULL);
		et_trade_end.setInputType(InputType.TYPE_NULL);
		et_trade_start.setInputType(InputType.TYPE_NULL);
		et_trade_end.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Date date_end = null;
				if ("".equals(et_trade_end.getText().toString())) {
					date_end = new Date();
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						date_end = sdf.parse(et_trade_end.getText().toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				DateTimePickDialogUtil dateTimePicKDialog_start = new DateTimePickDialogUtil(
						NewConsumeActivity.this, TimeUtil
								.getHourStringZeroTime(date_end, 0), 0);
				dateTimePicKDialog_start.dateTimePicKDialog(et_trade_end);
			}
		});
		et_trade_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Date date_start = null;
				if ("".equals(et_trade_start.getText().toString())) {
					date_start = new Date();
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						date_start = sdf.parse(et_trade_start.getText()
								.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				DateTimePickDialogUtil dateTimePicKDialog_start = new DateTimePickDialogUtil(
						NewConsumeActivity.this, TimeUtil
								.getHourStringZeroTime(date_start, 0), 0);
				dateTimePicKDialog_start.dateTimePicKDialog(et_trade_start);
			}
		});
		et_trade_no.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showNumDialog();
				}
				return false;
			}
		});
		et_card_no.setInputType(InputType.TYPE_NULL);
		et_card_no.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showNum2Dialog();
				}
				return false;
			}
		});
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);

		tv_eid = (TextView) findViewById(R.id.tv_eid);
		tv_eid.setText(getResources().getString(R.string.tv_eid) + eid);
	}

	/**
	 * 输入框
	 */
	private void showNumDialog() {

		final NumDialog.Builder builder = new NumDialog.Builder(this);
		builder.setTitle("请输入凭证号");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String sum = builder.GetSum();
				et_trade_no.setText(sum);
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

	/**
	 * 输入框
	 */
	private void showNum2Dialog() {

		final NumDialog.Builder builder = new NumDialog.Builder(this);
		builder.setTitle("请输入社保卡号");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String sum = builder.GetSum();
				et_card_no.setText(sum);
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

	public void startGetDataTask() {
		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute((Void) null);
		}
	}

	public class GetDataTask extends AsyncTask<Void, Void, Boolean> {
		private ArrayList<Trade> tempTradeList = new ArrayList<Trade>();

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				if (operateType == 0) {
					tempTradeList = (ArrayList<Trade>) tradeService
							.loadAllTradeOrderByTradeTime();
				} else if (operateType == 1) {// 查询交易详情
					String timeStart = et_trade_start.getText().toString();
					String timeEnd = et_trade_end.getText().toString();
					Date endDate;
					Date startDate;
					if ("".equals(timeEnd)) {
						endDate = new Date();
					} else {
						endDate = TimeUtil.getDate(timeEnd);
					}
					if ("".equals(timeStart)) {
						timeStart = "1970-01-01";
					}
					startDate = TimeUtil.getDate(timeStart);
					// tempTradeList = (ArrayList<Trade>) tradeService
					// .queryByTradeNoOrCardNoLike(trade_no, card_no);
					tempTradeList = (ArrayList<Trade>) tradeService
							.queryConsume(trade_no, card_no, startDate,
									endDate, 1);
				} else if (operateType == 2) {// 删除
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
						mTradeList.clear();
						for (int i = 0; i < tempTradeList.size(); i++) {
							mTradeList.add(tempTradeList.get(i));
						}
						showConsumeList(mTradeList);

					} else if (operateType == 1) {
						mTradeList.clear();
						for (int i = 0; i < tempTradeList.size(); i++) {
							mTradeList.add(tempTradeList.get(i));
						}
						showConsumeList(mTradeList);
					} else if (operateType == 2) {
					}

				} catch (Exception e) {
				}
			}

		}
	}

	private void showConsumeList(List<Trade> list) {
		try {

			if (mConsumeListAdapter == null) {
				lv_consume = (ListView) findViewById(R.id.lv_consume);
				mConsumeListAdapter = new NewConsumeListAdapter(this, list);
				lv_consume.setAdapter(mConsumeListAdapter);
				lv_consume.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Trade pi = (Trade) arg0.getItemAtPosition(arg2);
						showDialog(pi);
					}

				});
			} else {
				mConsumeListAdapter.notifyDataSetChanged();
			}

		} catch (Exception e) {
		}
	}

	private void showDialog(final Trade pi) {
		final MyDialog.Builder builder = new MyDialog.Builder(this);
		builder.setTitle("请选择操作");
		builder.setPositiveButton("重打印此交易小票",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						bankTrans(pi);
					}
				});
		builder.setNegativeButton("查看此交易详情",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						String fk_trade_id = pi.getId();
						Intent intent = new Intent();
						intent.putExtra("fk_trade_id", fk_trade_id);
						intent.setClass(NewConsumeActivity.this,
								NewConsumeDetailActivity.class);
						startActivity(intent);
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

	/**
	 * 重打印上一张
	 */
	private void bankTrans(Trade pi) {
		try {
			dialog.show();
			String fileName = "/mnt/sdcard/bank/SysCfg.dat";
			File file = new File(fileName);
			if (file.exists()) {
				String sysString = FileUtils.readFileSdcardFile(fileName);
				String shanghuhao = sysString.split("USERCNAME=")[1]
						.split("\n")[0];
				String shanghumingcheng = sysString.split("USERNO=")[1]
						.split("\n")[0];
				String zhongduanhao = sysString.split("TERMINAL=")[1]
						.split("\n")[0];
				String bankCode = "--";
				String pici = "--";
				PrintUtils
						.printRe(shanghumingcheng, shanghuhao, zhongduanhao,
								bankCode, pici,
								pi.getBank_card_no().substring(1, 20),
								pi.getTrace(), pi.getRef_no(),
								pi.getTrade_type(), pi.getTrade_money(),
								pi.getTrade_time(), mPrinter, true);
				PrintUtils.printNoteCount("\n\n\n", mPrinter);
				AlertDialog("重打印成功");
				dialog.cancel();
			} else {
				PrintUtils.printRe("", "", "", "", "", pi.getBank_card_no()
						.substring(1, 20), pi.getTrace(), pi.getRef_no(), pi
						.getTrade_type(), pi.getTrade_money(), pi
						.getTrade_time(), mPrinter, true);
				PrintUtils.printNoteCount("\n\n\n", mPrinter);
				AlertDialog("重打印成功");
				dialog.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialog("重打印异常");
			dialog.cancel();
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
