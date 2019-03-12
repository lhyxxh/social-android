package com.eastcom.social.pos.core._test;

import java.nio.charset.Charset;

import com.eastcom.social.pos.core.utils.ByteUtils;

public class Test2 {

	public static void main(String[] args) {

		// String terminalCode = "16091013";
		// String socialCardNo = "M5697420X";
		// String bankBinCode = "622823";
		// String idCardNo = "372421197905080019";
		// CheckSocialStatusMessage checkSocialStatusMessage = new
		// CheckSocialStatusMessage(terminalCode, socialCardNo, bankBinCode,
		// idCardNo);
		// byte a = (byte) 0xad;
		// int i = -83;
		// byte b = (byte)i;
		// System.out.print(b);

		// String socialCardNo = "Q41139593";
		// String password = "FFFFFFFFFFFFFFFF";
		// CheckSocialStatusSdLyMessage checkSocialStatusSdLyMessage = new
		// CheckSocialStatusSdLyMessage(socialCardNo, password);
		// byte a = (byte) 0xad;
		// int i = -83;
		// byte b = (byte)i;
		// System.out.print(b);

		// String rfsamNo = "16091013";
		// String psamNo = "371600811143";
		// int tradeRandom = 1;
		// Date tradeTime = new Date();
		// String tradeState = "0070";
		// String isEncrypt = "00";
		// int preNo = 100;
		// String bankCard = "55555555555555555555";
		// String socialCardNo = "M0058A052";
		// String refNo = "222233334444";
		// String tradeSn = "000000";
		// int tradeMoney = 2000 * 1;
		// String terminalCode = "BZ201681";
		// int mac = 10;
		// int sendCount = 1;
		// int tradeNo = 1;
		// String tradeType = SoTradeType.医保交易_82字节;
		// SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
		// tradeType, tradeState, isEncrypt,
		// preNo, bankCard, socialCardNo, refNo,
		// tradeTime, tradeSn, tradeMoney, psamNo,
		// terminalCode, rfsamNo, tradeRandom,
		// mac, sendCount, tradeNo);
		// byte b[] = ByteUtils.hexStr2bytes("37");
		// byte c[] = psamNo.getBytes();
		// System.out.print("a");

		// String eid = "16091013";
		// int versionMain = 2;
		// int versionSub = 2;
		// UpdateSoftMessage updateSoftMessage = new UpdateSoftMessage(eid,
		// versionMain, versionSub);
		// RfsamListMessage rfsamListMessage = new RfsamListMessage();
		// String hexString = rfsamListMessage.toHexString();
		// byte[] convertToBytes = rfsamListMessage.convertToBytes();

//		 UpdateSoftMessage updateSoftMessage = new UpdateSoftMessage(
//		 13, 2,20);
		// UpdateSoftMessage updateSoftMessage = new UpdateSoftMessage(
		// "01", "02", "0012");
//		int i = 4;
//		String s = "0004";
//		byte[] hexStr2bytes = ByteUtils.hexStr2bytes(s);
//		byte[] intToBytes2 = ByteUtils.intToBytes2(i);
//		byte[] intToBytes = ByteUtils.intToBytes(i);
//		Log.i("as", "as");
//		String aaString = "f2";
////		 byte b = aaString.getBytes();
////				 System.out.print(b);
//				 byte[] hexStr2bytes = ByteUtils.hexStr2bytes(aaString);
//				 byte[] b = aaString.getBytes();
//				 int parseInt = Integer.parseInt("F2",16);
//				 System.out.print(b);
//				 int i = 242;
//				 String hexString = Integer.toHexString(242);
//				 String hexString2 = Integer.toBinaryString(Integer.parseInt("242"));
//				 System.out.print(hexString);
//				 byte b = (byte) 0x9C;
//				 System.out.print(b);
//				 String a = "06666633333333331111";
//				 String bString = a.substring(0, 6)+"**********"+a.substring(16,20);
//				 System.out.println(bString.length());
//						 System.out.print(bString);
		
//		SimpleDateFormat sdf=new SimpleDateFormat("MMddHHmmss");//小写的mm表示的是分钟  
//		String ref_no=sdf.format(new Date());
//		 System.out.print(ref_no);
//		byte[] body = new byte[]{0x01,0x34,0x24,0x09,0x12,0x13};
//		String string = ByteUtils.bcd2Str(body, 0, 6);
//		System.out.print(string);
//		byte b= (byte)1;
//		System.out.println(b);
//		byte a = 23;
//		int i = (int) a;
//		System.out.print(i);
		

		
		try {
			
//			 int versionMain;
//
//			 int versionSub;
//			 int suffix_type;
//			 int name_length;
//			 String name;
//			int b = 0xff;
//			int a = 0x13;
//			byte[] body = {0x00 ,0x00 ,(byte) 0xFF ,(byte) 0xD6 ,0x00 ,0x0B ,0x00 ,0x01 ,0x00 ,0x00 ,0x01 ,0x00 ,0x04 ,0x31 ,0x32 ,0x33 ,0x34 ,0x4F ,0x00 };
//			int position = 0;
//			 versionMain = ByteUtils.bytes2Int(body, position, 2);
//			position += 2;
//			versionSub = ByteUtils.bytes2Int(body, position, 2);
//			position += 2;
//			suffix_type = (ByteUtils.bytes2Int(body, position, 1));
//			position += 1;
//			name_length = ByteUtils.bytes2Int(body, position, 2);
//			position += 2;
//			name = new String(body, position, name_length, "GB2312");
//			position += name_length;
			
//			int packetNo = 57;
//			byte[] b = new byte[4];
//			b = ByteUtils.intToBytes(packetNo);
//			System.out.print(b);
			
//			String aaString = "0001026222114098190950    2104003666000000026120                                                          10337235912209956013473000671      1898260956573250440801095657北京同仁堂山东医药连锁有限公司滨州黄三渤七店                                                        苹果自带wallet应用绑卡,享安全快捷的银联云闪付-含智能手机Pay;社保卡号:M40193142;372301196408220716;郭建民;男;20110428;20210428   00";
//			int l = aaString.length();
//			String test = test(159, 169,aaString);
//			String teString = aaString.substring(l-2, l);
//			System.out.println(test);
//			System.out.println(teString);
//			int a7 = 0xff;
//			String hexString = Integer.toHexString(a7);
//			System.out.println(hexString);
//			byte[] body = new byte[]{00, 00, 0xff ,a7 ,00 ,04 ,00 ,00 ,34 ,ae ,73 ,00};
//			ByteUtils.bytes2Int(body);
			
//			String alarmType = "10";
//			byte[] hexStr2bytes = ByteUtils.hexStr2bytes(alarmType);
//			String alarmType1 = "01";
//			byte[] hexStr2bytes1 = ByteUtils.hexStr2bytes(alarmType1);
//			System.out.println("");
			
//			String bandCardNo = "11111111112222222222";
//			String socialCardNo = "M88888888";
//			String refNo = "111122223333";
//			String tradeSn = "000000";
//			int tradeMoney = 100;
//			String psamNo = "201705220001";
//			String terminalCode = "12345678";
//			String rfsamNo = "17001000";
//			String mCipherText = SocialTradeMessage.getCipherText(
//					bandCardNo, socialCardNo, refNo,
//					new Date(), tradeSn, tradeMoney,
//					psamNo, terminalCode, rfsamNo);
//			System.out.println(mCipherText);
////			String alarmType = "11";
////			byte[] hexStr2bytes = ByteUtils.hexStr2bytes(alarmType);
////			System.out.println(hexStr2bytes);
			
//			CheckPolicyMessage checkPolicyMessage = new CheckPolicyMessage();
//			System.out.println(checkPolicyMessage);
//			CheckPolicyRespMessage checkPolicyRespMessage = new CheckPolicyRespMessage(message);
//			String kk = "山东省人力资源和社会保障厅关于职工基本医疗保险关系转移接续有关问题的通知";
//			byte[] bytes = kk.getBytes("GB2312");
//			System.out.println(kk.getBytes("GB2312"));
//			String aa = new String(bytes, "GB2312");
//			System.out.println(aa);
			
//			byte[] b = new byte[]{0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f};
//			byte[] a = new byte[512];
//			int i = a.length;
//			byte[] b = new byte[]{0x70, 0x64, 0x66};
//			String s = new String(b);
			
//			InputStream inputStream = new ByteArrayInputStream("abc".getBytes("UTF-8"));
//			inputStream.read(a);
//			System.out.println(a.length);
			
			String txt = "*00049533";
			String replace2 = txt.replace("k", "i");
			StringBuffer buffer = new StringBuffer("");
			byte[] bytes = replace2.substring(0, 1).getBytes(Charset.forName("ASCII"));
			buffer.append(ByteUtils.bcd2Str(replace2.substring(0, 1).getBytes(Charset.forName("ASCII"))));
			buffer.append(ByteUtils.bcd2Str(ByteUtils.intToBytes3(Integer.valueOf(replace2.substring(1, 8)))));
			buffer.append(ByteUtils.bcd2Str(replace2.substring(8, 9).getBytes(Charset.forName("ASCII"))));
			byte[] replace =txt.getBytes( Charset.forName("ASCII"));
			System.out.println(replace);
			System.out.println(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
