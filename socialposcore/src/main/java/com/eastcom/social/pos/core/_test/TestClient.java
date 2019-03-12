package com.eastcom.social.pos.core._test;

import android.util.Log;

import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.client.SoClientInitializer;
import com.eastcom.social.pos.core.socket.client.mylistener.ClientChannelInitedListener;
import com.eastcom.social.pos.core.socket.client.mylistener.ClientMessageRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.alarm.MyAlarmRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.checkhighblacklist.MyCheckHighBlackListRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.checkpolicy.MyCheckPolicyRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.checkpolicyversion.MyCheckPolicyVersionRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.checksocialstatusgdzh.MyCheckSocialStatusGdzhRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.checksocialstatussdbz.MyCheckSocialStatusSdBzRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.checksocialstatussdly.MyCheckSocialStatusSdLyRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.checkversion.MyCheckVersionRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.confirm.MyConfirmRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.encrypt.MyEncryptRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.gprsinfo.MyGprsInfoRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.healthproduct.MyHealthProductRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.medicineproduct.MyMedicineProductRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.nodata.MyNoDataRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.queryhealth.MyQueryHealthRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.querymedicine.MyQueryMedicineRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.rfsamlist.MyRfsamListRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.rfsamvalidtime.MyRfsamvalidTimeRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.setheartbeat.MySetHeartBeatRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.smsinfo.MySmsInfoRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.socialinfo.MySocialInfoRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.socketurl.MySocketUrlRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.summitcardlog.MySummitCardlogRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.telinfo.MyTelInfoRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.trade.MyTradeRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.tradedetail.MyTradeDetailRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.updatehighblacklist.MyUpdateHighBlackListRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.updatelowblacklist.MyUpdateLowBlackListRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.updatepolicy.MyUpdatePolicyRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.updatesoft.MyUpdateSoftRespListener;
import com.eastcom.social.pos.core.socket.config.ClientConfig;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.listener.ClientRunningListener;
import com.eastcom.social.pos.core.socket.listener.heartbeat.HeartbeatRespListener;
import com.eastcom.social.pos.core.socket.message.SoFollowCommad;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.confirm.ConfirmMssage;
import com.eastcom.social.pos.core.socket.message.heartbeat.HeartbeatRespMessage;
import com.eastcom.social.pos.core.socket.message.summitcardlog.SummitCardLogMessage;
import com.eastcom.social.pos.core.socket.task.CallBackTimeOutTaskManager;

class TestClient {
	private static SoClient client;

