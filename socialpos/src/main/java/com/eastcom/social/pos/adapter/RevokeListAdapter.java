package com.eastcom.social.pos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;

public class RevokeListAdapter extends BaseAdapter {

	private ArrayList<TradeDetail> mData = new ArrayList<TradeDetail>();
	private Context mContext;
	
	public RevokeListAdapter(Context mContext, ArrayList<TradeDetail> list) {
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
			convertView = inflater.inflate(R.layout.item_revoke, null);
			holder = new Holder();
			holder.tv_bar_code = (TextView) convertView.findViewById(R.id.tv_bar_code);
			holder.tv_super_code = (TextView) convertView.findViewById(R.id.tv_super_code);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag();
		holder.tv_bar_code.setText(mData.get(arg0).getBar_code());
		holder.tv_super_code.setText(mData.get(arg0).getSuper_vision_code());
		holder.tv_name.setText(mData.get(arg0).getProduct_name());
		int num = mData.get(arg0).getAmount();
		holder.tv_num.setText(num+"");
		float unit = (float) (mData.get(arg0).getActual_price()) / 100;
		holder.tv_unit.setText(unit + "元");
		holder.tv_price.setText(unit * num + "元");
		return convertView;
	}

	class Holder {
		TextView tv_bar_code;
		TextView tv_super_code;
		TextView tv_name;
		TextView tv_num;
		TextView tv_unit;
		TextView tv_price;
	}

}
