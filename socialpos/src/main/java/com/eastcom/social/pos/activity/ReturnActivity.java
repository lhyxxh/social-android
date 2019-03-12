package com.eastcom.social.pos.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.eastcom.social.pos.mispos.model.Return.ReturnAuthReq;
import com.eastcom.social.pos.mispos.model.Return.ReturnReq;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.FloatCalculator;
import com.eastcom.social.pos.util.MyByteUtils;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.PrintUtils;
import com.eastcom.social.pos.util.TimeUtil;
import com.eastcom.social.pos.util.ToastUtil;
import com.eastcom.social.pos.util.dialog.KeyBoardDialog;
import com.eastcom.social.pos.util.dialog.LoadingDialog;
import com.eastcom.social.pos.util.dialog.MyDialog;
import com.eastcom.social.pos.util.dialog.NumDialog;
import com.eastcom.social.pos.util.dialog.SearchDialog;
import com.eastcompeace.printer.api.Printer;
import com.example.usbcciddrv.jniUsbCcid;
import com.landicorp.android.mispos.MisPosClient;
import com.landicorp.android.mispos.MisPosRequest;
import com.printer.sdk.api.PrinterType;

@SuppressLint({ "ClickableViewAccessibility", "HandlerLeak" })
public class ReturnActivity extends BaseActivity implements OnClickListener {
	private EditText et_ref;
	private Button btn_search;
	private TextView tv_card_no;
	private TextView tv_ref_no;
	private TextView tv_sum;
	private TextView tv_time;
	private ListView listview;
	private TextView tv_return;

	private RelativeLayout rl4;
	private RelativeLayout rl3;
	private RelativeLayout rl2;
	private RelativeLayout rl1;

	private TradeService tradeService;
	private TradeDetailService tradeDetailService;
	private Trade mTrade = new Trade();
	private Trade mReturnTrade = new Trade();// 退货交易
	private ArrayList<TradeDetail> mTradeDetailList = new ArrayList<TradeDetail>();
	private ArrayList<TradeDetail> mReturnTradeDetailList = new ArrayList<TradeDetail>();
	private RevokeListAdapter mAdapter;

	private GetDataTask getDataTask;
	private int operateType = 4;
	private String key;
	private MisPosClient bank = null;
	private String bankResultString;
	private int tradeType = 0;

	private String eid = "";
	private TextView tv_eid;

	private boolean isConnected = false;// 是否连接到打印机
	public static Printer mPrinter;
	private UsbDevice mUsbDevice;
	private boolean is_58mm = true;
	private boolean is_thermal = true;
	private PendingIntent pendingIntent;
	private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

	private String mCipher = "";

	private SoClient client;

	private Date bankTradeTime = null;
	private Float paySum;
	private Float costSum;

	private LoadingDialog dialog;
	private AlertDialog.Builder builder;

