package com.eastcom.social.pos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastcom.social.pos.R;

public class NumberAdapter extends BaseAdapter {

	// gridview的items点击事件接口
	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}

	private ArrayList<String> mThumbIds = null;

	private Context mContext;
	private OnItemClickListener mOnItemClickListener;

	public NumberAdapter(Context mContext, ArrayList<String> mThumbIds) {
		this.mContext = mContext;
		this.mThumbIds = mThumbIds;
	}

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

	@Override
	public int getCount() {
		return mThumbIds.size();
	}

	@Override
	public Object getItem(int position) {
		return mThumbIds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.view_number, null);
			holder = new ViewHolder();
			holder.lable = (TextView) convertView.findViewById(R.id.lable);
			holder.clear = (ImageView) convertView.findViewById(R.id.clear);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.lable.setText(mThumbIds.get(position));
		// 为了支持删除数字的icon在position ==
		// 11的位置显示一个imageview,一定要保证传进来的mThumbIds的size大于这个位置的position，比如现在传进来的mThumbIds的size为12，那么就可以在position
		// == 11的地方显示一个icon，否则将不现实这个icon。
		if (position == 11) {
			holder.lable.setVisibility(View.GONE);
			holder.clear.setVisibility(View.VISIBLE);
			holder.clear.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mOnItemClickListener.onItemClick(v, position);
				}
			});
		}
		holder.lable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(v, position);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView lable;
		ImageView clear;
	}
}
