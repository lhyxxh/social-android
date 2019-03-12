package com.eastcom.social.pos.core.socket.listener;

import java.util.EventListener;

public interface ClientRunningListener extends EventListener {

	public void afterRunning();//Client成功连接回调
	public void clientReStart();//Client重启回调
	public void clientClosing();//Client关闭回调

}
