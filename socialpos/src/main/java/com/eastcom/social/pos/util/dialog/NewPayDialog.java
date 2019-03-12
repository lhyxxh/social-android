package com.eastcom.social.pos.util.dialog;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.adapter.NumberAdapter;

public class NewPayDialog extends Dialog {

	public interface OnConfirmListener {
		void onClick(String s, DialogInterface dialog);
	}

	public interface OnCancelListener {
		void onClick(DialogInterface dialog);
	}

	public NewPayDialog(Context context) {
		super(context);
	}

	public NewPayDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context mContext;
		private String mTitle;
		private String mPositiveButtonText;
		private String mNegativeButtonText;
		private OnConfirmListener mPositiveButtonClickListener;
		private OnCancelListener mNegativeButtonClickListener;
		private ArrayList<String> mThumbIds = new ArrayList<String>();

		private final StringBuilder sb = new StringBuilder();

		public Builder(Context mContext) {
			this.mContext = mContext;
		}

		public Builder setTitle(String mTitle) {
			this.mTitle = mTitle;
			return this;
		}

		public Builder setContent(ArrayList<String> mThumbIds) {
			this.mThumbIds = mThumbIds;
			return this;
		}

		public Builder setPositiveButtonText(String mPositiveButtonText) {
			this.mPositiveButtonText = mPositiveButtonText;
			return this;
		}

		public Builder setNegativeButtonText(String mNegativeButtonText) {
			this.mNegativeButtonText = mNegativeButtonText;
			return this;
		}

		public Builder setPositiveButtonClickListener(
				OnConfirmListener mPositiveButtonClickListener) {
			this.mPositiveButtonClickListener = mPositiveButtonClickListener;
			return this;
		}

		public Builder setNegativeButtonClickListener(
				OnCancelListener mNegativeButtonClickListener) {
			this.mNegativeButtonClickListener = mNegativeButtonClickListener;
			return this;
		}

		public NewPayDialog create() {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.new_pay_dialog, null); // 这里的view必须是final，否则设置layout.tv_amount为final的时候要报错。
			final NewPayDialog dialog = new NewPayDialog(mContext,
					R.style.Dialog);
			
			// dialog.addContentView(layout, new LayoutParams(
			// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(mTitle);
			// set the confirm button
			if (mPositiveButtonText != null) {
				((Button) layout.findViewById(R.id.positive_button))
						.setText(mPositiveButtonText);
				if (mPositiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positive_button))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									mPositiveButtonClickListener.onClick(
											sb.toString(), dialog);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positive_button).setVisibility(
						View.GONE);
			}

			// set the cancel button
			if (mNegativeButtonText != null) {
				((TextView) layout.findViewById(R.id.negative_button))
						.setText(mNegativeButtonText);
				if (mNegativeButtonClickListener != null) {
					((TextView) layout.findViewById(R.id.negative_button))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									mNegativeButtonClickListener
											.onClick(dialog);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negative_button).setVisibility(
						View.GONE);
			}

			final TextView tv_amount = (TextView) layout
					.findViewById(R.id.tv_amount);
			GridView gridview = (GridView) layout.findViewById(R.id.gridview);
			NumberAdapter adapter = new NumberAdapter(mContext, mThumbIds);
			adapter.setOnItemClickListener(new NumberAdapter.OnItemClickListener() {

				@Override
				public void onItemClick(View view, int position) {
					setAmount(tv_amount, position);
				}
			});
			gridview.setAdapter(adapter);
			dialog.setContentView(layout);
			return dialog;

		}

		private void setAmount(TextView tv_amount, int position) {
			switch (position) {
			case 0:
				sb.append(1);
				break;
			case 1:
				sb.append(2);
				break;
			case 2:
				sb.append(3);
				break;
			case 3:
				sb.append(4);
				break;
			case 4:
				sb.append(5);
				break;
			case 5:
				sb.append(6);
				break;
			case 6:
				sb.append(7);
				break;
			case 7:
				sb.append(8);
				break;
			case 8:
				sb.append(9);
				break;
			case 9:
				sb.append(0);
				break;
			case 10:
				if (!(sb.toString().indexOf('.') >= 0) && sb.length() > 0) { 
					sb.append(".");
				}
				break;
			case 11:
				if (sb.length() > 0) {
					sb.setLength(sb.length() - 1);
				}
				break;
			default:
				break;
			}

			tv_amount.setText(sb);
		}

		public NewPayDialog show() {
			final NewPayDialog dialog = create();
			dialog.show();
			return dialog;
		}
	}
}
