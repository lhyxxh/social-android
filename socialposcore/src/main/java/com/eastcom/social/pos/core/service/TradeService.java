package com.eastcom.social.pos.core.service;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.TradeDao;
import com.eastcom.social.pos.core.orm.dao.TradeDao.Properties;
import com.eastcom.social.pos.core.orm.entity.Trade;

@SuppressLint("SimpleDateFormat")
public class TradeService {

	private static final String TAG = TradeService.class.getSimpleName();
	private static TradeService instance;
	private DaoSession mDaoSession;
	private TradeDao tradeDao;

	private TradeService() {
	}

	public synchronized static TradeService getInstance(Context context) {
		if (instance == null) {
			instance = new TradeService();
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.tradeDao = instance.mDaoSession.getTradeDao();
		}
		return instance;
	}

	public Trade loadTrade(String id) {
		return tradeDao.load(id);
	}

	public List<Trade> loadAllTrade() {
		return tradeDao.loadAll();
	}
	public List<Trade> loadAllTradeOrderByTradeTime() {
		return tradeDao.queryBuilder().orderDesc(Properties.Trade_Time).list();
	}
	public List<Trade> loadAllTradeOrderByTradeTime(int cardType) {
		if (cardType == 2){
			return tradeDao.queryBuilder().whereOr(Properties.Trade_Type.eq(14),
					Properties.Trade_Type.eq(16)).orderDesc(Properties.Trade_Time).list();
		}else if (cardType == 3){
			return tradeDao.queryBuilder().whereOr(Properties.Trade_Type.eq(18),
					Properties.Trade_Type.eq(11)).orderDesc(Properties.Trade_Time).list();
		}else{
			return tradeDao.queryBuilder().orderDesc(Properties.Trade_Time).list();
		}
		
	}

	public List<Trade> queryTrade(String where, String... params) {
		return tradeDao.queryRaw(where, params);
	}

