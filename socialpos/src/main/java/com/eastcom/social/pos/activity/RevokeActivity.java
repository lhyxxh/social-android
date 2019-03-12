package com.eastcom.social.pos.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.adapter.RevokeListAdapter;
import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.message.SoTradeType;
import com.eastcom.social.pos.core.socket.message.trade.FinanceTradeMessage;
import com.eastcom.social.pos.core.socket.message.trade.SocialTradeMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;
import com.eastcom.social.pos.listener.MyBank;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.mispos.NewPos;
import com.eastcom.social.pos.mispos.model.Revoke.RevokeAuthReq;
import com.eastcom.social.pos.mispos.model.Revoke.RevokeReq;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.FloatCalculator;
import com.eastcom.social.pos.util.MyByteUtils;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.PrintUtils;
import com.eastcom.social.pos.util.TimeUtil;
import com.eastcom.social.pos.util.ToastUtil;
import com.eastcom.social.pos.util.dialog.LoadingDialog;
import com.eastcom.social.pos.util.dialog.MyDialog;
import com.eastcom.social.pos.util.dialog.NumDialog;
import com.eastcom.social.pos.util.dialog.SearchDialog;
import com.eastcompeace.printer.api.Printer;
import com.example.usbcciddrv.jniUsbCcid;
import com.landicorp.android.mispos.MisPosClient;
import com.landicorp.android.mispos.MisPosRequest;
import com.landicorp.android.mispos.MisPosResponse;
import com.printer.sdk.api.PrinterType;

@SuppressLint({ "HandlerLeak", "ClickableViewAccessibility" })
public class RevokeActivity extends BaseActivity implements OnClickListener {
	private EditText et_trade_no;
	private Button btn_search;
	private TextView tv_card_no;
	private TextView tv_trade_no;
	private TextView tv_sum;
	private TextView tv_time;
	private ListView listview;
	private TextView tv_cancle;
	private TextView tv_sys_time;

	private RelativeLayout rl4;
	private RelativeLayout rl3;
	private RelativeLayout rl2;
	private RelativeLayout rl1;

	private TradeService tradeService;
	private TradeDetailService tradeDetailService;
	private Trade mTrade = new Trade();
	private Trade mRevokeTrade = new Trade();// 撤销交易
	private ArrayList<TradeDetail> mTradeDetailList = new ArrayList<TradeDetail>();
	private ArrayList<TradeDetail> mRevokeTradeDetailList = new ArrayList<TradeDetail>();
	private RevokeListAdapter mAdapter;

	private GetDataTask getDataTask;
	private int operateType = 4;
	private String key;
	private MisPosClient bank = null;
	private String bankResultString;// misPos返回信息
	private int tradeType = 0;

	private boolean isConnected = false;// 是否连接到打印机
	public static Printer mPrinter;
	private UsbDevice mUsbDevice;
	private boolean is_58mm = true;
	private boolean is_thermal = true;
	private PendingIntent pendingIntent;
	private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

	private SoClient client;

	private String eid = "";
	private TextView tv_eid;

	private LoadingDialog dialog;
	private AlertDialog.Builder builder;

