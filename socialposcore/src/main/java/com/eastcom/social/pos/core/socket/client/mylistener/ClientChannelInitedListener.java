package com.eastcom.social.pos.core.socket.client.mylistener;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import com.eastcom.social.pos.core.event.listener.ChannelInitedListener;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.handler.alarm.ClientAlarmHandler;
import com.eastcom.social.pos.core.socket.handler.checkIncreamentVersion.ClientCheckIncreamentVersionHandler;
import com.eastcom.social.pos.core.socket.handler.checkhighblacklist.ClientCheckHighBlackListVersionHandler;
import com.eastcom.social.pos.core.socket.handler.checkpolicy.ClientCheckPolicyHandler;
import com.eastcom.social.pos.core.socket.handler.checkpolicyversion.ClientCheckPolicyVersionHandler;
import com.eastcom.social.pos.core.socket.handler.checksocialstatusgdzh.ClientCheckSocialStatusGdzhHandler;
import com.eastcom.social.pos.core.socket.handler.checksocialstatussdbz.ClientCheckSocialStatusSdBzHandler;
import com.eastcom.social.pos.core.socket.handler.checksocialstatussdly.ClientCheckSocialStatusSdLyHandler;
import com.eastcom.social.pos.core.socket.handler.checkversion.ClientCheckVersionHandler;
import com.eastcom.social.pos.core.socket.handler.codec.SoMessageDecoder;
import com.eastcom.social.pos.core.socket.handler.codec.SoMessageEncoder;
import com.eastcom.social.pos.core.socket.handler.confirm.ClientConfirmHandler;
import com.eastcom.social.pos.core.socket.handler.encrypt.ClientEncryptHandler;
import com.eastcom.social.pos.core.socket.handler.exception.ClientExceptionHandler;
import com.eastcom.social.pos.core.socket.handler.gprsinfo.ClientGprsInfoHandler;
import com.eastcom.social.pos.core.socket.handler.healthproduct.ClientHealthProductHandler;
import com.eastcom.social.pos.core.socket.handler.heartbeat.ClientHeartbeatHandler;
import com.eastcom.social.pos.core.socket.handler.medicineproduct.ClientMedicineProductHandler;
import com.eastcom.social.pos.core.socket.handler.queryhealth.ClientQueryHealthHandler;
import com.eastcom.social.pos.core.socket.handler.querymedicine.ClientQueryMedicineHandler;
import com.eastcom.social.pos.core.socket.handler.rfsamlist.ClientRfsamListHandler;
import com.eastcom.social.pos.core.socket.handler.rfsamvalidtime.ClientRfsamValidTimeHandler;
import com.eastcom.social.pos.core.socket.handler.setheartbeat.ClientSetHeartBeatHandler;
import com.eastcom.social.pos.core.socket.handler.shType.ClientShTypeHandler;
import com.eastcom.social.pos.core.socket.handler.smsinfo.ClientSmsInfoHandler;
import com.eastcom.social.pos.core.socket.handler.socialinfo.ClientSocialInfoHandler;
import com.eastcom.social.pos.core.socket.handler.socketurl.ClientSocketUrlHandler;
import com.eastcom.social.pos.core.socket.handler.summitcardlog.SummitCardLogHandler;
import com.eastcom.social.pos.core.socket.handler.systime.ClientSysTimeHandler;
import com.eastcom.social.pos.core.socket.handler.telinfo.ClientTelInfoHandler;
import com.eastcom.social.pos.core.socket.handler.trade.ClientTradeHandler;
import com.eastcom.social.pos.core.socket.handler.tradedetail.ClientTradeDetailHandler;
import com.eastcom.social.pos.core.socket.handler.updatehighblacklist.ClientUpdateHighBlackListHandler;
import com.eastcom.social.pos.core.socket.handler.updatelowblacklist.ClientUpdateLowBlackListHandler;
import com.eastcom.social.pos.core.socket.handler.updatepolicy.ClientUpdatePolicyHandler;
import com.eastcom.social.pos.core.socket.handler.updatesoft.ClientUpdateSoftHandler;
import com.eastcom.social.pos.core.socket.listener.CheckIncreamentVersion.CheckIncreamentVersionRespListener;
import com.eastcom.social.pos.core.socket.listener.MessageRespListener;
import com.eastcom.social.pos.core.socket.listener.alarm.AlarmRespListener;
import com.eastcom.social.pos.core.socket.listener.checkhighblacklist.CheckHighBlackListRespListener;
import com.eastcom.social.pos.core.socket.listener.checkpolicy.CheckPolicyRespListener;
import com.eastcom.social.pos.core.socket.listener.checkpolicyversion.CheckPolicyVersionRespListener;
import com.eastcom.social.pos.core.socket.listener.checksocialstatusgdzh.CheckSocialStatusGdzhRespListener;
import com.eastcom.social.pos.core.socket.listener.checksocialstatussdbz.CheckSocialStatusSdBzRespListener;
import com.eastcom.social.pos.core.socket.listener.checksocialstatussdly.CheckSocialStatusSdLyRespListener;
import com.eastcom.social.pos.core.socket.listener.checkversion.CheckVersionRespListener;
import com.eastcom.social.pos.core.socket.listener.confirm.ConfirmRespListener;
import com.eastcom.social.pos.core.socket.listener.encrypt.EncryptRespListener;
import com.eastcom.social.pos.core.socket.listener.gprsinfo.GprsInfoRespListener;
import com.eastcom.social.pos.core.socket.listener.healthproduct.HealthProductRespListener;
import com.eastcom.social.pos.core.socket.listener.heartbeat.HeartbeatRespListener;
import com.eastcom.social.pos.core.socket.listener.medicineproduct.MedicineProductRespListener;
import com.eastcom.social.pos.core.socket.listener.nodata.NoDataRespListener;
import com.eastcom.social.pos.core.socket.listener.queryhealth.QueryHealthRespListener;
import com.eastcom.social.pos.core.socket.listener.querymedicine.QueryMedicineRespListener;
import com.eastcom.social.pos.core.socket.listener.rfsamlist.RfsamListRespListener;
import com.eastcom.social.pos.core.socket.listener.rfsamvalidtime.RfsamValidTimeRespListener;
import com.eastcom.social.pos.core.socket.listener.setheartbeat.SetHeartBeatRespListener;
import com.eastcom.social.pos.core.socket.listener.shType.ShTypeRespListener;
import com.eastcom.social.pos.core.socket.listener.smsinfo.SmsInfoRespListener;
import com.eastcom.social.pos.core.socket.listener.socialinfo.SocialInfoRespListener;
import com.eastcom.social.pos.core.socket.listener.socketurl.SocketUrlRespListener;
import com.eastcom.social.pos.core.socket.listener.summitcardlog.SummitCardLogRespListener;
import com.eastcom.social.pos.core.socket.listener.systime.SysTimeRespListener;
import com.eastcom.social.pos.core.socket.listener.telinfo.TelInfoRespListener;
import com.eastcom.social.pos.core.socket.listener.trade.TradeRespListener;
import com.eastcom.social.pos.core.socket.listener.tradedetail.TradeDetailRespListener;
import com.eastcom.social.pos.core.socket.listener.updatehighblacklist.UpdateHighBlackListRespListener;
import com.eastcom.social.pos.core.socket.listener.updatelowblacklist.UpdateLowBlackListRespListener;
import com.eastcom.social.pos.core.socket.listener.updatepolicy.UpdatePolicyRespListener;
import com.eastcom.social.pos.core.socket.listener.updatesoft.UpdateSoftRespListener;

