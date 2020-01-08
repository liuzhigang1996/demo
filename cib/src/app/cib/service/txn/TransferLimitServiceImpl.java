package app.cib.service.txn;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.bnk.TxnLimit;
import app.cib.bo.txn.TxnLimitUsage;
import app.cib.core.CibIdGenerator;
import app.cib.dao.bnk.TxnLimitDao;
import app.cib.dao.txn.TxnLimitUsageDao;
import app.cib.service.bnk.CorporationService;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;

public class TransferLimitServiceImpl implements TransferLimitService {

	private static final String LIMIT_TYPE_NORMAL = "0";
	private static final String LIMIT_TYPE_ALL = "1";
	private static final String LIMIT_TYPE_CORP_NORMAL = "2";
	private static final String LIMIT_TYPE_CORP_ALL = "3";
	//add by lr begin for cr225
	  private static String MOP_DAILY_LIMIT_MODE1 = "3";
	  private static String MOP_DAILY_LIMIT_MODE2 = "2";
	  private static String MOP_DAILY_LIMIT_MODE3 = "4";
	  private static String MOP_DAILY_LIMIT_MODE4 = "1";
	  //end   
	private double dailyLimit;

	private double totalLimit;

	private String limitType;

	public TxnLimitDao getTxnLimitDao() {
		return txnLimitDao;
	}

	public void setTxnLimitDao(TxnLimitDao txnLimitDao) {
		this.txnLimitDao = txnLimitDao;
	}

	public TxnLimitUsageDao getTxnLimitUsageDao() {
		return txnLimitUsageDao;
	}

	public void setTxnLimitUsageDao(TxnLimitUsageDao txnLimitUsageDao) {
		this.txnLimitUsageDao = txnLimitUsageDao;
	}

