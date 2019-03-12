package com.eastcom.social.pos.core.event.listener;

import io.netty.channel.socket.SocketChannel;

import java.util.EventListener;

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

public interface ChannelInitedListener extends EventListener {

	
	
	/**
	 * 通道初始化，如配置编解码器
	 */
	public void channelInitedEvent(SocketChannel ch, int heartbeatRate);

	/**
	 * 通道活跃时，配置定时任务执行
	 */
	public void channelActiveInit(SocketChannel ch);

	/**
	 * 消息响应的全局配置
	 */
	public void messageRespChannelInit(SocketChannel ch,
			MessageRespListener listener);

	/**
	 * 各个指令相应的监听配置
	 */
	public void checkIncreamentVersionChannelInit(SocketChannel ch,
			CheckIncreamentVersionRespListener listener);

	public void checkVersionChannelInit(SocketChannel ch,
			CheckVersionRespListener listener);

	public void confirmChannelInit(SocketChannel ch,
			ConfirmRespListener listener);

	public void healthProductChannelInit(SocketChannel ch,
			HealthProductRespListener listener);

	public void heartbeatChannelInit(SocketChannel ch,
			HeartbeatRespListener listener);

	public void medicineProductChannelInit(SocketChannel ch,
			MedicineProductRespListener listener);

	public void queryHealthChannelInit(SocketChannel ch,
			QueryHealthRespListener listener);

	public void queryMedicineChannelInit(SocketChannel ch,
			QueryMedicineRespListener listener);

	public void socialTradeChannelInit(SocketChannel ch,
			TradeRespListener listener);

	public void tradeDetailChannelInit(SocketChannel ch,
			TradeDetailRespListener listener);

	public void updateSoftChannelInit(SocketChannel ch,
			UpdateSoftRespListener listener);


	public void rfsamListChannelInit(SocketChannel ch,
			RfsamListRespListener listener);

	public void rfsamValidTimeChannelInit(SocketChannel ch,
			RfsamValidTimeRespListener listener);

	public void alarmChannelInit(SocketChannel ch, AlarmRespListener listener);

	public void socialInfoChannelInit(SocketChannel ch,
			SocialInfoRespListener socialInfoRespListener);

	public void gprsInfoChannelInit(SocketChannel ch,
			GprsInfoRespListener gprsInfoRespListener);

	public void noDataChannelInit(SocketChannel ch, NoDataRespListener listener);

	public void summitCardLogChannelInit(SocketChannel ch,
			SummitCardLogRespListener listener);

	public void socketUrlChannelInit(SocketChannel ch,
			SocketUrlRespListener listener);

	public void encryptChannelInit(SocketChannel ch,
			EncryptRespListener listener);

	public void setHeartBeatChannelInit(SocketChannel ch,
			SetHeartBeatRespListener listener);

	public void checkHighBlackListChannelInit(SocketChannel ch,
			CheckHighBlackListRespListener listener);

	public void updateHighBlackListChannelInit(SocketChannel ch,
			UpdateHighBlackListRespListener listener);

	public void updateLowBlackListChannelInit(SocketChannel ch,
			UpdateLowBlackListRespListener listener);
	
	
	public void checkSocialStatusSdLyChannelInit(SocketChannel ch,
			CheckSocialStatusSdLyRespListener listener);
	
	public void checkSocialStatusGdzhChannelInit(SocketChannel ch,
			CheckSocialStatusGdzhRespListener listener);
	
	public void checkSocialStatusSdBzChannelInit(SocketChannel ch,
			CheckSocialStatusSdBzRespListener listener);
	
	public void telInfoChannelInit(SocketChannel ch,
			TelInfoRespListener telInfoRespListener);
	
	public void smsInfoChannelInit(SocketChannel ch,
			SmsInfoRespListener smsInfoRespListener);
	
	public void sysTimeChannelInit(SocketChannel ch,
			SysTimeRespListener sysTimeRespListener);
	
	public void checkPolicyChannelInit(SocketChannel ch,
			CheckPolicyRespListener checkPolicyRespListener);
	
	public void updatePolicyChannelInit(SocketChannel ch,
			UpdatePolicyRespListener updatePolicyRespListener);
	
	public void checkPolicyVersionChannelInit(SocketChannel ch,
			CheckPolicyVersionRespListener checkPolicyVersionRespListener);
	
	public void shTypeChannelInit(SocketChannel ch,
			ShTypeRespListener shTypeRespListener);

}
