package com.eastcom.social.pos.listener;

import java.util.UUID;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.core.orm.entity.CommandConfirm;
import com.eastcom.social.pos.core.service.CommandConfirmService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.listener.heartbeat.HeartbeatRespListener;
import com.eastcom.social.pos.core.socket.message.SoFollowCommad;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.alarm.AlarmMessage;
import com.eastcom.social.pos.core.socket.message.confirm.ConfirmMssage;
import com.eastcom.social.pos.core.socket.message.encrypt.EncryptMessage;
import com.eastcom.social.pos.core.socket.message.encrypt.EncryptRespMessage;
import com.eastcom.social.pos.core.socket.message.heartbeat.HeartbeatMessage;
import com.eastcom.social.pos.core.socket.message.heartbeat.HeartbeatRespMessage;
import com.eastcom.social.pos.core.socket.message.rfsamlist.RfsamListMessage;
import com.eastcom.social.pos.core.socket.message.rfsamlist.RfsamListRespMessage;
import com.eastcom.social.pos.core.socket.message.rfsamvalidtime.RfsamValidTimeMessage;
import com.eastcom.social.pos.core.socket.message.rfsamvalidtime.RfsamValidTimeRespMessage;
import com.eastcom.social.pos.core.socket.message.shType.ShTypeMessage;
import com.eastcom.social.pos.core.socket.message.shType.ShTypeRespMessage;
import com.eastcom.social.pos.core.socket.message.socketurl.SocketUrlMessage;
import com.eastcom.social.pos.core.socket.message.socketurl.SocketUrlRespMessage;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.AppUtils;
import com.eastcom.social.pos.util.MyLog;

/**
 * 心跳后续指令监听
 * 
 * @author eronc
 *
 */
public class MyHeartbeatRespListener implements HeartbeatRespListener {
	private SoClient client;
	private CommandConfirmService commandConfirmService;

	public MyHeartbeatRespListener() {
		commandConfirmService = CommandConfirmService.getInstance(MyApplicationLike
				.getContext());
	}

