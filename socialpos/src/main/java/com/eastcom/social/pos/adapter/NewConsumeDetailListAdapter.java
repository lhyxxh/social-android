package com.eastcom.social.pos.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.util.FloatCalculator;

public class NewConsumeDetailListAdapter extends BaseAdapter {

	private List<TradeDetail> mData = new ArrayList<TradeDetail>();
	private Context mContext;
	
	
	public NewConsumeDetailListAdapter(Context mContext, List<TradeDetail> mData) {
		super();
		this.mData = mData;
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
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_new_consume_detail_list, null);
			holder = new Holder();
			holder.tv_bar_code = (TextView) convertView.findViewById(R.id.tv_bar_code);
			holder.tv_super_code = (TextView) convertView.findViewById(R.id.tv_super_code);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
			holder.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag();
		holder.tv_bar_code.setText(mData.get(arg0).getBar_code()+"");
		holder.tv_super_code.setText(mData.get(arg0).getSuper_vision_code()+"");
		holder.tv_name.setText(mData.get(arg0).getProduct_name()+"");
		int price = mData.get(arg0).getActual_price();
		int amount = mData.get(arg0).getAmount();
		holder.tv_price.setText((float)price/100+"元");
		holder.tv_amount.setText(mData.get(arg0).getAmount()+"");
		float a = FloatCalculator.multiply(price, amount);
		float b = FloatCalculator.divide(a, 100);
		if (mData.get(arg0).getTrade_type() == 14) {
			holder.tv_sum.setText(b+"元");
		}else {
			holder.tv_sum.setText("-"+b+"元");
		}
		
		return convertView;
	}

	class Holder {
		TextView tv_bar_code;
		TextView tv_super_code;
		TextView tv_name;
		TextView tv_price;
		TextView tv_amount;
		TextView tv_sum;
	}
}
