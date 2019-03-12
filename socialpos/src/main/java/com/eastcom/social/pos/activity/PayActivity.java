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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.adapter.PayListAdapter;
import com.eastcom.social.pos.adapter.PayListAdapter.MyCallback;
import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.config.BinCode;
import com.eastcom.social.pos.core.orm.entity.Medicine;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.orm.entity.UnknowMedicine;
import com.eastcom.social.pos.core.service.MedicineService;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.service.UnknowMedicineService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.SoSocialCardType;
import com.eastcom.social.pos.core.socket.message.SoTradeType;
import com.eastcom.social.pos.core.socket.message.checksocialstatussdbz.CheckSocialStatusSdBzMessage;
import com.eastcom.social.pos.core.socket.message.checksocialstatussdbz.CheckSocialStatusSdBzRespMessage;
import com.eastcom.social.pos.core.socket.message.querymedicine.QueryMedicineMessage;
import com.eastcom.social.pos.core.socket.message.querymedicine.QueryMedicineRespMessage;
import com.eastcom.social.pos.core.socket.message.telinfo.TelInfoMessage;
import com.eastcom.social.pos.core.socket.message.trade.FinanceTradeMessage;
import com.eastcom.social.pos.core.socket.message.trade.SocialTradeMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;
import com.eastcom.social.pos.entity.CardInfo;
import com.eastcom.social.pos.entity.Drug;
import com.eastcom.social.pos.entity.MyTradeDetail;
import com.eastcom.social.pos.listener.MyBank;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.mispos.NewPos;
import com.eastcom.social.pos.mispos.model.Consume.ConsumeAuthReq;
import com.eastcom.social.pos.mispos.model.Consume.ConsumeReq;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.ArrayUtils;
import com.eastcom.social.pos.util.FloatCalculator;
import com.eastcom.social.pos.util.MyByteUtils;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.PrintUtils;
import com.eastcom.social.pos.util.TimeUtil;
import com.eastcom.social.pos.util.ToastUtil;
import com.eastcom.social.pos.util.dialog.CardInfoDialog;
import com.eastcom.social.pos.util.dialog.KeyBoardDialog;
import com.eastcom.social.pos.util.dialog.LoadingDialog;
import com.eastcom.social.pos.util.dialog.MyDialog;
import com.eastcom.social.pos.util.dialog.NumDialog;
import com.eastcom.social.pos.util.dialog.PayDialog;
import com.eastcom.social.pos.util.dialog.SearchDialog;
import com.eastcompeace.printer.api.Printer;
import com.example.usbcciddrv.jniUsbCcid;
import com.landicorp.android.mispos.MisPosClient;
import com.landicorp.android.mispos.MisPosRequest;
import com.landicorp.android.mispos.MisPosResponse;
import com.printer.sdk.api.PrinterType;

