package com.eastcom.social.pos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.entity.MyTradeDetail;

public class PayListAdapter extends BaseAdapter implements OnClickListener{

	private ArrayList<MyTradeDetail> mData = new ArrayList<MyTradeDetail>();
	private Context mContext;
	private MyCallback mCallback;
	
	public PayListAdapter(Context mContext, ArrayList<MyTradeDetail> list,MyCallback callback) {
		super();
		this.mData = list;
		this.mContext = mContext;
		this.mCallback = callback;
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
			convertView = inflater.inflate(R.layout.item_new_pay_list, null);
			holder = new Holder();
			holder.et_barcode = (TextView) convertView.findViewById(R.id.et_barcode);
			holder.et_super_code = (TextView) convertView.findViewById(R.id.et_super_code);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
			holder.iv_reduce = (ImageView) convertView.findViewById(R.id.iv_reduce);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.ll_delete = (LinearLayout) convertView
					.findViewById(R.id.ll_delete);
			convertView.setTag(holder);
		}
		holder = (Holder) convertView.getTag();
		holder.et_barcode.setText(mData.get(arg0).getDr().getBar_code());
		holder.et_super_code.setText(mData.get(arg0).getDr().getSuper_code());
		holder.tv_name.setText(mData.get(arg0).getTd().getProduct_name());
		float unit = (float) (mData.get(arg0).getTd().getActual_price()) / 100;
		holder.tv_unit.setText(unit + "元");
		int num = mData.get(arg0).getTd().getAmount();
		holder.tv_num.setText(num + "");
		holder.tv_price.setText(unit * num + "元");
		holder.ll_delete.setOnClickListener(this);
		holder.ll_delete.setTag(arg0);
		holder.iv_add.setOnClickListener(this);
		holder.iv_add.setTag(arg0);
		holder.iv_reduce.setOnClickListener(this);
		holder.iv_reduce.setTag(arg0);
		holder.tv_unit.setOnClickListener(this);
		holder.tv_unit.setTag(arg0);
		if (!"".equals(holder.et_super_code.getText().toString())) {
			holder.iv_add.setVisibility(View.GONE);
			holder.iv_reduce.setVisibility(View.GONE);
		}else {
			holder.iv_add.setVisibility(View.VISIBLE);
			holder.iv_reduce.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class Holder {
		TextView et_barcode;
		TextView et_super_code;
		TextView tv_name;
		TextView tv_unit;
		TextView tv_num;
		ImageView iv_add;
		ImageView iv_reduce;
		TextView tv_price;
		LinearLayout ll_delete;
	}

	/**
	 * 自定义接口，用于回调按钮点击事件到Activity
	 * 
	 * @author Ivan Xu 2014-11-26
	 */
	public interface MyCallback {
		void deleteClick(View v);
		void addClick(View v);
		void reduceClick(View v);
		void showUnit(View v);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add:
			mCallback.addClick(v);
			break;
		case R.id.iv_reduce:
			mCallback.reduceClick(v);
			break;
		case R.id.ll_delete:
			mCallback.deleteClick(v);
			break;
		case R.id.tv_unit:
			mCallback.showUnit(v);
			break;

		default:
			break;
		}
		 
	}
}
