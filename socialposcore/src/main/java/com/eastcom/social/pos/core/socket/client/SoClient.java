package com.eastcom.social.pos.core.socket.client;

import java.util.concurrent.TimeUnit;

import com.eastcom.social.pos.core.socket.config.ClientConfig;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.listener.ClientRunningListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

public class SoClient {

	public final static AttributeKey<ClientConfig> KEY_CONFIG = AttributeKey.valueOf("config");

	volatile boolean isRun;
	private boolean isConnect = false;//首次连接
	ClientConfig config;

	SoClientInitializer initializer;

	EventLoopGroup group = null;
	ChannelFuture channelFuture = null;

	public SoClient(ClientConfig config) {
		this.config = config;
		this.group = new NioEventLoopGroup();
	}

	public ClientConfig getConfig() {
		return config;
	}

	public void setConfig(ClientConfig config) {
		this.config = config;
	}

	/**
	 * 通道初始化器，需要在客户端运行前注册
	 */
	public void setInitializer(SoClientInitializer initializer) {
		this.initializer = initializer;
	}

	void connect(ClientRunningListener listener) {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			
			b.option(ChannelOption.TCP_NODELAY, true);
			b.handler(initializer);

			b.attr(KEY_CONFIG, config);

			channelFuture = b.connect(config.getHost(), config.getPort()).sync();

			if (listener != null) {
				listener.afterRunning();
			}

			System.out.println("client run");

			channelFuture.channel().closeFuture().sync();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		group.shutdownGracefully();
		

		if (isRun) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (isRun) {
			System.out.println("client restart");
			listener.clientReStart();
			connect(listener);
		} else {
			System.out.println("client close");
			listener.clientClosing();
		}
	}

	public void run(ClientRunningListener listener) {
		if (isRun) {
			System.out.println("client run");
			return;
		}

		isRun = true;

		System.out.println("client start running");

		connect(listener);
	}

	public void stop() {
		System.out.println("client --> start closing");
		isRun = false;
		if (channelFuture != null) {
				channelFuture.channel().close();
		} else {
			System.out.println("client --> close");
		}
		
	}

	public boolean getRunState() {
		return isRun;
	}

	/**
	 * 获取通道是否激活
	 */
	public boolean isAvtive() {
		if (channelFuture != null && channelFuture.channel() != null && channelFuture.channel().isActive()) {
			return true;
		}
		return false;
	}

	/**
	 * 发送消息
	 */
	public boolean sendMessage(SoMessage message) {
		return sendMessageAll(message, null, 0);
	}

	/**
	 * 发送消息，配置回调函数
	 */
	public boolean sendMessage(SoMessage message, ActivityCallBackListener listener) {
		return sendMessageAll(message, listener, 0);
	}

	/**
	 * 发送消息，配置回调函数及回调超时时间
	 */
	public boolean sendMessage(SoMessage message, ActivityCallBackListener listener, int timeOut) {
		return sendMessageAll(message, listener, timeOut);
	}

	// 私有通用方法
	private boolean sendMessageAll(SoMessage message, ActivityCallBackListener listener, int timeOut) {
		if (isRun) {
			System.out.println("SB to Server --> : ");
			System.out.println(message);
			System.out.println(message.toHexString());
			System.out.println();
			if (listener != null) {
				if (timeOut == 0) {
					ClientHandlerBase.SetNotice(message.getRespCommand(), listener);
				} else {
					ClientHandlerBase.SetNotice(message.getRespCommand(), listener, timeOut);
				}
			}
			channelFuture.channel().writeAndFlush(message);
			return true;
		}

		return false;
	}

	/**
	 * 设置某个指令的回调方法
	 */
	public void setCallBack(SoMessage message, ActivityCallBackListener listener) {
		ClientHandlerBase.SetNotice(message.getRespCommand(), listener);
	}

	/**
	 * 开启某个指令回调方法的执行
	 */
	public void openCallBack(SoMessage message) {
		ClientHandlerBase.openCallBack(message.getRespCommand());
	}

	/**
	 * 关闭某个指令回调方法的执行
	 */
	public void closeCallBack(SoMessage message) {
		ClientHandlerBase.closeCallBack(message.getRespCommand());
	}

	public boolean isConnect() {
		return isConnect;
	}

	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}

}