public class ClientChannelInitedListener implements ChannelInitedListener {
	
	private SoClient mSoClient;
	
	

	public ClientChannelInitedListener(SoClient mSoClient) {
		this.mSoClient = mSoClient;
	}



	@Override
	public void channelInitedEvent(SocketChannel ch, int heartbeatRate) {
		ch.pipeline().addLast(new IdleStateHandler(0,0,heartbeatRate));
		ch.pipeline().addLast(new SoMessageDecoder());
		ch.pipeline().addLast(new SoMessageEncoder());
	}
	
	

	@Override
	public void channelActiveInit(SocketChannel ch) {
		ClientHandlerBase clientHandlerBase = new ClientHandlerBase();
		ch.pipeline().addLast(clientHandlerBase);
	}

	@Override
	public void messageRespChannelInit(SocketChannel ch, MessageRespListener listener) {
		ClientHandlerBase.messageRespListener = listener;
	}

	@Override
	public void checkIncreamentVersionChannelInit(SocketChannel ch, CheckIncreamentVersionRespListener listener) {
		ch.pipeline().addLast(new ClientCheckIncreamentVersionHandler(listener));
	}

	@Override
	public void checkVersionChannelInit(SocketChannel ch, CheckVersionRespListener listener) {
		ch.pipeline().addLast(new ClientCheckVersionHandler(listener));
	}

