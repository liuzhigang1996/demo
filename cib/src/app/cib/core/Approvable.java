package app.cib.core;

import com.neturbo.set.exception.NTBException;

/**
 * @author panwen
 * 
 */
public interface Approvable {
	/**
	 * 查询授权内容详细信息，set/getResultData等方法要通过传入的bean调用，返回一个显示详细信息的页面链接。
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
	 * 授权通过需要做的处理，set/getResultData等方法要通过传入的bean调用，处理成功返回true。
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
	 * 授权拒绝需要做的处理，set/getResultData等方法要通过传入的bean调用，处理成功返回true;
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
	 * 交易撤销需要做的处理，set/getResultData等方法要通过传入的bean调用，处理成功返回true;
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
