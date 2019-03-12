package com.eastcom.social.pos.core.socket.client.mylistener.heartbeat;

import android.util.Log;

import com.eastcom.social.pos.core.socket.listener.heartbeat.HeartbeatRespListener;
import com.eastcom.social.pos.core.socket.message.SoFollowCommad;
import com.eastcom.social.pos.core.socket.message.heartbeat.HeartbeatRespMessage;

public class MyHearbeatRespListener implements HeartbeatRespListener {

	@Override
	public void handlerRespMessage(HeartbeatRespMessage message) {
		System.out.println("heartbeat resp!");
		
		try {
			switch (message.followCommand) {
			case SoFollowCommad.更新RFSAM名单:
				
				break;
			case SoFollowCommad.更新RFSAM卡默认有效时间:
				break;
			case SoFollowCommad.更新交易是否加密配置:
				break;
			case SoFollowCommad.更新心跳频率:
				break;
			case SoFollowCommad.更新服务器地址:
				break;
			case SoFollowCommad.重启标牌:
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