	@Override
	public void handlerRespMessage(HeartbeatRespMessage message) {
		Intent intent = new Intent();
		intent.setAction(Constance.BROADCAST_ACTION);
		intent.putExtra("nodata", false);
		MyApplicationLike.getContext().sendBroadcast(intent);
		boolean timeServiceRunning = AppUtils.isServiceRunning(
				MyApplicationLike.getContext(),
				"com.eastcom.social.pos.service.TimerService");
		if (!timeServiceRunning) {
			MyApplicationLike.getContext().startService(MyApplicationLike.timerService);
		}
		client = MySoClient.newInstance().getClient();
		boolean isConnect = client.isConnect();
		if (!isConnect) {
			setAlarm();
			checkRfsamStatus();
			getEncryptStatus();
			getShType();
			client.setConnect(true);
		}
		try {
			final String id = UUID.randomUUID().toString();
			switch (message.followCommand) {
			case SoFollowCommad.更新RFSAM名单:
				pushCommand(id, message.followCommand);
				client.sendMessage(new RfsamListMessage(),
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {
								MyLog.e("MyHeartbeatRespListener",
										"RfsamList time out");
							}

							@Override
							public void callBack(SoMessage message) {
								MyLog.i("MyHeartbeatRespListener",
										"RfsamList callback");
								try {
									client.sendMessage(new ConfirmMssage(
											SoFollowCommad.更新RFSAM名单, 1),
											new ActivityCallBackListener() {

												@Override
												public void doTimeOut() {
													MyLog.e("ConfirmMssage",
															"ConfirmRfsamList time out");
												}

												@Override
												public void callBack(
														SoMessage message) {
													popCommand(id);
													MyLog.i("MyHeartbeatRespListener",
															"ConfirmRfsamList");
												}
											});
									RfsamListRespMessage rfsamListRespMessage = new RfsamListRespMessage(
											message);
									String rfsamNo = rfsamListRespMessage
											.getRfsamNo();
									int rfsamStatus = rfsamListRespMessage
											.getRfsamStatus();
									int rfsamLeftInterval = rfsamListRespMessage
											.getRfsamLeftInterval();
									String psamNo = rfsamListRespMessage
											.getPsamNo();
									SaveRfsamListResp(rfsamNo, rfsamStatus,
											rfsamLeftInterval, psamNo);
									Intent intent = new Intent();
									intent.setAction(Constance.RFSAM_STATUS);
									intent.putExtra("rfsam_status", rfsamStatus);
									MyApplicationLike.getContext().sendBroadcast(
											intent);

								} catch (Exception e) {
									e.printStackTrace();
								}

							}

						});

				break;
			case SoFollowCommad.更新RFSAM卡默认有效时间:
				pushCommand(id, message.followCommand);
				client.sendMessage(new RfsamValidTimeMessage(),
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {

							}

							@Override
							public void callBack(SoMessage message) {
								RfsamValidTimeRespMessage rfsamValidTimeRespMessage = new RfsamValidTimeRespMessage(
										message);
								int rfsamValidTime = rfsamValidTimeRespMessage
										.getRfsamValidTime();
								LocalDataFactory localDataFactory = LocalDataFactory
										.newInstance(MyApplicationLike.getContext());
								localDataFactory.putInt(
										LocalDataFactory.RFSAM_VALID_TIME,
										rfsamValidTime);

								client.sendMessage(new ConfirmMssage(
										SoFollowCommad.更新RFSAM卡默认有效时间, 1),
										new ActivityCallBackListener() {

											@Override
											public void doTimeOut() {
											}

											@Override
											public void callBack(
													SoMessage message) {
												popCommand(id);
											}
										});
							}
						});

				break;
			case SoFollowCommad.更新交易是否加密配置:
				MyLog.i("EncryptMessage", "EncryptMessage SoFollowCommad");
				EncryptMessage encryptMessage = new EncryptMessage();
				client.sendMessage(encryptMessage,
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {
								MyLog.e("MyHeartbeatRespListener",
										"EncryptMessage time out");
							}

							@Override
							public void callBack(SoMessage message) {
								MyLog.i("MyHeartbeatRespListener",
										"EncryptMessage callBack");
								EncryptRespMessage encryptRespMessage = new EncryptRespMessage(
										message);
								int is_encrypt = encryptRespMessage
										.getIs_encrypt();

								LocalDataFactory localDataFactory = LocalDataFactory
										.newInstance(MyApplicationLike.getContext());
								localDataFactory
										.putInt(LocalDataFactory.IS_ENCRYPT,
												is_encrypt);
								client.sendMessage(new ConfirmMssage(
										SoFollowCommad.更新交易是否加密配置, 1),
										new ActivityCallBackListener() {

											@Override
											public void doTimeOut() {
												MyLog.e("MyHeartbeatRespListener",
														"ConfirmEncrypt time out");
											}

											@Override
											public void callBack(
													SoMessage message) {
												MyLog.e("MyHeartbeatRespListener",
														"ConfirmEncrypt");
											}
										});

							}
						}, 10);
				break;
			case SoFollowCommad.更新心跳频率:
				client.sendMessage(new HeartbeatMessage(message),
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {

							}

							@Override
							public void callBack(SoMessage message) {
								client.sendMessage(new ConfirmMssage(
										SoFollowCommad.更新心跳频率, 1),
										new ActivityCallBackListener() {

											@Override
											public void doTimeOut() {
											}

											@Override
											public void callBack(
													SoMessage message) {
											}
										});
							}
						});
				break;
			case SoFollowCommad.更新服务器地址:
				try {
					pushCommand(id, message.followCommand);
					client.sendMessage(new SocketUrlMessage(),
							new ActivityCallBackListener() {

								@Override
								public void doTimeOut() {
									MyLog.e("MyHeartbeatRespListener",
											"SocketUrl time out");
								}

								@Override
								public void callBack(SoMessage message) {
									MyLog.i("MyHeartbeatRespListener",
											"SocketUrl callBack");

									SocketUrlRespMessage socketUrlRespMessage = new SocketUrlRespMessage(
											message);
									String urlString = socketUrlRespMessage
											.getUrlString();
									final String host = urlString.split("\\:")[0];
									final int port = Integer.valueOf(urlString
											.split("\\:")[1].split("\\#")[0]);

									client.sendMessage(new ConfirmMssage(
											SoFollowCommad.更新服务器地址, 1),
											new ActivityCallBackListener() {

												@Override
												public void doTimeOut() {
													MyLog.e("MyHeartbeatRespListener",
															"ip change confirmmssage time out");
												}

												@Override
												public void callBack(
														SoMessage message) {
													MyLog.i("MyHeartbeatRespListener",
															"ip change confirmmssage callback");
													popCommand(id);
													// 通知首界面重新获取绑定关系
													Intent intent = new Intent();
													intent.setAction(Constance.BROADCAST_ACTION);
													intent.putExtra("nodata",
															true);
													MyApplicationLike.getContext()
															.sendBroadcast(
																	intent);
													LocalDataFactory localDataFactory = LocalDataFactory
															.newInstance(MyApplicationLike
																	.getContext());
													localDataFactory
															.putString(
																	LocalDataFactory.HOST,
																	host);
													localDataFactory
															.putInt(LocalDataFactory.POST,
																	Integer.valueOf(port));
													String eid = localDataFactory
															.getString(
																	LocalDataFactory.EID,
																	"");
													MySoClient.newInstance()
															.setClient(host,
																	port, eid);
												}
											});

								}
							});
				} catch (Exception e) {
					MyLog.e("heartbeatResp SocketUrl", "error" + e.getMessage());
				}
				break;
			case SoFollowCommad.重启标牌:
				client.sendMessage(new ConfirmMssage(SoFollowCommad.重启标牌, 1),
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {
							}

							@Override
							public void callBack(SoMessage message) {
							}
						});
				break;
			case SoFollowCommad.校验黑名单版本:
				client.sendMessage(
						new ConfirmMssage(SoFollowCommad.校验黑名单版本, 1),
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {
							}

							@Override
							public void callBack(SoMessage message) {
							}
						});
				break;
			case SoFollowCommad.没有后续动作:

				break;
			case SoFollowCommad.请求校对政策文件版本:
				pushCommand(id, message.followCommand);
				MyLog.e("MyHeartbeatRespListener", "SoFollowCommad 28");
				MyApplicationLike.getContext().stopService(
						MyApplicationLike.policyService);
				MyApplicationLike.getContext().startService(
						MyApplicationLike.policyService);

				break;
			case SoFollowCommad.请求更新固件:
				pushCommand(id, message.followCommand);
				MyLog.e("MyHeartbeatRespListener", "SoFollowCommad 14");
