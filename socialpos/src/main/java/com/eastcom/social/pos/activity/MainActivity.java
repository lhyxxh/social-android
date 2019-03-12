package com.eastcom.social.pos.activity;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.core.orm.entity.Medicine;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.service.MedicineService;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoFollowCommad;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.alarm.AlarmMessage;
import com.eastcom.social.pos.core.socket.message.checksocialstatussdbz.CheckSocialStatusSdBzMessage;
import com.eastcom.social.pos.core.socket.message.checksocialstatussdbz.CheckSocialStatusSdBzRespMessage;
import com.eastcom.social.pos.core.socket.message.confirm.ConfirmMssage;
import com.eastcom.social.pos.core.socket.message.rfsamlist.RfsamListMessage;
import com.eastcom.social.pos.core.socket.message.rfsamlist.RfsamListRespMessage;
import com.eastcom.social.pos.core.socket.message.smsinfo.SmsInfoMessage;
import com.eastcom.social.pos.core.socket.message.smsinfo.SmsInfoRespMessage;
import com.eastcom.social.pos.core.socket.message.summitcardlog.SummitCardLogMessage;
import com.eastcom.social.pos.entity.CardInfo;
import com.eastcom.social.pos.listener.MyBank;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.listener.ReschedulableTimerTask;
import com.eastcom.social.pos.mispos.NewPos;
import com.eastcom.social.pos.mispos.model.Balance.BalanceReq;
import com.eastcom.social.pos.mispos.model.RePrint.RePrintReq;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.AppUtils;
import com.eastcom.social.pos.util.FileUtils;
import com.eastcom.social.pos.util.FloatCalculator;
import com.eastcom.social.pos.util.MyByteUtils;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.NetWorkUtil;
import com.eastcom.social.pos.util.PrintUtils;
import com.eastcom.social.pos.util.TimeUtil;
import com.eastcom.social.pos.util.ToastUtil;
import com.eastcom.social.pos.util.VersionUtil;
import com.eastcom.social.pos.util.dialog.AccountsDialog;
import com.eastcom.social.pos.util.dialog.LoadingDialog;
import com.eastcom.social.pos.util.dialog.MyDialog;
import com.eastcom.social.pos.util.dialog.SettingDialog;
import com.eastcompeace.printer.api.Printer;
import com.example.jni_sendbroaddemo.Sendbroad;
import com.example.usbcciddrv.jniUsbCcid;
import com.landicorp.android.mispos.MisPosClient;
import com.landicorp.android.mispos.MisPosRequest;
import com.landicorp.android.mispos.MisPosResponse;
import com.printer.sdk.api.PrinterType;

