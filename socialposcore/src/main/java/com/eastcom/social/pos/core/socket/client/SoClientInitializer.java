package com.eastcom.social.pos.core.socket.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import com.eastcom.social.pos.core.event.listener.ChannelInitedListener;
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
import com.eastcom.social.pos.core.socket.listener.telinfo.TelInfoRespListener;
import com.eastcom.social.pos.core.socket.listener.trade.TradeRespListener;
import com.eastcom.social.pos.core.socket.listener.tradedetail.TradeDetailRespListener;
import com.eastcom.social.pos.core.socket.listener.updatehighblacklist.UpdateHighBlackListRespListener;
import com.eastcom.social.pos.core.socket.listener.updatelowblacklist.UpdateLowBlackListRespListener;
import com.eastcom.social.pos.core.socket.listener.updatepolicy.UpdatePolicyRespListener;
import com.eastcom.social.pos.core.socket.listener.updatesoft.UpdateSoftRespListener;

public class SoClientInitializer extends ChannelInitializer<SocketChannel> {

	private ChannelInitedListener channelInitedListener;
	private MessageRespListener messageRespListener;
	private int heartbeatRate;
	private CheckVersionRespListener checkVersionRespListener;
	private ConfirmRespListener confirmRespListener;
	private HealthProductRespListener healthProductRespListener;
	private HeartbeatRespListener heartbeatRespListener;
	private MedicineProductRespListener medicineProductRespListener;
	private QueryHealthRespListener queryHealthRespListener;
	private QueryMedicineRespListener queryMedicineRespListener;
	private TradeRespListener tradeRespListener;
	private TradeDetailRespListener tradeDetailRespListener;
	private UpdateSoftRespListener updateSoftRespListener;
	private RfsamListRespListener rfsamListRespListener;
	private RfsamValidTimeRespListener rfsamValidTimeRespListener;
	private AlarmRespListener alarmRespListener;
	private SocialInfoRespListener socialInfoRespListener;
	private GprsInfoRespListener gprsInfoRespListener;
	private NoDataRespListener noDataRespListener;

	private SummitCardLogRespListener changePwdRespListener;
	private SocketUrlRespListener socketUrlRespListener;
	private EncryptRespListener encryptRespListener;
	private SetHeartBeatRespListener setHeartBeatRespListener;
	
	private CheckHighBlackListRespListener checkHighBlackListRespListener;
	private UpdateHighBlackListRespListener updateHighBlackListRespListener;
	private UpdateLowBlackListRespListener updateLowBlackListRespListener;
	private CheckSocialStatusGdzhRespListener checkSocialStatusGdzhRespListener;
	private CheckSocialStatusSdLyRespListener checkSocialStatusSdLyRespListener;
	private CheckSocialStatusSdBzRespListener checkSocialStatusSdBzRespListener;
	private TelInfoRespListener telInfoRespListener;
	private SmsInfoRespListener smsInfoRespListener;
	private CheckPolicyRespListener checkPolicyRespListener;
	private UpdatePolicyRespListener updatePolicyRespListener;
	private CheckPolicyVersionRespListener checkPolicyVersionRespListener;
	private ShTypeRespListener shTypeRespListener;

	private CheckIncreamentVersionRespListener checkIncreamentVersionRespListener;
	

