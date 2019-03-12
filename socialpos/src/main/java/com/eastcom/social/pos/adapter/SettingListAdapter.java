package com.eastcom.social.pos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.core.orm.entity.Trade;

public class SettingListAdapter extends BaseAdapter {

	private ArrayList<Trade> mData = new ArrayList<Trade>();
	private Context mContext;

	public SettingListAdapter(Context mContext, ArrayList<Trade> list) {
		super();
		this.mData = list;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		Holder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_setting_list, null);
			holder = new Holder();
			holder.textView1 = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.textView2 = (TextView) convertView
					.findViewById(R.id.textView2);
			holder.textView3 = (TextView) convertView
					.findViewById(R.id.textView3);
			holder.textView4 = (TextView) convertView
					.findViewById(R.id.textView4);
			holder.textView5 = (TextView) convertView
					.findViewById(R.id.textView5);
			holder.textView6 = (TextView) convertView
					.findViewById(R.id.textView6);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag();
		holder.textView1.setText("流水号"+mData.get(arg0).getTrace());
		if (mData.get(arg0).getIs_upload() == 0) {
			holder.textView2.setText("已上传");
		} else {
			holder.textView2.setText("未上传");
		}
		holder.textView3.setText(mData.get(arg0).getTrade_money() + "分");
		holder.textView4.setText("银行卡号"+mData.get(arg0).getBank_card_no());
		holder.textView5.setText("重发次数"+mData.get(arg0).getSend_count());
		holder.textView6.setText("社保卡号"+mData.get(arg0).getSocial_card_no());
		return convertView;
	}

	class Holder {
		TextView textView1;
		TextView textView2;
		TextView textView3;
		TextView textView4;
		TextView textView5;
		TextView textView6;
	}

}
