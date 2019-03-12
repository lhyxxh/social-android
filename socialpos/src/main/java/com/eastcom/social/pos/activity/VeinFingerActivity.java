package com.eastcom.social.pos.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.entity.CardInfo;
import com.eastcom.social.pos.listener.MyBank;
import com.eastcom.social.pos.mispos.NewPos;
import com.eastcom.social.pos.service.DataFactory;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.ToastUtil;
import com.landicorp.android.mispos.MisPosClient;
import com.xgzx.veinmanager.VeinApi;
import com.xgzx.veinmanager.bluetooth.VeinConstant;

public class VeinFingerActivity extends BaseActivity implements OnClickListener {

	private Button btn_read;
	private ImageView iv_icon;
	private TextView tv_state, tv_name, tv_socialid;

	private RelativeLayout rl4;
	private RelativeLayout rl3;
	private RelativeLayout rl2;
	private RelativeLayout rl1;

	public int SUCCESS_OR_FAIL = 0;
	public int connRet = 0;
	private byte[] dataBuf;
	private VeinApi veinApi;
	private boolean threadFlag = false;// 循环检测验证线程
	public static final boolean USE_BT_COMM = false;// 是否使用蓝牙通讯模式，如果是蓝牙，则指静脉设备需要连接蓝牙透明串口设备
	private Object lock;
	public static boolean OnLine = false;
	private MyHandle mHandler;
	private MyDetectThread myDetectThread;
	private MyCountDownTimer myCountDownTimer;
	private MyCountDownTimer inputFingerCountDownTimer;
	private SoundPool soundPool;

	private MisPosClient bank = null;

	private String CARDID = "";
	private String SOCIALID = "";
	private String picturePath = "";
	private String NAME = "";

