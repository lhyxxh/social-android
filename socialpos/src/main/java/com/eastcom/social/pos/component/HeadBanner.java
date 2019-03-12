package com.eastcom.social.pos.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.util.StringExUtil;

public class HeadBanner extends RelativeLayout {

	public OnClickListener onBackClickListener;
	public OnClickListener onMenuClickListener;

	private TextView mTextViewTitle;
	private ImageView mImageViewBack;
	private ImageView mImageViewMenu;

	private Context mContext;

	public HeadBanner(Context context) {
		super(context);
		mContext = context;
	}

	public HeadBanner(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.head_banner, this);

		// 初始化视图
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.HeadBanner);
		String title = a.getString(R.styleable.HeadBanner_android_text);
		mTextViewTitle = (TextView) findViewById(R.id.TextViewTitle);
		if (!StringExUtil.isNullOrEmpty(title)) {
			mTextViewTitle.setText(title);
		}
		mImageViewBack = (ImageView) findViewById(R.id.imageViewBack);
		mImageViewBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Animation animAlpha = AnimationUtils.loadAnimation(mContext,
						R.anim.alpha_action);
				v.startAnimation(animAlpha);
				if (null != onBackClickListener)
					onBackClickListener.onClick(v);
			}
		});

		mImageViewMenu = (ImageView) findViewById(R.id.imageViewMenu);
		mImageViewMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Animation animAlpha = AnimationUtils.loadAnimation(mContext,
						R.anim.alpha_action);
				v.startAnimation(animAlpha);
				if (null != onMenuClickListener)
					onMenuClickListener.onClick(v);
			}
		});
	}

	/* 设置菜单按钮资源 */
	public void setMenuResource(int resId) {
		mImageViewMenu.setImageResource(resId);
		mImageViewMenu.setVisibility(View.VISIBLE);
	}

	/* 设置标题 */
	public void setTitle(String title) {
		mTextViewTitle.setText(title);
	}

	/* 获取标题 */
	public String getTitle() {
		return mTextViewTitle.getText().toString();
	}
}