	public static void main(String[] args) throws Exception {

		// ClientConfig config = new ClientConfig("120.24.72.174", 2333,
		// "16092693");
		// ClientConfig config = new ClientConfig("120.25.230.37", 11114,
		// "16091013");
		// ClientConfig config = new ClientConfig("120.25.230.37", 11114,
		// "17110108");
		// ClientConfig config = new ClientConfig("192.168.93.70", 11111,
		// "16091013");
		// ClientConfig config = new ClientConfig("183.63.14.204", 19011,
		// "16091095");
		// ClientConfig config = new ClientConfig("61.145.228.155", 8000,
		// "16093024");
		// ClientConfig config = new ClientConfig("61.145.228.155", 8000,
		// "16092421");
		ClientConfig config = new ClientConfig("120.25.230.37", 11115,
				"16093075");
		// ClientConfig config = new ClientConfig("120.25.70.236", 11110,
		// "17080244");
		// ClientConfig config = new ClientConfig("120.25.230.37", 11127,
		// "16091013");
		// ClientConfig config = new ClientConfig("172.16.100.243", 11115,
		// "17091901");

		// ClientConfig config = new ClientConfig("2.2.2.28", 8000, "16092879");

		// final SoClient client = new SoClient(config);
		client = new SoClient(config);
		// 配置通道初始化器
		SoClientInitializer initializer = new SoClientInitializer();
		// 基本通道加载类，必填
		initializer.setChannelInitedListener(new ClientChannelInitedListener(
				client));
		// 标牌心跳频率和超时检测频率，选填，默认为60，10
		initializer.setHeartbeatRate(10);
		// 指令返回值处理通道，选填
		initializer.setMessageRespListener(new ClientMessageRespListener());
		initializer
				.setCheckVersionRespListener(new MyCheckVersionRespListener());
		initializer.setConfirmRespListener(new MyConfirmRespListener());
		initializer
				.setHealthProductRespListener(new MyHealthProductRespListener());
		initializer.setHeartbeatRespListener(new T_MyHearbeatRespListener());
		initializer
				.setMedicineProductRespListener(new MyMedicineProductRespListener());
		initializer.setQueryHealthRespListener(new MyQueryHealthRespListener());
		initializer
				.setQueryMedicineRespListener(new MyQueryMedicineRespListener());
		initializer.setTradeRespListener(new MyTradeRespListener());
		initializer.setTradeDetailRespListener(new MyTradeDetailRespListener());
		initializer.setUpdateSoftRespListener(new MyUpdateSoftRespListener());
		initializer.setRfsamListRespListener(new MyRfsamListRespListener());
		initializer
				.setRfsamValidTimeRespListener(new MyRfsamvalidTimeRespListener());

		initializer.setAlarmRespListener(new MyAlarmRespListener());
		initializer.setSocialInfoRespListener(new MySocialInfoRespListener());
		initializer.setGprsInfoRespListener(new MyGprsInfoRespListener());

		initializer
				.setSummitCardLogRespListener(new MySummitCardlogRespListener());
		initializer.setSocketUrlRespListener(new MySocketUrlRespListener());
		initializer.setEncryptRespListener(new MyEncryptRespListener());
		initializer
				.setSetHeatbeatRespListener(new MySetHeartBeatRespListener());
		initializer
				.setCheckHighBlackListRespListener(new MyCheckHighBlackListRespListener());
		initializer
				.setUpdateHighBlackListRespListener(new MyUpdateHighBlackListRespListener());
		initializer
				.setUpdateLowBlackListRespListener(new MyUpdateLowBlackListRespListener());
		initializer.setSocialInfoRespListener(new MySocialInfoRespListener());
		initializer
				.setCheckSocialStatusSdLyRespListener(new MyCheckSocialStatusSdLyRespListener());
		initializer
				.setCheckSocialStatusSdBzRespListener(new MyCheckSocialStatusSdBzRespListener());
		initializer
				.setCheckSocialStatusGdzhRespListener(new MyCheckSocialStatusGdzhRespListener());
		initializer.setTelInfoRespListener(new MyTelInfoRespListener());
		initializer.setSmsInfoRespListener(new MySmsInfoRespListener());
		initializer.setCheckPolicyRespListener(new MyCheckPolicyRespListener());
		initializer
				.setCheckPolicyVersionRespListener(new MyCheckPolicyVersionRespListener());
		initializer
				.setUpdatePolicyRespListener(new MyUpdatePolicyRespListener());
		initializer.setNoDataRespListener(new MyNoDataRespListener());
		new CallBackTimeOutTaskManager(2, 10, false);
		client.setInitializer(initializer);
		new Thread() {
			public void run() {
				try {
					client.run(new ClientRunningListener() {
						@Override
						public void afterRunning() {
							System.out.println("client--afterRunning");
						}

						@Override
						public void clientReStart() {
							System.out.println("client--restart");
						}

						@Override
						public void clientClosing() {
							System.out.print("client--close");
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		Thread.sleep(2000);

	}

	private static void Test2() {
		SummitCardLogMessage summitCardLogMessage = new SummitCardLogMessage(
				"M12345673", 2);
		client.sendMessage(summitCardLogMessage,
				new ActivityCallBackListener() {

					@Override
					public void doTimeOut() {
						System.out.println("doTimeOut");
					}

					@Override
					public void callBack(SoMessage message) {
						System.out.println("SummitCardLog callBack");
					}
				});

		// final Date date = new Date();
		// final String rfsamNo = "15061509";
		// final String psamNo = "371600810197";
		// int random = 58;
		// FinanceTradeMessage financeTradeMessage = new
		// FinanceTradeMessage("36",
		// "0070", "00", 3, "00002222333344447777", "M00000000",
		// "127282821920", date, "000006", 1200, psamNo, "17060501",
		// rfsamNo, random, 1, 1, random, "02");
		// client.sendMessage(financeTradeMessage, new
		// ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// TradeRespMessage tradeRespMessage = new TradeRespMessage(
		// message);
		// int tradeRandom = tradeRespMessage.getTradeRandom();
		// System.out.println("tradeRandom:" + tradeRandom);
		//
		// String barCode = "888888";
		// String superVisionCode = "";
		// int actualPrice = 1300;
		// int amount = 1;
		// int socialCategory = 0;
		//
		// String tradeType = SoTradeType.医院交易撤单_82字节;
		// int tradeDetailNo = 1;
		//
		// TradeDetailMessage tradeDetailMessage = new TradeDetailMessage(
		// barCode, superVisionCode, actualPrice, amount,
		// socialCategory, rfsamNo, psamNo, tradeRandom, date,
		// tradeType, tradeDetailNo);
		// client.sendMessage(tradeDetailMessage,
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// TradeDetailRespMessage tradeDetailRespMessage = new
		// TradeDetailRespMessage(
		// message);
		// int tradeRandom = tradeDetailRespMessage
		// .getTradeRandom();
		// System.out
		// .println("tradeRandom:" + tradeRandom);
		// }
		// });
		// }
		// });

		// ShTypeMessage shTypeMessage = new ShTypeMessage();
		// client.sendMessage(shTypeMessage, new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// ShTypeRespMessage shTypeRespMessage = new ShTypeRespMessage(
		// message);
		// int shType = shTypeRespMessage.getShType();
		// System.out.println("shType:" + shType);
		// }
		// });

		// UpdateSoftMessage updateSoftMessage = new UpdateSoftMessage(1, 0, 1);
		// client.sendMessage(updateSoftMessage, new ActivityCallBackListener()
		// {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// UpdateSoftRespMessage updateSoftRespMessage = new
		// UpdateSoftRespMessage(
		// message);
		// byte[] body = updateSoftRespMessage.getBody();
		// int dataTotalNo = updateSoftRespMessage.getDataTotalNo();
		// System.out.println("callBack");
		// }
		// });
		// "16091013", "M88888888", "622823",
		// "372301198906192438");
		// 滨州体验
		// String terminalCode = "16091165";
		// String socialCardNo = "M03617209";//
		// String bankBinCode = "622212";
		// String idCardNo = "372330197211166214";
		// // String terminalCode = "16091013";
		// // String socialCardNo = "M03603843";//
		// // String bankBinCode = "622212";
		// // String idCardNo = "372330193306124676";
		// String terminalCode = "16091095";
		// String socialCardNo = "M40888884";
		// String bankBinCode = "622231";
		// String idCardNo = "372330193911160050";
		// String terminalCode = "16091095";
		// String socialCardNo = "M44026368";
		// String bankBinCode = "622823";
		// String idCardNo = "372334195902154128";
		//
		// CheckSocialStatusSdBzMessage checkSocialStatusMessage = new
		// CheckSocialStatusSdBzMessage(
		// terminalCode, socialCardNo, bankBinCode, idCardNo);
		// client.sendMessage(checkSocialStatusMessage,
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.print("timeout");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// CheckSocialStatusSdBzRespMessage checkSocialStatusRespMessage = new
		// CheckSocialStatusSdBzRespMessage(
		// message);
		// int isSuccess = checkSocialStatusRespMessage
		// .getIsSuccess();
		// int balanceValue = checkSocialStatusRespMessage
		// .getBalanceValue();
		// int status = checkSocialStatusRespMessage.getStatus();
		// String socialCardNo = checkSocialStatusRespMessage
		// .getSocialCardNo();
		// String tel = checkSocialStatusRespMessage.getTel();
		// System.out
		// .print("checksocialstatussdbzrespmessage callBack");
		// }
		// }, 10);

		// CheckPolicyMessage checkPolicyMessage = new CheckPolicyMessage();
		// client.sendMessage(checkPolicyMessage, new ActivityCallBackListener()
		// {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// CheckPolicyRespMessageNew checkPolicyRespMessage = new
		// CheckPolicyRespMessageNew(
		// message);
		// String name = checkPolicyRespMessage.getName();
		// int suffix = checkPolicyRespMessage.getSuffix_type();
		// System.out.println("callBack:" + name + "." + suffix);
		// }
		// });

		// SysTimeMessage sysTimeMessage = new SysTimeMessage();
		// client.sendMessage(sysTimeMessage, new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		// System.out.println("callBack");
		// SysTimeRespMessage sysTimeRespMessage = new SysTimeRespMessage(
		// message);
		// String sysTime = sysTimeRespMessage.getSysTime();
		// System.out.println("sysTime:" + sysTime);
		// }
		// }, 10);

		// client.sendMessage(new AlarmMessage("01", "16093155"),
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		// AlarmRespMessage alarmRespMessage = new AlarmRespMessage(
		// message);
		// int i = alarmRespMessage.getRespCommand();
		// System.out.println("callBack");
		// }
		// });

		// client.sendMessage(new RfsamListMessage(),
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage soMessage) {
		// RfsamListRespMessage rfsamListRespMessage = new RfsamListRespMessage(
		// soMessage);
		// System.out.println("RfsamListRespMessage callBack:" +
		// rfsamListRespMessage.getRfsamStatus());
		// }
		// }, 10);

		// "16091013", "M88888888", "622823",
		// "372301198906192438");
		// 滨州体验
		// String terminalCode = "16091013";
		// String socialCardNo = "M03603843";//
		// String bankBinCode = "622212";
		// String idCardNo = "372330193306124676";
		// String terminalCode = "16091095";
		// String socialCardNo = "M40888884";
		// String bankBinCode = "622231";
		// String idCardNo = "372330193911160050";
		// // String terminalCode = "16091095";
		// // String socialCardNo = "E4462989X";
		// // String bankBinCode = "622823";
		// // String idCardNo = "372330197110123311";
		//
		// CheckSocialStatusSdBzMessage checkSocialStatusMessage = new
		// CheckSocialStatusSdBzMessage(
		// terminalCode, socialCardNo, bankBinCode, idCardNo);
		// client.sendMessage(checkSocialStatusMessage,
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.print("timeout");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// CheckSocialStatusSdBzRespMessage checkSocialStatusRespMessage = new
		// CheckSocialStatusSdBzRespMessage(
		// message);
		// int isSuccess = checkSocialStatusRespMessage
		// .getIsSuccess();
		// int balanceValue = checkSocialStatusRespMessage
		// .getBalanceValue();
		// int status = checkSocialStatusRespMessage.getStatus();
		// String socialCardNo = checkSocialStatusRespMessage
		// .getSocialCardNo();
		// String tel = checkSocialStatusRespMessage.getTel();
		// System.out
		// .print("checksocialstatussdbzrespmessage callBack");
		// }
		// }, 10);

		// String socialCardNo = "2086126630";
		// // String socialCardNo = "*00049533";
		// String password = "FFFFFFFFFFFFFFFF";
		// CheckSocialStatusSdLyMessage checkSocialStatusSdLyMessage = new
		// CheckSocialStatusSdLyMessage(
		// socialCardNo, password);
		// client.sendMessage(checkSocialStatusSdLyMessage,
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.print("timeout");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		// CheckSocialStatusSdLyRespMessage checkSocialStatusSdLyRespMessage =
		// new CheckSocialStatusSdLyRespMessage(
		// message);
		// int status = checkSocialStatusSdLyRespMessage
		// .getStatus();
		// System.out
		// .print("CheckSocialStatusSdLyRespMessage callback:"
		// + status);
		// }
		// }, 10);

		// client.sendMessage(new AlarmMessage("10", "16091013"),
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		// AlarmRespMessage alarmRespMessage = new AlarmRespMessage(
		// message);
		// int i = alarmRespMessage.getRespCommand();
		// System.out.println("callBack:" + i);
		// }
		// });

		// String barCode = "6924484713567";
		// QueryMedicineMessage queryMedicineMessage = new QueryMedicineMessage(
		// barCode);
		// client.sendMessage(queryMedicineMessage,
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void callBack(SoMessage message) {
		// try {
		// QueryMedicineRespMessage queryMedicineRespMessage = new
		// QueryMedicineRespMessage(
		// message);
		// if (queryMedicineRespMessage.isExist()) {
		// System.out.print("isExist");
		// } else {
		// System.out.print("isNotExist");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// }
		//
		// @Override
		// public void doTimeOut() {
		// System.out.print("timeout");
		// }
		// }, 10);

		// CheckVersionMessage checkVersionMessage = new CheckVersionMessage(0,
		// 9);
		// client.sendMessage(checkVersionMessage, new
		// ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		// CheckVersionRespMessage checkVersionRespMessage = new
		// CheckVersionRespMessage(
		// message);
		// System.out.println("callBack");
		// }
		// }, 10);

		// client.sendMessage(new SocketUrlMessage(),
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// }
		//
		// @Override
		// public void callBack(SoMessage soMessage) {
		// SocketUrlRespMessage socketUrlRespMessage = new SocketUrlRespMessage(
		// soMessage);
		// String socket_url = socketUrlRespMessage
		// .getUrlString();
		// client.sendMessage(new ConfirmMssage(
		// SoFollowCommad.更新服务器地址, 1));
		// }
		// });

		// EncryptMessage encryptMessage = new EncryptMessage();
		// client.sendMessage(encryptMessage, new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage somessage) {
		// EncryptRespMessage encryptRespMessage = new EncryptRespMessage(
		// somessage);
		// int is_encrypt = encryptRespMessage.getIs_encrypt();
		// System.out.println("callBack:"+is_encrypt);
		// }
		// }, 10);

		// int versionMain = 1;
		// int versionSub = 0;
		// CheckPolicyVersionMessage checkPolicyVersionMessage = new
		// CheckPolicyVersionMessage(
		// versionMain, versionSub);
		// client.sendMessage(checkPolicyVersionMessage,
		// new ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		// CheckPolicyVersionRespMessage checkPolicyVersionRespMessage = new
		// CheckPolicyVersionRespMessage(
		// message);
		// int result = checkPolicyVersionRespMessage.getResult();
		//
		// System.out.println("callBack");
		// }
		// }, 10);

		// UpdatePolicyMessage updatePolicyMessage = new UpdatePolicyMessage(14,
		// 8,
		// 10822);
		// client.sendMessage(updatePolicyMessage, new
		// ActivityCallBackListener() {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// UpdatePolicyRespMessage updatePolicyRespMessage = new
		// UpdatePolicyRespMessage(
		// message);
		// System.out.println("callBack");
		// }
		// });

		// UpdateSoftMessage updateSoftMessage = new UpdateSoftMessage(3, 1, 0);
		// client.sendMessage(updateSoftMessage, new ActivityCallBackListener()
		// {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// UpdateSoftRespMessage updateSoftRespMessage = new
		// UpdateSoftRespMessage(
		// message);
		// byte[] body = updateSoftRespMessage.getBody();
		// int dataTotalNo = updateSoftRespMessage.getDataTotalNo();
		// System.out.println("callBack");
		// }
		// });

		// CheckPolicyMessage checkPolicyMessage = new CheckPolicyMessage();
		// client.sendMessage(checkPolicyMessage, new ActivityCallBackListener()
		// {
		//
		// @Override
		// public void doTimeOut() {
		// System.out.println("doTimeOut");
		// }
		//
		// @Override
		// public void callBack(SoMessage message) {
		//
		// CheckPolicyRespMessage checkPolicyRespMessage = new
		// CheckPolicyRespMessage(
		// message);
		// System.out.println("callBack");
		// }
		// });

	}

	public static class T_MyHearbeatRespListener implements
			HeartbeatRespListener {

		@Override
		public void handlerRespMessage(final HeartbeatRespMessage message) {
			System.out.println("heartbeat resp!" + message.followCommand);
			Test2();
			try {
				switch (message.followCommand) {
				case SoFollowCommad.更新RFSAM名单:
					// System.out.println("rfsamlist resp!");
					// if (client.isAvtive()) {
					// client.sendMessage(new RfsamListMessage(),
					// new ActivityCallBackListener() {
					//
					// @Override
					// public void doTimeOut() {
					// System.out.println("doTimeOut");
					// }
					//
					// @Override
					// public void callBack(SoMessage soMessage) {
					// RfsamListRespMessage rfsamListRespMessage = new
					// RfsamListRespMessage(
					// soMessage);
					// client.sendMessage(new
					// ConfirmMssage(message.followCommand,
					// 1), new ActivityCallBackListener() {
					//
					// @Override
					// public void doTimeOut() {
					// System.out.println("doTimeOut");
					// }
					//
					// @Override
					// public void callBack(SoMessage soMessage) {
					// System.out.println("ConfirmMssage callBack");
					// }
					// }, 10);
					// }
					// }, 10);
					// } else {
					// System.out.println("is not alive");
					// }

					break;
				case SoFollowCommad.更新RFSAM卡默认有效时间:
					System.out.println("rfsamvalid resp!");
					// client.sendMessage(new RfsamValidTimeMessage(),
					// new ActivityCallBackListener() {
					//
					// @Override
					// public void doTimeOut() {
					// // TODO Auto-generated method stub
					// }
					//
					// @Override
					// public void callBack(SoMessage soMessage) {
					// RfsamListRespMessage rfsamListRespMessage = new
					// RfsamListRespMessage(
					// soMessage);
					// client.sendMessage(new ConfirmMssage(
					// message.followCommand, 1));
					// }
					// });
					break;
				case SoFollowCommad.更新交易是否加密配置:
					System.out.println("e resp!");
					//
					// EncryptMessage encryptMessage = new EncryptMessage();
					// client.sendMessage(encryptMessage,
					// new ActivityCallBackListener() {
					//
					// @Override
					// public void doTimeOut() {
					// System.out.println("doTimeOut");
					// }
					//
					// @Override
					// public void callBack(
					// SoMessage somessage) {
					// client.sendMessage(new
					// ConfirmMssage(message.followCommand,
					// 1));
					// EncryptRespMessage encryptRespMessage = new
					// EncryptRespMessage(
					// somessage);
					// int is_encrypt = encryptRespMessage
					// .getIs_encrypt();
					// System.out.println("callBack");
					// }
					// }, 10);
					break;
				case SoFollowCommad.更新心跳频率:
					// System.out.println("heartbeat resp!");
					// client.sendMessage(new SetHeartBeatMessage(),
					// new ActivityCallBackListener() {
					//
					// @Override
					// public void doTimeOut() {
					// // TODO Auto-generated method stub
					// }
					//
					// @Override
					// public void callBack(SoMessage soMessage) {
					// SetHeartBeatRespMessage setHeartBeatRespMessage = new
					// SetHeartBeatRespMessage(
					// soMessage);
					// int heartbeat = setHeartBeatRespMessage
					// .getHeartbeat();
					// }
					// });
					break;
				case SoFollowCommad.更新服务器地址:
					System.out.println("update url resp!");
					// client.sendMessage(new SocketUrlMessage(),
					// new ActivityCallBackListener() {
					//
					// @Override
					// public void doTimeOut() {
					// // TODO Auto-generated method stub
					// }
					//
					// @Override
					// public void callBack(SoMessage soMessage) {
					// SocketUrlRespMessage socketUrlRespMessage = new
					// SocketUrlRespMessage(
					// soMessage);
					// String socket_url = socketUrlRespMessage
					// .getUrlString();
					client.sendMessage(new ConfirmMssage(message.followCommand,
							1));
					// }
					// });
					break;
				case SoFollowCommad.重启标牌:
					System.out.println("reset resp!");
					break;
				case SoFollowCommad.校验黑名单版本:
					System.out.println("blacklist resp!");
					break;
				case SoFollowCommad.请求校对政策文件版本:
					// System.out.println("policy resp!");
					// int a = message.getFollowCommand();
					// client.sendMessage(new ConfirmMssage(message
					// .getFollowCommand(), 1));
					break;
				default:
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e("**************", e.getMessage().toString());
			}
		}

	}

}