//				MyApplicationLike.getContext().stopService(
//						MyApplicationLike.updateSoftService);
//				MyApplicationLike.getContext().startService(
//						MyApplicationLike.updateSoftService);
				MyApplicationLike.getContext().stopService(
						MyApplicationLike.updateIncrementService);
				MyApplicationLike.getContext().startService(
						MyApplicationLike.updateIncrementService);
				break;
			case SoFollowCommad.请求定点机构类型:
				getShType();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 重新保存rfsamlist回复信息
	 * 
	 * @param psamNo
	 * @param rfsamLeftInterval
	 * @param rfsamStatus
	 * @param rfsamNo
	 */
	private void SaveRfsamListResp(String rfsamNo, int rfsamStatus,
			int rfsamLeftInterval, String psamNo) {
		LocalDataFactory localDataFactory = LocalDataFactory
				.newInstance(MyApplicationLike.getContext());
		localDataFactory.putInt(LocalDataFactory.RFSAM_LEFT_INTERVAL,
				rfsamLeftInterval);
		localDataFactory.putInt(LocalDataFactory.RFSAM_STATUS, rfsamStatus);

		String eid = localDataFactory.getString(LocalDataFactory.EID, "");
		String localPsam = localDataFactory
				.getString(LocalDataFactory.PSAM, "");

		if (localPsam.equals(psamNo)) {
			if (client.isAvtive()) {
				client.sendMessage(new AlarmMessage("09", eid),
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {
								MyLog.e("heart sendalarm", "timeout");
							}

							@Override
							public void callBack(SoMessage message) {
								MyLog.e("sendalarm", "pasm yi dong");
							}
						});
			}
		}

	}

	private void pushCommand(String id, int type) {
		try {
			commandConfirmService.saveCommandConfirm(new CommandConfirm(UUID
					.randomUUID().toString(), type));
		} catch (Exception e) {
		}

	}

	private void popCommand(String id) {
		try {
			commandConfirmService.deleteCommandConfirm(id);
		} catch (Exception e) {
		}
	}

	/**
	 * 获取标牌是否加密
	 */
	private void getEncryptStatus() {
		MyLog.i("MyHeartbeatRespListener", "getEncryptStatus");
		EncryptMessage encryptMessage = new EncryptMessage();
		client.sendMessage(encryptMessage, new ActivityCallBackListener() {

			@Override
			public void doTimeOut() {
				MyLog.i("MyHeartbeatRespListener",
						"getEncryptStatus doTimeOut  ");
			}

			@Override
			public void callBack(SoMessage message) {
				EncryptRespMessage encryptRespMessage = new EncryptRespMessage(
						message);
				int is_encrypt = encryptRespMessage.getIs_encrypt();
				MyLog.i("MyHeartbeatRespListener",
						"getEncryptStatus callBack  " + is_encrypt);
				LocalDataFactory localDataFactory = LocalDataFactory
						.newInstance(MyApplicationLike.getContext());
				localDataFactory
						.putInt(LocalDataFactory.IS_ENCRYPT, is_encrypt);

			}
		}, 10);
	}

	/**
	 * 获取标牌是否加密
	 */
	private void getShType() {
		MyLog.i("MyHeartbeatRespListener", "getShType");
		ShTypeMessage shTypeMessage = new ShTypeMessage();
		client.sendMessage(shTypeMessage, new ActivityCallBackListener() {

			@Override
			public void doTimeOut() {
				System.out.println("getShType doTimeOut");
//				try {
//					Thread.sleep(10000);
//					getShType();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}

			@Override
			public void callBack(SoMessage message) {
				ShTypeRespMessage shTypeRespMessage = new ShTypeRespMessage(
						message);
				int shType = shTypeRespMessage.getShType();
				MyLog.i("MyHeartbeatRespListener", "getShType:" + shType);
				LocalDataFactory localDataFactory = LocalDataFactory
						.newInstance(MyApplicationLike.getContext());
				localDataFactory.putInt(LocalDataFactory.SH_TYPE, shType);

			}
		});
	}

	/**
	 * 查询rfsam状态
	 */
	private void checkRfsamStatus() {
		MyLog.i("MyHeartbeatRespListener", "checkRfsamStatus");
		client.sendMessage(new RfsamListMessage(),
				new ActivityCallBackListener() {

					@Override
					public void doTimeOut() {
						MyLog.e("MyHeartbeatRespListener",
								"checkRfsamStatus doTimeOut");
					}

					@Override
					public void callBack(SoMessage message) {
						MyLog.e("MyHeartbeatRespListener",
								"checkRfsamStatus ok");
						client.sendMessage(new ConfirmMssage(
								SoFollowCommad.更新RFSAM名单, 1),
								new ActivityCallBackListener() {

									@Override
									public void doTimeOut() {
										MyLog.e("MyHeartbeatRespListener",
												"ConfirmRfsamList time out");
									}

									@Override
									public void callBack(SoMessage message) {
										MyLog.i("MyHeartbeatRespListener",
												"ConfirmRfsamList");
									}
								});
						RfsamListRespMessage rfsamListRespMessage = new RfsamListRespMessage(
								message);
						int rfsamStatus = rfsamListRespMessage.getRfsamStatus();
						LocalDataFactory localDataFactory = LocalDataFactory
								.newInstance(MyApplicationLike.getContext());
						localDataFactory.putInt(LocalDataFactory.RFSAM_STATUS,
								rfsamStatus);
						Intent intent = new Intent();
						intent.setAction(Constance.RFSAM_STATUS);
						intent.putExtra("rfsam_status", rfsamStatus);
						MyApplicationLike.getContext().sendBroadcast(intent);

					}

				});
	}

	/**
	 * 开启闹钟，开启定时服务
	 */
	private void setAlarm() {
		AlarmManager manager = (AlarmManager) MyApplicationLike.getContext()
				.getSystemService(Activity.ALARM_SERVICE);

		// 上传版本号（兼容旧升级）
		int uploadVersionTime = 60 * 60 * 1000;
		PendingIntent pi_UploadVersion = PendingIntent.getService(
				MyApplicationLike.getContext(), 0,
				MyApplicationLike.uploadVersionService, 0);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
				uploadVersionTime, pi_UploadVersion);
		MyApplicationLike.getContext().startService(
				MyApplicationLike.uploadVersionService);

		// 检测更新软件定时服务
//		MyApplicationLike.getContext().stopService(MyApplicationLike.updateSoftService);
//		MyApplicationLike.getContext()
//				.startService(MyApplicationLike.updateSoftService);
		MyApplicationLike.getContext().stopService(MyApplicationLike.updateIncrementService);
		MyApplicationLike.getContext()
				.startService(MyApplicationLike.updateIncrementService);

		// 上传交易数据定时服务
		int uploadTime = 60 * 60 * 1000;
		PendingIntent pi_Upload = PendingIntent.getService(
				MyApplicationLike.getContext(), 0, MyApplicationLike.uploadService, 0);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
				uploadTime, pi_Upload);

		// 查询未知药品服务
		int queryMedicineTime = 30 * 60 * 1000;
		PendingIntent pi_queryMedicine = PendingIntent.getService(
				MyApplicationLike.getContext(), 0,
				MyApplicationLike.queryMedicineService, 0);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
				queryMedicineTime, pi_queryMedicine);

		// 检测更新程序服务
		MyApplicationLike.getContext().startService(MyApplicationLike.updateService);

		// 慢速上传定时服务
		int slowUplodeTime = 2 * 60 * 60 * 1000;
		PendingIntent pi_slowUplode = PendingIntent.getService(
				MyApplicationLike.getContext(), 0, MyApplicationLike.slowUplodeService,
				0);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
				slowUplodeTime, pi_slowUplode);

		// GPRS信息上传定时服务
		int gprsTime = 9 * 60 * 60 * 1000;
		PendingIntent pi_gprs = PendingIntent.getService(
				MyApplicationLike.getContext(), 0, MyApplicationLike.gprsService, 0);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, gprsTime,
				pi_gprs);

		// 上传交易文件
		int uploadTradeFileTime = 9 * 60 * 60 * 1000;
		PendingIntent pi_uploadTradeFile = PendingIntent.getService(
				MyApplicationLike.getContext(), 0,
				MyApplicationLike.uploadTradeFileService, 0);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
				uploadTradeFileTime, pi_uploadTradeFile);

		// 检测政策文件定时服务
		// int policyTime = 2 * 60 * 60 * 1000;
		// PendingIntent pi_policy = PendingIntent.getService(
		// MyApplicationLike.getContext(), 0, MyApplicationLike.policyService, 0);
		// manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
		// policyTime, pi_policy);

	}
}