	public boolean addUsedLimitQuota(String account, String corpId,
			String txnType, double amountEq, double amountMopEq)
			throws NTBException {
		boolean flag = true;

		try {
			TxnLimit txnLimit = txnLimitDao.findByCorp(corpId, account);

			if (null == txnLimit) {
				txnLimit = txnLimitDao.findByCorp(corpId, TxnLimit.ACCOUNT_ALL);
			}

			if (null == txnLimit) {
				throw new NTBException("err.txn.NoLimit");
			}

			TxnLimitUsage txnLimitUsage = txnLimitUsageDao.findByCorpAndTxn(
					corpId, txnLimit.getAccount(), txnLimit.getTxnType());
			if (null == txnLimitUsage) {
				txnLimitUsage = new TxnLimitUsage();
				txnLimitUsage.setCorpId(txnLimit.getCorpId());
				txnLimitUsage.setAccount(txnLimit.getAccount());
				txnLimitUsage.setCurrency(txnLimit.getCurrency());
				txnLimitUsage.setPrefId(txnLimit.getPrefId());
				txnLimitUsage.setTxnType(txnLimit.getTxnType());
				txnLimitUsage.setUsageId(CibIdGenerator
						.getIdForOperation("TxnLimitUsage"));
				txnLimitUsage.setUsageDate(DateTime.getCurrentDate());
				Double amount = new Double(0.000);
				txnLimitUsage.setLimit(amount);
				txnLimitUsage.setLimit1(amount);
				txnLimitUsage.setLimit2(amount);
				txnLimitUsage.setLimit3(amount);
				txnLimitUsage.setLimit4(amount);
				txnLimitUsage.setLimit5(amount);
				txnLimitUsage.setLimit6(amount);
				txnLimitUsage.setLimit7(amount);
				txnLimitUsage.setLimit8(amount);
				txnLimitUsage.setLimit9(amount);
				txnLimitUsage.setLimit10(amount);
				txnLimitUsageDao.add(txnLimitUsage);
			} else {
				txnLimitUsage = checkLimitUsageDate(txnLimitUsage);
			}

			String txnLimitTxnType = txnLimit.getTxnType();

			if (TxnLimit.TXN_TYPE_ALL.equals(txnLimitTxnType)) {
				if (TxnLimit.ACCOUNT_ALL.equals(txnLimit.getAccount())) {
					txnLimitUsage.setLimit(new Double(txnLimitUsage.getLimit()
							.doubleValue()
							+ amountMopEq));
				} else {
					txnLimitUsage.setLimit(new Double(txnLimitUsage.getLimit()
							.doubleValue()
							+ amountEq));
				}
			} else {
				if (TxnLimit.ACCOUNT_ALL.equals(txnLimit.getAccount())) {
					if (txnType.equals(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT)) {
						txnLimitUsage.setLimit1(new Double(txnLimitUsage
								.getLimit1().doubleValue()
								+ amountMopEq));

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_BANK)) {
						txnLimitUsage.setLimit2(new Double(txnLimitUsage
								.getLimit2().doubleValue()
								+ amountMopEq));

					} else if (txnType
							.equals(Constants.TXN_TYPE_TRANSFER_MACAU)) {
						txnLimitUsage.setLimit3(new Double(txnLimitUsage
								.getLimit3().doubleValue()
								+ amountMopEq));

					} else if (txnType
							.equals(Constants.TXN_TYPE_TRANSFER_OVERSEAS)) {
						txnLimitUsage.setLimit4(new Double(txnLimitUsage
								.getLimit4().doubleValue()
								+ amountMopEq));

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_CORP)) {
						txnLimitUsage.setLimit5(new Double(txnLimitUsage
								.getLimit5().doubleValue()
								+ amountMopEq));

					} else if (txnType.equals(Constants.TXN_TYPE_PAY_BILLS)) {
						txnLimitUsage.setLimit6(new Double(txnLimitUsage
								.getLimit6().doubleValue()
								+ amountMopEq));

					} else if (txnType.equals(Constants.TXN_TYPE_TIME_DEPOSIT)) {
						txnLimitUsage.setLimit7(new Double(txnLimitUsage
								.getLimit7().doubleValue()
								+ amountMopEq));
					} else if (txnType.equals(Constants.TXN_TYPE_PAYROLL)) {
						txnLimitUsage.setLimit8(new Double(txnLimitUsage
								.getLimit8().doubleValue()
								+ amountMopEq));
					// add by hjs 2006-11-9
					} else if (txnType.equals(Constants.TXN_TYPE_BANK_DRAFT)) {
						txnLimitUsage.setLimit9(new Double(txnLimitUsage
								.getLimit9().doubleValue()
								+ amountMopEq));
					} else if (txnType.equals(Constants.TXN_TYPE_CASHIER_ORDER)) {
						txnLimitUsage.setLimit10(new Double(txnLimitUsage
								.getLimit10().doubleValue()
								+ amountMopEq));
					}

				} else {
					if (txnType.equals(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT)) {
						txnLimitUsage.setLimit1(new Double(txnLimitUsage
								.getLimit1().doubleValue()
								+ amountEq));

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_BANK)) {
						txnLimitUsage.setLimit2(new Double(txnLimitUsage
								.getLimit2().doubleValue()
								+ amountEq));

					} else if (txnType
							.equals(Constants.TXN_TYPE_TRANSFER_MACAU)) {
						txnLimitUsage.setLimit3(new Double(txnLimitUsage
								.getLimit3().doubleValue()
								+ amountEq));

					} else if (txnType
							.equals(Constants.TXN_TYPE_TRANSFER_OVERSEAS)) {
						txnLimitUsage.setLimit4(new Double(txnLimitUsage
								.getLimit4().doubleValue()
								+ amountEq));

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_CORP)) {
						txnLimitUsage.setLimit5(new Double(txnLimitUsage
								.getLimit5().doubleValue()
								+ amountEq));

					} else if (txnType.equals(Constants.TXN_TYPE_PAY_BILLS)) {
						txnLimitUsage.setLimit6(new Double(txnLimitUsage
								.getLimit6().doubleValue()
								+ amountEq));

					} else if (txnType.equals(Constants.TXN_TYPE_TIME_DEPOSIT)) {
						txnLimitUsage.setLimit7(new Double(txnLimitUsage
								.getLimit7().doubleValue()
								+ amountEq));

					} else if (txnType.equals(Constants.TXN_TYPE_PAYROLL)) {
						txnLimitUsage.setLimit8(new Double(txnLimitUsage
								.getLimit8().doubleValue()
								+ amountEq));
					// add by hjs 2006-11-9
					} else if (txnType.equals(Constants.TXN_TYPE_BANK_DRAFT)) {
						txnLimitUsage.setLimit9(new Double(txnLimitUsage
								.getLimit9().doubleValue()
								+ amountEq));

					} else if (txnType.equals(Constants.TXN_TYPE_CASHIER_ORDER)) {
						txnLimitUsage.setLimit10(new Double(txnLimitUsage
								.getLimit10().doubleValue()
								+ amountEq));

					}
				}
			}
			txnLimitUsageDao.add(txnLimitUsage);

		} catch (Exception e) {
			Log.error("Exception occur at app.cib.service.txn."
					+ "TransferLimitServiceImpl.addUsedLimitQuota: ", e);

			if (e instanceof NTBException) {
				throw (NTBException) e;
			}

			flag = false;
		}

		return flag;
	}

	public boolean checkLimitQuota(String account, String corpId,
			String txnType, double amountEq, double amountMopEq)
			throws NTBException {
		boolean flag = false;
		Double corpDailyLimit = 0.0;
		Double corpDailyTotalAmount = 0.0;
		Double corpDailyLimitSet = 0.0;
		try {
			TxnLimit txnLimit = txnLimitDao.findByCorp(corpId, account);

			if (null == txnLimit) {
				txnLimit = txnLimitDao.findByCorp(corpId, TxnLimit.ACCOUNT_ALL);
			}

			if (null == txnLimit) {
				throw new NTBException("err.txn.NoLimit");
			}

			TxnLimitUsage txnLimitUsage = txnLimitUsageDao.findByCorpAndTxn(
					corpId, txnLimit.getAccount(), txnLimit.getTxnType());
			if (null == txnLimitUsage) {
				txnLimitUsage = new TxnLimitUsage();
				txnLimitUsage.setCorpId(txnLimit.getCorpId());
				txnLimitUsage.setAccount(txnLimit.getAccount());
				txnLimitUsage.setCurrency(txnLimit.getCurrency());
				txnLimitUsage.setPrefId(txnLimit.getPrefId());
				txnLimitUsage.setTxnType(txnLimit.getTxnType());
				txnLimitUsage.setUsageId(CibIdGenerator
						.getIdForOperation("TxnLimitUsage"));
				txnLimitUsage.setUsageDate(DateTime.getCurrentDate());
				Double amount = new Double(0.000);
				txnLimitUsage.setLimit(amount);
				txnLimitUsage.setLimit1(amount);
				txnLimitUsage.setLimit2(amount);
				txnLimitUsage.setLimit3(amount);
				txnLimitUsage.setLimit4(amount);
				txnLimitUsage.setLimit5(amount);
				txnLimitUsage.setLimit6(amount);
				txnLimitUsage.setLimit7(amount);
				txnLimitUsage.setLimit8(amount);
				txnLimitUsage.setLimit9(amount);
				txnLimitUsage.setLimit10(amount);
				txnLimitUsageDao.add(txnLimitUsage);
			} else {
				txnLimitUsage = checkLimitUsageDate(txnLimitUsage);
			}
			// add by lr  for CR225 2017-3-3
			//corpDailyLimitSet = txnLimit.getLimit1();
			corpDailyLimit =getCorpDailyLimit(corpId) ;
			/* what are you doing? why ?
			 * if(corpDailyLimitSet != 0.0){
			corpDailyLimit = corpDailyLimit<=corpDailyLimitSet?corpDailyLimit:corpDailyLimitSet;
			
			}*///get limit from conf and compare with set
			corpDailyTotalAmount = getCorpDailyAcctTotal(corpId,DateTime.getCurrentDate()) ;//getTotal with id and date
			Double testDouble = corpDailyTotalAmount.doubleValue() + amountMopEq
			          -corpDailyLimit.doubleValue();
			if(compareTo(corpDailyTotalAmount.doubleValue() + amountMopEq -corpDailyLimit.doubleValue() , 0.010 ) >=0){   
				Log.error("***transfer error for the txnType is:"+txnType);
				Log.error("***the transfer amountEq is:"+amountEq);
			    Log.error("***amountMopEq is:"+amountMopEq);
				Log.error("***corpDailyLimit is:"+corpDailyLimit);
				Log.error("***if transfer the corpDailyTotalAmount will up to:"+(corpDailyTotalAmount+amountMopEq));
				return false;
			}
			//end	
			String txnLimitTxnType = txnLimit.getTxnType();    
		    
			Log.info("account="+txnLimit.getAccount());
			Log.info("txn limit usage limit="+txnLimitUsage.getLimit().doubleValue());
			Log.info("txnLimit.getLimit1().doubleValue()" +txnLimit.getLimit1().doubleValue());
			Log.info("amountMopEq="+amountMopEq);
			Log.info("Outstanding limit amount "+(txnLimitUsage.getLimit().doubleValue() + amountMopEq
							- txnLimit.getLimit1().doubleValue()));
			if (TxnLimit.TXN_TYPE_ALL.equals(txnLimitTxnType)) {
				if (TxnLimit.ACCOUNT_ALL.equals(txnLimit.getAccount())) {
					if (compareTo(txnLimitUsage.getLimit().doubleValue() + amountMopEq
							- txnLimit.getLimit1().doubleValue() , 0.010)<0) {
						flag = true;
					}
					// add by hjs
					this.setDailyLimit(txnLimit.getLimit1().doubleValue());
					this.setTotalLimit(txnLimitUsage.getLimit().doubleValue() + amountMopEq);
					this.setLimitType(LIMIT_TYPE_CORP_ALL);
				} else {
					if (compareTo(txnLimitUsage.getLimit().doubleValue() + amountEq
							- txnLimit.getLimit1().doubleValue() , 0.010)<0) {
						flag = true;
					}
					// add by hjs
					this.setDailyLimit(txnLimit.getLimit1().doubleValue());
					this.setTotalLimit(txnLimitUsage.getLimit().doubleValue() + amountEq);
					this.setLimitType(LIMIT_TYPE_ALL);
				}
			} else {
				if (TxnLimit.ACCOUNT_ALL.equals(txnLimit.getAccount())) {
					if (txnType.equals(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT)) {
						if (compareTo(txnLimitUsage.getLimit1().doubleValue()
								+ amountMopEq
								- txnLimit.getLimit1().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit1().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit1().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_BANK)) {
						if (compareTo(txnLimitUsage.getLimit2().doubleValue()
								+ amountMopEq
								- txnLimit.getLimit2().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit2().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit2().doubleValue() + amountMopEq);

					} else if (txnType
							.equals(Constants.TXN_TYPE_TRANSFER_MACAU)) {
						if (compareTo(txnLimitUsage.getLimit3().doubleValue()
								+ amountMopEq
								- txnLimit.getLimit3().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit3().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit3().doubleValue() + amountMopEq);

					} else if (txnType
							.equals(Constants.TXN_TYPE_TRANSFER_OVERSEAS)) {
						if (compareTo(txnLimitUsage.getLimit4().doubleValue()
								+ amountMopEq
								- txnLimit.getLimit4().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit4().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit4().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_CORP)) {
						if (compareTo(txnLimitUsage.getLimit5().doubleValue()
								+ amountMopEq
								- txnLimit.getLimit5().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit5().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit5().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_PAY_BILLS)) {
						if (compareTo(txnLimitUsage.getLimit6().doubleValue()
								+ amountMopEq
								- txnLimit.getLimit6().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit6().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit6().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TIME_DEPOSIT)) {
						if (compareTo(txnLimitUsage.getLimit7().doubleValue()
								+ amountMopEq //modify by hjs
								- txnLimit.getLimit7().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit7().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit7().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_PAYROLL)) {
						if (compareTo(txnLimitUsage.getLimit8().doubleValue()
								+ amountMopEq //modify by hjs
								- txnLimit.getLimit8().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit8().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit8().doubleValue() + amountMopEq);
					}
					// add by hjs
					 else if (txnType.equals(Constants.TXN_SUBTYPE_BANK_DRAFT)) {
						if (compareTo(txnLimitUsage.getLimit9().doubleValue()
								+ amountMopEq //modify by hjs
								- txnLimit.getLimit9().doubleValue() , 0.010)<0) {
							flag = true;
						}
						this.setDailyLimit(txnLimit.getLimit9().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit9().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_CASHIER_ORDER)) {
						if (compareTo(txnLimitUsage.getLimit10().doubleValue()
								+ amountMopEq //modify by hjs
								- txnLimit.getLimit10().doubleValue() , 0.010)<0) {
							flag = true;
						}
						this.setDailyLimit(txnLimit.getLimit10().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit10().doubleValue() + amountMopEq);

					}
					// add by hjs
					this.setLimitType(LIMIT_TYPE_CORP_NORMAL);
				} else {
					if (txnType.equals(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT)) {
						if (compareTo(txnLimitUsage.getLimit1().doubleValue() + amountEq
								- txnLimit.getLimit1().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit1().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit1().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_BANK)) {
						if (compareTo(txnLimitUsage.getLimit2().doubleValue() + amountEq
								- txnLimit.getLimit2().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit2().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit2().doubleValue() + amountEq);

					} else if (txnType
							.equals(Constants.TXN_TYPE_TRANSFER_MACAU)) {
						if (compareTo(txnLimitUsage.getLimit3().doubleValue() + amountEq
								- txnLimit.getLimit3().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit3().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit3().doubleValue() + amountEq);

					} else if (txnType
							.equals(Constants.TXN_TYPE_TRANSFER_OVERSEAS)) {
						if (compareTo(txnLimitUsage.getLimit4().doubleValue() + amountEq
								- txnLimit.getLimit4().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit4().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit4().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_CORP)) {
						if (compareTo(txnLimitUsage.getLimit5().doubleValue() + amountEq
								- txnLimit.getLimit5().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit5().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit5().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_PAY_BILLS)) {
						if (compareTo(txnLimitUsage.getLimit6().doubleValue() + amountEq
								- txnLimit.getLimit6().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit6().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit6().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TIME_DEPOSIT)) {
						if (compareTo(txnLimitUsage.getLimit7().doubleValue() + amountEq
								- txnLimit.getLimit7().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit7().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit7().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_PAYROLL)) {
						if (compareTo(txnLimitUsage.getLimit8().doubleValue() + amountEq
								- txnLimit.getLimit8().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit8().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit8().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_BANK_DRAFT)) {
						if (compareTo(txnLimitUsage.getLimit9().doubleValue() + amountEq
								- txnLimit.getLimit9().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit9().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit9().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_CASHIER_ORDER)) {
						if (compareTo(txnLimitUsage.getLimit10().doubleValue() + amountEq
								- txnLimit.getLimit10().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit10().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit10().doubleValue() + amountEq);

					}
					// add by hjs
					this.setLimitType(LIMIT_TYPE_NORMAL);
				}
			}
		} catch (Exception e) {
			Log.error("Exception occur at app.cib.service.txn."
					+ "TransferLimitServiceImpl.checkLimitQuota: ", e);

			if (e instanceof NTBException) {
				throw (NTBException) e;
			}

			flag = false;
		}
      
		Log.info("**transfer without exceeding the limit and the txnType is:"+txnType);
	    Log.info("**transfer amountEq is:"+amountEq);
	    Log.info("**amountMopEq is:"+amountMopEq);
	    Log.info("**corpDailyLimit is:"+corpDailyLimit);
	    Log.info("**corpDailyTotalAmount is:"+(corpDailyTotalAmount+amountMopEq));
		return flag;
	}
	
	// This action is copy of action checkLimitQuota(), just for check limit quota by cropId, add by li_zd at 20170608
	public boolean checkLimitQuotaByCorpId(String corpId, String txnType, double amountEq, double amountMopEq) throws NTBException {
		boolean flag = true;
		Double corpDailyLimit = 0.0;
		Double corpDailyTotalAmount = 0.0;
		try {
			// add by li_zd for CR225 2017-6-8
			corpDailyLimit = getCorpDailyLimit(corpId);// get corp daily limit by corpid
			corpDailyTotalAmount = getCorpDailyAcctTotal(corpId,DateTime.getCurrentDate()) ;//getTotal with id and date
			Double testDouble = corpDailyTotalAmount.doubleValue() + amountMopEq - corpDailyLimit.doubleValue();
			// test the difference between (corpDailyTotalAmount.doubleValue() + amountMopEq) and (amountMopEq - corpDailyLimit.doubleValue())
			if(compareTo(corpDailyTotalAmount.doubleValue() + amountMopEq -corpDailyLimit.doubleValue() , 0.010 ) >=0){   
				Log.error("***transfer error for the txnType is: " + txnType);
				Log.error("***the transfer amountEq is: " + amountEq);
			    Log.error("***amountMopEq is: " + amountMopEq);
				Log.error("***corpDailyLimit is: " + corpDailyLimit);
				Log.error("***if transfer the corpDailyTotalAmount will up to: " + (corpDailyTotalAmount+amountMopEq));
				return false;
			}
			//end	
		} catch (Exception e) {
			Log.error("Exception occur at app.cib.service.txn.TransferLimitServiceImpl.checkLimitQuotaByCropId(): ", e);

			if (e instanceof NTBException) {
				throw (NTBException) e;
			}
			flag = false;
		}
      
		Log.info("**transfer without exceeding the limit and the txnType is: "+txnType);
	    Log.info("**transfer amountEq is: " + amountEq);
	    Log.info("**amountMopEq is: " + amountMopEq);
	    Log.info("**corpDailyLimit is: " + corpDailyLimit);
	    Log.info("**corpDailyTotalAmount is: " + (corpDailyTotalAmount+amountMopEq));
		return flag;
	}
	
	// This action is copy of action checkLimitQuotaByCorpId(), just for check limit quota by cropId without corpDailyTotalAmount, add by li_zd at 20170711
	// Apply to Autopay instruction and Scheduled Transfer
	public boolean checkLimitQuotaByCorpId1(String corpId, String txnType, double amountEq, double amountMopEq) throws NTBException {
		boolean flag = true;
		Double corpDailyLimit = 0.0;
		try {
			// add by li_zd for CR225 2017-6-8
			corpDailyLimit = getCorpDailyLimit(corpId);// get corp daily limit by corpid
			Double testDouble = amountMopEq - corpDailyLimit.doubleValue();
			// test the difference between (corpDailyTotalAmount.doubleValue() + amountMopEq) and (amountMopEq - corpDailyLimit.doubleValue())
			if(compareTo(amountMopEq -corpDailyLimit.doubleValue() , 0.010 ) >=0){   
				Log.error("***transfer error for the txnType is: " + txnType);
				Log.error("***the transfer amountEq is: " + amountEq);
			    Log.error("***amountMopEq is: " + amountMopEq);
				Log.error("***corpDailyLimit is: " + corpDailyLimit);
//				Log.error("***if transfer the corpDailyTotalAmount will up to: " + (corpDailyTotalAmount+amountMopEq));
				return false;
			}
			//end	
		} catch (Exception e) {
			Log.error("Exception occur at app.cib.service.txn.TransferLimitServiceImpl.checkLimitQuotaByCorpId1(): ", e);

			if (e instanceof NTBException) {
				throw (NTBException) e;
			}
			flag = false;
		}
      
		Log.info("**transfer without exceeding the limit and the txnType is: "+txnType);
	    Log.info("**transfer amountEq is: " + amountEq);
	    Log.info("**amountMopEq is: " + amountMopEq);
	    Log.info("**corpDailyLimit is: " + corpDailyLimit);
//	    Log.info("**corpDailyTotalAmount is: " + (corpDailyTotalAmount+amountMopEq));
		return flag;
	}
	
	private int compareTo(double d1,double d2){
		
		DecimalFormat df = new DecimalFormat("#.000");
		
		System.out.println("df.format(d1)="+df.format(d1));
		
		BigDecimal value1 = new BigDecimal(df.format(d1)).setScale(3);
		BigDecimal value2 = new BigDecimal(df.format(d2)).setScale(3);
		
		return value1.compareTo(value2);
	}
	
	public boolean checkCurAmtLimitQuota(String account, String corpId,
			String txnType, double amountEq, double amountMopEq)
			throws NTBException {
		boolean flag = false;

		try {
			
			double corpDailyLimit =getCorpDailyLimit(corpId) ;
			
			Log.info("corpId="+corpId+"; account="+account+"; corpDailyLimit="+corpDailyLimit+"; amountMopEq="+amountMopEq);
			
			Log.info("amountMopEq - corpDailyLimit="+(amountMopEq - corpDailyLimit));
			
			
			if( compareTo(amountMopEq - corpDailyLimit, 0.010) >=0 ){   
				return flag;
			}
			
			TxnLimit txnLimit = txnLimitDao.findByCorp(corpId, account);

			if (null == txnLimit) {
				txnLimit = txnLimitDao.findByCorp(corpId, TxnLimit.ACCOUNT_ALL);
			}

			if (null == txnLimit) {
				throw new NTBException("err.txn.NoLimit");
			}

			String txnLimitTxnType = txnLimit.getTxnType();  
			
			TxnLimitUsage txnLimitUsage = txnLimitUsageDao.findByCorpAndTxn(
					corpId, txnLimit.getAccount(), txnLimit.getTxnType());
			if (null == txnLimitUsage) {
				txnLimitUsage = new TxnLimitUsage();
				txnLimitUsage.setCorpId(txnLimit.getCorpId());
				txnLimitUsage.setAccount(txnLimit.getAccount());
				txnLimitUsage.setCurrency(txnLimit.getCurrency());
				txnLimitUsage.setPrefId(txnLimit.getPrefId());
				txnLimitUsage.setTxnType(txnLimit.getTxnType());
				txnLimitUsage.setUsageId(CibIdGenerator
						.getIdForOperation("TxnLimitUsage"));
				txnLimitUsage.setUsageDate(DateTime.getCurrentDate());
				Double amount = new Double(0.000);
				txnLimitUsage.setLimit(amount);
				txnLimitUsage.setLimit1(amount);
				txnLimitUsage.setLimit2(amount);
				txnLimitUsage.setLimit3(amount);
				txnLimitUsage.setLimit4(amount);
				txnLimitUsage.setLimit5(amount);
				txnLimitUsage.setLimit6(amount);
				txnLimitUsage.setLimit7(amount);
				txnLimitUsage.setLimit8(amount);
				txnLimitUsage.setLimit9(amount);
				txnLimitUsage.setLimit10(amount);
				txnLimitUsageDao.add(txnLimitUsage);
			} else {
				txnLimitUsage = checkLimitUsageDate(txnLimitUsage);
			}
			
			
			if (TxnLimit.TXN_TYPE_ALL.equals(txnLimitTxnType)) {
				if (TxnLimit.ACCOUNT_ALL.equals(txnLimit.getAccount())) {
					if ( compareTo(amountMopEq - txnLimit.getLimit1().doubleValue() , 0.010) < 0 ) {
						flag = true;
					}
					// add by hjs
					this.setDailyLimit(txnLimit.getLimit1().doubleValue());
					this.setTotalLimit(txnLimitUsage.getLimit().doubleValue() + amountMopEq);
					this.setLimitType(LIMIT_TYPE_CORP_ALL);
				} else {
					if ( amountEq - txnLimit.getLimit1().doubleValue() < 0.010) {
						flag = true;
					}
					// add by hjs
					this.setDailyLimit(txnLimit.getLimit1().doubleValue());
					this.setTotalLimit(txnLimitUsage.getLimit().doubleValue() + amountEq);
					this.setLimitType(LIMIT_TYPE_ALL);
				}
			} else {
				if (TxnLimit.ACCOUNT_ALL.equals(txnLimit.getAccount())) {
					if (txnType.equals(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT)) {
						if ( compareTo(amountMopEq - txnLimit.getLimit1().doubleValue() , 0.010 )<0 ) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit1().doubleValue());
						//this.setTotalLimit(txnLimitUsage.getLimit1().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_BANK)) {
						if ( compareTo(amountMopEq - txnLimit.getLimit2().doubleValue() , 0.010)<0 ) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit2().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit2().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_MACAU)) {
						if ( compareTo(amountMopEq - txnLimit.getLimit3().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit3().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit3().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_OVERSEAS)) {
						if ( compareTo(amountMopEq - txnLimit.getLimit4().doubleValue() , 0.010) < 0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit4().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit4().doubleValue() + amountMopEq);
					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_CORP)) {
						if ( compareTo(amountMopEq - txnLimit.getLimit5().doubleValue() , 0.010) < 0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit5().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit5().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_PAY_BILLS)) {
						if ( compareTo(amountMopEq - txnLimit.getLimit6().doubleValue() , 0.010) < 0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit6().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit6().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TIME_DEPOSIT)) {
						if ( compareTo(amountMopEq - txnLimit.getLimit7().doubleValue() , 0.010) < 0) {
							flag = true;
						}
						this.setDailyLimit(txnLimit.getLimit7().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit7().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_PAYROLL)) {
						if ( compareTo(amountMopEq - txnLimit.getLimit8().doubleValue() , 0.010) < 0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit8().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit8().doubleValue() + amountMopEq);
					}
					// add by hjs
					 else if (txnType.equals(Constants.TXN_SUBTYPE_BANK_DRAFT)) {
						if ( compareTo( amountMopEq //modify by hjs
								- txnLimit.getLimit9().doubleValue() , 0.010) < 0 ) {
							flag = true;
						}
						this.setDailyLimit(txnLimit.getLimit9().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit9().doubleValue() + amountMopEq);

					} else if (txnType.equals(Constants.TXN_TYPE_CASHIER_ORDER)) {
						if ( compareTo(amountMopEq - txnLimit.getLimit10().doubleValue() , 0.010) < 0) {
							flag = true;
						}
						this.setDailyLimit(txnLimit.getLimit10().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit10().doubleValue() + amountMopEq);

					}
					this.setLimitType(LIMIT_TYPE_CORP_NORMAL);
				} else {
					if (txnType.equals(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT)) {
						if ( compareTo(amountEq - txnLimit.getLimit1().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit1().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit1().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_BANK)) {
						if ( compareTo(amountEq - txnLimit.getLimit2().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit2().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit2().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_MACAU)) {
						if ( compareTo(amountEq - txnLimit.getLimit3().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit3().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit3().doubleValue() + amountEq);

					} else if ( txnType.equals(Constants.TXN_TYPE_TRANSFER_OVERSEAS)) {
						if ( compareTo(amountEq - txnLimit.getLimit4().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit4().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit4().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_CORP)) {
						if ( compareTo(amountEq - txnLimit.getLimit5().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit5().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit5().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_PAY_BILLS)) {
						if ( compareTo(amountEq - txnLimit.getLimit6().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit6().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit6().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_TIME_DEPOSIT)) {
						if ( compareTo(amountEq - txnLimit.getLimit7().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit7().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit7().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_PAYROLL)) {
						if ( compareTo(amountEq - txnLimit.getLimit8().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit8().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit8().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_BANK_DRAFT)) {
						if (  compareTo(amountEq - txnLimit.getLimit9().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit9().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit9().doubleValue() + amountEq);

					} else if (txnType.equals(Constants.TXN_TYPE_CASHIER_ORDER)) {
						if ( compareTo(amountEq - txnLimit.getLimit10().doubleValue() , 0.010)<0) {
							flag = true;
						}
						// add by hjs
						this.setDailyLimit(txnLimit.getLimit10().doubleValue());
						this.setTotalLimit(txnLimitUsage.getLimit10().doubleValue() + amountEq);

					}
					// add by hjs
					this.setLimitType(LIMIT_TYPE_NORMAL);
				}
			}
		} catch (Exception e) {
			Log.error("Exception occur at app.cib.service.txn."
					+ "TransferLimitServiceImpl.checkLimitQuota: ", e);

			if (e instanceof NTBException) {
				throw (NTBException) e;
			}

			flag = false;
		}
      
		return flag;
	}
	
	//add by lr for CR225 2017-3-2
	public Double getCorpDailyLimit(String corpId)throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");
        String dailyLimitString = null;
        Double dailyLimit = 0.0d;
        Corporation corp = corpService.view(corpId);
        String corpType = corp.getCorpType();
        if(null!=corpType){
        	Log.info("the corpId is:"+corpId+" and the corpType is:"+corpType);
             if((MOP_DAILY_LIMIT_MODE1).equals(corpType)){//mode3
        	 dailyLimitString = Config.getProperty("MOP.DAILY.LIMIT.MODE1");
             }
             if((MOP_DAILY_LIMIT_MODE2).equals(corpType)){//mode2
        	 dailyLimitString = Config.getProperty("MOP.DAILY.LIMIT.MODE2");
             }
             if((MOP_DAILY_LIMIT_MODE3).equals(corpType)){//mode4
        	 dailyLimitString = Config.getProperty("MOP.DAILY.LIMIT.MODE3");
             }
             if((MOP_DAILY_LIMIT_MODE4).equals(corpType)){//mode1
        	 dailyLimitString = Config.getProperty("MOP.DAILY.LIMIT.MODE4");
             }
             BigDecimal bDecimal = new BigDecimal(dailyLimitString);
             dailyLimit = bDecimal.setScale(2).doubleValue();
             Log.info("dailyLimit="+dailyLimit);
       }
        else{
        	Log.error("without corpType");
        }

		return dailyLimit;
	}
	public Double getCorpDailyAcctTotal(String corpId,String date)throws NTBException {//get corporation daily transfer amount
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
		.getBean("ExchangeRatesService");
		Double todayTransferAmount = 0.0;	
    	List aList = new ArrayList();
    	String currency = null;
    	 try {
			aList = txnLimitUsageDao.getTxnLimitUsageList(corpId,date);
			for(int i = 0 ;i < aList.size();i++){
				Double temp = 0.0;
				TxnLimitUsage txnLimitUsage = new TxnLimitUsage();
				txnLimitUsage = (TxnLimitUsage)aList.get(i);
				temp = txnLimitUsage.getLimit()+
				txnLimitUsage.getLimit1()+txnLimitUsage.getLimit2()+
				txnLimitUsage.getLimit3()+txnLimitUsage.getLimit4()+
				txnLimitUsage.getLimit5()+txnLimitUsage.getLimit6()+
				txnLimitUsage.getLimit7()+txnLimitUsage.getLimit8()+
				txnLimitUsage.getLimit9()+txnLimitUsage.getLimit10();//JYP currency account
				currency = txnLimitUsage.getCurrency();
				BigDecimal equivalentMOP = exRatesService.getEquivalent(corpId, currency, "MOP",
						new BigDecimal(temp),
						null, 2);
//				BigDecimal equivalentMOP2 = exRatesService.getEquivalent(corpId, currency, "MOP",
//						new BigDecimal(equivalentMOP.doubleValue()),
//						null, 2);
				todayTransferAmount+=equivalentMOP.doubleValue();
				Log.info("the currency is:"+currency);
			}
    	 } catch (NTBException e) {
 			throw e;
 		}
		Log.info("the corporation today transferAmount is:"+todayTransferAmount);
    	return todayTransferAmount.doubleValue();
    }//end
	private TxnLimitUsage checkLimitUsageDate(TxnLimitUsage txnLimitUsage) {
		String curDate = DateTime.getCurrentDate();
        TxnLimitUsage newTxnLimitUsage = new TxnLimitUsage();
        newTxnLimitUsage.setCorpId(txnLimitUsage.getCorpId());
        newTxnLimitUsage.setAccount(txnLimitUsage.getAccount());
        newTxnLimitUsage.setCurrency(txnLimitUsage.getCurrency());
        newTxnLimitUsage.setPrefId(txnLimitUsage.getPrefId());
        newTxnLimitUsage.setTxnType(txnLimitUsage.getTxnType());
        newTxnLimitUsage.setUsageId(CibIdGenerator.getIdForOperation("TxnLimitUsage"));
        newTxnLimitUsage.setUsageDate(curDate);

		if (!curDate.equals(txnLimitUsage.getUsageDate())) {
			Double amount = new Double(0.000);
			newTxnLimitUsage.setLimit(amount);
			newTxnLimitUsage.setLimit1(amount);
			newTxnLimitUsage.setLimit2(amount);
			newTxnLimitUsage.setLimit3(amount);
			newTxnLimitUsage.setLimit4(amount);
			newTxnLimitUsage.setLimit5(amount);
			newTxnLimitUsage.setLimit6(amount);
			newTxnLimitUsage.setLimit7(amount);
			newTxnLimitUsage.setLimit8(amount);
			newTxnLimitUsage.setLimit9(amount);
			newTxnLimitUsage.setLimit10(amount);
		}else{
            newTxnLimitUsage.setLimit(txnLimitUsage.getLimit());
            newTxnLimitUsage.setLimit1(txnLimitUsage.getLimit1());
            newTxnLimitUsage.setLimit2(txnLimitUsage.getLimit2());
            newTxnLimitUsage.setLimit3(txnLimitUsage.getLimit3());
            newTxnLimitUsage.setLimit4(txnLimitUsage.getLimit4());
            newTxnLimitUsage.setLimit5(txnLimitUsage.getLimit5());
            newTxnLimitUsage.setLimit6(txnLimitUsage.getLimit6());
            newTxnLimitUsage.setLimit7(txnLimitUsage.getLimit7());
            newTxnLimitUsage.setLimit8(txnLimitUsage.getLimit8());
            newTxnLimitUsage.setLimit9(txnLimitUsage.getLimit9());
            newTxnLimitUsage.setLimit10(txnLimitUsage.getLimit10());
        }
		return newTxnLimitUsage;
	}

	private TxnLimitDao txnLimitDao = null;

	private TxnLimitUsageDao txnLimitUsageDao = null;


	/*
	 * add by hjs
	 */
	public double getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(double dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public double getTotalLimit() {
		return totalLimit;
	}

	public void setTotalLimit(double totalLimit) {
		this.totalLimit = totalLimit;
	}

	public String getLimitType() {
		return limitType;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

}
