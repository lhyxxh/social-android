package com.eastcom.social.pos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.gprsinfo.GprsInfoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.util.MyLog;

/**
 * rfsam卡授权服务
 * 
 * @author eronc
 *
 */
public class GPRSService extends Service {

	private LocationService locationService;


	private SoClient client;
	private String longtitude;
	private String latitude;
	private String country;
	private String city;
	private String district;
	private String street;
	private String addr;

	@Override
	public void onCreate() {
		super.onCreate();
		client = MySoClient.newInstance().getClient();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		locationService = ((MyApplicationLike) getApplication()).locationService;
		locationService = MyApplicationLike.locationService;
		locationService.registerListener(mListener);
		locationService.setLocationOption(locationService
				.getDefaultLocationClientOption());
		locationService.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		locationService.unregisterListener(mListener); // 注销掉监听
		locationService.stop(); // 停止定位服务
		super.onDestroy();
	}



	private void GRPS() {
		GprsInfoMessage gprsInfoMessage = new GprsInfoMessage(longtitude,
				latitude, country, city, district, street, addr);
		System.out.print("timeout");
		if (!"00049E-324".equals(longtitude)) {
			client.sendMessage(gprsInfoMessage, new ActivityCallBackListener() {
				@Override
				public void callBack(SoMessage message) {
					MyLog.i("GPRSService",
							"callBack");
					stopSelf();
				}

				@Override
				public void doTimeOut() {

					MyLog.i("GPRSService",
							"doTimeOut");
					stopSelf();
				}
			});
		}else {
			MyLog.i("GPRSService",
					"00049E-324");
			stopSelf();
		}
		
	}

	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				try {
					String lng = location.getLongitude() + "";
					String lat = location.getLatitude() + "";
					String[] a0 = lng.split("\\.");
					String a1 = ByteUtils
							.addZeroStr(a0[0], 4, true);
					String a2 = ByteUtils.addZeroStr(a0[1], 6,
							false);
					String b1 = ByteUtils
							.addZeroStr(lat.split("\\.")[0], 4, true);
					String b2 = ByteUtils.addZeroStr(lat.split("\\.")[1], 6,
							false);
					longtitude = a1 + a2;
					latitude = b1 + b2;
					country = location.getCountry() + "";
					city = location.getCity() + "";
					district = location.getDistrict() + "";
					street = location.getStreet() + "";
					addr = location.getAddrStr() + "";
					GRPS();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	};

}
