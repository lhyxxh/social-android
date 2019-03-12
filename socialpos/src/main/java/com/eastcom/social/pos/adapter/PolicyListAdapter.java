package com.eastcom.social.pos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastcom.social.pos.R;

public class PolicyListAdapter extends BaseAdapter {

	private ArrayList<String> mData = new ArrayList<String>();
	private Context mContext;
	
	public PolicyListAdapter(Context mContext, ArrayList<String> list) {
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
			convertView = inflater.inflate(R.layout.item_policy_list, null);
			holder = new Holder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag();
		holder.tv_name.setText(mData.get(arg0));
		return convertView;
	}

	class Holder {
		TextView tv_name;
	}
}
