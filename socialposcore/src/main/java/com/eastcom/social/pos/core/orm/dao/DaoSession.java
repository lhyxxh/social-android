package com.eastcom.social.pos.core.orm.dao;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.eastcom.social.pos.core.orm.entity.BlackList;
import com.eastcom.social.pos.core.orm.entity.CardInfo;
import com.eastcom.social.pos.core.orm.entity.CommandConfirm;
import com.eastcom.social.pos.core.orm.entity.Document;
import com.eastcom.social.pos.core.orm.entity.Health;
import com.eastcom.social.pos.core.orm.entity.Medicine;
import com.eastcom.social.pos.core.orm.entity.OmniTrade;
import com.eastcom.social.pos.core.orm.entity.PolicyDocument;
import com.eastcom.social.pos.core.orm.entity.TempBlackList;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.orm.entity.TradeFile;
import com.eastcom.social.pos.core.orm.entity.UnknowMedicine;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

	private final DaoConfig medicineDaoConfig;
	private final DaoConfig tradeDaoConfig;
	private final DaoConfig tradeDetailDaoConfig;
	private final DaoConfig healthDaoConfig;
	private final DaoConfig cardInfoDaoConfig;

	private final MedicineDao medicineDao;
	private final TradeDao tradeDao;
	private final TradeDetailDao tradeDetailDao;
	private final HealthDao healthDao;
	private final CardInfoDao cardInfoDao;

	private final DaoConfig blackListDaoConfig;
	private final BlackListDao blackListDao;

	private final DaoConfig tempBlackListDaoConfig;
	private final TempBlackListDao tempBlackListDao;

	private final DaoConfig policyDocumentDaoConfig;
	private final PolicyDocumentDao policyDocumentDao;

	private final DaoConfig commandConfirmDaoConfig;
	private final CommandConfirmDao commandConfirmDao;

	private final DaoConfig documentDaoConfig;
	private final DocumentDao documentDao;

	private final DaoConfig unknowMedicineDaoConfig;
	private final UnknowMedicineDao unknowMedicineDao;
	
	private final DaoConfig tradeFileDaoConfig;
	private final TradeFileDao tradeFileDao;
	
	private final DaoConfig omniTradeDaoConfig;
	private final OmniTradeDao omniTradeDao;

	public DaoSession(SQLiteDatabase db, IdentityScopeType type,
			Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);

		medicineDaoConfig = daoConfigMap.get(MedicineDao.class).clone();
		medicineDaoConfig.initIdentityScope(type);
		tradeDaoConfig = daoConfigMap.get(TradeDao.class).clone();
		tradeDaoConfig.initIdentityScope(type);
		tradeDetailDaoConfig = daoConfigMap.get(TradeDetailDao.class).clone();
		tradeDetailDaoConfig.initIdentityScope(type);
		healthDaoConfig = daoConfigMap.get(HealthDao.class).clone();
		healthDaoConfig.initIdentityScope(type);
		cardInfoDaoConfig = daoConfigMap.get(CardInfoDao.class).clone();
		cardInfoDaoConfig.initIdentityScope(type);

		medicineDao = new MedicineDao(medicineDaoConfig, this);
		tradeDao = new TradeDao(tradeDaoConfig, this);
		tradeDetailDao = new TradeDetailDao(tradeDetailDaoConfig, this);
		healthDao = new HealthDao(healthDaoConfig, this);
		cardInfoDao = new CardInfoDao(cardInfoDaoConfig, this);

		registerDao(Medicine.class, medicineDao);
		registerDao(Trade.class, tradeDao);
		registerDao(TradeDetail.class, tradeDetailDao);
		registerDao(Health.class, healthDao);
		registerDao(CardInfo.class, cardInfoDao);

		blackListDaoConfig = daoConfigMap.get(BlackListDao.class).clone();
		blackListDaoConfig.initIdentityScope(type);
		blackListDao = new BlackListDao(blackListDaoConfig, this);
		registerDao(BlackList.class, blackListDao);

		tempBlackListDaoConfig = daoConfigMap.get(TempBlackListDao.class)
				.clone();
		tempBlackListDaoConfig.initIdentityScope(type);
		tempBlackListDao = new TempBlackListDao(tempBlackListDaoConfig, this);
		registerDao(TempBlackList.class, tempBlackListDao);

		policyDocumentDaoConfig = daoConfigMap.get(PolicyDocumentDao.class)
				.clone();
		policyDocumentDaoConfig.initIdentityScope(type);
		policyDocumentDao = new PolicyDocumentDao(policyDocumentDaoConfig, this);
		registerDao(PolicyDocument.class, policyDocumentDao);

		commandConfirmDaoConfig = daoConfigMap.get(CommandConfirmDao.class)
				.clone();
		commandConfirmDaoConfig.initIdentityScope(type);
		commandConfirmDao = new CommandConfirmDao(commandConfirmDaoConfig, this);
		registerDao(CommandConfirm.class, commandConfirmDao);

		documentDaoConfig = daoConfigMap.get(DocumentDao.class).clone();
		documentDaoConfig.initIdentityScope(type);
		documentDao = new DocumentDao(documentDaoConfig, this);
		registerDao(Document.class, documentDao);

		unknowMedicineDaoConfig = daoConfigMap.get(UnknowMedicineDao.class)
				.clone();
		unknowMedicineDaoConfig.initIdentityScope(type);
		unknowMedicineDao = new UnknowMedicineDao(unknowMedicineDaoConfig, this);
		registerDao(UnknowMedicine.class, unknowMedicineDao);
		
		tradeFileDaoConfig = daoConfigMap.get(TradeFileDao.class)
				.clone();
		tradeFileDaoConfig.initIdentityScope(type);
		tradeFileDao = new TradeFileDao(tradeFileDaoConfig, this);
		registerDao(TradeFile.class, tradeFileDao);
		
		omniTradeDaoConfig = daoConfigMap.get(OmniTradeDao.class)
				.clone();
		omniTradeDaoConfig.initIdentityScope(type);
		omniTradeDao = new OmniTradeDao(omniTradeDaoConfig, this);
		registerDao(OmniTrade.class, omniTradeDao);
	}

	public void clear() {
		medicineDaoConfig.getIdentityScope().clear();
		tradeDaoConfig.getIdentityScope().clear();
		tradeDaoConfig.getIdentityScope().clear();
		tradeDetailDaoConfig.getIdentityScope().clear();
		cardInfoDaoConfig.getIdentityScope().clear();

		blackListDaoConfig.getIdentityScope().clear();
		tempBlackListDaoConfig.getIdentityScope().clear();
		policyDocumentDaoConfig.getIdentityScope().clear();

		commandConfirmDaoConfig.getIdentityScope().clear();

		documentDaoConfig.getIdentityScope().clear();
		unknowMedicineDaoConfig.getIdentityScope().clear();
		
		tradeFileDaoConfig.getIdentityScope().clear();
		omniTradeDaoConfig.getIdentityScope().clear();
	}

	public MedicineDao getMedicineDao() {
		return medicineDao;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public TradeDetailDao getTradeDetailDao() {
		return tradeDetailDao;
	}

	public HealthDao getHealthDao() {
		return healthDao;
	}

	public CardInfoDao getCardInfoDao() {
		return cardInfoDao;
	}

	public BlackListDao getBlackListDao() {
		return blackListDao;
	}

	public TempBlackListDao getTempBlackListDao() {
		return tempBlackListDao;
	}

	public PolicyDocumentDao getPolicyDocumentDao() {
		return policyDocumentDao;
	}

	public CommandConfirmDao getCommandConfirmDao() {
		return commandConfirmDao;
	}

	public DocumentDao getDocumentDao() {
		return documentDao;
	}

	public UnknowMedicineDao getUnknowMedicineDao() {
		return unknowMedicineDao;
	}
	
	public TradeFileDao getTradeFileDao() {
		return tradeFileDao;
	}
	
	public OmniTradeDao getOmniTradeDao() {
		return omniTradeDao;
	}

}