	private LocalDataFactory localDataFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vein_finger);
		veinApi = new VeinApi(VeinFingerActivity.this);
		bank = MyBank.newInstance().getBank();
		localDataFactory = LocalDataFactory
				.newInstance(VeinFingerActivity.this);
		initView();
		lock = new Object();
	}

	private void initView() {
		btn_read = (Button) findViewById(R.id.btn_read);
		tv_state = (TextView) findViewById(R.id.tv_state);
		tv_socialid = (TextView) findViewById(R.id.tv_socialid);
		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);

		btn_read.setOnClickListener(this);

		rl4 = (RelativeLayout) findViewById(R.id.rl4);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);

		rl4.setOnClickListener(this);
		rl3.setOnClickListener(this);
		rl2.setOnClickListener(this);
		rl1.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.iv_icon:
			if ("".equals(SOCIALID)) {
				ToastUtil.show(VeinFingerActivity.this, "请先读卡", 5000);
				return;
			}
			seleteFromAlbum();
			break;
		case R.id.btn_read:
			matchPersonMessage();
			break;
		case R.id.rl4:
			intent.setClass(VeinFingerActivity.this, PolicyWordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl3:
			intent.setClass(VeinFingerActivity.this, ChartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl2:
			intent.setClass(VeinFingerActivity.this, NewConsumeActivity.class);
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

	/**
	 * 上传指静脉数据及社保卡数据
	 */
	private void upload() {
		operateType = 0;
		startGetDataTask();
	}

	/**
	 * 打开指静脉设备
	 */
	private void openDev() {

		threadFlag = true;
		VeinApi.UserVeinLib = true;
		SUCCESS_OR_FAIL = veinApi.ConnectDev();
		veinApi.SetSameFingerCheck(1);
		if (SUCCESS_OR_FAIL == VeinApi.XG_ERR_SUCCESS) {
			myDetectThread = new MyDetectThread();
			myCountDownTimer = new MyCountDownTimer(2500, 500);
			inputFingerCountDownTimer = new MyCountDownTimer(
					VeinConstant.FingerTimeOut * 1000 + 500, 500);
			connRet = 1;
			mHandler = new MyHandle();
			Message Msg = mHandler.obtainMessage(-VeinApi.XG_INPUT_FINGER);
			mHandler.sendMessage(Msg);
			myDetectThread.start();
		} else {
			ToastUtil.show(VeinFingerActivity.this, "指静脉设备打开失败,请检查", 5000);
		}
	}

	/**
	 * 根据音频资源播放
	 * 
	 * @param resId
	 */
	private void playSound(int resId) {
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(VeinFingerActivity.this, resId, 1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				soundPool.play(1, 1, 1, 0, 0, 1);
			}
		});
	}

	/**
	 * 根据音频资源播放
	 * 
	 * @param resId
	 */
	private void playSoundAfter(int resId, final int resIdAfter) {
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(VeinFingerActivity.this, resId, 1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				soundPool.play(1, 1, 1, 0, 0, 1);
				try {
					playSound(resIdAfter);
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
	}

	@SuppressLint("HandlerLeak")
	private class MyHandle extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			myCountDownTimer.cancel();
			inputFingerCountDownTimer.cancel();
			if (msg.what == -VeinApi.XG_INPUT_FINGER) {
				tv_state.setText("连接设备成功");
			} else if (msg.what == 0) {
				tv_state.setText("请放手指");
				myCountDownTimer.start();
			} else if (msg.what == 1) {
				tv_state.setText("检测到手指，获取指静脉特征中。");
			} else if (msg.what == 2) {
				tv_state.setText("请输入密码");
			} else if (msg.what == 3) {
				tv_state.setText("采样成功");
			} else if (msg.what == 4) {

			} else {
				tv_state.setText("设备通讯出错，重新连接");
				myCountDownTimer.start();
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				int time = msg.getData().getInt("time");
				tv_state.setText("第" + time + "次获取指静脉特征成功。");
			} else if (msg.what == 2) {
				tv_state.setText("请输入密码");
			} else if (msg.what == 4) {
				Bundle bundle = msg.getData();
				String SOCIALID = bundle.getString("SOCIALID");
				String NAME = bundle.getString("NAME");
				tv_name.setText("姓名：" + NAME);
				tv_socialid.setText("社保卡号：" + SOCIALID);
				openDev();
			} else if (msg.what == 5) {
				tv_name.setText("姓名：");
				tv_socialid.setText("社保卡号：");
				ToastUtil.show(VeinFingerActivity.this, "读卡信息失败", 5000);
			} else if (msg.what == 6) {
				tv_name.setText("姓名：");
				tv_socialid.setText("社保卡号：");
				ToastUtil.show(VeinFingerActivity.this, "未插卡，请重试", 5000);
			} else if (msg.what == 7) {
			} else if (msg.what == 8) {
				playSound(R.raw.input_again);
			} else if (msg.what == 9) {
				tv_state.setText("欢迎光临");
				tv_name.setText("姓名：");
				tv_socialid.setText("社保卡号：");
			} else if (msg.what == 10) {
				tv_state.setText("密码检验失败，请重试");
				tv_name.setText("姓名：");
				tv_socialid.setText("社保卡号：");
			} else {
				tv_state.setText("设备通讯出错，重新连接");
				myCountDownTimer.start();
			}
		}
	};

	class MyDetectThread extends Thread {

		@SuppressWarnings("static-access")
		@Override
		public void run() {
			playSound(R.raw.input_finger);
			threadFlag = false;
			while (true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 检查是否已经连接上了
				if (USE_BT_COMM) {
					if (VeinConstant.bDevConnect == false)
						continue;
				} else {
					if (connRet <= 0)
						continue;
				}

				synchronized (lock) {
					while (threadFlag) {
						try {
							lock.wait(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (OnLine)
						continue; // 有其他操作在跟设备通讯
					int Finger = veinApi.DetectFinger();
					if (Finger == 1) {
						byte[] data = new byte[5 * 2048];
						byte[] EnrollBuf = new byte[5 * 2048];
						int userId = veinApi.GetEmptyId();
						for (int i = 1; i <= 3; i++) {
							int ret = veinApi.XGZXGetChara(VeinApi.DevHandle,
									0, data);
							if (ret >= 0) {

								veinApi.XGZXEnroll(VeinApi.VeinLibHandle,
										userId, data, ret);
								veinApi.XGZXSaveEnrollData(
										VeinApi.VeinLibHandle, userId, 0, null,
										ret);
							}
							if (i != 3) {// 等待时间
								try {
									Message message = new Message();
									message.what = 1;
									Bundle bundle = new Bundle();
									bundle.putInt("time", i);
									message.setData(bundle);
									mHandler2.sendMessage(message);
									playSound(R.raw.input_again);
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							} else {
								playSoundAfter(R.raw.enroll_ok,
										R.raw.upload_photo);
							}
						}
						int bufLength = veinApi.XGZXGetEnrollData(
								VeinApi.VeinLibHandle, EnrollBuf, userId);
						dataBuf = new byte[bufLength];
						System.arraycopy(EnrollBuf, 0, dataBuf, 0, bufLength);
						closeDev();
						// 停止录入，匹配个人信息
						threadFlag = false;
						connRet = 0;
						seleteFromAlbum();
					} else if (Finger == 0) {
						Message Msg = mHandler.obtainMessage(0);
						mHandler.sendMessage(Msg);
					}

				}

			}
		}
	}

	/**
	 * 关闭设备
	 */
	private void closeDev() {
		int ret = VeinApi.XGZXCloseVeinDev(VeinApi.DevHandle, 0);
		if (ret == 0) {
			Message Msg2 = mHandler.obtainMessage(3);
			mHandler.sendMessage(Msg2);
			myDetectThread = null;
		}
	}

	private static final int PHOTO_REQUEST_GALLERY = 2;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {

				Uri selectedImage = data.getData(); // 获取系统返回的照片的Uri
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);// 从系统表中查询指定Uri对应的照片
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex); // 获取照片路径
				cursor.close();
				Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
				iv_icon.setImageBitmap(bitmap);
				MyLog.i("VeinActivity", "getBitMap");
				// 上传信息
				upload();
			}
		}
	}

	/**
	 * 从相册中选择照片
	 */
	private void seleteFromAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/**
	 * 匹配个人信息
	 */
	private void matchPersonMessage() {
		tv_state.setText("请插入社保卡");
		iv_icon.setImageResource(R.drawable.ic_icon);
		tv_name.setText("姓名：");
		tv_socialid.setText("社保卡号：");
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(VeinFingerActivity.this, R.raw.insert_card, 1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				try {
					soundPool.play(1, 1, 1, 0, 0, 1);
					String insideMsg = bank.GetOutsideInfo();
					if (insideMsg==null || insideMsg.equals(""))
						Toast.makeText(VeinFingerActivity.this, "内部认证信息为空", Toast.LENGTH_SHORT).show();
					else {
					    String insideAuth = NewPos.outsideAuth(insideMsg);
						String cardInfoString = bank.ReadCardInfo(insideAuth);
						MyLog.i("readCardInfo", cardInfoString + "");
						CardInfo cardInfo = new CardInfo();
						setCardInfo(cardInfoString, cardInfo);
						CARDID = cardInfo.getId_card_no();
						SOCIALID = cardInfo.getSocial_card_no();
						NAME = cardInfo.getName();
						Message message1 = new Message();
						message1.what = 4;
						Bundle bundle = new Bundle();
						bundle.putString("SOCIALID", SOCIALID);
						bundle.putString("NAME", NAME);
						message1.setData(bundle);
						mHandler2.sendMessage(message1);
					}

				} catch (Exception e) {
					Message message = new Message();
					message.what = 5;
					mHandler2.sendMessage(message);
				}

			}
		});

	}

	private void setCardInfo(String string, CardInfo cardInfo) {
		String social_card_no = string.split("~nsbkkh~t")[1].split("~n")[0];// 社保卡号
		String id_card_no = string.split("~nsfzhm~t")[1].split("~n")[0];// 身份证号
		String name = string.split("~nxm~t")[1].split("~n")[0];// 姓名 ~nxm~t
		String id_code = string.split("~nsbm~t")[1].split("~n")[0];// 识别码
		int sexInt = Integer.valueOf(id_card_no.substring(16, 17));
		String sex = sexInt % 2 == 0 ? "女" : "男";
		String startDate = string.split("~nfkrq~t")[1].split("~n")[0];// 发卡日期
		String endDtae = string.split("~nyxq~t")[1].split("~n")[0];// 有效期
		String bank_no = string.split("~nyhzh~t")[1].split("~n")[0];// 银行账号

		// ~n
		cardInfo.setBank_no(bank_no);
		cardInfo.setSocial_card_no(social_card_no);
		cardInfo.setId_card_no(id_card_no);
		cardInfo.setName(name);
		cardInfo.setId_code(id_code);
		cardInfo.setSex(sex);
		cardInfo.setStartDate(startDate);
		cardInfo.setEndDtae(endDtae);
	}

	// 验证成功失败，提示信息倒计时
	class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long arg0) {
		}

		@Override
		public void onFinish() {
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		threadFlag = false;
		connRet = 0;
		veinApi.CloseVein();
		VeinApi.VeinLibHandle = 0;
	}

	@Override
	public void Nodata(boolean nodata) {

	}

	private int operateType = 0;
	private GetDataTask getDataTask;
	private String buf;

	public void startGetDataTask() {

		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute();
		}
	}

	public class GetDataTask extends AsyncTask<Void, Integer, Boolean> {
		private String message = "";

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				if (operateType == 0) {
					if (!"".equals(buf)) {
						String buf = Base64.encodeToString(dataBuf,
								Base64.DEFAULT);
						int length = buf.length();
						String buf1 = buf.substring(0, length / 2);
						String buf2 = buf.substring(length / 2, length);
						message = DataFactory.save1(buf1, SOCIALID);
						message = DataFactory.save(buf2, dataBuf.length, "0",
								SOCIALID, NAME, CARDID);
					} else {
						return false;
					}

				} else if (operateType == 1) {
					File file = new File(picturePath);
					if (file.exists()) {
						message = DataFactory.uploadImg(SOCIALID, file,
								SOCIALID);
					} else {
						MyLog.e("VeinFingerActivity", "file not exist");
						return false;
					}
				} else if (operateType == 2) {
					String department = localDataFactory.getString(LocalDataFactory.DEPARTMENT, "");
					String org = localDataFactory.getString(LocalDataFactory.ORG, "");
					String addr = localDataFactory.getString(LocalDataFactory.ADDR, "");
					String city = localDataFactory.getString(LocalDataFactory.CITY, "");
					String district = localDataFactory.getString(LocalDataFactory.DISTRICT, "");
					String latitude = localDataFactory.getString(LocalDataFactory.LATITUDE, "");
					String longtitude = localDataFactory.getString(LocalDataFactory.LONGTITUDE, "");
					String street = localDataFactory.getString(LocalDataFactory.STREET, "");
					message = DataFactory.uploadLoaction(SOCIALID, addr, city,
							district, latitude, longtitude, street, CARDID,
							NAME, org, department);
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
			try {
				if (result) {
					if (operateType == 0) {
						operateType = 1;
						startGetDataTask();
					} else if (operateType == 1) {
						if ("0".equals(message)) {
							tv_state.setText("上传成功");
							playSound(R.raw.upload_ok);
							operateType = 2;
							startGetDataTask();
						} else {
							tv_state.setText("上传失败");
							playSound(R.raw.upload_fail);
						}
					} else if (operateType == 2) {
						ToastUtil.show(VeinFingerActivity.this, "信息上传成功", 5000);
					}
				} else {
					MyLog.e("VeinFingerActivity", "error" + operateType);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void setTime(String time) {

	}

}