	@Override
	public void confirmChannelInit(SocketChannel ch, ConfirmRespListener listener) {
		ch.pipeline().addLast(new ClientConfirmHandler(listener));
	}

	@Override
	public void healthProductChannelInit(SocketChannel ch, HealthProductRespListener listener) {
		ch.pipeline().addLast(new ClientHealthProductHandler(listener));
	}

	@Override
	public void heartbeatChannelInit(SocketChannel ch, HeartbeatRespListener listener) {
		ClientHeartbeatHandler clientHeartbeatHandler = new ClientHeartbeatHandler(listener);
		clientHeartbeatHandler.setmSoClient(mSoClient);
		ch.pipeline().addLast(clientHeartbeatHandler);
	}

	@Override
	public void medicineProductChannelInit(SocketChannel ch, MedicineProductRespListener listener) {
		ch.pipeline().addLast(new ClientMedicineProductHandler(listener));
	}

	@Override
	public void queryHealthChannelInit(SocketChannel ch, QueryHealthRespListener listener) {
		ch.pipeline().addLast(new ClientQueryHealthHandler(listener));
	}

	@Override
	public void queryMedicineChannelInit(SocketChannel ch, QueryMedicineRespListener listener) {
		ch.pipeline().addLast(new ClientQueryMedicineHandler(listener));
	}

	@Override
	public void socialTradeChannelInit(SocketChannel ch, TradeRespListener listener) {
		ch.pipeline().addLast(new ClientTradeHandler(listener));
	}

	@Override
	public void tradeDetailChannelInit(SocketChannel ch, TradeDetailRespListener listener) {
		ch.pipeline().addLast(new ClientTradeDetailHandler(listener));
	}

	@Override
	public void updateSoftChannelInit(SocketChannel ch, UpdateSoftRespListener listener) {
		ch.pipeline().addLast(new ClientUpdateSoftHandler(listener));
	}


	@Override
	public void rfsamListChannelInit(SocketChannel ch, RfsamListRespListener listener) {
		ch.pipeline().addLast(new ClientRfsamListHandler(listener));
	}

	@Override
	public void rfsamValidTimeChannelInit(SocketChannel ch, RfsamValidTimeRespListener listener) {
		ch.pipeline().addLast(new ClientRfsamValidTimeHandler(listener));
	}

	@Override
	public void alarmChannelInit(SocketChannel ch, AlarmRespListener listener) {
		ch.pipeline().addLast(new ClientAlarmHandler(listener));
	}

	@Override
	public void socialInfoChannelInit(SocketChannel ch, SocialInfoRespListener listener) {
		ch.pipeline().addLast(new ClientSocialInfoHandler(listener));
	}

	@Override
	public void noDataChannelInit(SocketChannel ch, NoDataRespListener listener) {
		ch.pipeline().addLast(new ClientExceptionHandler(listener));
	}

