package com.eastcom.social.pos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.util.MyLog;

public class DateTimeChangeReciever extends BroadcastReceiver {
	public static boolean flagTimeService = false;
	



	@Override
	public void onReceive(Context context, Intent intent) {

		MyLog.i("DateTimeChangeReciever", "TIME_SET" + flagTimeService);
		if (!flagTimeService) {
			MyApplicationLike.getContext().stopService(MyApplicationLike.timerService);
			MyApplicationLike.getContext().startService(MyApplicationLike.timerService);
		}else {
			flagTimeService = false;
		}
		

	}

}