public class PayActivity extends BaseActivity implements MyCallback,
		OnClickListener, OnTouchListener {
	private TradeDetailService tradeDetailService;
	private TradeService tradeService;
	private MedicineService medicineService;
	private UnknowMedicineService unknowMedicineService;

	private String rfsam = "";
	private String psam = "";
	private String eid = "";

	private EditText et_bar_code;
	private EditText et_super_code;
	private TextView tv_name;
	private TextView tv_unit;
	private TextView tv_num;
	private ImageView iv_add;
	private ImageView iv_reduce;
	private TextView tv_price;
	private TextView tv_pay;
	private TextView tv_read_card_info;
	private TextView tv_sum;
	private TextView tv_eid;
	private LinearLayout ll_delete;
	private TextView tv_sys_time;

	private RelativeLayout rl4;
	private RelativeLayout rl3;
	private RelativeLayout rl2;
	private RelativeLayout rl1;

	private ArrayList<MyTradeDetail> mMyTradeDetailList = new ArrayList<MyTradeDetail>();
	private PayListAdapter mAdapter;
	private ListView listView;

	private Spinner spinner;

	private ArrayList<TradeDetail> mTradeDetailsList = new ArrayList<TradeDetail>();
	private Trade mTrade = new Trade();

	private Drug mDrug = new Drug();

	private GetDataTask getDataTask;
	private int operateType = 1;
	private String key;

	private float paySum = 1000;
	private float goodUnit = 0;// 正在录入药品单价
	private float totalSum;

	private boolean isConnected = false;// 是否连接到打印机
	public static Printer mPrinter;
	private UsbDevice mUsbDevice;
	private boolean is_58mm = true;
	private boolean is_thermal = true;
	private PendingIntent pendingIntent;
	private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

	private SoClient client;

	private MisPosClient bank = null;

	private String keyString;// misPos支付接口返回结果字符串
	private Date bankTradeTime = null;
	private int tradeType = 0;// 支付方式，默认社保卡

	private LoadingDialog dialog;
	private AlertDialog.Builder builder;

	private String card_balance = "";
	private static String old_bank_no = "";// 社保账号
	private static String old_social_card_no = "";// 社保卡
	private boolean isReadCard = false;
	private int isBlackCard = 100;// 0:未知 1:白名单
	private CardInfo cardInfo = new CardInfo();
	private String oldTelephone;

	private LocalDataFactory localDataFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		localDataFactory = LocalDataFactory.newInstance(this);
		eid = localDataFactory.getString(LocalDataFactory.WHOLE_EID, "");
		psam = localDataFactory.getString(LocalDataFactory.PSAM, "");
		rfsam = localDataFactory.getString(LocalDataFactory.RFSAM, "");
		tradeDetailService = TradeDetailService.getInstance(this);
		tradeService = TradeService.getInstance(this);
		medicineService = MedicineService.getInstance(this);
		unknowMedicineService = UnknowMedicineService.getInstance(this);
		client = MySoClient.newInstance().getClient();
		initView();
		startGetDataTask("");
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
		bank = MyBank.newInstance().getBank();
		et_bar_code = (EditText) findViewById(R.id.et_bar_code);
		et_super_code = (EditText) findViewById(R.id.et_super_code);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_unit = (TextView) findViewById(R.id.tv_unit);
		tv_num = (TextView) findViewById(R.id.tv_num);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		iv_reduce = (ImageView) findViewById(R.id.iv_reduce);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_pay = (TextView) findViewById(R.id.tv_pay);
		tv_read_card_info = (TextView) findViewById(R.id.tv_read_card_info);
		tv_sum = (TextView) findViewById(R.id.tv_sum);
		tv_eid = (TextView) findViewById(R.id.tv_eid);
		tv_eid.setText(getResources().getString(R.string.tv_eid) + eid);
		ll_delete = (LinearLayout) findViewById(R.id.ll_delete);

		rl4 = (RelativeLayout) findViewById(R.id.rl4);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);

		spinner = (Spinner) findViewById(R.id.spinner_bar_code);

		iv_reduce.setOnClickListener(this);
		iv_add.setOnClickListener(this);
		tv_pay.setOnClickListener(this);
		tv_read_card_info.setOnClickListener(this);
		ll_delete.setOnClickListener(this);
		rl4.setOnClickListener(this);
		rl3.setOnClickListener(this);
		rl2.setOnClickListener(this);
		rl1.setOnClickListener(this);
		et_bar_code.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_bar_code.setInputType(InputType.TYPE_NULL);
		et_bar_code.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE
						|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

					searchBarcode();
				}
				return true;
			}

		});
		et_super_code.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_super_code.setInputType(InputType.TYPE_NULL);
		et_super_code.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE
						|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					searchSupercode();
				}
				return false;
			}

		});
		et_bar_code.setOnTouchListener(this);
		et_super_code.setOnTouchListener(this);
		tv_unit.setOnClickListener(this);

		dialog = new LoadingDialog(this, "请稍等");
		dialog.setCanceledOnTouchOutside(false);
		builder = new AlertDialog.Builder(PayActivity.this);

		tv_sys_time = (TextView) findViewById(R.id.tv_sys_time);
	}

	private void AlertDialog(String title) {

		builder.setTitle("温馨提示");
		builder.setMessage(title);
		builder.setPositiveButton("确定", null);
		builder.create().show();
	}

	private void searchBarcode() {
		key = et_bar_code.getText().toString();
//		if (key.length() == 20) {
//			et_bar_code.setText("");
//			et_super_code.setText("");
//			resetMessage();
//			ToastUtil.show(PayActivity.this, "你输入的是电子监管码，请重新输入", 5000);
//			return;
//		} else if (key.length() != 13 && key.length() != 14
//				&& key.length() != 12 && !"888888".equals(key)) {
//			et_bar_code.setText("");
//			et_super_code.setText("");
//			resetMessage();
//			ToastUtil.show(PayActivity.this, "输入有误,请重新输入", 5000);
//			return;
//		}
		if ("".equals(key)) {
			return;
		}
		operateType = 0;
		startGetDataTask("");
	}

	private void searchSupercode() {
		String bar_code = et_bar_code.getText().toString();
		if ("".equals(bar_code)) {
			et_super_code.setText("");
			et_bar_code.requestFocus();
			ToastUtil.show(PayActivity.this, "请先输入药品条形码或电子监管码", 5000);
		}

		String tempKey = et_super_code.getText().toString();

		if (tempKey.length() == 20) {// 电子监管码

			// 只有一盒药
			if ("1".equals(tv_num.getText().toString())) {
				boolean goFlag = false;
				// 查询有没有相同监管码
				for (int i = 0; i < mMyTradeDetailList.size(); i++) {
					TradeDetail pi = mMyTradeDetailList.get(i).getTd();
					// 重复电子监管码的药，不准再购买
					if (tempKey.equals(pi.getSuper_vision_code())) {
						ToastUtil.show(PayActivity.this, "药品已被购买", 5000);
						goFlag = true;
						break;
					} else {// 没有相同的监管码
					}

				}
				if (!goFlag) {
					// 加入此盒有监管码的药品
					TradeDetail td = new TradeDetail();
					MyTradeDetail mtd = new MyTradeDetail();
					td.setBar_code(mDrug.getBar_code());
					td.setSuper_vision_code(tempKey);
					mDrug.setSuper_code(tempKey);
					int unit = (int) (FloatCalculator.multiply(goodUnit, 100));
					td.setActual_price(unit);
					td.setAmount(1);
					td.setSocial_category(1);
					td.setProduct_name(mDrug.getName());
					mtd.setTd(td);
					mtd.setDr(mDrug);
					mMyTradeDetailList.add(mtd);
				}

				et_bar_code.setText("");
				et_super_code.setText("");
				resetMessage();
				et_bar_code.setFocusable(true);
				et_bar_code.requestFocus();// 获取焦点
				showList(mMyTradeDetailList);
				account(mMyTradeDetailList);
			} else {// 不止一盒药
					// 统计没有监管码的
				boolean isExist = false;
				for (int i = 0; i < mMyTradeDetailList.size(); i++) {
					TradeDetail pi = mMyTradeDetailList.get(i).getTd();
					if (pi.getBar_code().equals(
							et_bar_code.getText().toString())) {// 条形码相同
						isExist = true;
						break;
					}

				}
				boolean goFlag1 = false;
				if (isExist) {
					// 重复电子监管码的药，不准再购买
					for (int i = 0; i < mMyTradeDetailList.size(); i++) {
						TradeDetail pi = mMyTradeDetailList.get(i).getTd();
						if (tempKey.equals(pi.getSuper_vision_code())) {// 有相同电子码
							ToastUtil.show(PayActivity.this, "有相同电子码", 5000);
							goFlag1 = true;
							break;
						} else {
							int a = Integer
									.valueOf(tv_num.getText().toString());
							int b = pi.getAmount();
							mMyTradeDetailList.get(i).getTd().setAmount(a + b);
						}

					}

				} else {// 没有相同的条形码
					TradeDetail td = new TradeDetail();
					Drug tempDr = new Drug();
					tempDr.setBar_code(mDrug.getBar_code());
					tempDr.setName(mDrug.getName());
					tempDr.setOriginal_price(mDrug.getOriginal_price());
					MyTradeDetail mtd = new MyTradeDetail();
					td.setBar_code(tempDr.getBar_code());
					td.setSuper_vision_code(tempKey);
					tempDr.setSuper_code(tempKey);
					int unit = (int) (FloatCalculator.multiply(goodUnit, 100));
					td.setActual_price(unit);
					td.setAmount(Integer.valueOf(tv_num.getText().toString()) - 1);
					td.setSocial_category(1);
					td.setProduct_name(tempDr.getName());
					mtd.setTd(td);
					mtd.setDr(tempDr);
					mMyTradeDetailList.add(mtd);

				}
				if (!goFlag1) {
					// 加入此盒有监管码的药品
					TradeDetail td = new TradeDetail();
					MyTradeDetail mtd = new MyTradeDetail();
					td.setBar_code(mDrug.getBar_code());
					td.setSuper_vision_code(tempKey);
					mDrug.setSuper_code(tempKey);
					// 单价
					int unit = (int) (FloatCalculator.multiply(goodUnit, 100));
					td.setActual_price(unit);
					td.setAmount(1);
					td.setSocial_category(1);
					td.setProduct_name(mDrug.getName());
					mtd.setTd(td);
					mtd.setDr(mDrug);
					mMyTradeDetailList.add(mtd);
				}

				et_bar_code.setText("");
				et_super_code.setText("");
				resetMessage();
				et_bar_code.setFocusable(true);
				et_bar_code.requestFocus();// 获取焦点
				showList(mMyTradeDetailList);
				account(mMyTradeDetailList);

			}

		} else if (tempKey.length() == 13 || tempKey.length() == 14
				|| tempKey.length() == 12 || "888888".equals(tempKey)) {// 条形码
			if (tempKey.equals(et_bar_code.getText().toString())) {// 相同条形码
				int i = Integer.valueOf(tv_num.getText().toString());
				tv_num.setText((i + 1) + "");
				int mSum = i * mDrug.getOriginal_price();
				tv_sum.setText(mSum + "");
				et_super_code.setText("");
				account(mMyTradeDetailList);
				return;
			} else {// 不同条形码
				boolean flag = false;
				for (int i = 0; i < mMyTradeDetailList.size(); i++) {
					TradeDetail pi = mMyTradeDetailList.get(i).getTd();
					if (pi.getBar_code().equals(
							et_bar_code.getText().toString())) {
						int a = Integer.valueOf(tv_num.getText().toString());
						int b = pi.getAmount();
						mMyTradeDetailList.get(i).getTd().setAmount(a + b);
						et_super_code.setText("");
						resetMessage();
						et_bar_code.setFocusable(true);
						et_bar_code.requestFocus();// 获取焦点
						showList(mMyTradeDetailList);
						account(mMyTradeDetailList);
						flag = true;
						break;
					}
				}
				if (!flag) {// 已经更新
					TradeDetail td = new TradeDetail();
					MyTradeDetail mtd = new MyTradeDetail();
					td.setBar_code(mDrug.getBar_code());
					td.setSuper_vision_code("");
					mDrug.setSuper_code("");
					int unit = (int) (FloatCalculator.multiply(goodUnit, 100));
					td.setActual_price(unit);
					td.setAmount(Integer.valueOf(tv_num.getText().toString()));
					td.setSocial_category(1);
					td.setProduct_name(mDrug.getName());
					mtd.setTd(td);
					mtd.setDr(mDrug);
					mMyTradeDetailList.add(mtd);

					et_super_code.setText("");
					resetMessage();
					et_bar_code.setFocusable(true);
					et_bar_code.requestFocus();// 获取焦点
					showList(mMyTradeDetailList);
					account(mMyTradeDetailList);
				}
				// 搜索新条形码
				key = tempKey;
				et_bar_code.setText(key);
				operateType = 0;
				startGetDataTask("");

			}
		} else {
			et_super_code.setText("");
			ToastUtil.show(PayActivity.this, "输入有误,请重新输入", 5000);
			return;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.et_bar_code:

				showBarCodeDialog();
				break;
			case R.id.et_super_code:
				if ("".equals(et_bar_code.getText().toString())) {
					ToastUtil.show(PayActivity.this, "请先输入药品条形码", 5000);

				} else {
					showSyperCodeDialog();
				}

				break;

			default:
				break;
			}
		}

		return false;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.ll_delete:
			et_bar_code.setText("");
			et_super_code.setText("");

			resetMessage();
			account(mMyTradeDetailList);
			break;
		case R.id.tv_pay:
			selectPayType();
			break;
		case R.id.tv_read_card_info:// 读取基本信息
			if (client.isAvtive()) {
				if (jniUsbCcid.GetAuthorization() == 1) {
					jniUsbCcid.Eb_State(2, 0, 2);
					jniUsbCcid.GetRfsamCardNo();
					jniUsbCcid.GetPsamCardNo();
					readCardInfoBalance();
				} else {
					Message message1 = new Message();
					message1.what = 11;
					mHandler2.sendMessage(message1);
				}
			} else {
				AlertDialog("TCP已断开，请检查重试");
			}
			break;
		case R.id.rl4:
			intent.setClass(PayActivity.this, PolicyWordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl3:
			intent.setClass(PayActivity.this, ChartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl2:
			intent.setClass(PayActivity.this, NewConsumeActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl1:
			finish();
			break;
		case R.id.iv_add:
			int sum = Integer.valueOf(tv_num.getText().toString());
			tv_num.setText((sum + 1) + "");
			float price1 = FloatCalculator.multiply(goodUnit + "", (sum + 1)
					+ "");
			tv_price.setText(price1 + "元");
			account(mMyTradeDetailList);
			break;
		case R.id.iv_reduce:
			int sum2 = Integer.valueOf(tv_num.getText().toString());
			if (sum2 == 1) {
				ToastUtil.show(PayActivity.this, "药品购买数量不能为0", 5000);
				return;
			}
			tv_num.setText((sum2 - 1) + "");
			float price2 = FloatCalculator.multiply(goodUnit + "", (sum2 - 1)
					+ "");
			tv_price.setText(price2 + "元");
			account(mMyTradeDetailList);
			break;
		case R.id.tv_unit:
			if (!"".equals(tv_unit.getText().toString())) {
				showUnitDialog();
			}

			break;

		default:
			break;
		}
	}

	private void selectPayType() {
		if (!isConnected) {
			showConnectDialog();
			return;
		}
		if (mMyTradeDetailList.size() == 0
				&& "".equals(tv_name.getText().toString())) {
			ToastUtil.show(PayActivity.this, "没有药品结账", 5000);
			return;
		}
		if (accountTotal(mMyTradeDetailList)) {
			showAlertDialog();
		} else {
			ToastUtil.show(PayActivity.this, "药品单价或药品数量不能为0，请检查", 3000);
		}
	}

	/**
	 * 计算药品总价
	 * 
	 * @param mTradeDetailsList
	 */
	private void account(ArrayList<MyTradeDetail> mTradeDetailsList) {
		int sum = 0;
		for (int i = 0; i < mTradeDetailsList.size(); i++) {
			int amount = mTradeDetailsList.get(i).getTd().getAmount();
			sum += amount;
		}
		sum += Integer.valueOf(tv_num.getText().toString());
		tv_sum.setText(sum + "");
	}

	/**
	 * 计算药品总价
	 * 
	 * @param mTradeDetailsList
	 */
	private boolean accountTotal(ArrayList<MyTradeDetail> mTradeDetailsList) {
		int price = 0;
		for (int i = 0; i < mTradeDetailsList.size(); i++) {
			int amount = mTradeDetailsList.get(i).getTd().getAmount();
			int unit = mTradeDetailsList.get(i).getTd().getActual_price();
			if (unit == 0) {
				return false;
			}
			price += unit * amount;
		}
		String lastUnit = tv_unit.getText().toString();
		Float a = (float) 0;
		if (!"".equals(lastUnit)) {
			int l = lastUnit.length();
			a = Float.valueOf(lastUnit.substring(0, l - 1));
			if (a == 0 && !"".equals(tv_name.getText().toString())) {
				return false;
			}
		}
		float add1 = FloatCalculator.divide(price, 100);
		float num = Float.valueOf(tv_num.getText().toString());
		float add2 = FloatCalculator.multiply(a, num);
		float total = FloatCalculator.add(add2, add1);
		totalSum = total;
		return true;
	}

	public void startGetDataTask(String key) {
		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute(key);
		}
	}

	public class GetDataTask extends AsyncTask<String, Void, Boolean> {
		private List<Medicine> tempMedicine = new ArrayList<Medicine>();// 条形码搜索的临时药品

		private Drug mTempDrug = new Drug();// 条形码搜索的临时药品

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				if (operateType == 0) {
//					tempMedicine = medicineService.queryByBarCode(key);
					tempMedicine = medicineService.queryLikeBarCode(key);
					if (tempMedicine.size()==0 && key.length()==13){
						UnknowMedicineService.getInstance(PayActivity.this).saveUnknowMedicine(new UnknowMedicine(key));
						QueryMedicineMessage queryMedicineMessage = new QueryMedicineMessage(key);
						client.sendMessage(queryMedicineMessage,
								new ActivityCallBackListener() {

									@Override
									public void callBack(SoMessage message) {
										try {
											QueryMedicineRespMessage queryMedicineRespMessage = new QueryMedicineRespMessage(
													message);
											if (queryMedicineRespMessage.isExist()) {
												//保存药品
												Medicine medicine = new Medicine();
												medicine.setEntity(medicine,
														queryMedicineRespMessage);
												medicineService.saveMedicine(medicine);
												//更新已有交易明细
												updateMedicine(queryMedicineRespMessage
																.getBarCode(),
														queryMedicineRespMessage.getName());
												startGetDataTask("");
											}
										} catch (Exception e) {
											MyLog.w("PayActivity", "更新药品信息出错:"
													+ e.getMessage());
										}

									}

									@Override
									public void doTimeOut() {
										MyLog.w("PayActivity", "更新药品信息超时");
									}
								}, 5);
					}
				} else if (operateType == 6) {// 交易记录保存本地数据库
					// 结算
					if (!"".equals(et_bar_code.getText().toString())) {
						bindLastDetail(mDrug);
					}
					mTrade.setTrade_money((int) (FloatCalculator.multiply(
							paySum, 100)));
					// 补全交易总表其他非空字段
					int tradeNo = localDataFactory.getInt(
							LocalDataFactory.TRADE_NO, 0);
					mTrade.setTrade_no(tradeNo + 1);
					mTrade.setTrace(keyString.substring(30, 36));      //流水号
					mTrade.setPre_trade_no(tradeNo);
					localDataFactory.putInt(LocalDataFactory.TRADE_NO,
							tradeNo + 1);
					mTrade.setSign_board_no(eid);
					mTrade.setPsam_no(psam);
					mTrade.setRfsam_no(rfsam);
					String ter_no = MyByteUtils.getByte2String(121, 129,
							keyString);                                     //终端号？
					mTrade.setTerminal_code(ter_no);
					mTrade.setTrade_time(bankTradeTime);
					mTrade.setSend_count(0);
					String ref_no = MyByteUtils.getByte2String(147, 159,
							keyString);                                      //参考号
					mTrade.setRef_no(ref_no);
					String bankCardNo = ByteUtils.addZeroStr(MyByteUtils
							.getByte2String(6, 26, keyString).trim(), 20, true);
					mTrade.setBank_card_no(bankCardNo);                     //卡号
					if (tradeType == 1) {
						mTrade.setTrade_type(Integer
								.valueOf(SoTradeType.金融交易_82字节));
						mTrade.setId_card_no("00000000000000000000");// 读卡保存的身份证号
						mTrade.setSocial_card_no("M00000000");// 读卡保存的社保卡号
					} else {
						mTrade.setTrade_type(Integer
								.valueOf(SoTradeType.医保交易_82字节));
						mTrade.setId_card_no(cardInfo.getId_card_no());// 读卡保存的身份证号
						mTrade.setSocial_card_no(cardInfo.getSocial_card_no());// 读卡保存的社保卡号
					}
					mTrade.setTrade_state(70);
					mTrade.setSs_pay(1);
					mTrade.setIndividual_pay(1);
					mTrade.setIs_revoke(0);
					mTrade.setIs_upload(1);
					int size = 0;
					for (int i = 0; i < mMyTradeDetailList.size(); i++) {
						size += mMyTradeDetailList.get(i).getTd().getAmount();
					}
					mTrade.setAmount(size);

					String FK_TRADE_ID = UUID.randomUUID().toString();
					mTrade.setId(FK_TRADE_ID);

					LocalDataFactory localDataFactory = LocalDataFactory
							.newInstance(PayActivity.this);
					int is_Encrypt = localDataFactory.getInt(
							LocalDataFactory.IS_ENCRYPT, 0);
					if (is_Encrypt == 1) {// 加密处理
						MyLog.i("PayActivity", "is_Encrypt");
						String bandCardNo = mTrade.getBank_card_no();
						String socialCardNo = mTrade.getSocial_card_no();
						String refNo = mTrade.getRef_no();
						String tradeSn = "000000";
						int tradeMoney = mTrade.getTrade_money();
						String psamNo = mTrade.getPsam_no();
						String terminalCode = mTrade.getTerminal_code();
						String rfsamNo = mTrade.getRfsam_no();
						MyLog.i("PayActivity", psamNo);
						String mCipherText = SocialTradeMessage.getCipherText(
								bandCardNo, socialCardNo, refNo,
								mTrade.getTrade_time(), tradeSn, tradeMoney,
								psamNo, terminalCode, rfsamNo);
						String mCipher = jniUsbCcid.Trade_record(mCipherText);
						mTrade.setEncrypt(mCipher);
					}
					boolean isConsultationFee = false;
					MyLog.i("PayActivity", "mMyTradeDetailList's size = "
							+ mMyTradeDetailList.size());
					for (int i = 0; i < mMyTradeDetailList.size(); i++) {
						mMyTradeDetailList.get(i).getTd().setIs_upload(1);
						mMyTradeDetailList.get(i).getTd()
								.setId(UUID.randomUUID().toString());
						mMyTradeDetailList.get(i).getTd()
								.setFk_trade_id(FK_TRADE_ID);// 设置交易汇总信息
						mMyTradeDetailList.get(i).getTd()
								.setDetail_trade(i + 1);// 设置明细交易记录序号
						mMyTradeDetailList.get(i).getTd()
								.setTrade_type(tradeType == 1 ? 101 : 201);// 交易类型，101表示正常，102表示撤单
						if ("888888".equals(mMyTradeDetailList.get(i).getTd()
								.getBar_code())) {
							isConsultationFee = true;
						}
					}
					MyLog.i("PayActivity", "isConsultationFee = "
							+ isConsultationFee);
					if (isConsultationFee) {
						mTrade.setTrade_type(Integer
								.valueOf(SoTradeType.医院交易_82字节));
					}
					for (int i = 0; i < mMyTradeDetailList.size(); i++) {
						TradeDetail td = mMyTradeDetailList.get(i).getTd();
						if (isConsultationFee) {
							td.setSocial_category(0);
						}
						mTradeDetailsList.add(td);
					}
					tradeDetailService.saveTradeDetailLists(mTradeDetailsList);
					tradeService.saveTrade(mTrade);

				} else if (operateType == 7) {// rfsam卡交易数据加密,获取密文

				} else if (operateType == 13) {
					MyApplicationLike.stopTimeConsumingService(false);
					CheckSocialStatusSdBzMessage checkSocialStatusMessage = new CheckSocialStatusSdBzMessage(
							eid.substring(eid.length() - 8, eid.length()),
							cardInfo.getSocial_card_no(), cardInfo.getBank_no()
									.substring(0, 6), cardInfo.getId_card_no());
					MyLog.w("PayActivity", "start check status\n");
					client.sendMessage(checkSocialStatusMessage,
							new ActivityCallBackListener() {
								@Override
								public void callBack(SoMessage message) {
									MyLog.w("PayActivity", "check status ok\n");
									if ("".equals(card_balance)) {// 非超时返回
										CheckSocialStatusSdBzRespMessage checkSocialStatusSdBzRespMessage = new CheckSocialStatusSdBzRespMessage(
												message);
										isBlackCard = checkSocialStatusSdBzRespMessage
												.getStatus();

										if (checkSocialStatusSdBzRespMessage
												.getIsSuccess() == 1) {// 0-失败
																		// 1-卡状态和余额查询成功
																		// 2-卡状态查询成功，余额查询失败
											int balanceValue = checkSocialStatusSdBzRespMessage
													.getBalanceValue();
											float balance = FloatCalculator
													.divide(balanceValue, 100);
											String tel = checkSocialStatusSdBzRespMessage
													.getTel();
											checkBalanceSuccess(balance, tel);

										} else if (checkSocialStatusSdBzRespMessage
												.getIsSuccess() == 2) {
											String socialCardNo = cardInfo
													.getSocial_card_no();
											if (isBlackCard == 255) {// 外地卡
												bankTransCheck();
											} else if (socialCardNo
													.startsWith("M")) {
												bankTransCheck();
											} else {
												checkBalanceFail();
											}
										} else {
											checkBalanceFail();
										}

									} else {
										// 取消等待框
										mHandler2.sendEmptyMessage(12);
									}
								}

								@Override
								public void doTimeOut() {
									// 指令发送失败，启动ping
									MyLog.w("PayActivity",
											"check status timeout");
									card_balance = "timeout";
									isBlackCard = 100;
									Bundle bundle = new Bundle();
									bundle.putSerializable("cardInfo", cardInfo);
									bundle.putString("balance", "超时");
									Message message4 = new Message();
									message4.what = 10;
									message4.setData(bundle);
									mHandler2.sendMessage(message4);
								}
							}, 10);

				}

			} catch (Exception e) {
				getDataTask = null;
				MyLog.w("PayActivity1159", "" + operateType + e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
			try {
				if (result) {
					if (operateType == 0) {
						mTempDrug = new Drug();
						goodUnit = 0;
						if (tempMedicine.size() > 0) {
							Medicine pi = tempMedicine.get(0);
							mTempDrug.setName(pi.getName());
							mTempDrug.setBar_code(key);

							List<String> list = new ArrayList<String>();
							if (tempMedicine.size()>=5){
								for (int i=0; i<5; i++){
									list.add(tempMedicine.get(i).getBar_code());
								}
							}else{
								for (int i =0; i<tempMedicine.size(); i++){
									list.add(tempMedicine.get(i).getBar_code());
								}
							}

							ArrayAdapter<String> adapter = new ArrayAdapter<String>(PayActivity.this, android.R.layout.simple_spinner_item, list);
							spinner.setAdapter(adapter);
							spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
								@Override
								public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
									Medicine medicine = tempMedicine.get(position);
									mTempDrug = new Drug();
									mTempDrug.setName(medicine.getName());
									mTempDrug.setBar_code(medicine.getBar_code());
									et_bar_code.setText(medicine.getBar_code());
									bindMessage(mTempDrug);
									account(mMyTradeDetailList);
									mDrug = mTempDrug;
									et_super_code.requestFocus();
								}

								@Override
								public void onNothingSelected(AdapterView<?> parent) {

								}
							});
						} else {

							mTempDrug.setName("未知药品");
							mTempDrug.setBar_code(key);
							UnknowMedicine unknowMedicine = new UnknowMedicine(
									key);
							unknowMedicineService
									.saveUnknowMedicine(unknowMedicine);
						}
						bindMessage(mTempDrug);
						account(mMyTradeDetailList);
						mDrug = mTempDrug;
						et_super_code.requestFocus();
						ToastUtil.show(PayActivity.this, "请输入药品单价", 5000);
					} else if (operateType == 6) {
						MyLog.i("PayActivity", "save ok");
						if (isConnected) {
							try {
								// 打印自己的模板
								PrintUtils.printRe(
										MyByteUtils.getByte2String(169, 269,       //商户中文名
												keyString).trim(),
										MyByteUtils.getByte2String(106, 121,       //商户号
												keyString).trim(),

										MyByteUtils.getByte2String(121, 129,       //终端号
												keyString).trim(),
										MyByteUtils.getByte2String(2, 6,           //银行行号
												keyString).trim(),
										MyByteUtils.getByte2String(129, 135,       //批次号
												keyString).trim(),
										mTrade.getBank_card_no().substring(1,
												20), mTrade.getTrace(), mTrade
												.getRef_no(), mTrade
												.getTrade_type(), mTrade
												.getTrade_money(), mTrade
												.getTrade_time(), mPrinter,
										false);

								IsOldCard(keyString.substring(6, 26).trim());                          //卡号
								// 打印余额
								if (!"".equals(card_balance)
										&& !"0.0".equals(card_balance)) {
									float f1 = Float.valueOf(card_balance); // 余额转换为浮点
									float f3 = FloatCalculator.subtract(f1,
											paySum);// 计算余额
									PrintUtils.printNote(mPrinter, f1 + "", f3
											+ "", old_social_card_no);
									card_balance = "";
								} else {
									PrintUtils.printNoteCount("\n\n\n",
											mPrinter);
								}
							} catch (Exception e) {
								MyLog.e("PayActivity", "打印失败" + e.getMessage());
							}
						} else {
							MyLog.e("PayActivity", "mPrinter is not Connected");
						}
						// 清空药品信息
						et_bar_code.setText("");
						et_super_code.setText("");
						resetMessage();
						operateType = 7;
						startGetDataTask("");
					} else if (operateType == 7) {
						try {
							setTradeDetailMessage(mTradeDetailsList, mTrade);
						} catch (Exception e) {
							MyLog.e("PayActivity1123", e.getMessage() + "");
						}
						mMyTradeDetailList.clear();
						mTradeDetailsList.clear();
						showList(mMyTradeDetailList);
						account(mMyTradeDetailList);
					}
				} else {
					ToastUtil
							.show(PayActivity.this, "fail" + operateType, 5000);
				}

			} catch (Exception e) {
				if (operateType == 6) {
					MyLog.i("PayActivity", "save not ok");
				}
				mMyTradeDetailList.clear();
				mTradeDetailsList.clear();
				showList(mMyTradeDetailList);
				account(mMyTradeDetailList);
				e.printStackTrace();
			}
		}

	}

	private void showList(ArrayList<MyTradeDetail> list) {
		try {

			if (mAdapter == null) {
				listView = (ListView) findViewById(R.id.listview);
				mAdapter = new PayListAdapter(this, list, this);
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

	private void bindLastDetail(Drug dr) {
		TradeDetail td = new TradeDetail();
		MyTradeDetail mtd = new MyTradeDetail();
		td.setBar_code(et_bar_code.getText().toString());
		td.setActual_price((int) (FloatCalculator.multiply(goodUnit, 100)));
		td.setAmount(Integer.valueOf(tv_num.getText().toString()));
		td.setSuper_vision_code(et_super_code.getText().toString());
		td.setSocial_category(1);
		td.setProduct_name(tv_name.getText().toString());

		// 组合打印交易实体
		mtd.setTd(td);
		mtd.setDr(dr);
		mMyTradeDetailList.add(mtd);
	}

	/**
	 * 绑定药品信息
	 * 
	 * @param pi
	 */
	private void bindMessage(Drug pi) {
		if ("0".equals(tv_num.getText().toString())) {
			tv_num.setText("1");
		} else {

		}
		tv_name.setText(pi.getName());

		tv_unit.setText(FloatCalculator.divide(pi.getOriginal_price(), 100)
				+ "元");
		tv_price.setText(FloatCalculator.divide(pi.getOriginal_price(), 100)
				+ "元");
	}

	private void resetMessage() {

		tv_name.setText("");
		tv_unit.setText("");
		tv_price.setText("");
		tv_num.setText("0");
		et_bar_code.requestFocus();

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
				ToastUtil.show(PayActivity.this, "连接打印机出错,请检查", 5000);
				break;
			case Printer.Handler_Get_Device_List_Completed:
				if (mUsbDevice != null) {
					initPrinter(mUsbDevice);
				}
				break;
			case Printer.Handler_Get_Device_List_Error:
				ToastUtil.show(PayActivity.this, "打印机连接错误", 5000);
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
			// Toast.makeText(getApplicationContext(), action,
			// Toast.LENGTH_LONG)
			// .show();

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

	/**
	 * 连接打印机对话框
	 */
	private void showConnectDialog() {

		final MyDialog.Builder builder = new MyDialog.Builder(this);
		builder.setTitle("还没链接打印机,请连接打印机后重试?");
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

	@Override
	public void deleteClick(View v) {
		int pos = (Integer) v.getTag();
		showAlertDialog(pos);
	}

	/**
	 * 删除单一药品记录
	 * 
	 * @param pos
	 */
	private void showAlertDialog(final int pos) {

		final MyDialog.Builder builder = new MyDialog.Builder(this);
		builder.setTitle("是否删除此条消费记录?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mMyTradeDetailList.remove(pos);
				showList(mMyTradeDetailList);
				account(mMyTradeDetailList);
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

	@Override
	public void addClick(View v) {
		int pos = (Integer) v.getTag();
		int amount = mMyTradeDetailList.get(pos).getTd().getAmount();
		String aaString = mMyTradeDetailList.get(pos).getDr().getSuper_code();
		if (!"".equals(aaString)) {
			ToastUtil.show(PayActivity.this, "此药品不能增加", 5000);
			return;
		}
		mMyTradeDetailList.get(pos).getTd().setAmount(amount + 1);
		showList(mMyTradeDetailList);
		account(mMyTradeDetailList);
	}

	@Override
	public void reduceClick(View v) {
		int pos = (Integer) v.getTag();
		int amount = mMyTradeDetailList.get(pos).getTd().getAmount();

		if (amount == 1) {
			// 或者执行删除
			ToastUtil.show(PayActivity.this, "数量为1,不能减少", 5000);
			return;
		}
		mMyTradeDetailList.get(pos).getTd().setAmount(amount - 1);
		showList(mMyTradeDetailList);
		account(mMyTradeDetailList);
	}

	@Override
	public void showUnit(View v) {
		final int pos = (Integer) v.getTag();
		final KeyBoardDialog.Builder builder = new KeyBoardDialog.Builder(this);
		builder.setTitle("请输入 药品单价");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				if ("".equals(builder.GetSum())) {
					ToastUtil.show(PayActivity.this, "金额为空,请重新输入", 5000);
				} else {
					goodUnit = Float.valueOf(builder.GetSum());
					mMyTradeDetailList.get(pos).getTd()
							.setActual_price((int) (goodUnit * 100));
					showList(mMyTradeDetailList);
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

	/**
	 * 编辑总价对话框
	 */
	private void showAlertDialog() {

		final PayDialog.Builder builder = new PayDialog.Builder(this);
		builder.setTitle("请确认支付金额(单位：元)");
		builder.setHint(totalSum + "");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if ("".equals(builder.GetSum())) {
					ToastUtil.show(PayActivity.this, "价格为空,请重新输入", 5000);
					return;
				}
				dialog.dismiss();
				// 设置你的操作事项
				paySum = Float.valueOf(builder.GetSum());
				showPayDialog();
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
	 * 编辑单价对话框
	 */
	private void showUnitDialog() {

		final KeyBoardDialog.Builder builder = new KeyBoardDialog.Builder(this);
		builder.setTitle("请输入 药品单价");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				if ("".equals(builder.GetSum())) {
					ToastUtil.show(PayActivity.this, "金额为空,请重新输入", 5000);
				} else {
					goodUnit = Float.valueOf(builder.GetSum());
					tv_unit.setText(goodUnit + "元");
					float num = Float.valueOf(tv_num.getText().toString());
					float price = FloatCalculator.multiply(goodUnit, num);
					tv_price.setText(price + "元");
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

	/**
	 * 上传交易信息到后台
	 * 
	 * @param mTradeDetailsList
	 * @param mTrade
	 */
	private void setTradeDetailMessage(
			ArrayList<TradeDetail> mTradeDetailsList, Trade mTrade) {
		if (mTrade.getEncrypt() == null) {
			MyLog.i("setTradeDetailMessage", "00");
			sendTradeMessage(mTrade);
		} else {
			MyLog.i("setTradeDetailMessage", "01");
			sendTradeMessageCipher(mTrade);
		}
	}

	/**
	 * 上传手机号
	 * 
	 * @param socialCardNo
	 * @param tel
	 */
	private void sendTel(final String socialCardNo, final String tel) {
		if (!oldTelephone.equals(tel)) {
			// 获取的手机号与填写的手机号不一致
			try {
				MyApplicationLike.stopTimeConsumingService(false);
				TelInfoMessage telInfoMessage = new TelInfoMessage(
						socialCardNo, tel);
				client.sendMessage(telInfoMessage,
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {
								MyLog.e("上传了手机号失败", socialCardNo + "---" + tel
										+ "timeout");
							}

							@Override
							public void callBack(SoMessage message) {
								MyLog.i("上传了手机号", socialCardNo + "---" + tel);
							}
						}, 10);
			} catch (Exception e) {
				MyLog.e("PayActivity", "上传了手机号失败" + socialCardNo + "---" + tel);
			}
		} else {
			MyLog.e("PayActivity", "手机号与后台一致");
		}

	}

	private void sendTradeMessage(Trade mTrade) {
		String tradeState = mTrade.getTrade_state() == 71 ? "0071" : "0070";
		String isEncrypt = "00";
		int preNo = mTrade.getPre_trade_no();
		String bankCard = mTrade.getBank_card_no();
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
		MyLog.i("PayActivity", "tradeType = " + mTrade.getTrade_type());
		if (mTrade.getTrade_type() == Integer.valueOf(SoTradeType.医院交易_82字节)) {
			String tradeType = SoTradeType.医院交易_82字节;
			FinanceTradeMessage financeTradeMessage = new FinanceTradeMessage(
					tradeType, tradeState, isEncrypt, preNo, bankCard,
					socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
					psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
					tradeNo);

			client.sendMessage(financeTradeMessage);
		} else if (mTrade.getTrade_type() == Integer
				.valueOf(SoTradeType.金融交易_82字节)) {
			String tradeType = SoTradeType.金融交易_82字节;
			FinanceTradeMessage financeTradeMessage = new FinanceTradeMessage(
					tradeType, tradeState, isEncrypt, preNo, bankCard,
					socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
					psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
					tradeNo);

			client.sendMessage(financeTradeMessage);
		} else {
			String tradeType = SoTradeType.医保交易_82字节;
			SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
					tradeType, tradeState, isEncrypt, preNo, bankCard,
					socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
					psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
					tradeNo);

			client.sendMessage(socialTradeRespMessage);
		}

	}

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
		if (mTrade.getTrade_type() == Integer.valueOf(SoTradeType.医院交易_82字节)) {
			tradeType = SoTradeType.医院交易_82字节;
		} else if (mTrade.getTrade_type() == Integer
				.valueOf(SoTradeType.金融交易_82字节)) {
			tradeType = SoTradeType.金融交易_82字节;
		} else {
			tradeType = SoTradeType.医保交易_82字节;
		}
		String cipherText = mTrade.getEncrypt();
		SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
				tradeType, tradeState, isEncrypt, preNo, rfsamNo, tradeRandom,
				mac, sendCount, tradeNo, cipherText);
		MyApplicationLike.stopTimeConsumingService(false);
		client.sendMessage(socialTradeRespMessage);
	}

	private void bankTrans() {
		// 真实总价
		String aaa = (int) (FloatCalculator.multiply(paySum, 100)) + "";
		final String byteString = ByteUtils.addZeroStr(aaa, 12, true);// 补0

		// 开辟线程执行支付动作
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				MisPosResponse response;
				Message message = new Message();
				message.what = 1;
				mHandler2.sendMessage(message);
				try {
					MyByteUtils.deleteFolderFile();
					String aa = "";
					String outsideMsg = bank.GetOutsideInfo();
					if (outsideMsg==null || outsideMsg.equals(""))
						Toast.makeText(PayActivity.this, "外部认证信息为空", Toast.LENGTH_SHORT).show();
					else {
						String outsideAuth = NewPos.outsideAuth(outsideMsg);
						if (tradeType == 0) {
							MisPosRequest request = new MisPosRequest();
							request.posid = "00000000";
							request.operid = "11111111";
							request.trans = "00";
							request.CardType = "02";
							request.amount = byteString;
							request.szRsv = outsideAuth;
//							response = bank.posTrans(request);
							keyString = bank._bankTrans(NewPos.mergeReqstr(request));
//							keyString = bank._bankTrans("00000000111111110002"
//									+ byteString + outsideAuth);
							aa = keyString;
						} else {

							keyString = bank._bankTrans("00000000111111110001"
									+ byteString + outsideAuth);
							aa = keyString;
						}
						if ("00".equals(keyString.substring(0, 2))) {
							Message message1 = new Message();
							message1.what = 4;
							mHandler2.sendMessage(message1);

							String year = Calendar.getInstance().get(Calendar.YEAR)
									+ "";// 当前年份
							String m_d = MyByteUtils.getByte2String(159, 169,   //时间
									keyString);
							bankTradeTime = TimeUtil.getDateTime2(year + m_d);
							MyLog.e("PayActivity BankTrans ok", "" + aa);
							operateType = 6;
							startGetDataTask("");
							// 支付日期未做对应处理,请注意

						} else {
							bankTradeTime = new Date();
							String error = MyByteUtils.getByte2String(63, 103, aa)
									.trim();
							Bundle b = new Bundle();
							if ("".equals(error)) {
								error = "交易失败";
							}
							b.putString("error", error);
							Message message1 = new Message();
							message1.what = 3;
							message1.setData(b);
							mHandler2.sendMessage(message1);
						}
					}
				} catch (Exception e) {
					bankTradeTime = new Date();
					String error = "交易异常";
					Bundle b = new Bundle();
					b.putString("error", error);
					Message message1 = new Message();
					message1.what = 3;
					message1.setData(b);
					mHandler2.sendMessage(message1);
				}

			}
		});
		thread.start();

	}

	/**
	 * 读卡信息并查询余额
	 */
	private void readCardInfoBalance() {

		LocalDataFactory localDataFactory = LocalDataFactory
				.newInstance(PayActivity.this);
		int rfsam_status = localDataFactory.getInt(
				LocalDataFactory.RFSAM_STATUS, 0);
		if (rfsam_status == 1) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Message message1 = new Message();
						message1.what = 1;
						mHandler2.sendMessage(message1);
						String insideMsg = bank.GetOutsideInfo();
						String insideAuth = NewPos.outsideAuth(insideMsg);
						if (insideAuth==null || insideAuth.equals(""))
							mHandler2.sendEmptyMessage(13);  //提示加密机返回为空
						else {

							// 读卡
							String cardInfoString = bank.ReadCardInfo(insideAuth);
							MyLog.i("readCardInfo", cardInfoString + "");
							CardInfo.setCardInfo(cardInfoString + " ", cardInfo);
							old_bank_no = cardInfo.getBank_no();
							old_social_card_no = cardInfo.getSocial_card_no();
							mHandler2.sendEmptyMessage(8);
						}
					} catch (Exception e) {
						isReadCard = false;
						isBlackCard = 101;
						Message message = new Message();
						message.what = 9;
						mHandler2.sendMessage(message);
					}
				}
			});
			thread.start();
		} else {
			AlertDialog("后台授权状态异常");
		}
	}

	/**
	 * 判断查余社保卡与消费社保卡是否一致
	 * 
	 * @param new_card_no
	 */
	private void IsOldCard(String new_card_no) {
		if (!new_card_no.equals(old_bank_no)) {
			card_balance = "";
		}
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.show();
				break;
			case 2:
				dialog.cancel();
				AlertDialog("交易取消");
				isReadCard = false;
				isBlackCard = 0;
				break;
			case 3:
				dialog.cancel();
				String error = msg.getData().getString("error");
				AlertDialog("" + error);
				isReadCard = false;
				isBlackCard = 0;
				break;
			case 4:
				dialog.cancel();
				AlertDialog("支付成功");
				isReadCard = false;
				isBlackCard = 0;

				break;
			case 6:
				dialog.cancel();
				AlertDialog("余额查询失败");
				break;
			case 7:
				Drug mTempDrug = (Drug) msg.getData().getSerializable(
						"mTempDrug");
				Medicine medicine = (Medicine) msg.getData().getSerializable(
						"medicine");
				drugBind(mTempDrug, medicine);
				break;
			case 8:
				try {
					card_balance = "";// 清空上次保存的余额
					String bincodeString = cardInfo.getBank_no()
							.substring(0, 6);
					if (ArrayUtils.contains(BinCode.binCodes, bincodeString)) {
						operateType = 13;
						startGetDataTask("");
					} else {
						dialog.cancel();
						AlertDialog("此类社保卡不提供服务");
					}

				} catch (Exception e) {
					isReadCard = false;
					isBlackCard = 0;
					dialog.cancel();
					MyLog.w("PayActivity", e.getMessage() + "");
					e.printStackTrace();
				}
				break;
			case 9:
				card_balance = "";
				old_bank_no = "";
				old_social_card_no = "";
				dialog.cancel();
				AlertDialog("读卡信息失败");
				break;
			case 10:
				dialog.cancel();
				isReadCard = true;
				try {
					CardInfo cardInfo = new CardInfo();
					String balance = msg.getData().getString("balance");
					String tel = msg.getData().getString("tel");
					cardInfo = (CardInfo) msg.getData().getSerializable(
							"cardInfo");
					showCardDialog(cardInfo, balance + "", isBlackCard, tel);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 11:
				dialog.cancel();
				AlertDialog("授权失败");
				jniUsbCcid.Eb_State(2, 0, 1);
				break;
			case 12:
				dialog.cancel();
				break;
			case 13:
				Toast.makeText(PayActivity.this, "内部认证信息为空", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mUsbReceiver);

	}

	/**
	 * 基本信息对话框
	 * 
	 * @param isBlackCard
	 */
	private void showCardDialog(final CardInfo cardInfo, String balance,
			int isBlackCard, String telephone) {
		oldTelephone = telephone;
		final CardInfoDialog.Builder builder = new CardInfoDialog.Builder(this);
		builder.setCardInfo(cardInfo);
		builder.setBalance(balance);
		builder.setIsBlackCard(isBlackCard);
		builder.SetTel(telephone);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String tel = builder.GetTel();
				if (tel.length() == 11) {
					dialog.dismiss();
					sendTel(cardInfo.getSocial_card_no(),
							ByteUtils.addZeroStr(tel, 12, true));
				} else {
					ToastUtil.show(PayActivity.this, "请输入正确的手机号", 5000);
				}
			}
		});
		builder.setCancleButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				isReadCard = false;
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 选择支付方式对话框
	 */
	private void showPayDialog() {

		final SearchDialog.Builder builder = new SearchDialog.Builder(this);
		builder.setTitle("请选择支付方式");
		builder.setBankButton(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				tradeType = 1;
				if (jniUsbCcid.GetAuthorization() == 1) {
					jniUsbCcid.Eb_State(2, 0, 2);

					LocalDataFactory localDataFactory = LocalDataFactory
							.newInstance(PayActivity.this);
					int rfsam_status = localDataFactory.getInt(
							LocalDataFactory.RFSAM_STATUS, 0);
					if (rfsam_status != 2) {
						bankTrans();
					} else {
						AlertDialog("设备已停用");
					}
				} else {
					jniUsbCcid.Eb_State(2, 0, 1);
					ToastUtil.show(PayActivity.this, "授权失败,请检查", 3000);
				}

			}
		});
		builder.setSocialButton(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (isReadCard) {  //屏蔽测试
					if (isBlackCard == 1 || isBlackCard == 255) {
						tradeType = 0;
						if (jniUsbCcid.GetAuthorization() == 1) {
							jniUsbCcid.Eb_State(2, 0, 2);
							bankTrans();
						} else {
							ToastUtil.show(PayActivity.this, "授权失败,请检查", 3000);
							jniUsbCcid.Eb_State(2, 0, 1);
						}
					} else {
						String msg = SoSocialCardType
								.getSocialCardType(isBlackCard);
						ToastUtil.show(PayActivity.this, msg + "", 5000);
					}
				} else {
					AlertDialog("请先成功读卡");
				}

			}
		});
		builder.create().show();
	}

	/**
	 * 输入框
	 */
	private void showBarCodeDialog() {

		final NumDialog.Builder builder = new NumDialog.Builder(this);
		builder.setTitle("请输入条形码");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String sum = builder.GetSum();
				et_bar_code.setText(sum);
				searchBarcode();
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
	private void showSyperCodeDialog() {

		final NumDialog.Builder builder = new NumDialog.Builder(this);
		builder.setTitle("请输入电子监管码");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String sum = builder.GetSum();
				et_super_code.setText(sum);
				searchSupercode();
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
	 * 查余额成功
	 */
	private void checkBalanceSuccess(float balance, String tel) {

		Bundle bundle = new Bundle();
		bundle.putSerializable("cardInfo", cardInfo);
		bundle.putString("balance", balance + "元");
		if (tel.length() > 0) {
			bundle.putString("tel", tel.substring(1, tel.length()));
		} else {
			bundle.putString("tel", "");
		}
		card_balance = balance + "";

		Message message2 = new Message();
		message2.what = 10;
		message2.setData(bundle);
		mHandler2.sendMessage(message2);

	}

	/**
	 * 查余额失败
	 */
	private void checkBalanceFail() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("cardInfo", cardInfo);
		bundle.putString("balance", "失败");
		card_balance = "";
		Message message3 = new Message();
		message3.what = 10;
		message3.setData(bundle);
		mHandler2.sendMessage(message3);
	}

	/**
	 * pos查余
	 */
	private void bankTransCheck() {
		String outsideMsg = bank.GetOutsideInfo();
		if (outsideMsg==null || outsideMsg.equals(""))
			Toast.makeText(PayActivity.this, "外部认证信息为空", Toast.LENGTH_SHORT).show();
		else {
			String outsideAuth = NewPos.outsideAuth(outsideMsg);
			keyString = bank._bankTrans("000000001111111103" + outsideAuth);
			String result = keyString.substring(0, 2);
			if ("00".equals(result)) {
				String a = keyString.substring(49, 61);
				int i = Integer.valueOf(a);
				checkBalanceSuccess((float) i / 100, "0");
			} else {
				checkBalanceFail();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return false;
	}

	private void drugBind(Drug mTempDrug, Medicine medicine) {
		bindMessage(mTempDrug);
		et_bar_code.setText(key);
		account(mMyTradeDetailList);
		mDrug = mTempDrug;
		et_super_code.requestFocus();
		ToastUtil.show(PayActivity.this, "请输入药品单价", 5000);
	}

	@Override
	public void Nodata(boolean nodata) {

	}

	@Override
	public void setTime(String time) {
		tv_sys_time.setText(time);
	}

	/**
	 * 更新药品
	 */
	private void updateMedicine(String barCode, String product_name) {
		List<TradeDetail> preList = tradeDetailService.queryByBarCode(barCode);
		List<TradeDetail> list = new ArrayList<TradeDetail>();
		for (int i = 0; i < preList.size(); i++) {
			TradeDetail tradeDetail = preList.get(i);
			tradeDetail.setProduct_name(product_name);
			list.add(tradeDetail);
		}
		tradeDetailService.saveTradeDetailLists(list);
	}
}