	private int preTradeNo;
	private LocalDataFactory localDataFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_return);
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
		et_ref = (EditText) findViewById(R.id.et_ref);
		et_ref.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_ref.setInputType(InputType.TYPE_NULL);
		et_ref.setOnEditorActionListener(new OnEditorActionListener() {

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
		et_ref.setOnTouchListener(new OnTouchListener() {

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
		tv_ref_no = (TextView) findViewById(R.id.tv_ref_no);
		tv_sum = (TextView) findViewById(R.id.tv_sum);
		tv_time = (TextView) findViewById(R.id.tv_time);
		listview = (ListView) findViewById(R.id.listview);
		tv_return = (TextView) findViewById(R.id.tv_return);

		btn_search.setOnClickListener(this);
		tv_return.setOnClickListener(this);

		dialog = new LoadingDialog(this, "正在进行退货,请稍等");
		dialog.setCanceledOnTouchOutside(false);
		builder = new AlertDialog.Builder(ReturnActivity.this);
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
			intent.setClass(ReturnActivity.this, PolicyWordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl3:
			intent.setClass(ReturnActivity.this, ChartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl2:
			intent.setClass(ReturnActivity.this, NewConsumeActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl1:
			finish();
			break;
		case R.id.btn_search:
			key = et_ref.getText().toString();
			resetMessage();
			operateType = 0;
			startGetDataTask();
			break;
		case R.id.tv_return:

			if ("".equals(tv_ref_no.getText().toString())) {
				ToastUtil.show(ReturnActivity.this, "没有可以撤销的消费，请重新确认", 5000);
			} else {// 退货一分钱
				String ref_no = et_ref.getText().toString();
				String time = tv_time.getText().toString();
				String a = time.substring(5, 7);
				String b = time.substring(8, 10);
				String c = time.substring(11, 13);
				String d = time.substring(14, 16);
				String e = time.substring(17, 19);
				String date = a + b + c + d + e;
				showKeyBoardDialog(ref_no, date);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 编辑退货总价对话框
	 */
	private void showKeyBoardDialog(final String ref_no, final String date) {

		final KeyBoardDialog.Builder builder = new KeyBoardDialog.Builder(this);
		builder.setTitle("请输入退货金额(单位：元)");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				if ("".equals(builder.GetSum())) {
					ToastUtil.show(ReturnActivity.this, "退货金额为空,请重新输入", 5000);
				} else {
					paySum = Float.valueOf(builder.GetSum());
					if (paySum > costSum) {
						ToastUtil.show(ReturnActivity.this, "退货金额大于消费金额,请重新输入",
								5000);
					} else {
						dialog.dismiss();
						returnTrade(ref_no, date);
					}
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

	private void returnTrade(String ref_no, String date) {
		if (!isConnected) {
			showConnectDialog(ref_no, date);
		} else {
			showPayDialog(ref_no, date);

		}

	}

	private void bankReturnTrade(final String ref_no, final String date) {
		// 真实退价
		String aaa = (int) (FloatCalculator.multiply(paySum, 100)) + "";
		final String byteString = ByteUtils.addZeroStr(aaa, 12, true);// 补0
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				mHandler2.sendMessage(message);
				try {
					MyByteUtils.deleteFolderFile();
					String a = "";
					String outsideMsg = bank.GetOutsideInfo();
					if (outsideMsg==null || outsideMsg.equals(""))
						Toast.makeText(ReturnActivity.this, "外部认证信息为空", Toast.LENGTH_SHORT).show();
					else {
						String outsideAuth = NewPos.outsideAuth(outsideMsg);
//						if (tradeType == 0) {
//							a = "00000000111111110202" + byteString + date + ref_no + outsideAuth;
//							bankResultString = bank._bankTrans(a);
//						} else {
//							a = "00000000111111110201" + byteString + date + ref_no + outsideAuth;
//							bankResultString = bank._bankTrans(a);
//						}
						MisPosRequest request = new MisPosRequest();
						request.posid = "00000000";
						request.operid = "11111111";
						request.trans = "02";
						request.amount = byteString;
						request.old_reference = ref_no;
						request.old_date = date;
						request.szRsv = outsideAuth;
						bankResultString = bank._bankTrans(NewPos.mergeReqstr(request));

						MyLog.i("ReturnActivity", "bankResult" + a);
						if ("00".equals(bankResultString.substring(0, 2))) {
							String year = Calendar.getInstance().get(Calendar.YEAR)
									+ "";// 当前年份
							String m_d = MyByteUtils.getByte2String(159, 169,
									bankResultString);
							bankTradeTime = TimeUtil.getDateTime2(year + m_d);
							operateType = 3;
							startGetDataTask();
							Message message2 = new Message();
							message2.what = 2;
							mHandler2.sendMessage(message2);

						} else {
							Message message2 = new Message();
							message2.what = 3;
							mHandler2.sendMessage(message2);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message message2 = new Message();
					message2.what = 4;
					mHandler2.sendMessage(message2);
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
				AlertDialog("退货成功");
				break;
			case 3:
				dialog.cancel();
				AlertDialog("退货失败,请重试");
				break;
			case 4:
				dialog.cancel();
				AlertDialog("退货异常,请重试");
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

		private ArrayList<Trade> tempTradeList = new ArrayList<Trade>();
		private ArrayList<TradeDetail> tempTradeDetailList = new ArrayList<TradeDetail>();

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				if (operateType == 0) {
					if ("".equals(key)) {
						tempTradeList = (ArrayList<Trade>) tradeService
								.loadAllTrade();
					} else {
						// key = "%"+key+"%";
						tempTradeList = (ArrayList<Trade>) tradeService
								.queryByRefNo(key);
					}

				} else if (operateType == 1) {// 查询交易详情
					tempTradeDetailList = (ArrayList<TradeDetail>) tradeDetailService
							.queryByFkTradeId(mTrade.getId());
				} else if (operateType == 2) {
					mCipher = "";

				} else if (operateType == 3) {// 插入撤销交易
					String a = MyByteUtils.ReadTxtFile();
					try {

						String b = new String(a.getBytes(), "UTF-8");
						PrintUtils.printNote(b, mPrinter);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						ToastUtil.show(ReturnActivity.this, "打印失败", 5000);
					}
					addReturnTrade(mTrade, mReturnTrade, bankTradeTime);
					addReturnTradeDetail(mReturnTrade, mTradeDetailList,
							mReturnTradeDetailList, bankTradeTime);
					tradeService.saveTrade(mReturnTrade);
				} else if (operateType == 4) {
					int no = tradeService.queryPreTradeNo();
					preTradeNo = no;
				}

			} catch (Exception e) {
				getDataTask = null;
				MyLog.w("ReturnActivity-doInBackground",
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

						if (tempTradeList.size() > 0) {
							// for (int i = 0; i < tempTradeList.size(); i++) {
							// Trade pi = tempTradeList.get(i);
							// if (key.equals(pi.getTrace())) {
							// mTrade = tempTradeList.get(i);
							// break;
							// }
							// }
							mTrade = tempTradeList.get(0);
							tv_ref_no.setText(et_ref.getText().toString());
							bindMessage(mTrade);
							operateType = 1;
							startGetDataTask();
						} else {
							ToastUtil.show(ReturnActivity.this, "查无此参考号的交易",
									5000);
						}

					} else if (operateType == 1) {
						mTradeDetailList.clear();
						for (int i = 0; i < tempTradeDetailList.size(); i++) {
							mTradeDetailList.add(tempTradeDetailList.get(i));
						}

						showList(mTradeDetailList);
					} else if (operateType == 2) {
						try {
							setTradeDetailMessage(mReturnTradeDetailList,
									mReturnTrade, mCipher);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					else if (operateType == 3) {

						// 清空信息
						et_ref.setText("");
						resetMessage();
						mTradeDetailList.clear();
						showList(mTradeDetailList);
						operateType = 2;
						startGetDataTask();
					}

				} catch (Exception e) {
					MyLog.w("ReturnActivity-onPostExecute", "" + operateType
							+ e.getMessage());
				}
			}

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
				ToastUtil.show(ReturnActivity.this, "没有正在连接的打印机", 5000);
				break;
			case Printer.Handler_Get_Device_List_Completed:
				if (mUsbDevice != null) {
					initPrinter(mUsbDevice);
				}
				break;
			case Printer.Handler_Get_Device_List_Error:
				ToastUtil.show(ReturnActivity.this, "打印机连接错误", 5000);
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
	private void showConnectDialog(final String ref_no, final String date) {

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
						bankReturnTrade(ref_no, date);
					}
				});
		builder.create().show();
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

	private void bindMessage(Trade trade) {
		tv_card_no.setText(trade.getSocial_card_no());
		tv_sum.setText(FloatCalculator.divide(trade.getTrade_money(), 100) + "");
		costSum = FloatCalculator.divide(trade.getTrade_money(), 100);
		String time = trade.getTrade_time().getTime() / 1000 + "";
		String dateString = TimeUtil
				.timeStamp2Date(time, "yyyy-MM-dd HH:mm:ss");
		tv_time.setText(dateString);
	}

	private void resetMessage() {
		tv_card_no.setText("");
		tv_sum.setText("");
		tv_time.setText("");
		tv_ref_no.setText("");
		mTradeDetailList.clear();
	}

	/**
	 * 新增退货汇总
	 * 
	 * @param mTrade
	 * @param mRevokeTrade
	 * @param now
	 */
	private void addReturnTrade(Trade mTrade, Trade mRevokeTrade, Date now) {
		mRevokeTrade.setAmount(mTrade.getAmount());
		mRevokeTrade.setId(UUID.randomUUID().toString());
		mRevokeTrade.setId_card_no(mTrade.getId_card_no());
		mRevokeTrade.setIndividual_pay(mTrade.getIndividual_pay());
		mRevokeTrade.setIs_revoke(1);// 退货
		mRevokeTrade.setIs_upload(1);
		mRevokeTrade.setPre_trade_no(preTradeNo);
		mRevokeTrade.setPsam_no(mTrade.getPsam_no());
		mRevokeTrade.setRef_no(mTrade.getRef_no());
		mRevokeTrade.setRfsam_no(mTrade.getRfsam_no());
		mRevokeTrade.setSign_board_no(mTrade.getSign_board_no());
		mRevokeTrade.setSocial_card_no(mTrade.getSocial_card_no());
		mRevokeTrade.setSs_pay(mTrade.getSs_pay());
		mRevokeTrade.setTerminal_code(mTrade.getTerminal_code());
		mRevokeTrade.setTrade_money((int) (FloatCalculator
				.multiply(paySum, 100)));
		mRevokeTrade.setTrade_no(Integer.valueOf(bankResultString.substring(30,   //流水号
				36)));
		mRevokeTrade.setTrade_state(mTrade.getTrade_state());
		mRevokeTrade.setTrade_time(now);
		mRevokeTrade.setTrade_type(mTrade.getTrade_type() == Integer
				.valueOf(SoTradeType.金融交易_82字节) ? 11 : Integer
				.valueOf(SoTradeType.医保交易撤单_82字节));
		mRevokeTrade.setTrace(mTrade.getTrace());
	}

	/**
	 * 新增退货明细
	 * 
	 * @param mTradeDetailList
	 * @param now
	 */
	private void addReturnTradeDetail(Trade mReturnTrade,
			ArrayList<TradeDetail> mTradeDetailList,
			ArrayList<TradeDetail> mReturnTradeDetailList, Date now) {
		String fk_trade_id = mReturnTrade.getId();
		for (int i = 0; i < mTradeDetailList.size(); i++) {
			TradeDetail pi = mTradeDetailList.get(i);
			pi.setId(UUID.randomUUID().toString());
			pi.setFk_trade_id(fk_trade_id);
			pi.setIs_upload(1);
			mReturnTradeDetailList.add(pi);
		}
	}

	/**
	 * 上传交易信息到后台
	 * 
	 * @param mTradeDetailsList
	 * @param mTrade
	 * @param cipherText
	 */
	private void setTradeDetailMessage(
			ArrayList<TradeDetail> mTradeDetailsList, Trade mTrade,
			String cipherText) {
		try {
			sendTradeMessage(mTrade);
		} catch (Exception e) {
		}

		// sendTradeMessageCipher(mTrade, cipherText);

	}


	private void sendTradeMessage(Trade mTrade) {
		String tradeState = mTrade.getTrade_state() == 71 ? "0071" : "0070";
		String isEncrypt = "00";
		int preNo = mTrade.getPre_trade_no();
		String bankCard = mTrade.getSocial_card_no();
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
		if (mTrade.getTrade_type() == 11) {
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

	/**
	 * 选择支付方式对话框
	 *
	 * @param ref_no
	 */
	private void showPayDialog(final String ref_no, final String date) {

		final SearchDialog.Builder builder = new SearchDialog.Builder(this);
		builder.setTitle("请选择支付方式");
		builder.setBankButton(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				tradeType = 1;
				if (jniUsbCcid.GetAuthorization() == 1) {
					bankReturnTrade(ref_no, date);
				} else {
					ToastUtil.show(ReturnActivity.this, "授权失败,请检查", 3000);
				}

			}
		});
		builder.setSocialButton(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				tradeType = 0;
				if (jniUsbCcid.GetAuthorization() == 1) {
					jniUsbCcid.Eb_State(2, 0, 2);
					bankReturnTrade(ref_no, date);
				} else {
					jniUsbCcid.Eb_State(2, 0, 1);
					ToastUtil.show(ReturnActivity.this, "授权失败,请检查", 3000);
				}
			}
		});
		builder.create().show();
	}

	/**
	 * 输入框
	 */
	private void showNumDialog() {

		final NumDialog.Builder builder = new NumDialog.Builder(this);
		builder.setTitle("请输入参考号");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String sum = builder.GetSum();
				et_ref.setText(sum);

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
						et_ref.setText(sum);
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
		// TODO Auto-generated method stub

	}

}
