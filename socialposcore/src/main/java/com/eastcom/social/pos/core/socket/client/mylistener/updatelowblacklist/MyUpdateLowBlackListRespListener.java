package com.eastcom.social.pos.core.socket.client.mylistener.updatelowblacklist;

import com.eastcom.social.pos.core.socket.listener.updatelowblacklist.UpdateLowBlackListRespListener;
import com.eastcom.social.pos.core.socket.message.updatelowblacklist.UpdateLowBlackListRespMessage;

public class MyUpdateLowBlackListRespListener implements
		UpdateLowBlackListRespListener {

	@Override
	public void handlerRespMessage(UpdateLowBlackListRespMessage message) {
		byte[] data = message.getData();
		int length = message.getDataTotal();
		for (int i = 0; i < length; i++) {
			byte[] temp1 = new byte[10]; 
			System.arraycopy(data, 0, temp1, 0, 10);
			byte[] temp2 = new byte[9]; 
			System.arraycopy(temp1, 1, temp2, 0, 9);
			String s_data;
			s_data = new String(temp2);
			System.out.println("resp!" + s_data+"-"+temp1[0]);
		}
		

	}

}
