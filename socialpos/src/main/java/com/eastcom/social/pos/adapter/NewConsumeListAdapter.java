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
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.util.TimeUtil;

public class NewConsumeListAdapter extends BaseAdapter {

	private List<Trade> mData = new ArrayList<Trade>();
	private Context mContext;
	
	
	public NewConsumeListAdapter(Context mContext, List<Trade> mData) {
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
			convertView = inflater.inflate(R.layout.item_new_consume_list, null);
			holder = new Holder();
			holder.tv_trade_no = (TextView) convertView.findViewById(R.id.tv_trade_no);
			holder.tv_trade_time = (TextView) convertView.findViewById(R.id.tv_trade_time);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			holder.tv_card_no = (TextView) convertView.findViewById(R.id.tv_card_no);
			holder.tv_card_amount = (TextView) convertView.findViewById(R.id.tv_card_amount);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag();
		holder.tv_trade_no.setText(mData.get(arg0).getTrace()+"");
		String dataString = mData.get(arg0).getTrade_time().getTime()/1000+"";
		holder.tv_trade_time.setText(TimeUtil.timeStamp2Date(dataString, "yyyy-MM-dd HH:mm:ss"));
		if (mData.get(arg0).getTrade_type() == 14) {
			holder.tv_price.setText((float)(mData.get(arg0).getTrade_money())/100+"元");
		}else{
			holder.tv_price.setText("-"+(float)(mData.get(arg0).getTrade_money())/100+"元");
		}
			
		
		holder.tv_card_no.setText(mData.get(arg0).getSocial_card_no()+"");
		holder.tv_card_amount.setText(mData.get(arg0).getAmount()+"");
		return convertView;
	}

	class Holder {
		TextView tv_trade_no;
		TextView tv_trade_time;
		TextView tv_price;
		TextView tv_card_no;
		TextView tv_card_amount;
	}
}
