package com.eastcom.social.pos.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.entity.ChartEntity;
import com.eastcom.social.pos.util.FloatCalculator;
import com.eastcom.social.pos.util.MyYAxisValueFormatter;
import com.eastcom.social.pos.util.TimeUtil;
import com.eastcom.social.pos.util.ToastUtil;
import com.eastcom.social.pos.util.dialog.DateTimePickDialogUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ChartActivity extends Activity implements OnClickListener {

	private BarChart mChart;
	private Typeface mTf;
	private GetChartDataTask mGetChartDataTask;
	private List<ChartEntity> mChartData = new ArrayList<ChartEntity>();
//	private List<ChartEntity> mChartDataPay = new ArrayList<ChartEntity>();
//	private List<ChartEntity> mChartDataConsume = new ArrayList<ChartEntity>();
	private TradeService tradeService;
	private List<List<Trade>> mTradeLists = new ArrayList<List<Trade>>();
	private int type = 1;

	private EditText et_trade_start;
	private TextView tv_type;
	private TextView tv_total;
	private TextView tv_count;
	private TextView tv_total_p;
	private TextView tv_total_c;
	private Button btn_search;

	private RelativeLayout rl4;
	private RelativeLayout rl3;
	private RelativeLayout rl2;
	private RelativeLayout rl1;

	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		tradeService = TradeService.getInstance(ChartActivity.this);
		initView();
		initData();

	}

	private void initData() {
		String dateStartString = TimeUtil.getDateStringZeroTime(new Date(), -6);
		et_trade_start.setText(dateStartString);
		startGetChartDataTask(type);
	}

	private void initView() {
		rl4 = (RelativeLayout) findViewById(R.id.rl4);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);

		rl4.setOnClickListener(this);
		rl3.setOnClickListener(this);
		rl2.setOnClickListener(this);
		rl1.setOnClickListener(this);

		tv_total = (TextView) findViewById(R.id.tv_total);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_total_c = (TextView) findViewById(R.id.tv_total_c);
		tv_total_p = (TextView) findViewById(R.id.tv_total_p);
		et_trade_start = (EditText) findViewById(R.id.et_trade_start);
		tv_type = (TextView) findViewById(R.id.tv_type);
		et_trade_start.setInputType(InputType.TYPE_NULL);
		tv_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 统计类型选择框
				seleteType();
			}
		});
		et_trade_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Date date_start = null;
				if ("".equals(et_trade_start.getText().toString())) {
					date_start = new Date();
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						date_start = sdf.parse(et_trade_start.getText()
								.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				DateTimePickDialogUtil dateTimePicKDialog_start = new DateTimePickDialogUtil(
						ChartActivity.this, TimeUtil.getDateStringZeroTime(
								date_start, 0), 0);
				dateTimePicKDialog_start.dateTimePicKDialog(et_trade_start);
			}
		});
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		initChartView();
		initDialog();
	}

	private void initDialog() {
		final String[] typeString = new String[] { "天/周", "周/月", "月/年" };
		dialog = new AlertDialog.Builder(ChartActivity.this)
				.setTitle("请选择统计方式")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[] { "天/周", "周/月", "月/年" }, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								type = which + 1;
								tv_type.setText(typeString[which]);
								dialog.dismiss();
								if (type == 1) {
									String dateStartString = TimeUtil.getDateStringZeroTime(new Date(), -6);
									et_trade_start.setText(dateStartString);
								}else if (type == 2){
									Date e_date = new Date();// 搜索时间当天
									Calendar calendar = Calendar.getInstance();
									int y = e_date.getYear()+1900;
									int m = e_date.getMonth();
									calendar.set(y, m,
											1);
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String dateTime = sdf.format(calendar.getTime());
									et_trade_start.setText(dateTime);
								}else if (type == 3){
									Date e_date = new Date();// 搜索时间当天
									Calendar calendar = Calendar.getInstance();
									int y = e_date.getYear()+1900;
									int m = 0;
									calendar.set(y, m,
											1);
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String dateTime = sdf.format(calendar.getTime());
									et_trade_start.setText(dateTime);
								}
							}
						}).setNegativeButton("取消", null).create();

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_search:
			tv_count.setText("");
			tv_total.setText("");
			startGetChartDataTask(type);
			break;
		case R.id.rl4:
			intent.setClass(ChartActivity.this, PolicyWordActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl3:
			intent.setClass(ChartActivity.this, ChartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl2:
			intent.setClass(ChartActivity.this, NewConsumeActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.rl1:
			finish();
			break;
		default:
			break;
		}
	}

	private void seleteType() {
		dialog.show();
	}

	private void initChartView() {
		mChart = (BarChart) findViewById(R.id.chart1);
		mChart.setDrawBarShadow(false);
		mChart.setDrawValueAboveBar(true);
		mChart.setDescription("");
		mChart.setMaxVisibleValueCount(60);
		mChart.setPinchZoom(false);
		mChart.setDrawGridBackground(false);
		mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setTypeface(mTf);
		xAxis.setDrawGridLines(false);
		xAxis.setSpaceBetweenLabels(2);
		YAxisValueFormatter custom = new MyYAxisValueFormatter();
		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setTypeface(mTf);
		leftAxis.setLabelCount(8, false);
		leftAxis.setValueFormatter(custom);
		leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
		leftAxis.setSpaceTop(15f);
		YAxis rightAxis = mChart.getAxisRight();
		rightAxis.setDrawGridLines(false);
		rightAxis.setTypeface(mTf);
		rightAxis.setLabelCount(8, false);
		rightAxis.setValueFormatter(custom);
		rightAxis.setSpaceTop(15f);
		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.BELOW_CHART_LEFT);
		l.setForm(LegendForm.SQUARE);
		l.setFormSize(9f);
		l.setTextSize(11f);
		l.setXEntrySpace(4f);
		mChart.setScaleEnabled(false);
		mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
				ChartEntity chartEntity = mChartData.get(e.getXIndex());
//				ChartEntity chartEntity_p = mChartDataPay.get(e.getXIndex());
//				ChartEntity chartEntity_c = mChartDataConsume.get(e.getXIndex());
				ToastUtil.show(ChartActivity.this,
						"当日收入:" + chartEntity.getIncome()+ ",消费总笔数:"
								+ chartEntity.getCount(), 5000);
			}

			@Override
			public void onNothingSelected() {

			}
		});

	}

	/*
	 * 通过数据源组合Chart数据
	 */
	public BarData fillChartData(List<ChartEntity> source1) {
		ArrayList<String> xVals = new ArrayList<String>();
		ArrayList<BarEntry> yValsTakeBack1 = new ArrayList<BarEntry>();
//		ArrayList<BarEntry> yValsTakeBack2 = new ArrayList<BarEntry>();
//		ArrayList<BarEntry> yValsTakeBack3 = new ArrayList<BarEntry>();
		for (int j = 0; j < source1.size(); j++) {
			xVals.add(source1.get(j).getDate());
			yValsTakeBack1.add(new BarEntry((float) source1.get(j).getIncome(), j));
//			yValsTakeBack2.add(new BarEntry((float) source2.get(j).getIncome(), j));
//			yValsTakeBack3.add(new BarEntry((float) source3.get(j).getIncome(), j));
		}
			
		BarDataSet set1 = new BarDataSet(yValsTakeBack1, "社保卡交易");
		set1.setBarSpacePercent(50f);
		set1.setColor(Color.BLUE);
		
//		BarDataSet set2 = new BarDataSet(yValsTakeBack2, "社保卡消費");
//		set2.setBarSpacePercent(50f);
//		set2.setColor(Color.GREEN);
//		BarDataSet set3 = new BarDataSet(yValsTakeBack3, "社保卡撤销");
//		set3.setBarSpacePercent(50f);
//		set3.setColor(Color.RED);

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);
//		dataSets.add(set2);
//		dataSets.add(set3);

		BarData data = new BarData(xVals, dataSets);
		data.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat  mFormat = new DecimalFormat("###,###,###,##0.00");
				 return mFormat.format(value) + "元";
			}
		});
		return data;
	}

	private void showChart(BarData data) {
		if (mChart.getData() != null) {
			mChart.clearValues();
		}
		mChart.setData(data);
		mChart.invalidate();
		// 设置动画
		mChart.animateY(1000);
	}

	private void initData(List<List<Trade>> mTradeLists) {
		String dateStartString = et_trade_start.getText().toString();
		Date dateStart = TimeUtil.getDate(dateStartString);
		int m_total = 0;
		int m_count = 0;
		int count_p = 0;
		int income_p = 0;
		int count_c = 0;
		int income_c = 0;
		if (type == 1) {
			for (int i = 0; i < mTradeLists.size(); i++) {
				int total = tradeService
						.getPercentageSocial(mTradeLists.get(i))+tradeService.getPercentageBank(mTradeLists.get(i));
				int count = mTradeLists.get(i).size();
				ChartEntity pi = new ChartEntity();
				String x = TimeUtil.getDateStringBeforeDay(dateStart, i);
				pi.setDate(x);
				float income = FloatCalculator.divide(total, 100);
				pi.setIncome((float) income);
				pi.setCount(count);
				mChartData.add(pi);
				m_total += total;
				m_count += count;
				
				List<Trade> mTrades = mTradeLists.get(i);
				
				
				for (int j = 0; j < mTrades.size(); j++) {
					Trade trade = mTrades.get(j);
					if (trade.getTrade_type() == 14||trade.getTrade_type() == 18) {
						count_p++;
						income_p+=trade.getTrade_money();
					}else if(trade.getTrade_type() == 16||trade.getTrade_type() == 11){
						count_c++;
						income_c+=trade.getTrade_money();
					}
						
				}
			}
		} else if (type == 2) {
			for (int i = 0; i < mTradeLists.size(); i++) {
				int total = tradeService
						.getPercentageSocial(mTradeLists.get(i))+tradeService.getPercentageBank(mTradeLists.get(i));
				int count = mTradeLists.get(i).size();
				ChartEntity pi = new ChartEntity();
				pi.setDate("第" + (i + 1) + "周");
				float income = FloatCalculator.divide(total, 100);
				pi.setIncome((float) income);
				pi.setCount(count);
				mChartData.add(pi);
				m_total += total;
				m_count += count;
				
				List<Trade> mTrades = mTradeLists.get(i);
				
				for (int j = 0; j < mTrades.size(); j++) {
					Trade trade = mTrades.get(j);
					if (trade.getTrade_type() == 14||trade.getTrade_type() == 18) {
						count_p++;
						income_p+=trade.getTrade_money();
					}else if(trade.getTrade_type() == 16||trade.getTrade_type() == 11){
						count_c++;
						income_c+=trade.getTrade_money();
					}
						
				}
			}
		} else if (type == 3) {
			for (int i = 0; i < mTradeLists.size(); i++) {
				int total = tradeService
						.getPercentageSocial(mTradeLists.get(i))+tradeService.getPercentageBank(mTradeLists.get(i));
				int count = mTradeLists.get(i).size();
				ChartEntity pi = new ChartEntity();
				pi.setDate((i + 1) + "月");
				float income = FloatCalculator.divide(total, 100);
				pi.setIncome((float) income);
				pi.setCount(count);
				mChartData.add(pi);
				m_total += total;
				m_count += count;
				
				List<Trade> mTrades = mTradeLists.get(i);
				
				
				for (int j = 0; j < mTrades.size(); j++) {
					Trade trade = mTrades.get(j);
					if (trade.getTrade_type() == 14||trade.getTrade_type() == 18) {
						count_p++;
						income_p+=trade.getTrade_money();
					}else if(trade.getTrade_type() == 16||trade.getTrade_type() == 11){
						count_c++;
						income_c+=trade.getTrade_money();
					}
						
				}
			}
		}

		float income = FloatCalculator.divide(m_total, 100);
		tv_count.setText("" + m_count);
		tv_total.setText("" + income+"元");
		Float p = FloatCalculator.divide(income_p, 100);
		Float c = FloatCalculator.divide(income_c, 100);
		tv_total_c.setText(""+count_c+"/"+c+"元");
		tv_total_p.setText(""+count_p+"/"+p+"元");
	}

	/*
	 * 开始获取数据
	 */
	private void startGetChartDataTask(int a) {
		if (null == mGetChartDataTask) {
			mGetChartDataTask = new GetChartDataTask();
			mGetChartDataTask.execute(a);
		}
	}

	/*
	 * 获取表格数据任务
	 */
	public class GetChartDataTask extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				int a = params[0];// 1 天 2 星期 3 月
				mTradeLists.clear();
				mChartData.clear();
				String dateString = et_trade_start.getText().toString();
				if (a == 1) {// 按天
					Date date = TimeUtil.getDate(dateString);// 搜索时间当天
					for (int i = 0; i <7; i++) {
						List<Trade> tempTradeList = new ArrayList<Trade>();
						String dateStartString = TimeUtil
								.getDateStringZeroTime(date, i);
						Date startTime = TimeUtil.getDate(dateStartString);
						String dateEndString = TimeUtil.getDateStringZeroTime(
								date, i+1);
						Date endTime = TimeUtil.getDate(dateEndString);
						tempTradeList = tradeService.queryByDuringTime(
								startTime, endTime);
						mTradeLists.add(tempTradeList);
					}
				} else if (a == 2) {// 按星期
					Date e_date = TimeUtil.getDate(dateString);// 搜索时间当天
					Date date = new Date(e_date.getYear(), e_date.getMonth(), 1);// 搜索时间当月1号
					for (int i = 0; i < 4; i++) {
						List<Trade> tempTradeList = new ArrayList<Trade>();
						String dateStartString = TimeUtil
								.getDateStringZeroTime(date, i * 7);
						Date startTime = TimeUtil.getDate(dateStartString);
						String dateEndString = TimeUtil.getDateStringZeroTime(
								date, (i + 1) * 7);
						Date endTime = TimeUtil.getDate(dateEndString);
						tempTradeList = tradeService.queryByDuringTime(
								startTime, endTime);
						mTradeLists.add(tempTradeList);
					}
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
					if (lastDay != 28) {// 当月不止4周
						List<Trade> tempTradeList = new ArrayList<Trade>();
						String dateStartString = TimeUtil
								.getDateStringZeroTime(date, 28);
						Date startTime = TimeUtil.getDate(dateStartString);
						String dateEndString = TimeUtil.getDateStringZeroTime(
								date, lastDay + 1);
						Date endTime = TimeUtil.getDate(dateEndString);
						tempTradeList = tradeService.queryByDuringTime(
								startTime, endTime);
						mTradeLists.add(tempTradeList);
					}
				} else if (a == 3) {// 按月
					Date date = TimeUtil.getDate(dateString);// 搜索时间当天
					for (int i = 0; i < 12; i++) {
						List<Trade> tempTradeList = new ArrayList<Trade>();
						@SuppressWarnings("deprecation")
						Date startTime = new Date(date.getYear(), i, 1);// 当月第一日
						Date endTime = TimeUtil.getDate(TimeUtil.getLastDayOfMonth(startTime),1);// 下月第一天
						tempTradeList = tradeService.queryByDuringTime(
								startTime, endTime);
						mTradeLists.add(tempTradeList);
					}
				}

				return true;
			} catch (Exception ex) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetChartDataTask = null;
			if (success) {
				initData(mTradeLists);
				BarData mSource = fillChartData(mChartData);
				if (null != mSource) {
					showChart(mSource);
				}

			} else {
				ToastUtil.show(ChartActivity.this, "获取失败，请重试", 3000);
			}
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return false;
	}



}
