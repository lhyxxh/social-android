package com.eastcom.social.pos.core.socket.listener.updatepolicy;

import com.eastcom.social.pos.core.socket.message.updatepolicy.UpdatePolicyRespMessage;

public interface UpdatePolicyRespListener {

	public void handlerRespMessage(UpdatePolicyRespMessage message);

}
