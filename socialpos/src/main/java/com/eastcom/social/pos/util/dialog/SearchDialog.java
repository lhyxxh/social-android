package com.eastcom.social.pos.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.eastcom.social.pos.R;

public class SearchDialog extends Dialog {

	public SearchDialog(Context context) {
		super(context);
	}

	public SearchDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		
		private View contentView;
		
		private DialogInterface.OnClickListener bankButtonClickListener;
		private DialogInterface.OnClickListener socialButtonClickListener;
		private TextView tv_bank;
		private TextView tv_social;

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

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		

		public Builder setBankButton(DialogInterface.OnClickListener listener) {
			this.bankButtonClickListener = listener;
			return this;
		}

		public Builder setSocialButton(DialogInterface.OnClickListener listener) {
			this.socialButtonClickListener = listener;
			return this;
		}

		public SearchDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final SearchDialog dialog = new SearchDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.view_search_dialog, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);

			

			
			if (bankButtonClickListener != null) {
				((TextView) layout.findViewById(R.id.tv_bank))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								bankButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_NEGATIVE);

							}
						});
			}
		

			
			if (socialButtonClickListener != null) {
				((TextView) layout.findViewById(R.id.tv_social))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								socialButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_NEGATIVE);

							}
						});
			}
		
			dialog.setContentView(layout);
			return dialog;
		}

	}
}
