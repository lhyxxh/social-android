package com.eastcom.social.pos.util.dialog;

import com.eastcom.social.pos.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class CustomWaitDialog {
	private Context mContext;
	private ImageView image;
	private ImageView loading_pic_bigView;
	private Dialog waitDialog;
	private Animation mAnimation;

	public CustomWaitDialog(Context mContext) {
		this.mContext = mContext;
		waitDialog = new Dialog(mContext, R.style.common_dialog);
		waitDialog.setContentView(R.layout.loading);
		waitDialog.setCanceledOnTouchOutside(false);

		Window window = waitDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		lp.dimAmount = 0.8f;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		loading_pic_bigView = (ImageView) waitDialog
				.findViewById(R.id.loading_pic_bigView);
		loading_pic_bigView.setAlpha(0.6f);

		image = (ImageView) waitDialog.findViewById(R.id.loading_pic_big);
		mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading);
	}

	public void show() {
		image.startAnimation(mAnimation);
		waitDialog.show();
	}

	public void dismiss() {
		waitDialog.dismiss();
	}
	
	public void setOnDismissListener(
			DialogInterface.OnDismissListener dismissListener) {
		waitDialog.setOnDismissListener(dismissListener);
	}
}