/**
 * 首页界面
 * 
 * @author Ljj 上午10:06:39
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	private TradeService tradeService;
	private RelativeLayout rl1;
	private RelativeLayout rl2;
	private RelativeLayout rl3;
	private RelativeLayout rl4;
	private RelativeLayout rl5;
	private RelativeLayout rl6;
	private RelativeLayout rl7;
	private RelativeLayout rl8;
	private RelativeLayout rl9;
	private RelativeLayout rl10;
	private RelativeLayout rl11;
	private RelativeLayout rl12;
	private RelativeLayout rl_vein_finger;
	private RelativeLayout rl_verify_finger;
	private TextView tv_version;
	private TextView tv_eid;
	private TextView tv_nodata;
	private TextView tv_rfsam_status;
	private TextView tv_sys_time;
	private TextView tv_client_status;
	private TextView tv_network_status;

	private GetDataTask getDataTask;
	private Trade mTrade = new Trade();

	public int n = 0;
	public static int flag;
	String rfsam = "";
	String psam = "";
	String lastRfsam = "";// 上次读取成功的卡号
	String lastPsam = "";// 上次读取成功的卡号

	private MisPosClient bank = null;
	private String keyString = "";

	private boolean isConnected = false;// 是否连接到打印机
	public static Printer mPrinter;
	private UsbDevice mUsbDevice;
	private boolean is_58mm = true;
	private boolean is_thermal = true;
	private PendingIntent pendingIntent;
	private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

	private MySoClient mySoClient;
	private SoClient client;

	private LoadingDialog dialog;
	private AlertDialog.Builder builder;

	private Sendbroad sendbroad = new Sendbroad();

	String eid = "";// 一体机唯一标识
	private LocalDataFactory localDataFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_main);
		initView();
		initNewMedicine();  //增加一些自己的药品
		mySoClient = MySoClient.newInstance();
		client = mySoClient.getClient();
		bank = MyBank.newInstance().getBank();
		localDataFactory = LocalDataFactory.newInstance(this);
		localDataFactory.putInt(LocalDataFactory.RFSAM_STATUS, 0);// 初始化rfsam状态，为未知
		tradeService = TradeService.getInstance(this);
		initJNI();
		startService();
		changeSys();
		new Thread() {
			public void run() {
				readEid();
				clearLog();
				checkSoCopyLib();
			}

		}.start();
	}

	private void initNewMedicine() {
		// TODO Auto-generated method stub
		 // 字符列表
        List<String> list = new ArrayList<String>();
        // 文件路径
//        String filePath = "file:///android_assets/new-medicine.xls";
        String filePath = "new-medicine.xls";
        File file = new File(filePath);
        
        System.out.println(file.exists());
     // 输入流
        InputStream is = null;
        // Excel工作簿
        Workbook workbook = null;
        try {
            // 加载Excel文件
        	is = this.getResources().getAssets().open(filePath);
//            is = new FileInputStream(filePath);
            // 获取workbook
            workbook = Workbook.getWorkbook(is);
        } catch (Exception e) {
        	
        }
        List<Medicine> medicineList = new ArrayList<Medicine>();
        for (int n=1; n<=5; n++){
        	Sheet sheet = workbook.getSheet(n);// 这里只取得第一个sheet的值，默认从0开始
	        Log.i("jxi",sheet.getColumns()+"");// 查看sheet的列
	        Log.i("jxi",sheet.getRows()+"");// 查看sheet的行
	        
	        Cell cell = null;// 单个单元格
	        // 开始循环，取得cell里的内容，按具体类型来取
	        // 这里只取String类型
	        Medicine medicine;
	        StringBuffer sb = new StringBuffer();
            for (int i=1;i<sheet.getRows();i++){
	            medicine = new Medicine();
	            String bar_code = sheet.getCell(0,i).getContents();
	            while (bar_code.length()<13){
	            	bar_code = "0"+bar_code;
	            }
	            medicine.setId(bar_code);
	            medicine.setBar_code(bar_code);    
	//            medicine.setOriginal_price(Integer.parseInt(sheet.getCell(2,i).getContents()));
	            medicine.setOriginal_price(0);
	            medicine.setName(sheet.getCell(1,i).getContents());
	            medicine.setTrade_name(sheet.getCell(1,i).getContents());
	            medicine.setProduction_unit("-");
	            medicine.setDosage_form("");
	            medicine.setRecipe_category(0);
	            medicine.setSocial_category(0);
	            medicine.setSpecification("");
	            medicineList.add(medicine);
	//            list.add(sb.toString());//将每行的字符串用一个String类型的集合保存。
            }
        }
        
        
        workbook.close();// 关闭工作簿
        MedicineService medicineService = MedicineService.getInstance(this);
        medicineService.saveMedicineLists(medicineList);
	}

	/**
	 * 判断so替换情况
	 */
	private void checkSoCopyLib() {
		boolean soCopyLib = localDataFactory.getBoolean(
				LocalDataFactory.SOCOPYLIB, false);
		if (!soCopyLib) {
			String systemSupplierSoName = AppUtils.getSystemSupplierSoName();
			if (!"".equals(systemSupplierSoName)) {
				AppUtils.soCopyLib(MainActivity.this, systemSupplierSoName);
			}
		} else {
			MyLog.i("MainActivity", "checkSoCopyLib ok");
		}

		// 判断是否存在root权限
		boolean execCmdResult = AppUtils.execCmdResult("pm disable");
		MyLog.i("MainActivity", "pm disable:" + execCmdResult);
		// 修改设备是否为root
		if (!execCmdResult) {
			localDataFactory.putBoolean(LocalDataFactory.SOCOPYLIB, true);
			mHandler2.sendEmptyMessage(40);
		}
	}

	/**
	 * 清理日志
	 */
	private void clearLog() {
		try {
			MyLog.delFile();
		} catch (Exception e) {
			MyLog.e("-----", "delete log error");
		}
	}

	/**
	 * 修改配置文件
	 */
	@SuppressLint("SdCardPath")
	private void changeSys() {
		new Thread() {
			public void run() {
				try {
					String fileName = "/mnt/sdcard/bank/SysCfg.dat";
					File file = new File(fileName);
					if (file.exists()) {
						String sysString = FileUtils
								.readFileSdcardFile(fileName);
						if (sysString.contains("USERNO")) {
							if (!sysString.contains("120.25.210.39")) {
								MyLog.i("MainActivity", "write NewBankSys ");
								String userno = sysString.split("USERNO=")[1]
										.split("\n")[0];
								String terminal = sysString.split("TERMINAL=")[1]
										.split("\n")[0];
								String usercname = sysString
										.split("USERCNAME=")[1].split("\n")[0];
								String simplename = sysString
										.split("SIMPLENAME=")[1].split("\n")[0];
								String newBankSys = FileUtils
										.getNewBankSys(userno, terminal,
												usercname, simplename);
								FileUtils.writeFileSdcardFile(fileName,
										newBankSys);
							}
							MyByteUtils.getSysCfg(false);
						} else {
							MyLog.i("MainActivity", "USERNO is not exist");
						}

					}
					File pos_key = new File(
							"/mnt/sdcard/bank/certs/pos_key.pem");
					if (!pos_key.exists()) {
						FileUtils.copyAssetsFile(MainActivity.this,
								"pos_key.txt", "/mnt/sdcard/bank/certs",
								"pos_key.pem");
					}
					File rootca = new File("/mnt/sdcard/bank/certs/rootca.pem");
					if (!rootca.exists()) {
						FileUtils.copyAssetsFile(MainActivity.this,
								"rootca.txt", "/mnt/sdcard/bank/certs",
								"rootca.pem");
					}
					File pos = new File("/mnt/sdcard/bank/certs/pos.pem");
					if (!pos.exists()) {
						FileUtils.copyAssetsFile(MainActivity.this, "pos.txt",
								"/mnt/sdcard/bank/certs", "pos.pem");
					}

				} catch (Exception e) {
					MyLog.e("MainActivity", "changeSys  error" + e.getMessage());
				}

			};
		}.start();
	}

	/**
	 * 读取eid，先获取本地保存的，不匹配读取设备
	 */
	private void readEid() {
		String eid_old = localDataFactory.getString(LocalDataFactory.WHOLE_EID,
				"");
		eid = eid_old;
		int i = 0;
		if (!startWith(eid_old)) {
			sendbroad.openNumber();
			while (++i < 10) {
				String eid_now = sendbroad.readNumber(10);
				if (eid_now!=null) {
					eid = eid_now.trim();
					if (startWith(eid)) {
						break;
					}
				}
			}
			saveEid(eid);
		}
		if (i == 10) {
			Bundle bundle = new Bundle();
			bundle.putString("eid", eid);
			Message message = new Message();
			message.what = 31;
			message.setData(bundle);
			mHandler2.sendMessage(message);
		} else {
//			 eid = "ST371616091165";
//            eid = "ST371616091013";
			String m_eid = eid.substring(eid.length() - 8, eid.length());
			localDataFactory.putString(LocalDataFactory.EID, m_eid);
			Bundle bundle = new Bundle();
			bundle.putString("eid", eid);
			Message message = new Message();
			message.what = 30;
			message.setData(bundle);
			mHandler2.sendMessage(message);
			// 定时确认网络状态
			timer_ping.schedule(task_ping, 0, loop_ping);
			timer_check_wlan.schedule(task_check_wlan, loop_check_wlan,
					loop_check_wlan);
		}
	}

	/**
	 * 判断网络状态
	 */
	private int loop_check_wlan = 30000;
	Timer timer_check_wlan = new Timer();
	TimerTask task_check_wlan = new TimerTask() {

		@Override
		public void run() {
			int netWorkErrorType = NetWorkUtil
					.getNetWorkErrorType(MainActivity.this);
			switch (netWorkErrorType) {
			case 0:
				sendNetWorkStatus(38, "");
				timer_check_wlan.cancel();
				break;
			case 1:
				sendNetWorkStatus(38, "SIM卡状态异常，请确认卡是否插好或尝试重新插入");
				break;
			case 2:
				sendNetWorkStatus(38, "网络状态异常，请检查移动网络是否启用");
				break;
			case 3:
				sendNetWorkStatus(38, "无法识别SIM类型，请检查APN");
				break;

			default:
				break;
			}

		}
	};

	/**
	 * ping网络
	 */
	private int max_loop_ping = 180000;
	private int loop = 1;
	private int loop_ping = 5000;
	Timer timer_ping = new Timer();
	ReschedulableTimerTask task_ping = new ReschedulableTimerTask() {

		@Override
		public void run() {
			if (loop * loop_ping >= max_loop_ping) {
				// 时间大于3分钟,修改任务周期
				loop = 0;
				task_ping.setPeriod(60000);
			}
			loop++;
			String host = localDataFactory.getString(LocalDataFactory.HOST, "");
			if ("".equals(host)) {
				host = Constance.host;
			}
			// 判断网络状态
			int result = NetWorkUtil.pingUrl(host);
			if (result == 0) {
				// 添加白名单地址
				// AppUtils.addWhiteList();
				mySoClient.run();
				timer_ping.cancel();
				sendNetWorkStatus(37, "tcp网络正常");
			} else {
				sendNetWorkStatus(37, "tcp网络断开");
			}
		}

	};

	/**
	 * 提示网络状态
	 */
	private void sendNetWorkStatus(int what, String networkStatus) {
		Bundle bundle = new Bundle();
		bundle.putString("networkStatus", networkStatus);
		Message message = new Message();
		message.what = what;
		message.setData(bundle);
		mHandler2.sendMessage(message);
	}

	/**
	 * 打开喂狗及授权服务
	 */
	private void startService() {
		MyApplicationLike.authService.setAction("AUTH");
		startService(MyApplicationLike.authService);
		MyApplicationLike.feedDogService.setAction("FEEDDOG");
		startService(MyApplicationLike.feedDogService);
	}

	/**
	 * 关闭喂狗及授权服务
	 */
	public void stopService() {

		stopService(MyApplicationLike.authService);
		stopService(MyApplicationLike.feedDogService);
	}

	private void saveEid(String whole_eid) {
		localDataFactory.putString(LocalDataFactory.WHOLE_EID, whole_eid);
		localDataFactory.putInt(LocalDataFactory.TIME, 2);
	}

	/**
	 * 判断eid
	 * 
	 * @param eid_old
	 * @return
	 */
	private boolean startWith(String eid_old) {
		int length = eid_old.length();
		if (length > 8) {
			if (Pattern.compile("[0-9]*")
					.matcher(eid_old.substring(length - 8, length)).matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 不能操作
	 */
	private void waiting() {
		rl1.setClickable(false);
		rl2.setClickable(false);
		rl3.setClickable(false);
		rl4.setClickable(false);
		rl5.setClickable(false);
		rl6.setClickable(false);
		rl7.setClickable(false);
		rl8.setClickable(false);
		rl9.setClickable(false);
		rl10.setClickable(false);
		rl11.setClickable(false);
		dialog.show();
	}

	/**
	 * 继续操作
	 */
	private void continuation() {
		rl1.setClickable(true);
		rl2.setClickable(true);
		rl3.setClickable(true);
		rl4.setClickable(true);
		rl5.setClickable(true);
		rl6.setClickable(true);
		rl7.setClickable(true);
		rl8.setClickable(true);
		rl9.setClickable(true);
		rl10.setClickable(true);
		rl11.setClickable(true);
		dialog.cancel();
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
				ToastUtil.show(MainActivity.this, "没有正在连接的打印机", 5000);
				break;
			case Printer.Handler_Get_Device_List_Completed:
				if (mUsbDevice != null) {
					initPrinter(mUsbDevice);
				}
				break;
			case Printer.Handler_Get_Device_List_Error:
				ToastUtil.show(MainActivity.this, "打印机连接错误", 5000);
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
	}

	private void initView() {
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl4 = (RelativeLayout) findViewById(R.id.rl4);
		rl5 = (RelativeLayout) findViewById(R.id.rl5);
		rl1.setOnClickListener(this);
		rl2.setOnClickListener(this);
		rl3.setOnClickListener(this);
		rl4.setOnClickListener(this);
		rl5.setOnClickListener(this);
		rl6 = (RelativeLayout) findViewById(R.id.rl6);
		rl7 = (RelativeLayout) findViewById(R.id.rl7);
		rl8 = (RelativeLayout) findViewById(R.id.rl8);
		rl9 = (RelativeLayout) findViewById(R.id.rl9);
		rl10 = (RelativeLayout) findViewById(R.id.rl10);
		rl11 = (RelativeLayout) findViewById(R.id.rl11);
		rl12 = (RelativeLayout) findViewById(R.id.rl12);
		rl_vein_finger = (RelativeLayout) findViewById(R.id.rl_vein_finger);
		rl_verify_finger = (RelativeLayout) findViewById(R.id.rl_verify_finger);
		rl6.setOnClickListener(this);
		rl7.setOnClickListener(this);
		rl8.setOnClickListener(this);
		rl9.setOnClickListener(this);
		rl10.setOnClickListener(this);
		rl11.setOnClickListener(this);
		rl12.setOnClickListener(this);
		rl_vein_finger.setOnClickListener(this);
		rl_verify_finger.setOnClickListener(this);
		dialog = new LoadingDialog(this, "请稍等");
		dialog.setCanceledOnTouchOutside(false);
		builder = new AlertDialog.Builder(MainActivity.this,
				R.style.AlertDialogCustom);
		tv_eid = (TextView) findViewById(R.id.tv_eid);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_nodata = (TextView) findViewById(R.id.tv_nodata);
		tv_rfsam_status = (TextView) findViewById(R.id.tv_rfsam_status);
		String version = VersionUtil.getVersion(this);
		if (version != null) {
			tv_version.setText(version);
		}
		tv_sys_time = (TextView) findViewById(R.id.tv_sys_time);
		tv_client_status = (TextView) findViewById(R.id.tv_client_status);
		tv_network_status = (TextView) findViewById(R.id.tv_network_status);
	}

	/**
	 * 重打印上一张
	 */
	private void bankTrans(final int type) {

		// 开辟线程执行结算动作
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (type == 2) {
						Message message11 = new Message();
						message11.what = 14;
						mHandler2.sendMessage(message11);
						MyByteUtils.deleteFolderFile();

						String outsideMsg = bank.GetOutsideInfo();
						if (outsideMsg==null || outsideMsg.equals(""))
							Toast.makeText(MainActivity.this, "外部认证信息为空", Toast.LENGTH_SHORT).show();
						else {
							String outsideAuth = NewPos.outsideAuth(outsideMsg);
//							keyString = bank
//									._bankTrans("000000001111111104000000000000000000000000000000000000000000"+outsideAuth);
							MisPosRequest request = new MisPosRequest();
							request.posid = "00000000";
							request.operid = "11111111";
							request.trans = "04";
							request.old_trace = "000000";
							request.szRsv = outsideAuth;
							keyString = bank._bankTrans(NewPos.mergeReqstr(request));
							String result = keyString.substring(0, 2);
							// 处理返回值
							if ("00".equals(result)) {

								// String a = MyByteUtils.ReadTxtFile();
								try {
									// String b = new String(a.getBytes(), "UTF-8");
									mPrinter.init();
									// PrintUtils.printNote(b, mPrinter);

									String year = Calendar.getInstance().get(
											Calendar.YEAR)
											+ "";// 当前年份
									String m_d = MyByteUtils.getByte2String(159,
											169, keyString);
									Date bankTradeTime = TimeUtil.getDateTime2(year
											+ m_d);
									int tradeMoney = Integer.valueOf(MyByteUtils
											.getByte2String(36, 48, keyString));
									int tradeType;
									int leng = keyString.length();
									String oldType = keyString.substring(leng - 2,
											leng);
									if ("00".equals(oldType)) {
										tradeType = 14;
									} else {
										tradeType = 18;
									}

									PrintUtils.printRe(
											MyByteUtils.getByte2String(169, 269,     //商户中文名
													keyString).trim(),
											MyByteUtils.getByte2String(106, 121,     //商户号
													keyString).trim(),
											MyByteUtils.getByte2String(121, 129,     //终端号
													keyString).trim(),
											MyByteUtils.getByte2String(2, 6,         //银行行号
													keyString).trim(),
											MyByteUtils.getByte2String(129, 135,     //批次号
													keyString).trim(),
											MyByteUtils.getByte2String(6, 26,        //卡号
													keyString).trim(),
											MyByteUtils.getByte2String(30, 36,       //有效期
													keyString).trim(),
											MyByteUtils.getByte2String(147, 159,     //参考号
													keyString).trim(), tradeType,
											tradeMoney, bankTradeTime, mPrinter,
											true);

									PrintUtils.printNoteCount("\n\n\n", mPrinter);
									Message message = new Message();
									message.what = 2;
									mHandler2.sendMessage(message);
								} catch (Exception e) {
									e.printStackTrace();
									Message message = new Message();
									message.what = 10;
									mHandler2.sendMessage(message);
								}
							} else {
								Message message = new Message();
								message.what = 4;
								mHandler2.sendMessage(message);
								MyLog.e("MainActivity bankTrans", keyString + "");
							}
						}

					}

				} catch (Exception e) {
					e.printStackTrace();

					Message message = new Message();
					message.what = 7;
					mHandler2.sendMessage(message);
				}

			}
		});
		thread.start();

	}

	/**
	 * 查余
	 */
	private void bankTransCheck() {

		// 开辟线程执行结算动作
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message message11 = new Message();
				message11.what = 13;
				mHandler2.sendMessage(message11);
				MyByteUtils.deleteFolderFile();
//				String readCardInfo = bank.ReadCardInfo();
//				String socialCardNo = readSocialCardNo(readCardInfo);
////				String res = bank._bankTrans("90");
//				if (socialCardNo.startsWith("M")) {
//					// 以M为开头的社保卡
//					checkSocialCardStatus(readCardInfo);
//				} else {
						String outsideMsg = bank.GetOutsideInfo();
//						String insideMsg = bank.GetInsideInfo();
					if (outsideMsg==null || outsideMsg.equals(""))
						Toast.makeText(MainActivity.this, "外部认证信息为空", Toast.LENGTH_SHORT).show();
					else {
						String outsideAuth = NewPos.outsideAuth(outsideMsg);
						MisPosRequest request = new MisPosRequest();
						request.posid = "00000000";
						request.operid = "11111111";
						request.trans = "03";
						request.szRsv = outsideAuth;
//						try {
//							MisPosResponse response = bank.posTrans(request);
//							String result = response.trace;
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						keyString = bank._bankTrans("000000001111111103"+ outsideAuth);
                        try {
//							MisPosResponse response = bank.posTrans(request);
                            keyString = bank._bankTrans(NewPos.mergeReqstr(request));
                            String result = keyString.substring(0, 2);
                            if ("00".equals(result)) {
                                Message message = new Message();
                                message.what = 5;
                                mHandler2.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.what = 6;
                                mHandler2.sendMessage(message);
                                MyLog.e("MainActivity bankTransCheck", keyString + "");
                            }
                        }catch (Exception e){
                            Message message = new Message();
                            message.what = 6;
                            mHandler2.sendMessage(message);
                            MyLog.e("MainActivity bankTransCheck", keyString + "");
                        }

					}
//				}

			}
		});
		thread.start();

	}

	private void checkSocialCardStatus(String cardInfoString) {
		CardInfo cardInfo = new CardInfo();
		CardInfo.setCardInfo(cardInfoString, cardInfo);
		CheckSocialStatusSdBzMessage checkSocialStatusMessage = new CheckSocialStatusSdBzMessage(
				eid.substring(eid.length() - 8, eid.length()),
				cardInfo.getSocial_card_no(), cardInfo.getBank_no().substring(
						0, 6), cardInfo.getId_card_no());
		client.sendMessage(checkSocialStatusMessage,
				new ActivityCallBackListener() {
					@Override
					public void callBack(SoMessage message) {
						MyLog.w("MainActivity", "check status ok\n");
						CheckSocialStatusSdBzRespMessage checkSocialStatusSdBzRespMessage = new CheckSocialStatusSdBzRespMessage(
								message);
						int balanceValue = checkSocialStatusSdBzRespMessage
								.getBalanceValue();
						float balance = FloatCalculator.divide(balanceValue,
								100);
						Message message1 = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("balance", String.valueOf(balance));
						message1.what = 41;
						message1.setData(bundle);
						mHandler2.sendMessage(message1);
					}

					@Override
					public void doTimeOut() {
						// 指令发送失败，启动ping
						MyLog.w("MainActivity", " check status doTimeOut\n");
						mHandler2.sendEmptyMessage(6);
					}
				}, 10);
	}

	/**
	 * 结算
	 */
	private void bankTransRe() {

		// 开辟线程执行结算动作
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Message message11 = new Message();
					message11.what = 12;
					mHandler2.sendMessage(message11);
					MyByteUtils.deleteFolderFile();

//					String outsideMsg = bank.GetOutsideInfo();
//					if (outsideMsg==null || outsideMsg.equals(""))
//						Toast.makeText(MainActivity.this, "外部认证信息为空", Toast.LENGTH_SHORT).show();
//					else {
//						String outsideAuth = NewPos.outsideAuth(outsideMsg);
						keyString = bank._bankTrans("000000001111111106");
						String result = keyString.substring(0, 2);
						if ("00".equals(result)) {
							String a = MyByteUtils.ReadTxtFile();
							try {
								String b = new String(a.getBytes(), "UTF-8");
								mPrinter.init();
								PrintUtils.printNoteCount(b, mPrinter);
								Message message = new Message();
								message.what = 1;
								mHandler2.sendMessage(message);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
								Message message = new Message();
								message.what = 10;
								mHandler2.sendMessage(message);
							}
						} else {
							Message message = new Message();
							message.what = 3;
							mHandler2.sendMessage(message);
							MyLog.e("MainActivity bankTransRe", keyString + "");
						}
//					}
				} catch (Exception e) {
					e.printStackTrace();
					Message message = new Message();
					message.what = 3;
					mHandler2.sendMessage(message);
				}

			}
		});
		thread.start();
	}

	/**
	 * 重打印结算
	 */
	private void bankBalanceRe() {

		// 开辟线程执行重打印结算动作
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Message message11 = new Message();
					message11.what = 15;
					mHandler2.sendMessage(message11);
					MyByteUtils.deleteFolderFile();
					String outsideMsg = bank.GetOutsideInfo();
					if (outsideMsg==null || outsideMsg.equals(""))
						Toast.makeText(MainActivity.this, "外部认证信息为空", Toast.LENGTH_SHORT).show();
					else {
						String outsideAuth = NewPos.outsideAuth(outsideMsg);
						keyString = bank._bankTrans("000000001111111162" + outsideAuth);
						String result = keyString.substring(0, 2);
						if ("00".equals(result)) {
							// 调整入库和打印顺序
							String a = MyByteUtils.ReadTxtFile();
							try {

								String b = new String(a.getBytes(), "UTF-8");
								mPrinter.init();
								PrintUtils.printNoteCount(b, mPrinter);
								Message message = new Message();
								message.what = 16;
								mHandler2.sendMessage(message);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
								Message message = new Message();
								message.what = 17;
								mHandler2.sendMessage(message);
							}
						} else {
							Message message = new Message();
							message.what = 17;
							mHandler2.sendMessage(message);
							MyLog.e("MainActivity bankBalanceRe", keyString + "");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message message = new Message();
					message.what = 17;
					mHandler2.sendMessage(message);
				}

			}
		});
		thread.start();
	}

	/**
	 * 修改密码
	 */
	private void bankResetPassword() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Message message11 = new Message();
					message11.what = 12;
					mHandler2.sendMessage(message11);
					String insideMsg = bank.GetInsideInfo();
					if (insideMsg==null || insideMsg.equals(""))
						Toast.makeText(MainActivity.this, "内部认证信息为空", Toast.LENGTH_SHORT).show();
					else {
						String insideAuth = NewPos.outsideAuth(insideMsg);
						String readCardInfo = bank.ReadCardInfo(insideAuth);
						String socialCardNo = readSocialCardNo(readCardInfo);
						int result = bank.UnblockChangePin("");
						if (result == 0) {
							Message message = new Message();
							message.what = 23;
							Bundle bundle = new Bundle();
							bundle.putString("socialCardNo", socialCardNo);
							message.setData(bundle);
							mHandler2.sendMessage(message);
						} else {
							Message message = new Message();
							message.what = 24;
							mHandler2.sendMessage(message);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					Message message = new Message();
					message.what = 24;
					mHandler2.sendMessage(message);
				}

			}
		});
		thread.start();
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: // 获取告警
				continuation();
				break;
			case 1: // 获取告警
				AlertDialog("结算成功");
				continuation();
				break;
			case 2: // 重打印成功
				continuation();
				AlertDialog("重打印成功");
				break;
			case 3: // 重打印结算单
				AlertDialog("结算失败");
				continuation();
				break;
			case 4:
				AlertDialog("重打印失败");
				continuation();
				break;
			case 5: // 查余
				try {
					String code = keyString.substring(48, 49);
					String a = keyString.substring(49, 61);
					int i = Integer.valueOf(a);
					String b = (float) i / 100 + "";
					if (!"-".equals(code)) {
						AlertDialog("余额为:" + b + "元");
					} else {
						AlertDialog("余额为:-" + b + "元");
					}
					continuation();
				} catch (Exception e) {
					e.printStackTrace();
					AlertDialog("查询余额失败");
					continuation();
				}

				break;
			case 6:// 查余失败
				AlertDialog("查余失败");
				continuation();
				break;
			case 7:
				AlertDialog("重打印异常");
				continuation();
				break;
			case 8:
				AlertDialog("请检查安全模块连接");

				continuation();
				break;
			case 9:
				AlertDialog("获取psam卡,rfsam卡信息失败,请检查设备后重试");
				if (client.isAvtive()) {
					sendAlarmMessage("04",
							eid.substring(eid.length() - 8, eid.length()));
				} else {
					MyLog.e("sendalarm", "client isnot Avtive 04");
				}
				continuation();
				jniUsbCcid.Eb_State(4, 0, 1);
				jniUsbCcid.Eb_State(3, 0, 1);
				break;
			case 10:
				AlertDialog("打印失败");
				continuation();
				break;
			case 11:
				AlertDialog("查余异常");
				continuation();
				break;
			case 12:
				waiting();
				break;
			case 13:
				waiting();
				break;
			case 14:
				waiting();
				break;
			case 15:
				waiting();
				break;
			case 16:
				continuation();
				AlertDialog("重打印结算成功");
				break;
			case 17:
				continuation();
				AlertDialog("重打印结算失败，请重试");
				break;
			case 18:
				continuation();
				AlertDialog("标牌授权失败");
				jniUsbCcid.Eb_State(2, 0, 1);
				break;
			case 19:
				AlertDialog("结算异常");
				continuation();
				break;
			case 20: // 完成操作
				continuation();
				break;
			case 23:
				AlertDialog("修改密码成功");
				continuation();
				Bundle data = msg.getData();
				String socialCardNo = data.getString("socialCardNo");
				if (client.isAvtive()) {
					sendChangePwdMessage(socialCardNo, 1);
				}
				break;
			case 24:
				AlertDialog("修改密码失败");
				continuation();
				break;
			case 30:
				String eid = msg.getData().getString("eid");
				tv_eid.setText("一体机编号:" + eid);
				break;
			case 31:
				String eid_error = msg.getData().getString("eid");
				tv_eid.setText("一体机编号不匹配:" + eid_error + eid_error.length());
				break;
			case 35:
				String content = msg.getData().getString("content");
				tv_rfsam_status.setText(content);
				break;
			case 37:
				String client_status = msg.getData().getString("networkStatus");
				tv_client_status.setText(client_status);
				break;
			case 38:
				String networkStatus = msg.getData().getString("networkStatus");
				tv_network_status.setText(networkStatus);
				break;
			case 40:
				AlertDialog("系统版本过低，软件更新失败，" + "\n" + "请尽快联系客服人员，协助安排人员上门处理"
						+ "\n" + "联系电话：4006461212");
				break;
			case 41: // tcp查余
				String balance = msg.getData().getString("balance");
				AlertDialog("余额为:" + balance + "元");
				continuation();

				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 上传告警信息
	 */
	private void sendAlarmMessage(final String alarmType, String code) {
		try {
			MyLog.i("SendAlarm", "alarmType = " + alarmType + "code =" + code);
			MyApplicationLike.stopTimeConsumingService(false);
			client.sendMessage(new AlarmMessage(alarmType, code),
					new ActivityCallBackListener() {

						@Override
						public void doTimeOut() {
							MyLog.e("sendalarm", "timeout");
						}

						@Override
						public void callBack(SoMessage message) {
							MyLog.e("sendalarm", "AlarmMessage:" + alarmType);
						}
					});
		} catch (Exception e) {
			MyLog.e("sendalarm", "Exception");
		}

	}

	/**
	 * 上传修改密码信息
	 */
	private void sendChangePwdMessage(String socialCardNo, int desc) {
		MyApplicationLike.stopTimeConsumingService(false);
		client.sendMessage(new SummitCardLogMessage(socialCardNo, desc),
				new ActivityCallBackListener() {

					@Override
					public void doTimeOut() {
						MyLog.e("changepwd", "timeout");
					}

					@Override
					public void callBack(SoMessage message) {
						MyLog.e("changepwd", "xiugaimima");
					}
				});
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
		int v_id = v.getId();
		if (v_id == R.id.rl6) {
			intent.setClass(MainActivity.this, PolicyWordActivity.class);
			startActivity(intent);
		} else if (v_id == R.id.rl7) {
			intent.setClass(MainActivity.this, NewConsumeActivity.class);
			startActivity(intent);
		} else if (v_id == R.id.rl8) {
			intent.setClass(MainActivity.this, ChartActivity.class);
			startActivity(intent);
		} else if (v_id == R.id.rl9) {
			showSettingDialog();
		} else if (v_id == R.id.rl10) {
			intent.setClass(MainActivity.this, AppStoreActivity.class);
			startActivity(intent);
		} else {
			int rfsam_status = localDataFactory.getInt(
					LocalDataFactory.RFSAM_STATUS, 0);
			if (rfsam_status == 2) {
				if ("".equals(tv_rfsam_status.getText().toString())) {
					SendSmsRequest();
				}
				AlertDialog("设备已停用");
				//lhy调试屏蔽
//			} else if (rfsam_status == 0) {
//				AlertDialog("后台授权状态正在获取，请稍等");
//				checkRfsamStatus();
			} else {
				switch (v_id) {
				case R.id.rl1:
//					if ("设备已绑定".equals(tv_nodata.getText().toString())) {
//						waiting();
//						if (moveFlag != 2) {
//							if (getAndSave()) {
//								if (jniUsbCcid.GetAuthorization() == 1) {
//									intent.setClass(MainActivity.this,
//											PayActivity.class);
//									startActivity(intent);
//								} else {
//									Message message1 = new Message();
//									message1.what = 18;
//									mHandler2.sendMessage(message1);
//								}
//							} else {
//								Message message = new Message();
//								message.what = 9;
//								mHandler2.sendMessage(message);
//							}
//						} else {
//							Message message = new Message();
//							message.what = 8;
//							mHandler2.sendMessage(message);
//						}
//						continuation();
//					} else {
//						AlertDialog("设备绑定未获取或未绑定");
//					}
					intent.setClass(MainActivity.this,
							PayActivity.class);
					startActivity(intent);
					break;
				case R.id.rl2:
					if ("设备已绑定".equals(tv_nodata.getText().toString())) {
						waiting();
						if (moveFlag != 2) {
							if (getAndSave()) {
								if (jniUsbCcid.GetAuthorization() == 1) {
									intent.setClass(MainActivity.this,
											RevokeActivity.class);
									startActivity(intent);
								} else {
									Message message1 = new Message();
									message1.what = 18;
									mHandler2.sendMessage(message1);
								}
							} else {
								Message message = new Message();
								message.what = 9;
								mHandler2.sendMessage(message);
							}
						} else {
							Message message = new Message();
							message.what = 8;
							mHandler2.sendMessage(message);
						}
						continuation();
					} else {
						AlertDialog("设备绑定未获取或未绑定");
					}
					break;
				case R.id.rl3:
					if ("设备已绑定".equals(tv_nodata.getText().toString())) {
						waiting();
						if (moveFlag != 2) {
							if (getAndSave()) {
								if (jniUsbCcid.GetAuthorization() == 1) {
									intent.setClass(MainActivity.this,
											ReturnActivity.class);
									startActivity(intent);
								} else {
									Message message1 = new Message();
									message1.what = 18;
									mHandler2.sendMessage(message1);
								}
							} else {
								Message message = new Message();
								message.what = 9;
								mHandler2.sendMessage(message);
							}
						} else {
							Message message = new Message();
							message.what = 8;
							mHandler2.sendMessage(message);
						}
						continuation();
					} else {
						AlertDialog("设备绑定未获取或未绑定");
					}
					break;
				case R.id.rl4:
					if (jniUsbCcid.GetAuthorization() == 1) {
						bankTransCheck();
					} else {
						Message message1 = new Message();
						message1.what = 18;
						mHandler2.sendMessage(message1);
					}

					break;
				case R.id.rl5:

					if (jniUsbCcid.GetAuthorization() == 1) {
						if (isConnected) {
							bankTransRe();
						} else {
							showConnectDialog();
						}
					} else {
						Message message1 = new Message();
						message1.what = 18;
						mHandler2.sendMessage(message1);
					}

					break;
				case R.id.rl11:
					showAlertDialog();
					break;
				case R.id.rl12:
					if (jniUsbCcid.GetAuthorization() == 1) {
						bankResetPassword();
					} else {
						Message message1 = new Message();
						message1.what = 18;
						mHandler2.sendMessage(message1);
					}
					break;
				case R.id.rl_verify_finger:
					intent.setClass(MainActivity.this,
							VerifyFingerActivity.class);
					startActivity(intent);
					break;
				case R.id.rl_vein_finger:
					intent.setClass(MainActivity.this, VeinFingerActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		}

	}

	/**
	 * 修改密码前先读卡获取社保卡号
	 * 
	 * @return 社保卡号
	 */
	private String readSocialCardNo(String readCardInfo) {
		String socialCardNo;
		try {
			socialCardNo = readCardInfo.split("~nsbkkh~t")[1].split("~n")[0];// 社保卡号
		} catch (Exception e) {
			socialCardNo = "000000000";
			e.printStackTrace();
		}
		return socialCardNo;
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

	public void startGetDataTask(String key) {
		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute(key);
		}
	}

	public class GetDataTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				tradeService.saveTrade(mTrade);
			} catch (Exception e) {
				getDataTask = null;
				MyLog.w("MainActivity", e.getMessage() + "");
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
			if (result) {
			} else {
			}
		}

	}

	/**
	 * 初始化JNI
	 */
	private void initJNI() {
		flag = jniUsbCcid.InitDev();
		jniUsbCcid.PowerOff();
		jniUsbCcid.PowerOn();
		jniUsbCcid.Eb_State(7, 0, 2);
		jniUsbCcid.Eb_State(5, 0, 2);
		jniUsbCcid.Eb_State(6, 0, 2);
		moveTimer.schedule(moveTimerTask, 1, 6000);
	}

	/**
	 * 获取psam,rfsam卡信息并保存到本地数据库
	 */
	private boolean getAndSave() {
		boolean result = true;
		rfsam = jniUsbCcid.GetRfsamCardNo();
		psam = jniUsbCcid.GetPsamCardNo();
		if ("".equals(psam) || "".equals(rfsam) || psam == null
				|| rfsam == null) {
			result = false;
		} else {
			jniUsbCcid.Eb_State(4, 0, 2);
			jniUsbCcid.Eb_State(3, 0, 2);
			localDataFactory.putString(LocalDataFactory.PSAM, psam);
			localDataFactory.putString(LocalDataFactory.RFSAM, rfsam);
			result = true;
		}
		return result;
	}

	private int moveFlag = 0;// 告警标志 0 正常 1移动 2拆除
	private Timer moveTimer = new Timer();
	private MoveTimerTask moveTimerTask = new MoveTimerTask();

	// 移动检测定时器
	public class MoveTimerTask extends TimerTask {

		@Override
		public void run() {
			int tamperAndMove = jniUsbCcid.Alarm_Info();
			if ((tamperAndMove & 0x03) > 1) {
				moveFlag = 2;
				jniUsbCcid.Eb_State(2, 0, 1);
			}
			if ((tamperAndMove & 0x03) == 1) {
				moveFlag = 1;
				sendAlarmMessage("01",
						eid.substring(eid.length() - 8, eid.length()));
				jniUsbCcid.Eb_State(2, 1, 1);
			}
			if ((tamperAndMove & 0x03) == 0 && moveFlag != 0) {
				moveFlag = 0;
				jniUsbCcid.Eb_State(2, 0, 2);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyLog.i("MainActivity", "ondestroy");
		moveTimer.cancel();
		timer_check_wlan.cancel();
		Constance.START_SERVICE = false;
		if (client != null && client.isAvtive()) {
			client.stop();
		}
		MySoClient.mInstance = null;
		MySoClient.closeAlarm();
		unregisterReceiver(mUsbReceiver);
		if (flag == 0) {
			jniUsbCcid.ReleaseDev();
		}
		stopService();
		sendbroad.closeNumber();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	/**
	 * 结算对话框
	 */
	private void showAlertDialog() {

		final AccountsDialog.Builder builder = new AccountsDialog.Builder(this);
		builder.setTitle("请确认是否重打印?");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				bankTrans(2);
			}
		});

		builder.setNegativeButton("否",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.setReButton(new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				bankBalanceRe();
			}
		});
		builder.create().show();

	}

	/**
	 * 管理输入框
	 */
	private void showSettingDialog() {

		final SettingDialog.Builder builder = new SettingDialog.Builder(this);
		builder.setTitle("请输入管理密码");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (!"123456".equals(builder.GetSum())) {
					AlertDialog("密码错误");
					dialog.dismiss();
				} else {
					dialog.dismiss();
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, SettingActivity.class);
					startActivityForResult(intent, 1000);
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

	@Override
	public void getRfsamStatus(int status) {
		super.getRfsamStatus(status);
		if (status == 1) {
			tv_rfsam_status.setText("");
		} else {
			tv_rfsam_status.setText("设备已停用");
			SendSmsRequest();
		}

	};

	private void checkRfsamStatus() {
//		if (client != null && client.isAvtive()) {
		if (client != null) {
			MyApplicationLike.stopTimeConsumingService(false);
			client.sendMessage(new RfsamListMessage(),
					new ActivityCallBackListener() {

						@Override
						public void doTimeOut() {
							MyLog.i("MainActivity", "checkRfsamStatus timeout");
							MyLog.i("PayActivity", "" + client.isAvtive());
							client.stop();
							MySoClient.newInstance().run();

						}

						@Override
						public void callBack(SoMessage message) {
							try {
								MyLog.i("MainActivity",
										"checkRfsamStatus callBack");
								client.sendMessage(new ConfirmMssage(
										SoFollowCommad.更新RFSAM名单, 1),
										new ActivityCallBackListener() {

											@Override
											public void doTimeOut() {
												MyLog.e("MainActivity",
														"ConfirmRfsamList time out");
											}

											@Override
											public void callBack(
													SoMessage message) {
												MyLog.i("MainActivity",
														"ConfirmRfsamList");
											}
										});
								RfsamListRespMessage rfsamListRespMessage = new RfsamListRespMessage(
										message);
								int rfsamStatus = rfsamListRespMessage
										.getRfsamStatus();
								localDataFactory.putInt(
										LocalDataFactory.RFSAM_STATUS,
										rfsamStatus);
								Intent intent = new Intent();
								intent.setAction(Constance.RFSAM_STATUS);
								intent.putExtra("rfsam_status", rfsamStatus);
								MyApplicationLike.getContext()
										.sendBroadcast(intent);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}

					});
		}
	}

	private void SendSmsRequest() {
		MyLog.i("MainActivity", "SendSmsRequest");
		if (client != null && client.isAvtive()) {
			try {
				MyApplicationLike.stopTimeConsumingService(false);
				client.sendMessage(new SmsInfoMessage(),
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {
								MyLog.i("MainActivity",
										"SmsInfoMessage doTimeOut");
							}

							@Override
							public void callBack(SoMessage message) {
								MyLog.i("MainActivity",
										"SendSmsRequest callBack");
								SmsInfoRespMessage smsInfoRespMessage = new SmsInfoRespMessage(
										message);
								String contacts = smsInfoRespMessage
										.getContacts();
								String content_way = smsInfoRespMessage
										.getContent_way();
								String reason = smsInfoRespMessage.getReason();
								String content = "设备已停用\n" + "联系人：" + contacts
										+ "\n联系方式：" + content_way + "\n原因："
										+ reason;
								smsInfoRespMessage.getContent_way();
								Message msg = new Message();
								msg.what = 35;
								Bundle bundle = new Bundle();
								bundle.putString("content", content);
								msg.setData(bundle);

								mHandler2.sendMessage(msg);
							}
						}, 10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ToastUtil.show(MainActivity.this, "网络已断开", 3000);
		}
	}

	@Override
	public void Nodata(boolean nodata) {
		if (nodata) {
			tv_nodata.setText("设备未绑定");
			tv_rfsam_status.setText("");
		} else {
			tv_nodata.setText("设备已绑定");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000 && resultCode == 1000) {
			finish();
		} else {
			client = mySoClient.getClient();
		}
	}

	@Override
	public void setTime(String time) {
		tv_sys_time.setText(time);
	}

	@Override
	public void setPingStatus(boolean status) {
		super.setPingStatus(status);
		if (status) {
			tv_client_status.setText("tcp网络正常");
		} else {
			tv_client_status.setText("tcp网络断开");
		}

	}

}
