package app.cib.util.smsclient.util;

public class MailConstant {

	public static int SIZE = 1024*1024*10;
	public static String ERROR_LIMITSIZE = "0011\u9644\u4ef6\u5927\u5c0f\u8d85\u904e10M" ;
	
	
	
	


	// 发送email 的返回状态
	/**
	 * 郵件發送成功
	 */
	public static final String SEND_EMAIL_RESPONSE_MSG_NORMAL_0000 = "0000";// 郵件發送成功
	/**
	 * 收件人為空
	 */
	public static final String SEND_EMAIL_RESPONSE_MSG_0001 = "0001";
	/**
	 * 超過收件人的上限
	 */
	public static final String SEND_EMAIL_RESPONSE_MSG_0002 = "0002";
	/**
	 * 接收的發送郵件參數為空
	 */
	public static final String SEND_EMAIL_RESPONSE_MSG_0003 = "0003";
	/**
	 * 通訊錄中找不到收件人
	 */
	public static final String SEND_EMAIL_RESPONSE_MSG_0008 = "0008";
	/**
	 * 發送郵件系統錯誤
	 */
	public static final String SEND_EMAIL_RESPONSE_MSG_0009 = "0009";
	
	public static final String  ERROR_CODE_STATUS_FAILED = "2222";//發送失敗
}