	private Date bankTradeTime = null;
	private LocalDataFactory localDataFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_revoke);
		tradeService = TradeService.getInstance(this);
		tradeDetailService = TradeDetailService.getInstance(this);
		bank = MyBank.newInstance().getBank();
		client = MySoClient.newInstance().getClient();
		localDataFactory = LocalDataFactory.newInstance(this);
		eid = localDataFactory.getString(LocalDataFactory.WHOLE_EID, "");
		initView();
		startGetDataTask();
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
		et_trade_no = (EditText) findViewById(R.id.et_trade_no);
		et_trade_no.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_trade_no.setInputType(InputType.TYPE_NULL);
		et_trade_no.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE
						|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					showNumDialog();
				}
				return false;
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
		btn_search = (Button) findViewById(R.id.btn_search);
		tv_card_no = (TextView) findViewById(R.id.tv_card_no);
		tv_trade_no = (TextView) findViewById(R.id.tv_trade_no);
		tv_sum = (TextView) findViewById(R.id.tv_sum);
		tv_time = (TextView) findViewById(R.id.tv_time);
		listview = (ListView) findViewById(R.id.listview);
		tv_cancle = (TextView) findViewById(R.id.tv_cancle);

		btn_search.setOnClickListener(this);
		tv_cancle.setOnClickListener(this);

		dialog = new LoadingDialog(this, "正在进行撤销,请稍等");
		dialog.setCanceledOnTouchOutside(false);
		builder = new AlertDialog.Builder(RevokeActivity.this);
		tv_sys_time = (TextView) findViewById(R.id.tv_sys_time);
	}

	private void AlertDialog(String title) {

		builder.setTitle("温馨提示");
		builder.setMessage(title);
		builder.setPositiveButton("确定", null);
		builder.create().show();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl4:
			intent.setClass(RevokeActivity.this, PolicyWordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl3:
			intent.setClass(RevokeActivity.this, ChartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl2:
			intent.setClass(RevokeActivity.this, NewConsumeActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl1:
			finish();
			break;
		case R.id.btn_search:
			key = et_trade_no.getText().toString();
			operateType = 0;
			startGetDataTask();
			break;
		case R.id.tv_cancle:
			if ("".equals(tv_trade_no.getText().toString())) {
				ToastUtil.show(RevokeActivity.this, "没有可以撤销的消费，请重新确认", 5000);
			} else {
				String trade_no = tv_trade_no.getText().toString();
				revokeTrade(trade_no);
			}
			break;
		case R.id.et_trade_no:
			break;
		default:
			break;
		}
	}

	private void revokeTrade(String trade_no) {
		if (!isConnected) {
			showConnectDialog(trade_no);
		} else {
			showPayDialog(trade_no);
		}
	}

	private void bankRevokeTrade(final String trade_no) {
		int paySum = mTrade.getTrade_money();
		String aaa = paySum + "";
		final String byteString = ByteUtils.addZeroStr(aaa, 12, true);// 补0
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				mHandler2.sendMessage(message);
				try {
					MyByteUtils.deleteFolderFile();
					String outsideMsg = bank.GetOutsideInfo();
					if (outsideMsg==null || outsideMsg.equals(""))
						Toast.makeText(RevokeActivity.this, "外部认证信息为空", Toast.LENGTH_SHORT).show();
					else {
						String outsideAuth = NewPos.outsideAuth(outsideMsg);
//						if (tradeType == 0) {
//							String aaString = "00000000111111110102" + byteString
//									+ "0000000000000000000000" + trade_no + outsideAuth;
//							bankResultString = bank._bankTrans(aaString);
//						} else {
//							String aaString = "00000000111111110101" + byteString
//									+ "0000000000000000000000" + trade_no + outsideAuth;
//							bankResultString = bank._bankTrans(aaString);
//						}
						MisPosRequest request = new MisPosRequest();
						request.posid = "00000000";
						request.operid = "11111111";
						request.trans = "01";
						request.amount = byteString;
						request.old_trace = trade_no;
						request.szRsv = outsideAuth;
//							MisPosResponse response = bank.posTrans(request);
						bankResultString = bank._bankTrans(NewPos.mergeReqstr(request));
						if ("00".equals(bankResultString.substring(0, 2))) {
							Message message2 = new Message();
							message2.what = 2;
							mHandler2.sendMessage(message2);
							String year = Calendar.getInstance().get(Calendar.YEAR)
									+ "";// 当前年份
							String m_d = MyByteUtils.getByte2String(159, 169,
									bankResultString);    //日期
							bankTradeTime = TimeUtil.getDateTime2(year + m_d);
							// bankTradeTime = TimeUtil.getDateTime(now);
							// PrintInfo();
							operateType = 3;
							startGetDataTask();
						} else {
							Message message3 = new Message();
							message3.what = 3;
							mHandler2.sendMessage(message3);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();

	}

	public Handler mHandler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.show();
				break;
			case 2:
				dialog.cancel();
				AlertDialog("撤销成功");
				break;
			case 3:
				dialog.cancel();
				AlertDialog("撤销失败,请重试");
				break;
			case 4:
				dialog.cancel();
				AlertDialog("撤销异常,请重试");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void startGetDataTask() {
		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute((Void) null);
		}
	}

	public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

		// private ArrayList<Trade> tempTradeList = new ArrayList<Trade>();
		private Trade tempTrade = new Trade();
		private ArrayList<TradeDetail> tempTradeDetailList = new ArrayList<TradeDetail>();
		private String error = "";

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				if (operateType == 0) {
					List<Trade> list = tradeService.queryByTradeNoList(key);
					if (list.size() > 0) {
						tempTrade = list.get(0);
					} else {
						tempTrade = null;
					}
				} else if (operateType == 1) {
					tempTradeDetailList = (ArrayList<TradeDetail>) tradeDetailService
							.queryByFkTradeId(mTrade.getId());
				} else if (operateType == 2) {

				} else if (operateType == 3) {// 插入撤销交易
					addRevokeTrade(mTrade, mRevokeTrade, bankTradeTime);
					addRevokeTradeDetail(mRevokeTrade, mTradeDetailList,
							mRevokeTradeDetailList, bankTradeTime);

					LocalDataFactory localDataFactory = LocalDataFactory
							.newInstance(RevokeActivity.this);
					int is_Encrypt = localDataFactory.getInt(
							LocalDataFactory.IS_ENCRYPT, 0);
					if (is_Encrypt == 1) {// 加密处理
						String bandCardNo = mRevokeTrade.getBank_card_no();
						String socialCardNo = mRevokeTrade.getSocial_card_no();
						String refNo = mRevokeTrade.getRef_no();
						String tradeSn = "000000";
						int tradeMoney = mRevokeTrade.getTrade_money();
						String psamNo = mRevokeTrade.getPsam_no();
						String terminalCode = mRevokeTrade.getTerminal_code();
						String rfsamNo = mRevokeTrade.getRfsam_no();
						String mCipherText = SocialTradeMessage.getCipherText(
								bandCardNo, socialCardNo, refNo,
								mRevokeTrade.getTrade_time(), tradeSn,
								tradeMoney, psamNo, terminalCode, rfsamNo);
						String mCipher = jniUsbCcid.Trade_record(mCipherText);
						mRevokeTrade.setEncrypt(mCipher);
					}

					mTrade.setIs_revoke(1);
					tradeService.saveTrade(mTrade);
					tradeService.saveTrade(mRevokeTrade);
					tradeDetailService
							.saveTradeDetailLists(mRevokeTradeDetailList);
				} else if (operateType == 4) {
				}

			} catch (Exception e) {
				error = e.getMessage();
				getDataTask = null;
				MyLog.w("RevokeActivity-doInBackground",
						"" + operateType + e.getMessage());
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
						if (tempTrade != null) {
							mTrade = tempTrade;
							if (mTrade.getIs_revoke() == 0) {
								tv_trade_no.setText(et_trade_no.getText()
										.toString());
								bindMessage(mTrade);
								operateType = 1;
								startGetDataTask();
							} else {
								ToastUtil.show(RevokeActivity.this,
										"此流水号的交易已被撤销", 5000);
								resetMessage();
							}

						} else {
							ToastUtil.show(RevokeActivity.this, "查无此流水号的消费交易",
									5000);
							resetMessage();
						}

					} else if (operateType == 1) {
						mTradeDetailList.clear();
						for (int i = 0; i < tempTradeDetailList.size(); i++) {
							mTradeDetailList.add(tempTradeDetailList.get(i));
						}
						showList(mTradeDetailList);
					} else if (operateType == 2) {
						setTradeDetailMessage(mRevokeTradeDetailList,
								mRevokeTrade);
						et_trade_no.setText("");
						resetMessage();
					} else if (operateType == 3) {
						PrintInfo();
						operateType = 2;
						startGetDataTask();
					}

				} catch (Exception e) {
					e.printStackTrace();
					MyLog.w("Revoke1123", "" + operateType + e.getMessage());
				}
			} else {
				ToastUtil.show(RevokeActivity.this, error + "--" + operateType,
						3000);
			}

		}

	}

	private void PrintInfo() {
		if (isConnected) {
			try {

				// 打印自己的模板
				PrintUtils.printRe(
						MyByteUtils.getByte2String(169, 269, bankResultString)    //商户中文名
								.trim(),
						MyByteUtils.getByte2String(106, 121, bankResultString)    //商户号
								.trim(),
						MyByteUtils.getByte2String(121, 129, bankResultString)    //终端号
								.trim(),
						MyByteUtils.getByte2String(2, 6, bankResultString)        //银行行号
								.trim(),
						MyByteUtils.getByte2String(129, 135, bankResultString)     //批次号
								.trim(), mRevokeTrade.getBank_card_no()
								.substring(1, 20), mRevokeTrade.getTrace(),
						mRevokeTrade.getRef_no(), mRevokeTrade.getTrade_type(),
						mRevokeTrade.getTrade_money(), mRevokeTrade
								.getTrade_time(), mPrinter, false);
				PrintUtils.printNoteCount("\n\n\n", mPrinter);
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.show(RevokeActivity.this, "打印失败", 5000);
			}

		}
	}

	private void resetMessage() {
		tv_card_no.setText("");
		tv_sum.setText("");
		tv_time.setText("");
		tv_trade_no.setText("");
		mTradeDetailList.clear();
		showList(mTradeDetailList);
	}

	private void showList(ArrayList<TradeDetail> list) {
		try {

			if (mAdapter == null) {
				listview = (ListView) findViewById(R.id.listview);
				mAdapter = new RevokeListAdapter(this, list);
				listview.setAdapter(mAdapter);
				listview.setOnItemClickListener(new OnItemClickListener() {

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
				ToastUtil.show(RevokeActivity.this, "没有正在连接的打印机", 5000);
				break;
			case Printer.Handler_Get_Device_List_Completed:
				if (mUsbDevice != null) {
					initPrinter(mUsbDevice);
				}
				break;
			case Printer.Handler_Get_Device_List_Error:
				ToastUtil.show(RevokeActivity.this, "打印机连接错误", 5000);
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
			Toast.makeText(getApplicationContext(), action, Toast.LENGTH_LONG)
					.show();

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
		isConnected = mPrinter.openConnection(device);
		setButtonState();
	}

	/**
	 * 关闭链接打印机
	 */
	public void closeConnection() {
		mPrinter.closeConnection();
		isConnected = false;
		setButtonState();
	}

	/**
	 * 重置状态
	 */
	private void setButtonState() {

	}

	/**
	 * 连接打印机对话框
	 */
	private void showConnectDialog(final String trade_no) {

		final MyDialog.Builder builder = new MyDialog.Builder(this);
		builder.setTitle("还没链接打印机,是否连接到打印机?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Printer printer = new Printer(getApplicationContext());
				printer.getDeviceList(mHandler);
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
	private void showNumDialog() {

		final NumDialog.Builder builder = new NumDialog.Builder(this);
		builder.setTitle("请输入凭证号");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String sum = builder.GetSum();
				et_trade_no.setText(sum);

				key = sum;
				operateType = 0;
				startGetDataTask();
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						String sum = builder.GetSum();
						et_trade_no.setText(sum);
					}
				});

		builder.create().show();

	}

	private void bindMessage(Trade trade) {
		tv_card_no.setText(trade.getSocial_card_no());

		tv_sum.setText(FloatCalculator.divide(trade.getTrade_money(), 100) + "");
		String time = trade.getTrade_time().getTime() / 1000 + "";
		String dateString = TimeUtil
				.timeStamp2Date(time, "yyyy-MM-dd HH:mm:ss");

		tv_time.setText(dateString);

	}

	/**
	 * 新增撤销汇总
	 * 
	 * @param mTrade
	 * @param mRevokeTrade
	 * @param now
	 */
	private void addRevokeTrade(Trade mTrade, Trade mRevokeTrade, Date now) {
		mRevokeTrade.setAmount(mTrade.getAmount());
		mRevokeTrade.setId(UUID.randomUUID().toString());
		mRevokeTrade.setId_card_no(mTrade.getId_card_no());
		mRevokeTrade.setIndividual_pay(mTrade.getIndividual_pay());
		mRevokeTrade.setIs_revoke(1);
		mRevokeTrade.setIs_upload(1);
		mRevokeTrade.setPsam_no(mTrade.getPsam_no());
		String ref_no = MyByteUtils.getByte2String(147, 159, bankResultString);   //参考号
		mRevokeTrade.setRef_no(ref_no);
		mRevokeTrade.setRfsam_no(mTrade.getRfsam_no());
		mRevokeTrade.setSign_board_no(mTrade.getSign_board_no());
		mRevokeTrade.setSocial_card_no(mTrade.getSocial_card_no());
		mRevokeTrade.setBank_card_no(mTrade.getBank_card_no());
		mRevokeTrade.setSs_pay(mTrade.getSs_pay());
		mRevokeTrade.setTerminal_code(mTrade.getTerminal_code());
		mRevokeTrade.setTrade_money(mTrade.getTrade_money());
		int tradeNo = localDataFactory.getInt(LocalDataFactory.TRADE_NO, 0);
		mTrade.setPre_trade_no(tradeNo);
		localDataFactory.putInt(LocalDataFactory.TRADE_NO, tradeNo + 1);
		mRevokeTrade.setTrade_no(tradeNo);
		mRevokeTrade.setTrade_state(mTrade.getTrade_state());
		mRevokeTrade.setTrade_time(now);
		if (mTrade.getTrade_type() == Integer.valueOf(SoTradeType.医院交易_82字节)) {
			mRevokeTrade
					.setTrade_type(Integer.valueOf(SoTradeType.医院交易撤单_82字节));
		} else if (mTrade.getTrade_type() == Integer
				.valueOf(SoTradeType.金融交易_82字节)) {
			mRevokeTrade.setTrade_type(11);
		} else {
			mRevokeTrade
					.setTrade_type(Integer.valueOf(SoTradeType.医保交易撤单_82字节));
		}
		mRevokeTrade.setTrace(bankResultString.substring(30, 36));

	}

	/**
	 * 新增撤销明细
	 * 
	 * @param mTradeDetailList
	 * @param mRevokeTradeDetailList
	 * @param now
	 */
	private void addRevokeTradeDetail(Trade mRevokeTrade,
			ArrayList<TradeDetail> mTradeDetailList,
			ArrayList<TradeDetail> mRevokeTradeDetailList, Date now) {
		String fk_trade_id = mRevokeTrade.getId();
		for (int i = 0; i < mTradeDetailList.size(); i++) {
			TradeDetail pi = mTradeDetailList.get(i);
			pi.setId(UUID.randomUUID().toString());
			pi.setFk_trade_id(fk_trade_id);
			pi.setIs_upload(1);
			mRevokeTradeDetailList.add(pi);
		}
	}

	/**
	 * 上传交易信息到后台
	 * 
	 * @param mTradeDetailsList
	 * @param mTrade
	 *
	 */
	private void setTradeDetailMessage(
			ArrayList<TradeDetail> mTradeDetailsList, Trade mTrade) {
		if (mTrade.getEncrypt() == null) {
			sendTradeMessage(mTrade);
		} else {
			sendTradeMessageCipher(mTrade);
		}
	}

	/**
	 * 密文
	 * 
	 * @param mTrade
	 */
	private void sendTradeMessageCipher(Trade mTrade) {
		String tradeState = mTrade.getTrade_state() == 71 ? "0071" : "0070";
		String isEncrypt = "01";
		int preNo = mTrade.getPre_trade_no();
		String rfsamNo = mTrade.getRfsam_no();
		int tradeRandom = mTrade.getTrade_no();
		int mac = 10;
		int sendCount = 1;
		int tradeNo = mTrade.getTrade_no();
		String tradeType;
		if (mTrade.getTrade_type() == Integer.valueOf(SoTradeType.医院交易撤单_82字节)) {
			tradeType = SoTradeType.医院交易撤单_82字节;
		} else if (mTrade.getTrade_type() == 11) {
			tradeType = SoTradeType.金融交易撤单_82字节;
		} else {
			tradeType = SoTradeType.医保交易撤单_82字节;
		}
		String cipherText = mTrade.getEncrypt();
		SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
				tradeType, tradeState, isEncrypt, preNo, rfsamNo, tradeRandom,
				mac, sendCount, tradeNo, cipherText);
		MyApplicationLike.stopTimeConsumingService(false);
		client.sendMessage(socialTradeRespMessage);
	}

	private void sendTradeMessage(Trade mTrade) {
		String tradeState = mTrade.getTrade_state() == 71 ? "0071" : "0070";
		String isEncrypt = "00";
		int preNo = mTrade.getPre_trade_no();
		String bankCard = MyByteUtils.getByte2String(6, 26, bankResultString); //卡号
		String socialCardNo = mTrade.getSocial_card_no();
		String refNo = mTrade.getRef_no();
		Date tradeTime = mTrade.getTrade_time();
		String tradeSn = "000000";
		int tradeMoney = mTrade.getTrade_money();
		String psamNo = mTrade.getPsam_no();
		String terminalCode = mTrade.getTerminal_code();
		String rfsamNo = mTrade.getRfsam_no();
		int tradeRandom = mTrade.getTrade_no();
		int mac = 10;
		int sendCount = 1;
		int tradeNo = mTrade.getTrade_no();
		MyApplicationLike.stopTimeConsumingService(false);
		if (mTrade.getTrade_type() == Integer.valueOf(SoTradeType.医院交易撤单_82字节)) {
			String tradeType = SoTradeType.医院交易撤单_82字节;
			FinanceTradeMessage financeTradeMessage = new FinanceTradeMessage(
					tradeType, tradeState, isEncrypt, preNo, bankCard,
					socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
					psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
					tradeNo);

			client.sendMessage(financeTradeMessage);
		}else if (mTrade.getTrade_type() == 11) {
			String tradeType = SoTradeType.金融交易撤单_82字节;
			FinanceTradeMessage financeTradeMessage = new FinanceTradeMessage(
					tradeType, tradeState, isEncrypt, preNo, bankCard,
					socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
					psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
					tradeNo);

			client.sendMessage(financeTradeMessage);
		} else {
			String tradeType = SoTradeType.医保交易撤单_82字节;
			SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
					tradeType, tradeState, isEncrypt, preNo, bankCard,
					socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
					psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
					tradeNo);

			client.sendMessage(socialTradeRespMessage);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mUsbReceiver);
	}

	/**
	 * 选择支付方式对话框
	 * 
	 * @param trade_no
	 */
	private void showPayDialog(final String trade_no) {

		final SearchDialog.Builder builder = new SearchDialog.Builder(this);
		builder.setTitle("请选择支付方式");
		builder.setBankButton(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				tradeType = 1;
				if (jniUsbCcid.GetAuthorization() == 1) {
					bankRevokeTrade(trade_no);
				} else {
					ToastUtil.show(RevokeActivity.this, "授权失败,请检查", 3000);
				}

			}
		});
		builder.setSocialButton(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				tradeType = 0;
				if (jniUsbCcid.GetAuthorization() == 1) {
					jniUsbCcid.Eb_State(2, 0, 2);
					bankRevokeTrade(trade_no);
				} else {
					jniUsbCcid.Eb_State(2, 0, 1);
					ToastUtil.show(RevokeActivity.this, "授权失败,请检查", 3000);
				}
			}
		});
		builder.create().show();
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
		tv_sys_time.setText(time);
	}

}
