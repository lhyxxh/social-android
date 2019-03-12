package com.eastcom.social.pos.listener;

import com.landicorp.android.mispos.MisPosClient;





/**
 * 联迪接口bank单例
 * @author eronc
 *
 */
public class MyBank {
	public static MyBank mInstance;
	private MisPosClient bank;

	public static MyBank newInstance() {
		if (mInstance == null) {
			mInstance = new MyBank();
		}
		return mInstance;
	}

	public MyBank() {
		try {
			bank = new MisPosClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MisPosClient getBank() {
		return bank;
	}

	public void setBank(MisPosClient bank) {
		this.bank = bank;
	}


}
