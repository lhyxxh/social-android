package com.eastcom.social.pos.core.socket.message.smsinfo;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class SmsInfoRespMessage extends SoMessage {

	private int contacts_lengh;
	private String contacts;
	private int contacts_way_lengh;
	private String content_way;
	private int reason_lengh;
	private String reason;


	public SmsInfoRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		int position = 1;
		this.contacts_lengh = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.contacts = new String(body, position, contacts_lengh, CHARSET_GB);
		position += contacts_lengh;
		this.contacts_way_lengh = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.content_way = new String(body, position, contacts_way_lengh, CHARSET_GB);
		position += contacts_way_lengh;
		this.reason_lengh = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.reason = new String(body, position, reason_lengh, CHARSET_GB);
		position += reason_lengh;
	}


	public int getContacts_lengh() {
		return contacts_lengh;
	}


	public void setContacts_lengh(int contacts_lengh) {
		this.contacts_lengh = contacts_lengh;
	}




	public String getContacts() {
		return contacts;
	}


	public void setContacts(String contacts) {
		this.contacts = contacts;
	}


	public int getContacts_way_lengh() {
		return contacts_way_lengh;
	}


	public void setContacts_way_lengh(int contacts_way_lengh) {
		this.contacts_way_lengh = contacts_way_lengh;
	}


	public String getContent_way() {
		return content_way;
	}


	public void setContent_way(String content_way) {
		this.content_way = content_way;
	}


	public int getReason_lengh() {
		return reason_lengh;
	}


	public void setReason_lengh(int reason_lengh) {
		this.reason_lengh = reason_lengh;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}







}
