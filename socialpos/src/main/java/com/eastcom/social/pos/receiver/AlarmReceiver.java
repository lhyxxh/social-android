package com.eastcom.social.pos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.service.LocalDataFactory;

/**
 * 定时器广播
 * 
 * @author eronc
 *
 */
public class AlarmReceiver extends BroadcastReceiver {
	private int time;

	public AlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if ("UPLOAD".equals(intent.getAction())) {
			context.startService(MyApplicationLike.updateService);
		} else if ("AUTH".equals(intent.getAction())) {
			
			LocalDataFactory localDataFactory = LocalDataFactory.newInstance(context);
			time = localDataFactory.getInt(LocalDataFactory.TIME, 2);
			
			MyApplicationLike.authService.putExtra("time", time);
			context.startService(MyApplicationLike.authService);
		} else if ("FEEDDOG".equals(intent.getAction())) {
			context.startService(MyApplicationLike.feedDogService);
		} else if ("UPDATE".equals(intent.getAction())) {
			context.startService(MyApplicationLike.updateService);
		} else if ("SLOWUPLOAD".equals(intent.getAction())) {
			context.startService(MyApplicationLike.slowUplodeService);
		}else if ("GRPS".equals(intent.getAction())) {
			context.startService(MyApplicationLike.gprsService);
		}else if ("POLICY".equals(intent.getAction())) {
			context.startService(MyApplicationLike.policyService);
		}
		
	}
}