	/**
	 * SocketChannel的通道处理器加载完成后，ctx才会切换到活跃状态 因此，定时心跳任务可以放置在通道首个切换ctx状态的handler中
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		if (channelInitedListener != null) {
			channelInitedListener.channelInitedEvent(ch,heartbeatRate);
			channelInitedListener.messageRespChannelInit(ch, messageRespListener);
			channelInitedListener.channelActiveInit(ch);
			channelInitedListener.checkVersionChannelInit(ch, checkVersionRespListener);
			channelInitedListener.confirmChannelInit(ch, confirmRespListener);
			channelInitedListener.healthProductChannelInit(ch, healthProductRespListener);
			channelInitedListener.heartbeatChannelInit(ch, heartbeatRespListener);
			channelInitedListener.medicineProductChannelInit(ch, medicineProductRespListener);
			channelInitedListener.queryHealthChannelInit(ch, queryHealthRespListener);
			channelInitedListener.queryMedicineChannelInit(ch, queryMedicineRespListener);
			channelInitedListener.socialTradeChannelInit(ch, tradeRespListener);
			channelInitedListener.tradeDetailChannelInit(ch, tradeDetailRespListener);
			channelInitedListener.updateSoftChannelInit(ch, updateSoftRespListener);
			channelInitedListener.rfsamListChannelInit(ch, rfsamListRespListener);
			channelInitedListener.rfsamValidTimeChannelInit(ch, rfsamValidTimeRespListener);
			channelInitedListener.alarmChannelInit(ch, alarmRespListener);
			channelInitedListener.socialInfoChannelInit(ch, socialInfoRespListener);
			channelInitedListener.gprsInfoChannelInit(ch, gprsInfoRespListener);
			
			channelInitedListener.summitCardLogChannelInit(ch, changePwdRespListener);
			channelInitedListener.socketUrlChannelInit(ch, socketUrlRespListener);
			channelInitedListener.encryptChannelInit(ch, encryptRespListener);
			channelInitedListener.setHeartBeatChannelInit(ch, setHeartBeatRespListener);
			
			channelInitedListener.checkHighBlackListChannelInit(ch, checkHighBlackListRespListener);
			channelInitedListener.updateHighBlackListChannelInit(ch, updateHighBlackListRespListener);
			channelInitedListener.updateLowBlackListChannelInit(ch, updateLowBlackListRespListener);
			channelInitedListener.checkSocialStatusGdzhChannelInit(ch, checkSocialStatusGdzhRespListener);
			channelInitedListener.checkSocialStatusSdLyChannelInit(ch, checkSocialStatusSdLyRespListener);
			channelInitedListener.checkSocialStatusSdBzChannelInit(ch, checkSocialStatusSdBzRespListener);
			channelInitedListener.telInfoChannelInit(ch, telInfoRespListener);
			channelInitedListener.smsInfoChannelInit(ch, smsInfoRespListener);
			channelInitedListener.updatePolicyChannelInit(ch, updatePolicyRespListener);
			channelInitedListener.checkPolicyChannelInit(ch, checkPolicyRespListener);
			channelInitedListener.checkPolicyVersionChannelInit(ch, checkPolicyVersionRespListener);
			channelInitedListener.shTypeChannelInit(ch, shTypeRespListener);
			channelInitedListener.noDataChannelInit(ch, noDataRespListener);

			channelInitedListener.checkIncreamentVersionChannelInit(ch, checkIncreamentVersionRespListener);
			
		}
	}
	



	public void setChannelInitedListener(ChannelInitedListener channelInitedListener) {
		this.channelInitedListener = channelInitedListener;
	}

	public void setMessageRespListener(MessageRespListener messageRespListener) {
		this.messageRespListener = messageRespListener;
	}

	public void setHeartbeatRate(int heartbeatRate) {
		this.heartbeatRate = heartbeatRate;
	}

	public void setCheckVersionRespListener(CheckVersionRespListener checkVersionRespListener) {
		this.checkVersionRespListener = checkVersionRespListener;
	}

	public void setConfirmRespListener(ConfirmRespListener confirmRespListener) {
		this.confirmRespListener = confirmRespListener;
	}

	public void setHealthProductRespListener(HealthProductRespListener healthProductRespListener) {
		this.healthProductRespListener = healthProductRespListener;
	}

	public void setHeartbeatRespListener(HeartbeatRespListener heartbeatRespListener) {
		this.heartbeatRespListener = heartbeatRespListener;
	}

	public void setMedicineProductRespListener(MedicineProductRespListener medicineProductRespListener) {
		this.medicineProductRespListener = medicineProductRespListener;
	}

	public void setQueryHealthRespListener(QueryHealthRespListener queryHealthRespListener) {
		this.queryHealthRespListener = queryHealthRespListener;
	}

	public void setQueryMedicineRespListener(QueryMedicineRespListener queryMedicineRespListener) {
		this.queryMedicineRespListener = queryMedicineRespListener;
	}

	public void setTradeRespListener(TradeRespListener tradeRespListener) {
		this.tradeRespListener = tradeRespListener;
	}

	public void setTradeDetailRespListener(TradeDetailRespListener tradeDetailRespListener) {
		this.tradeDetailRespListener = tradeDetailRespListener;
	}

	public void setUpdateSoftRespListener(UpdateSoftRespListener updateSoftRespListener) {
		this.updateSoftRespListener = updateSoftRespListener;
	}


	public void setRfsamListRespListener(RfsamListRespListener rfsamListRespListener) {
		this.rfsamListRespListener = rfsamListRespListener;
	}

	public void setRfsamValidTimeRespListener(RfsamValidTimeRespListener rfsamValidTimeRespListener) {
		this.rfsamValidTimeRespListener = rfsamValidTimeRespListener;
	}

	public void setAlarmRespListener(AlarmRespListener alarmRespListener) {
		this.alarmRespListener = alarmRespListener;
	}

	public void setSocialInfoRespListener(SocialInfoRespListener socialInfoRespListener) {
		this.socialInfoRespListener = socialInfoRespListener;
	}

	public void setNoDataRespListener(NoDataRespListener noDataRespListener) {
		this.noDataRespListener = noDataRespListener;
	}

	public void setSummitCardLogRespListener(SummitCardLogRespListener changePwdRespListener) {
		this.changePwdRespListener = changePwdRespListener;
	}

	public void setEncryptRespListener(EncryptRespListener encryptRespListener) {
		this.encryptRespListener = encryptRespListener;
	}

	public void setSocketUrlRespListener(SocketUrlRespListener socketUrlRespListener) {
		this.socketUrlRespListener = socketUrlRespListener;
	}

	public void setSetHeatbeatRespListener(SetHeartBeatRespListener setHeartBeatRespListener) {
		this.setHeartBeatRespListener = setHeartBeatRespListener;
	}

	public void setGprsInfoRespListener(GprsInfoRespListener gprsInfoRespListener) {
		this.gprsInfoRespListener = gprsInfoRespListener;
	}
	
	public void setCheckHighBlackListRespListener(CheckHighBlackListRespListener checkHighBlackListRespListener) {
		this.checkHighBlackListRespListener = checkHighBlackListRespListener;
	}
	public void setUpdateHighBlackListRespListener(UpdateHighBlackListRespListener updateHighBlackListRespListener) {
		this.updateHighBlackListRespListener = updateHighBlackListRespListener;
	}
	public void setUpdateLowBlackListRespListener(UpdateLowBlackListRespListener updateLowBlackListRespListener) {
		this.updateLowBlackListRespListener = updateLowBlackListRespListener;
	}
	public void setCheckSocialStatusSdLyRespListener(CheckSocialStatusSdLyRespListener checkSocialStatusSdLyRespListener) {
		this.checkSocialStatusSdLyRespListener = checkSocialStatusSdLyRespListener;
	}
	public void setCheckSocialStatusGdzhRespListener(CheckSocialStatusGdzhRespListener checkSocialStatusGdzhRespListener) {
		this.checkSocialStatusGdzhRespListener = checkSocialStatusGdzhRespListener;
	}
	public void setCheckSocialStatusSdBzRespListener(CheckSocialStatusSdBzRespListener checkSocialStatusSdBzRespListener) {
		this.checkSocialStatusSdBzRespListener = checkSocialStatusSdBzRespListener;
	}
	public void setTelInfoRespListener(TelInfoRespListener telInfoRespListener) {
		this.telInfoRespListener = telInfoRespListener;
	}
	public void setSmsInfoRespListener(SmsInfoRespListener smsInfoRespListener) {
		this.smsInfoRespListener = smsInfoRespListener;
	}
	public void setCheckPolicyRespListener(CheckPolicyRespListener checkPolicyRespListener) {
		this.checkPolicyRespListener = checkPolicyRespListener;
	}
	public void setUpdatePolicyRespListener(UpdatePolicyRespListener updatePolicyRespListener) {
		this.updatePolicyRespListener = updatePolicyRespListener;
	}
	public void setCheckPolicyVersionRespListener(CheckPolicyVersionRespListener checkPolicyVersionRespListener) {
		this.checkPolicyVersionRespListener = checkPolicyVersionRespListener;
	}
	public void setShTypeRespListener(ShTypeRespListener shTypeRespListener) {
		this.shTypeRespListener = shTypeRespListener;
	}

	public void setCheckIncreamentVersionRespListener(CheckIncreamentVersionRespListener checkIncreamentVersionRespListener){
		this.checkIncreamentVersionRespListener = checkIncreamentVersionRespListener;
	}
	
}
