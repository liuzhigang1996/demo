package app.cib.core;

import javax.sql.*;

import org.springframework.transaction.*;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import java.util.*;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

import java.sql.*;

/**
 * @author nabai 2005-12-17
 */
public class IdGeneratorJdbcDao {

	private static Connection con;

	private DataSource dataSource;

	private PlatformTransactionManager transactionManager;

	// ������ݿ�����±��
	private static String UPDATE_SEQ = "UPDATE TELLER_SEQUENCE SET TELLER=?,CURRENT_SEQUENCE=?,EFFECTIVE_DATE=? WHERE SEQ_NO=? ";

	private static String ADD_SEQ = "INSERT INTO TELLER_SEQUENCE(SEQ_NO,TELLER,CURRENT_SEQUENCE,EFFECTIVE_DATE) VALUES(?,?,?,?)";

	public IdGeneratorJdbcDao() {
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public synchronized void addTellerSequence(String seqNo, String teller,
			Integer currentSeqNo, String effectiveDate) throws Exception {
		for (int i = 0; i < 3; i++) {
			try {
				if (con == null || con.isClosed()) {
					con = dataSource.getConnection();
				}
				PreparedStatement stat = con.prepareStatement(ADD_SEQ);
				stat.setString(1, seqNo);
				stat.setString(2, teller);
				stat.setInt(3, currentSeqNo.intValue());
				stat.setString(4, effectiveDate);
				stat.executeUpdate();
				stat.close();
				con.commit();
				//add by linrui for free conn
				freeConnection();
				//end
				return;
			} catch (Exception e) {
				try {
					/*if (con == null || con.isClosed()) {
					} else {
						con.close();
					}*/
					//add by linrui for free conn
					freeConnection();
					//end
				} catch (Exception e2) {
					Log.error("Error closing connection when updating teller sequence", e2);
				}
				Log.error("Error updating teller sequence, " + i +" times tried", e);
			}finally{
				//add by linrui for free conn
				freeConnection();
				//end
			}
		}
		throw new NTBException("Error update teller sequnce in ID gerator");
	}

	public synchronized void updateTellerSequence(String seqNo, String teller,
			Integer currentSeqNo, String effectiveDate) {
		try {
			if (con == null || con.isClosed()) {
				con = dataSource.getConnection();
			}
			PreparedStatement stat = con.prepareStatement(UPDATE_SEQ);
			stat.setString(1, teller);
			stat.setInt(2, currentSeqNo.intValue());
			stat.setString(3, effectiveDate);
			stat.setString(4, seqNo);
			stat.executeUpdate();
			Log.info("PrepardStatement:" + stat.toString());
			stat.close();
			con.commit();
		} catch (Exception e) {
			Log.error("Error updating teller sequence", e);
		}finally{
			//add by linrui for free conn
			freeConnection();
			//end
		}

		/*
		 * DefaultTransactionDefinition def = new
		 * DefaultTransactionDefinition(); TransactionStatus status =
		 * transactionManager.getTransaction(def); try { JdbcTemplate
		 * jdbcTemplate = new JdbcTemplate(dataSource);
		 * jdbcTemplate.update(UPDATE_ID, new Object[]{teller, currentSeqNo,
		 * effectiveDate, seqNo}); } catch (DataAccessException e1) {
		 * Log.error("Error updating teller sequence", e1);
		 * transactionManager.rollback(status); //
		 * Ҳ���Ԉ���status.setRollbackOnly(); throw e1; }catch(Exception e2){
		 * Log.error("Error updating teller sequence", e2);
		 * transactionManager.rollback(status); //
		 * Ҳ���Ԉ���status.setRollbackOnly(); } transactionManager.commit(status);
		 */
	}

	public synchronized void freeConnection() {
		try {
			if (con == null || con.isClosed()) {
			} else {
				con.close();
			}
		} catch (Exception e) {
			Log.error("Error closing connection for  teller sequence", e);
		}
	}
}
