package com.eastcom.social.pos.core.socket.client.mylistener.medicineproduct;

import com.eastcom.social.pos.core.socket.listener.medicineproduct.MedicineProductRespListener;
import com.eastcom.social.pos.core.socket.message.medicineproduct.MedicineProductRespMessage;

public class MyMedicineProductRespListener implements MedicineProductRespListener {

	@Override
	public void handlerRespMessage(MedicineProductRespMessage message) {
		System.out.println("medicineproduct resp!");
	}

}
