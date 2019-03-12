package com.eastcom.social.pos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;

public class ConsumeDetailListAdapter extends BaseAdapter implements OnClickListener{

	private ArrayList<TradeDetail> mData = new ArrayList<TradeDetail>();
	private Context mContext;
	
	public ConsumeDetailListAdapter(Context mContext, ArrayList<TradeDetail> list) {
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
			convertView = inflater.inflate(R.layout.item_consume_detail_list, null);
			holder = new Holder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag();
		holder.tv_name.setText(mData.get(arg0).getProduct_name());
		float unit = (float) (mData.get(arg0).getActual_price()) / 100;
		holder.tv_unit.setText(unit + "元/盒");
		int num = mData.get(arg0).getAmount();
		holder.tv_num.setText(num + "");
		holder.tv_price.setText(unit * num + "元");
		return convertView;
	}

	class Holder {
		TextView tv_name;
		TextView tv_unit;
		TextView tv_num;
		TextView tv_price;
	}

	@Override
	public void onClick(View v) {
	}
}
