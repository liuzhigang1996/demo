package app.cib.core;

import com.neturbo.set.exception.NTBException;

/**
 * @author panwen
 * 
 */
public interface Approvable {
	/**
	 * ��ѯ��Ȩ������ϸ��Ϣ��set/getResultData�ȷ���Ҫͨ�������bean���ã�����һ����ʾ��ϸ��Ϣ��ҳ�����ӡ�
	 * 
	 * @param txnType
	 * @param id
	 * @param bean
	 * @return
	 * @throws NTBException
	 */
	String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException;

	/**
	 * ��Ȩͨ����Ҫ���Ĵ���set/getResultData�ȷ���Ҫͨ�������bean���ã�����ɹ�����true��
	 * 
	 * @param txnType
	 * @param id
	 * @param bean
	 * @return
	 * @throws NTBException
	 */
	boolean approve(String txnType, String id, CibAction bean)
			throws NTBException;

	/**
	 * ��Ȩ�ܾ���Ҫ���Ĵ���set/getResultData�ȷ���Ҫͨ�������bean���ã�����ɹ�����true;
	 * 
	 * @param txnType
	 * @param id
	 * @param bean
	 * @return
	 * @throws NTBException
	 */
	boolean reject(String txnType, String id, CibAction bean)
			throws NTBException;

	/**
	 * ���׳�����Ҫ���Ĵ���set/getResultData�ȷ���Ҫͨ�������bean���ã�����ɹ�����true;
	 * 
	 * @param txnType
	 * @param id
	 * @param bean
	 * @return
	 * @throws NTBException
	 */
	boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException;
}
