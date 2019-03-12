package com.eastcom.social.pos.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.core.socket.message.SoSocialCardType;
import com.eastcom.social.pos.entity.CardInfo;
import com.eastcom.social.pos.util.PhoneFormatCheckUtils;
import com.eastcom.social.pos.util.ToastUtil;

public class CardInfoDialog extends Dialog {

	public CardInfoDialog(Context context) {
		super(context);
	}

	public CardInfoDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder implements android.view.View.OnClickListener {
		private Context context;
		private String positiveButtonText;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener cancleButtonClickListener;
		private CardInfo cardInfo;
		private String balance = "";
		private int isBlackCard = 0;
		private String tel;
		private TextView tv_tel;
		private TextView tv_social_card_no;
		private TextView tv_id_card_no;
		private TextView tv_name;
		private TextView tv_sex;
		private TextView tv_start_date;
		private TextView tv_end_date;
		private TextView tv_balance;
		private TextView tv_black_card;

		private LinearLayout ll_cardinfo;
		private LinearLayout ll_tel_num;

		private TextView tv_1;
		private TextView tv_2;
		private TextView tv_3;
		private TextView tv_4;
		private TextView tv_5;
		private TextView tv_6;
		private TextView tv_7;
		private TextView tv_8;
		private TextView tv_9;
		private TextView tv_0;
		private TextView tv_delete;
		private TextView tv_num_nagetive;
		private TextView tv_num_positive;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setCancleButton(DialogInterface.OnClickListener listener) {
			this.cancleButtonClickListener = listener;
			return this;
		}

		public Builder setCardInfo(CardInfo cardInfo) {
			this.cardInfo = cardInfo;
			return this;
		}

		public Builder setBalance(String balance) {
			this.balance = balance;
			return this;
		}

		public Builder setIsBlackCard(int isBlackCard) {
			this.isBlackCard = isBlackCard;
			return this;
		}

		public String GetTel() {
			return tv_tel.getText().toString();
		}

		public void SetTel(String tel) {
			this.tel = tel;
		}

		public CardInfoDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CardInfoDialog dialog = new CardInfoDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.view_cardinfo_dialog, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			tv_tel = (TextView) layout.findViewById(R.id.tv_tel);
			tv_social_card_no = (TextView) layout
					.findViewById(R.id.tv_social_card_no);
			tv_id_card_no = (TextView) layout.findViewById(R.id.tv_id_card_no);
			tv_name = (TextView) layout.findViewById(R.id.tv_name);
			tv_sex = (TextView) layout.findViewById(R.id.tv_sex);
			tv_start_date = (TextView) layout.findViewById(R.id.tv_start_date);
			tv_end_date = (TextView) layout.findViewById(R.id.tv_end_date);
			tv_balance = (TextView) layout.findViewById(R.id.tv_balance);
			tv_black_card = (TextView) layout.findViewById(R.id.tv_black_card);
			tv_social_card_no.setText(StringCode(cardInfo.getSocial_card_no()));
			tv_id_card_no.setText(StringCode(cardInfo.getId_card_no()));
			tv_name.setText(StringCode(cardInfo.getName()));
			tv_sex.setText(StringCode(cardInfo.getSex()));
			tv_start_date.setText(StringCode(cardInfo.getStartDate()));
			tv_end_date.setText(StringCode(cardInfo.getEndDtae()));
			if (!"".equals(balance)) {
				tv_balance.setText(balance);
			}
			if ("00000000000".equals(tel) || "".equals(tel)) {
				tv_tel.setText("");
			} else {
				tv_tel.setText(tel);
			}
			String msg = SoSocialCardType.getSocialCardType(isBlackCard);
			tv_black_card.setText(msg);
			if (positiveButtonText != null) {
				((TextView) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((TextView) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			if (cancleButtonClickListener != null) {
				((TextView) layout.findViewById(R.id.cancleButton))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								cancleButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_POSITIVE);
							}
						});
			}
			tv_tel.setOnClickListener(this);
			ll_cardinfo = (LinearLayout) layout.findViewById(R.id.ll_cardinfo);
			ll_tel_num = (LinearLayout) layout.findViewById(R.id.ll_tel_num);
			tv_num_nagetive = (TextView) layout
					.findViewById(R.id.tv_num_nagetive);
			tv_num_positive = (TextView) layout
					.findViewById(R.id.tv_num_positive);
			tv_num_nagetive.setOnClickListener(this);
			tv_num_positive.setOnClickListener(this);
			tv_2 = (TextView) layout.findViewById(R.id.tv_2);
			tv_1 = (TextView) layout.findViewById(R.id.tv_1);
			tv_3 = (TextView) layout.findViewById(R.id.tv_3);
			tv_4 = (TextView) layout.findViewById(R.id.tv_4);
			tv_5 = (TextView) layout.findViewById(R.id.tv_5);
			tv_6 = (TextView) layout.findViewById(R.id.tv_6);
			tv_2.setOnClickListener(this);
			tv_3.setOnClickListener(this);
			tv_1.setOnClickListener(this);
			tv_5.setOnClickListener(this);
			tv_6.setOnClickListener(this);
			tv_4.setOnClickListener(this);
			tv_7 = (TextView) layout.findViewById(R.id.tv_7);
			tv_8 = (TextView) layout.findViewById(R.id.tv_8);
			tv_9 = (TextView) layout.findViewById(R.id.tv_9);
			tv_0 = (TextView) layout.findViewById(R.id.tv_0);
			tv_delete = (TextView) layout.findViewById(R.id.tv_delete);
			tv_7.setOnClickListener(this);
			tv_8.setOnClickListener(this);
			tv_9.setOnClickListener(this);
			tv_0.setOnClickListener(this);
			tv_delete.setOnClickListener(this);
			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog;
		}

		private String StringCode(String pString) {
			StringBuilder sb = new StringBuilder(pString);
			int length = sb.length();
			if (length > 4) {
				for (int i = 0; i < length - 4; i++) {
					sb.replace(i, i + 1, "*");
				}
			}
			if (length < 4 && length > 1) {
				for (int i = 0; i < length - 1; i++) {
					sb.replace(i, i + 1, "*");
				}
			}
			return sb.toString();
		}

		@Override
		public void onClick(View v) {
				String oldString = tv_tel.getText().toString();
				int len = oldString.length();
				switch (v.getId()) {
				case R.id.tv_1:
					tv_tel.setText(oldString + "1");
					break;
				case R.id.tv_2:
					tv_tel.setText(oldString + "2");
					break;
				case R.id.tv_3:
					tv_tel.setText(oldString + "3");
					break;
				case R.id.tv_4:
					tv_tel.setText(oldString + "4");
					break;
				case R.id.tv_5:
					tv_tel.setText(oldString + "5");
					break;
				case R.id.tv_6:
					tv_tel.setText(oldString + "6");
					break;
				case R.id.tv_7:
					tv_tel.setText(oldString + "7");
					break;
				case R.id.tv_8:
					tv_tel.setText(oldString + "8");
					break;
				case R.id.tv_9:
					tv_tel.setText(oldString + "9");
					break;
				case R.id.tv_0:
					tv_tel.setText(oldString + "0");
					break;
				case R.id.tv_delete:
					if (len > 0) {
						tv_tel.setText(oldString.substring(0, len - 1));
					}
					break;
				case R.id.tv_num_nagetive:
					tv_tel.setText("");
					ll_cardinfo.setVisibility(View.VISIBLE);
					ll_tel_num.setVisibility(View.GONE);
					break;
				case R.id.tv_num_positive:
					String newString = tv_tel.getText().toString();
					if (!PhoneFormatCheckUtils.isChinaPhoneLegal(newString)) {
						ToastUtil.show(context, "输入手机号格式不正确,请重新输入", 5000);
					}else{
						ll_cardinfo.setVisibility(View.VISIBLE);
						ll_tel_num.setVisibility(View.GONE);
					}
					break;
				case R.id.tv_tel:
					ll_cardinfo.setVisibility(View.GONE);
					ll_tel_num.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
		}
	}

}
