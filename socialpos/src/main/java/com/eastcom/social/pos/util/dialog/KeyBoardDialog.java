package com.eastcom.social.pos.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.eastcom.social.pos.R;

public class KeyBoardDialog extends Dialog {

	public KeyBoardDialog(Context context) {
		super(context);
	}

	public KeyBoardDialog(Context context, int theme) {
		super(context, theme);
	}
	
	

	public static class Builder implements android.view.View.OnClickListener {
		private Context context;
		private String title;
		private String hint;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private EditText et_sum;
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
		private TextView tv_doc;
		private TextView tv_delete;

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		public Builder setHint(String hint) {
			this.hint = hint;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		// 获取部署点名称
		public String GetSum() {

			return et_sum.getText().toString();
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public KeyBoardDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final KeyBoardDialog dialog = new KeyBoardDialog(context, R.style.Dialog);
			
			View layout = inflater.inflate(R.layout.view_keyboard_dialog, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);
			et_sum = (EditText) layout.findViewById(R.id.et_sum);
			et_sum.setText(hint);
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
			tv_doc = (TextView) layout.findViewById(R.id.tv_doc);
			tv_7 = (TextView) layout.findViewById(R.id.tv_7);
			tv_8 = (TextView) layout.findViewById(R.id.tv_8);
			tv_9 = (TextView) layout.findViewById(R.id.tv_9);
			tv_0 = (TextView) layout.findViewById(R.id.tv_0);
			tv_delete = (TextView) layout.findViewById(R.id.tv_delete);
			tv_7.setOnClickListener(this);
			tv_8.setOnClickListener(this);
			tv_9.setOnClickListener(this);
			tv_0.setOnClickListener(this);
			tv_doc.setOnClickListener(this);
			tv_delete.setOnClickListener(this);
			et_sum.setImeOptions(EditorInfo.IME_ACTION_DONE);
			et_sum.setInputType(InputType.TYPE_NULL);
			// set the confirm button
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
			// set the cancel button
			if (negativeButtonText != null) {
				((TextView) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((TextView) layout.findViewById(R.id.negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			dialog.setContentView(layout);
			return dialog;
		}

		@Override
		public void onClick(View v) {
			String oldString = et_sum.getText().toString();
			int len = oldString.length();
			switch (v.getId()) {
			case R.id.tv_1:
				et_sum.setText(oldString + "1");
				break;
			case R.id.tv_2:
				et_sum.setText(oldString + "2");
				break;
			case R.id.tv_3:
				et_sum.setText(oldString + "3");
				break;
			case R.id.tv_4:
				et_sum.setText(oldString + "4");
				break;
			case R.id.tv_5:
				et_sum.setText(oldString + "5");
				break;
			case R.id.tv_6:
				et_sum.setText(oldString + "6");
				break;
			case R.id.tv_7:
				et_sum.setText(oldString + "7");
				break;
			case R.id.tv_8:
				et_sum.setText(oldString + "8");
				break;
			case R.id.tv_9:
				et_sum.setText(oldString + "9");
				break;
			case R.id.tv_0:
				et_sum.setText(oldString + "0");
				break;
			case R.id.tv_doc:
				if (oldString.contains(".")||"".equals(oldString)) {
					return;
				}else {
					et_sum.setText(oldString + ".");
				}
				
				break;
			case R.id.tv_delete:
				if (len>0) {
					et_sum.setText(oldString.substring(0, len - 1));
				}
				break;

			default:
				break;
			}
			String newString = et_sum.getText().toString();
			et_sum.setSelection(newString.length());
		}
	}
}
