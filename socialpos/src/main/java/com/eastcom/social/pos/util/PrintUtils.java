package com.eastcom.social.pos.util;

import java.util.Date;

import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcompeace.printer.api.Printer;

public class PrintUtils {
	public static void printNote(Printer mPrinter, String a1, String a2,
			String a3) {
		mPrinter.init();
		StringBuffer sb = new StringBuffer();
		mPrinter.setPrinter(Printer.COMM_ALIGN, Printer.COMM_ALIGN_LEFT);
		mPrinter.setCharacterMultiple(0, 0);
		sb.append("消费前余额:" + a1 + "元\n");
		sb.append("消费后余额:" + a2 + "元\n");
		sb.append("社保卡号:" + a3 + "\n");
		sb.append("\n\n\n\n\n\n");
		mPrinter.printText(sb.toString());
	}

	public static void printNote(String pText, Printer mPrinter) {
		mPrinter.init();
		mPrinter.printText(pText);

	}

	public static void printNoteCount(String pText, Printer mPrinter) {
		mPrinter.init();

		mPrinter.printText(pText);
		mPrinter.printText("\n\n\n");

	}

	public static void printRe(String shanghuhao,String shanghumingcheng,String zhongduanhao,String bankCode,
			String pici,String bankCardNo,String trace,String refNo,int type,
			int Money,Date time,Printer mPrinter,boolean reFlag) {
		String tradeType = getTradeType(type);
		String tradeMoney = FloatCalculator.divide(Money,
						100)+"元";
		String tradeTime =TimeUtil.getWholeDateString(time);
		StringBuffer sb = new StringBuffer();
		sb.append("             银联商务\n"+
				"POS签购单（商户联）   请妥善保管\n"+
				"--------------------------------\n"+
				"商户名称:"+shanghuhao+"\n"+
				"商户号  :"+shanghumingcheng+"\n"+
				"终端号  :"+zhongduanhao+"\n"+
				"日期时间:"+tradeTime+" \n"+
				"发卡行  :"+bankCode+" \n"+
				"卡号    :"+bankCardNo+"\n"+
				"批次号  :"+pici+"  凭证号:"+trace+"\n"+
				"系统参考号:"+refNo+" \n"+
				"交易类型:"+tradeType+"\n"+
				"金额 RMB:"+tradeMoney+"\n");
		if (reFlag) {
			sb.append("       *****重打印*****\n");
		}
		sb.append("--------------------------------\n"+
				"本人确认以上交易,同意将其记入本卡帐户\n"+
				"持卡人签名(SIGNATURE):\n"+
				"\n"+
				"\n"+
				"\n"+
				"\n"+
				"\n"+
				"           银联商务     \n"+
				"POS签购单（持卡人联）  请妥善保管\n"+
				"--------------------------------\n"+
				"商户名称:"+shanghuhao+"\n"+
				"商户号  :"+shanghumingcheng+"\n"+
				"终端号  :"+zhongduanhao+"\n"+
				"日期时间:"+tradeTime+" \n"+
				"发卡行  :"+bankCode+" \n"+
				"卡号    :"+bankCardNo+"\n"+
				"批次号  :"+pici+"  凭证号:"+trace+"\n"+
				"系统参考号:"+refNo+" \n"+
				"交易类型:"+tradeType+"\n"+
				"金额 RMB:"+tradeMoney+"\n");
		if (reFlag) {
			sb.append("       *****重打印*****\n");
		}
		sb.append("--------------------------------\n");
		
		mPrinter.init();
		mPrinter.printText(sb.toString()); 
		
	}
	
	private static String getTradeType(int type){
		String result = "";
		switch (type) {
		case 14:
			result = "社保卡消费";
			break;
		case 16:
			result = "社保卡撤销";
			break;
		case 18:
			result = "银行卡消费";
			break;
		case 11:
			result = "银行卡消费";
			break;
		case 34:
			result = "医院消费";
			break;
		case 36:
			result = "医院撤销";
			break;

		}
		return result;
	}
	
}
