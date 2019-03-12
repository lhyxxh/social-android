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

public class ConsumeListAdapter extends BaseAdapter {

	private List<Trade> mData = new ArrayList<Trade>();
	private Context mContext;
	
	
	public ConsumeListAdapter(Context mContext, List<Trade> mData) {
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
			convertView = inflater.inflate(R.layout.item_consume_list, null);
			holder = new Holder();
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag();
		String dataString = mData.get(arg0).getTrade_time().getTime()/1000+"";
//		holder.tv_date.setText(dataString);
		holder.tv_date.setText(TimeUtil.timeStamp2Date(dataString, "yyyy-MM-dd HH:mm:ss"));
		holder.tv_price.setText((float)(mData.get(arg0).getTrade_money())/100+"元");
		return convertView;
	}

	class Holder {
		TextView tv_date;
		TextView tv_price;
	}
}
