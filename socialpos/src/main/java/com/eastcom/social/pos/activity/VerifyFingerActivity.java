package com.eastcom.social.pos.activity;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.service.DataFactory;
import com.eastcom.social.pos.util.ToastUtil;
import com.xgzx.veinmanager.VeinApi;
import com.xgzx.veinmanager.bluetooth.VeinConstant;

public class VerifyFingerActivity extends BaseActivity implements
		OnClickListener {

	private Button btn_verify;
	private TextView tv_state, tv_socialid, tv_name;
	private ImageView iv_icon;

	private RelativeLayout rl4;
	private RelativeLayout rl3;
	private RelativeLayout rl2;
	private RelativeLayout rl1;

	public int SUCCESS_OR_FAIL = 0;
	public int connRet = 0;
	private boolean is_veinFinger = false;
	private byte[] dataBufVerify;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_finger);
		veinApi = new VeinApi(VerifyFingerActivity.this);
		initView();
		lock = new Object();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		threadFlag = false;
		connRet = 0;
		veinApi.CloseVein();
		VeinApi.VeinLibHandle = 0;
	}

	private void initView() {
		btn_verify = (Button) findViewById(R.id.btn_verify);
		tv_state = (TextView) findViewById(R.id.tv_state);
		tv_socialid = (TextView) findViewById(R.id.tv_socialid);
		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		btn_verify.setOnClickListener(this);

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
		case R.id.btn_verify:
			openDev();
			break;
		case R.id.rl4:
			intent.setClass(VerifyFingerActivity.this, PolicyWordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl3:
			intent.setClass(VerifyFingerActivity.this, ChartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl2:
			intent.setClass(VerifyFingerActivity.this, NewConsumeActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl1:
			finish();
			break;
		}
	}

	/**
	 * 验证指纹
	 */
	private void verify() {
		if (is_veinFinger) {
			operateType = 0;
			startGetDataTask();
		} else {
			ToastUtil.show(VerifyFingerActivity.this, "请先录入指纹", 5000);
		}
	}

	/**
	 * 根据音频资源播放
	 * 
	 * @param resId
	 */
	private void playSound(final int resId) {
		try {
			soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
			soundPool.load(VerifyFingerActivity.this, resId, 1);
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					soundPool.play(1, 1, 1, 0, 0, 1);
					if (resId == R.raw.verify_fail) {
						try {
							Thread.sleep(2000);
							openDev();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开指静脉设备
	 */
	private void openDev() {
		iv_icon.setImageResource(R.drawable.ic_icon);
		tv_name.setText("姓名：");
		tv_socialid.setText("社保卡号：");
		threadFlag = true;
		is_veinFinger = false;
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
		}else {
			ToastUtil.show(VerifyFingerActivity.this, "指静脉设备打开失败,请检查", 5000);
		}
	}

	/**
	 * 关闭设备
	 */
	private void closeDev() {
		int ret = VeinApi.XGZXCloseVeinDev(VeinApi.DevHandle, 0);
		if (ret == 0) {
			Message Msg = mHandler.obtainMessage(3);
			mHandler.sendMessage(Msg);
			myDetectThread = null;
		} else {
		}
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
			} else if (msg.what == 3) {
				tv_state.setText("采样成功");
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
						Message Msg = mHandler.obtainMessage(1);
						mHandler.sendMessage(Msg);
						byte[] data = new byte[2048];
						int ret = veinApi.XGZXGetChara(VeinApi.DevHandle, 0,
								data);
						if (ret >= 0) {
							Message message = new Message();
							message.what = 2;
							Bundle bundle = new Bundle();
							message.setData(bundle);
							mHandler.sendMessage(message);
							dataBufVerify = new byte[ret];
							System.arraycopy(data, 0, dataBufVerify, 0, ret);
							bufVerify = Base64.encodeToString(dataBufVerify,
									Base64.DEFAULT);
						}
						closeDev();
						threadFlag = false;
						connRet = 0;
						is_veinFinger = true;
						verify();
					} else if (Finger == 0) {
						Message Msg = mHandler.obtainMessage(0);
						mHandler.sendMessage(Msg);
					}
				}

			}
		}
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			threadFlag = false;
			connRet = 0;
			veinApi.CloseVein();
			VeinApi.VeinLibHandle = 0;
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void Nodata(boolean nodata) {

	}

	private int operateType = 0;
	private GetDataTask getDataTask;
	private String bufVerify;

	public void startGetDataTask() {

		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute();
		}
	}

	private String sbCardId = "";

	public class GetDataTask extends AsyncTask<Void, Integer, Boolean> {
		private String message = "";

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				if (operateType == 0) {
					message = DataFactory.verify(bufVerify,
							dataBufVerify.length, "0");
				} else if (operateType == 1) {
						message = DataFactory.getImg(sbCardId);
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
						JSONObject json = new JSONObject(message);
						String resultString = json.getString("result");
						if (resultString.equals("0")) {
							playSound(R.raw.verify_ok);
							sbCardId = json.getString("sbCardId");
							String sbnName = json.getString("sbnName");
							bindMessage(sbCardId, sbnName);
							operateType =1;
							startGetDataTask();
						} else {
							playSound(R.raw.verify_fail);
							clearMessage();
						}
					} else if (operateType == 1) {
						try {
							JSONObject json = new JSONObject(message);
							String basestr = json.getString("basestr");
							Bitmap bitmap = stringtoBitmap(basestr.split(",")[1]);
							iv_icon.setImageBitmap(bitmap);
						} catch (Exception e) {
							ToastUtil.show(VerifyFingerActivity.this, "获取图片失败", 5000);
						}
						
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Base64字符串转图片
	 * 
	 * @param string
	 * @return
	 */
	public Bitmap stringtoBitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 匹配社保卡信息
	 * 
	 * @param result
	 */
	public void bindMessage(String sbCardId, String sbnName) {
		tv_state.setText("验证成功");
		tv_name.setText("社保卡号:" + sbCardId);
		tv_socialid.setText("姓名:" + sbnName);


	}

	/**
	 * 清空社保卡信息
	 */
	public void clearMessage() {
		tv_state.setText("欢迎光临");
		tv_name.setText("社保卡号:");
		tv_socialid.setText("姓名:");
	}

	@Override
	public void setTime(String time) {
		// TODO Auto-generated method stub
		
	}
}
