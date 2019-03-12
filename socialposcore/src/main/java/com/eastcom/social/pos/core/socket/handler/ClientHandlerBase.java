package com.eastcom.social.pos.core.socket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.Date;
import java.util.Map;

import android.annotation.SuppressLint;

import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.listener.MessageRespListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.model.ActivityCallBackModel;
import com.eastcom.social.pos.core.socket.task.CallBackTimeOutTask;
import com.eastcom.social.pos.core.socket.task.ClientHeartbeatTask;

/**
 * 增加了消息响应监听器，处理消息返回值
 */
public class ClientHandlerBase extends ChannelInboundHandlerAdapter {

	public static MessageRespListener messageRespListener;
	

	@SuppressLint("UseSparseArrays")
	public volatile static Map<Integer, ActivityCallBackModel> map = CallBackTimeOutTask.map;

	public volatile ScheduledFuture<?> clientHeartbeatTask;

	public ClientHandlerBase() {
	}

	/**
	 * ctx状态由inactive切换为active的时候，会执行此方法
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 发送一次心跳
		ctx.executor().execute(new ClientHeartbeatTask(ctx));
	}

	

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
		System.out.println("exceptionCaught");
	}

	/**
	 * 设置回调方法
	 */
	public static synchronized void SetNotice(Integer command,
			ActivityCallBackListener listener) {
		map.put(command, new ActivityCallBackModel(listener));
	}

	/**
	 * 设置回调方法，包括超时时间
	 */
	public static synchronized void SetNotice(Integer command,
			ActivityCallBackListener listener, int timeOut) {
		map.put(command, new ActivityCallBackModel(listener, timeOut));
	}

	/**
	 * 清除回调方法
	 */
	public static synchronized void RemoveNotice(Integer command) {
		map.remove(command);
	}

	/**
	 * 开启某个指令回调方法的执行
	 */
	public static synchronized void openCallBack(Integer command) {
		if (map.get(command) != null && !map.get(command).isDoCallBack()) {
			ActivityCallBackModel model = map.get(command);
			model.setDoCallBack(true);
			map.put(command, model);
		}
	}

	/**
	 * 关闭某个指令回调方法的执行
	 */
	public static synchronized void closeCallBack(Integer command) {
		if (map.get(command) != null && map.get(command).isDoCallBack()) {
			ActivityCallBackModel model = map.get(command);
			model.setDoCallBack(false);
			map.put(command, model);
		}
	}

	/**
	 * 执行回调方法
	 */
	public static void SendNotice(SoMessage message) {
		ActivityCallBackModel callBackModel = map.get(message.getCommand());
		if (callBackModel != null && callBackModel.isDoCallBack()) {
			if (callBackModel.getListener() != null) {
				callBackModel.setLastExecuteDate(new Date());
				callBackModel.getListener().callBack(message);
			}
		}
	}

	/**
	 * 执行回调超时方法
	 */
	public static void doTimeOut(Integer command) {
		ActivityCallBackModel callBackModel = map.get(command);
		if (callBackModel != null && callBackModel.isDoCallBackTimeOut()) {
			if (callBackModel.getListener() != null) {
				callBackModel.setLastExecuteDate(new Date());
				callBackModel.getListener().doTimeOut();
			}
		}
	}

	/**
	 * 开启某个指令回调超时方法的执行
	 */
	public static synchronized void openCallBackTimeOut(Integer command) {
		if (map.get(command) != null && !map.get(command).isDoCallBackTimeOut()) {
			ActivityCallBackModel model = map.get(command);
			model.setDoCallBackTimeOut(true);
			map.put(command, model);
		}
	}

	/**
	 * 关闭某个指令回调超时方法的执行
	 */
	public static synchronized void closeCallBackTimeOut(Integer command) {
		if (map.get(command) != null && map.get(command).isDoCallBackTimeOut()) {
			ActivityCallBackModel model = map.get(command);
			model.setDoCallBackTimeOut(false);
			map.put(command, model);
		}
	}

}
