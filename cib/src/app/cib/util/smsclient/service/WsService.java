package app.cib.util.smsclient.service;

import com.neturbo.set.exception.NTBException;

import java.io.FileInputStream;
import java.util.*;

/**
 * <p>
 * Title:webService �ӿ�
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 */
public interface WsService {


	/**
	 * ���Ͷ���
	 * @param templateType
	 * @param to
	 * @param dataMap
	 * @param language
	 * @return
	 * @throws NTBException
	 */
	public String sendSms(String templateType, String to, Map dataMap,
			String language) throws NTBException;
}