	@Override
	public void summitCardLogChannelInit(SocketChannel ch, SummitCardLogRespListener listener) {
		ch.pipeline().addLast(new SummitCardLogHandler(listener));
	}

	@Override
	public void socketUrlChannelInit(SocketChannel ch, SocketUrlRespListener listener) {
		ch.pipeline().addLast(new ClientSocketUrlHandler(listener));
	}

	@Override
	public void encryptChannelInit(SocketChannel ch, EncryptRespListener listener) {
		ch.pipeline().addLast(new ClientEncryptHandler(listener));
	}

	@Override
	public void setHeartBeatChannelInit(SocketChannel ch, SetHeartBeatRespListener listener) {
		ch.pipeline().addLast(new ClientSetHeartBeatHandler(listener));
	}

	@Override
	public void gprsInfoChannelInit(SocketChannel ch, GprsInfoRespListener listener) {
		ch.pipeline().addLast(new ClientGprsInfoHandler(listener));
	}

	@Override
	public void updateHighBlackListChannelInit(SocketChannel ch,
			UpdateHighBlackListRespListener listener) {
		ch.pipeline().addLast(new ClientUpdateHighBlackListHandler(listener));
	}

	@Override
	public void checkHighBlackListChannelInit(SocketChannel ch,
			CheckHighBlackListRespListener listener) {
		ch.pipeline().addLast(new ClientCheckHighBlackListVersionHandler(listener));		
	}

	@Override
	public void updateLowBlackListChannelInit(SocketChannel ch,
			UpdateLowBlackListRespListener listener) {
		ch.pipeline().addLast(new ClientUpdateLowBlackListHandler(listener));			
	}


	@Override
	public void checkSocialStatusSdLyChannelInit(SocketChannel ch,
			CheckSocialStatusSdLyRespListener listener) {
		ch.pipeline().addLast(new ClientCheckSocialStatusSdLyHandler(listener));
	}

	@Override
	public void checkSocialStatusSdBzChannelInit(SocketChannel ch,
			CheckSocialStatusSdBzRespListener listener) {
		ch.pipeline().addLast(new ClientCheckSocialStatusSdBzHandler(listener));
	}

	@Override
	public void telInfoChannelInit(SocketChannel ch,
			TelInfoRespListener listener) {
		ch.pipeline().addLast(new ClientTelInfoHandler(listener));		
	}

	@Override
	public void smsInfoChannelInit(SocketChannel ch,
			SmsInfoRespListener listener) {
		ch.pipeline().addLast(new ClientSmsInfoHandler(listener));
	}


	@Override
	public void sysTimeChannelInit(SocketChannel ch,
			SysTimeRespListener listener) {
		ch.pipeline().addLast(new ClientSysTimeHandler(listener));		
	}



	@Override
	public void checkSocialStatusGdzhChannelInit(SocketChannel ch,
			CheckSocialStatusGdzhRespListener listener) {
		ch.pipeline().addLast(new ClientCheckSocialStatusGdzhHandler(listener));		
	}



	@Override
	public void checkPolicyChannelInit(SocketChannel ch,
			CheckPolicyRespListener listener) {
		ch.pipeline().addLast(new ClientCheckPolicyHandler(listener));				
	}



	@Override
	public void updatePolicyChannelInit(SocketChannel ch,
			UpdatePolicyRespListener listener) {
		ch.pipeline().addLast(new ClientUpdatePolicyHandler(listener));		
	}



	@Override
	public void checkPolicyVersionChannelInit(SocketChannel ch,
			CheckPolicyVersionRespListener listener) {
		ch.pipeline().addLast(new ClientCheckPolicyVersionHandler(listener));			
	}



	@Override
	public void shTypeChannelInit(SocketChannel ch,
			ShTypeRespListener listener) {
		ch.pipeline().addLast(new ClientShTypeHandler(listener));	
	}



	

}