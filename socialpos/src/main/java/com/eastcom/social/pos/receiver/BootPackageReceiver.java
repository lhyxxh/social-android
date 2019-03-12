package com.eastcom.social.pos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootPackageReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		//接收安装广播 
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {   
        	sendCloseBroadcast(context);
        }   
        //接收卸载广播  
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {   
            String packageName = intent.getDataString();   
            System.out.println("卸载了:"  + packageName + "包名的程序");
 
        }
	}

	/**
	 * 发送 关闭未知来源选项广播
	 * @param context 
	 */
	private void sendCloseBroadcast(Context context) {
		Intent intent = new Intent();
		intent.setAction("android.intent.for.install.n");
		context.sendBroadcast(intent);
	}
}
