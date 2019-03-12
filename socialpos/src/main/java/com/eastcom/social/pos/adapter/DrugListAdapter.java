package com.eastcom.social.pos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.entity.Drug;

public class DrugListAdapter extends BaseAdapter {

	private ArrayList<Drug> mData = new ArrayList<Drug>();
	private Context mContext;
	
	public DrugListAdapter(Context mContext, ArrayList<Drug> list) {
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
			convertView = inflater.inflate(R.layout.item_drug_list, null);
			holder = new Holder();
			holder.tv_bar_code = (TextView) convertView.findViewById(R.id.tv_bar_code);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_production_unit = (TextView) convertView.findViewById(R.id.tv_production_unit);
			
			holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
			holder.tv_specification = (TextView) convertView.findViewById(R.id.tv_specification);
			holder.tv_dosage_form = (TextView) convertView.findViewById(R.id.tv_dosage_form);
			holder.tv_social_category = (TextView) convertView
					.findViewById(R.id.tv_social_category);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag();
		holder.tv_bar_code.setText(mData.get(arg0).getBar_code());
		holder.tv_name.setText(mData.get(arg0).getName());
		holder.tv_production_unit.setText(mData.get(arg0).getProduction_unit());
		holder.tv_unit.setText(mData.get(arg0).getOriginal_price()+"");
		holder.tv_specification.setText(mData.get(arg0).getSpecification());
		holder.tv_dosage_form.setText(mData.get(arg0).getDosage_form());
		holder.tv_social_category.setText(mData.get(arg0).getSocial_category()+"");
		return convertView;
	}

	class Holder {
		TextView tv_bar_code;
		TextView tv_name;
		TextView tv_production_unit;
		TextView tv_unit;
		TextView tv_specification;
		TextView tv_dosage_form;
		TextView tv_social_category;
	}


}
