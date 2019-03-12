package com.eastcom.social.pos.core.socket.message.gprsinfo;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class GprsInfoMessage extends SoMessage {

	private String longtitude;
	private String latitude;
	private String country;
	private String city;
	private String district;
	private String street;
	private String addr;

	public GprsInfoMessage(String longtitude, String latitude, String country,
			String city, String district, String street, String addr) {
		short bodyLength = (short) (5 + 5 + 2 + country.getBytes().length + 2
				+ city.getBytes().length + 2 + district.getBytes().length+ 2 + street.getBytes().length
				+ 2 + addr.getBytes().length);

		this.latitude = latitude;
		this.longtitude = longtitude;
		this.country = country;
		this.city = city;
		this.district = district;
		this.street = street;
		this.addr = addr;

		byte[] data = new byte[bodyLength];

		int position = 0;
		System.arraycopy(ByteUtils.hexStr2bytes(longtitude), 0, data, position,
				4);
		position += 5;
		System.arraycopy(ByteUtils.hexStr2bytes(latitude), 0, data, position, 4);
		position += 5;
		
		System.arraycopy(ByteUtils.intToBytes2(country.getBytes(CHARSET_GB).length), 0, data,
				position, 2);
		position += 2;
		System.arraycopy(country.getBytes(CHARSET_GB), 0, data, position,
				country.getBytes(CHARSET_GB).length);
		position += country.getBytes(CHARSET_GB).length;
		
		System.arraycopy(ByteUtils.intToBytes2(city.getBytes(CHARSET_GB).length), 0, data,
				position, 2);
		position += 2;
		System.arraycopy(city.getBytes(CHARSET_GB), 0, data, position,
				city.getBytes(CHARSET_GB).length);
		position += city.getBytes(CHARSET_GB).length;
		
		System.arraycopy(ByteUtils.intToBytes2(district.getBytes(CHARSET_GB).length), 0, data,
				position, 2);
		position += 2;
		System.arraycopy(district.getBytes(CHARSET_GB), 0, data, position,
				district.getBytes(CHARSET_GB).length);
		position += district.getBytes(CHARSET_GB).length;
		
		System.arraycopy(ByteUtils.intToBytes2(street.getBytes(CHARSET_GB).length), 0, data,
				position, 2);
		position += 2;
		System.arraycopy(street.getBytes(CHARSET_GB), 0, data, position,
				street.getBytes(CHARSET_GB).length);
		position += street.getBytes(CHARSET_GB).length;
		
		System.arraycopy(ByteUtils.intToBytes2(addr.getBytes(CHARSET_GB).length), 0, data,
				position, 2);
		position += 2;
		System.arraycopy(addr.getBytes(CHARSET_GB), 0, data, position,
				addr.getBytes(CHARSET_GB).length);
		position += addr.getBytes(CHARSET_GB).length;

		this.command = SoCommand.提交GPRS定位信息;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

    
}