	public void saveTrade(final Trade trade) {
		mDaoSession.clear();
		if (trade == null) {
			return;
		}
		tradeDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				tradeDao.insertOrReplace(trade);
			}
		});
	}

	public void saveTradeLists(final List<Trade> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		tradeDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (Trade trade : list) {
					try {
						tradeDao.insertOrReplace(trade);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		});
	}

	public void deleteAllTrade() {
		tradeDao.deleteAll();
	}

	public void deleteTrade(String id) {
		tradeDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	public void deleteTrade(Trade trade) {
		tradeDao.delete(trade);
	}

	public List<Trade> queryById(String id) {
		mDaoSession.clear();
		List<Trade> list = tradeDao.queryBuilder().where(Properties.Id.eq(id)).orderDesc(Properties.Trade_Time)
				.list();
		return list;
	}

	public List<Trade> queryByDuringTime(Date timeStart, Date timeEnd) {
		List<Trade> list = tradeDao
				.queryBuilder()
				.where(Properties.Trade_Time.ge(timeStart),
						Properties.Trade_Time.le(timeEnd)).list();
		return list;
	}
	public List<Trade> queryByStartTime(Date timeStart) {
		List<Trade> list = tradeDao
				.queryBuilder()
				.where(Properties.Trade_Time.ge(timeStart)).list();
		return list;
	}

	/**
	 * 根据流水号模糊查询
	 * 
	 * @param trade_no
	 * @return
	 */
	public List<Trade> queryByTradeNoLike(String trade_no) {
		List<Trade> list = tradeDao.queryBuilder()
				.where(Properties.Trace.like(trade_no)).orderDesc(Properties.Trade_Time).list();
		return list;
	}

	/**
	 * 根据参考号模糊查询
	 * 
	 * @param ref_no
	 * @return
	 */
	public List<Trade> queryByRefNoLike(String ref_no) {
		List<Trade> list = tradeDao.queryBuilder()
				.where(Properties.Ref_No.like(ref_no)).orderDesc(Properties.Trade_Time).list();

		return list;
	}

	/**
	 * 根据流水号完全查询消费交易
	 * 
	 * @param trade_no
	 * @return
	 */
	public Trade queryByTrace(String trace) {
		Trade list = tradeDao
				.queryBuilder()
				.where(Properties.Trace.eq(trace))
				.whereOr(Properties.Trade_Type.eq(14),
						Properties.Trade_Type.eq(18)).unique();
		return list;
	}

	/**
	 * 根据交易序号完全查询
	 * 
	 * @param trade_no
	 * @return
	 */
	public List<Trade> queryByTradeNo(int trade_no) {
		List<Trade> list = tradeDao.queryBuilder()
				.where(Properties.Trade_No.eq(trade_no)).orderDesc(Properties.Trade_Time).list();
		return list;
	}

	/**
	 * 根据流水号完全查询 list
	 * 
	 * @param trade_no
	 * @return
	 */
	public List<Trade> queryByTradeNoList(String trade_no) {
		List<Trade> list = tradeDao.queryBuilder()
				.where(Properties.Trace.eq(trade_no)).orderDesc(Properties.Trade_Time).list();
		return list;
	}

	/**
	 * 根据参考号完全查询
	 * 
	 * @param ref_no
	 * @return
	 */
	public List<Trade> queryByRefNo(String ref_no) {
		List<Trade> list = tradeDao
				.queryBuilder()
				.where(Properties.Ref_No.eq(ref_no))
				.whereOr(Properties.Trade_Type.eq(14),
						Properties.Trade_Type.eq(18)).orderDesc(Properties.Trade_Time).list();
		return list;
	}

	/**
	 * 根据流水号或社保卡号模糊查询
	 * 
	 * @param trade_no
	 * @param card_no
	 * @return
	 */
	public List<Trade> queryByTradeNoOrCardNoLike(String trade_no,
			String card_no) {
		List<Trade> list = tradeDao
				.queryBuilder()
				.whereOr(Properties.Trace.like(trade_no),
						Properties.Social_Card_No.like(card_no)).orderDesc(Properties.Trade_Time).list();

		return list;
	}

	/**
	 * 获取没有上传成功的交易列表
	 * 
	 * @param slow
	 *            是否慢速上传 根据重复次数判断
	 * @return
	 */
	public List<Trade> queryByIsUpload(boolean slow) {
		List<Trade> list;
		if (!slow) {
			list = tradeDao
					.queryBuilder()
					.where(Properties.Is_Upload.eq(1),
							Properties.Send_Count.lt(5)).orderDesc(Properties.Trade_Time).list();
		} else {
			list = tradeDao
					.queryBuilder()
					.where(Properties.Is_Upload.eq(1),
							Properties.Send_Count.ge(5)).orderDesc(Properties.Trade_Time).list();
		}
		return list;
	}
	public List<Trade> queryByIsUpload() {
		List<Trade> list;
			list = tradeDao
					.queryBuilder()
					.where(Properties.Is_Upload.eq(1)
							).orderDesc(Properties.Trade_Time).list();
		return list;
	}

	/**
	 * 获取流水号最大的交易的流水号
	 * 
	 * @return
	 */
	public int queryPreTradeNo() {
		List<Trade> list = tradeDao.queryBuilder()
				.orderDesc(Properties.Trade_No).list();
		if (list.size() > 0) {
			Trade trade = list.get(0);
			return trade.getTrade_no();
		} else {
			return 1;
		}
	}

	/**
	 * 统计医保交易收入
	 * 
	 * @return
	 */
	public int getPercentageSocial(List<Trade> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			Trade trade = list.get(i);
			if (trade.getTrade_type() == 14) {
				sum += trade.getTrade_money();
			} else if (trade.getTrade_type() == 16) {
				sum -= trade.getTrade_money();
			}
		}
		return sum;
	}

	/**
	 * 统计非医保交易收入
	 * 
	 * @return
	 */
	public int getPercentageBank(List<Trade> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			Trade trade = list.get(i);
			if (trade.getTrade_type() == 18) {
				sum += trade.getTrade_money();
			} else if (trade.getTrade_type() == 11) {
				sum -= trade.getTrade_money();
			}
		}
		return sum;
	}

	/**
	 * 统计非医保交易收入(年)
	 * 
	 * @return
	 */
	public List<Trade> getPercentageBankYear() {
		String year = Calendar.getInstance().get(Calendar.YEAR) + "";// 当前年份
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		ParsePosition pos = new ParsePosition(0);
		Date startDate = formatter.parse(year + "0101000000", pos);
		List<Trade> list = tradeDao
				.queryBuilder()
				.where(Properties.Trade_Time.ge(startDate),
						Properties.Trade_Type.in(18, 11)).list();
		return list;
	}

	/**
	 * 统计非医保交易收入(月)
	 * 
	 * @return
	 */
	public List<Trade> getPercentageBankMonth() {
		// mDaoSession.clear();
		String year = Calendar.getInstance().get(Calendar.YEAR) + "";// 当前年份
		String month = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		ParsePosition pos = new ParsePosition(0);
		Date startDate = formatter.parse(year + month + "01000000", pos);
		List<Trade> list = tradeDao
				.queryBuilder()
				.where(Properties.Trade_Time.ge(startDate),
						Properties.Trade_Type.in(18, 11)).list();
		return list;
	}

	/**
	 * 统计非医保交易收入(日)
	 * 
	 * @return
	 */
	public List<Trade> getPercentageBankDay() {
		// mDaoSession.clear();
		String year = Calendar.getInstance().get(Calendar.YEAR) + "";// 当前年份
		String month = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "";
		String day = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		ParsePosition pos = new ParsePosition(0);
		Date startDate = formatter.parse(year + month + day + "000000", pos);
		List<Trade> list = tradeDao
				.queryBuilder()
				.where(Properties.Trade_Time.ge(startDate),
						Properties.Trade_Type.in(18, 11)).list();
		return list;
	}

	/**
	 * 统计医保交易收入(年)
	 * 
	 * @return
	 */
	public List<Trade> getPercentageSocialYear() {
		// mDaoSession.clear();
		String year = Calendar.getInstance().get(Calendar.YEAR) + "";// 当前年份
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		ParsePosition pos = new ParsePosition(0);
		Date startDate = formatter.parse(year + "0101000000", pos);
		List<Trade> list = tradeDao
				.queryBuilder()
				.where(Properties.Trade_Time.ge(startDate),
						Properties.Trade_Type.in(14, 16)).list();
		return list;
	}

	/**
	 * 统计医保交易收入(月)
	 * 
	 * @return
	 */
	public List<Trade> getPercentageSocialMonth() {
		// mDaoSession.clear();
		String year = Calendar.getInstance().get(Calendar.YEAR) + "";// 当前年份
		String month = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		ParsePosition pos = new ParsePosition(0);
		Date startDate = formatter.parse(year + month + "01000000", pos);
		List<Trade> list = tradeDao
				.queryBuilder()
				.where(Properties.Trade_Time.ge(startDate),
						Properties.Trade_Type.in(14, 16)).list();
		return list;
	}

	/**
	 * 统计医保交易收入(日)
	 * 
	 * @return
	 */
	public List<Trade> getPercentageSocialDay() {
		String year = Calendar.getInstance().get(Calendar.YEAR) + "";// 当前年份
		String month = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "";
		String day = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		ParsePosition pos = new ParsePosition(0);
		Date startDate = formatter.parse(year + month + day + "000000", pos);
		List<Trade> list = tradeDao
				.queryBuilder()
				.where(Properties.Trade_Time.ge(startDate),
						Properties.Trade_Type.in(14, 16)).list();
		return list;
	}

	/**
	 * 根据流水号或社保卡号模糊查询
	 * 
	 * @param trade_no
	 * @param card_no
	 * @return
	 */
	public List<Trade> queryConsume(String trade_no, String card_no,
			Date timeStart, Date timeEnd,int socialTrade) {
		List<Trade> list = new ArrayList<Trade>();
		if (socialTrade == 2) {
			 list = tradeDao
					.queryBuilder()
					.where(Properties.Trace.like(trade_no))
					.where(Properties.Social_Card_No.like(card_no))
					.whereOr(Properties.Trade_Type.eq(14),
							Properties.Trade_Type.eq(16))
					.where(Properties.Trade_Time.ge(timeStart),
							Properties.Trade_Time.le(timeEnd)).list();
		}else if (socialTrade == 3){
			 list = tradeDao
					.queryBuilder()
					.where(Properties.Trace.like(trade_no))
					.where(Properties.Bank_Card_No.like(card_no))
					.whereOr(Properties.Trade_Type.eq(18),
					Properties.Trade_Type.eq(11))
					.where(Properties.Trade_Time.ge(timeStart),
							Properties.Trade_Time.le(timeEnd)).list();
		}else{
			list = tradeDao
					.queryBuilder()
					.where(Properties.Trace.like(trade_no))
					.whereOr(Properties.Bank_Card_No.like(card_no),Properties.Social_Card_No.like(card_no))
					.where(Properties.Trade_Time.ge(timeStart),
							Properties.Trade_Time.le(timeEnd)).list();
		}
		return list;
	}
	
	/**
	 * 
	 * @param timeStart
	 * @param timeEnd
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<Trade> queryUploadTrade(Date timeStart, Date timeEnd,int limit, int offset) {
		List<Trade> list = new ArrayList<Trade>();
			list = tradeDao
					.queryBuilder().where(Properties.Trade_Time.ge(timeStart),
							Properties.Trade_Time.le(timeEnd)).orderAsc(Properties.Trade_Time).limit(limit).offset(offset).list();
		return list;
	}
}
