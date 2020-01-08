package com.jsax.service.txn;

import org.springframework.context.ApplicationContext;

import app.cib.service.txn.TransferService;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.neturbo.set.core.Config;

public class CgdMacauService extends JsaxAction implements JsaxService {

	public void doTransaction() throws Exception {
		//String[] name = getParameter("bank").split(",");
		//String bank = name[0];
		String bank = getParameter("bank");
		
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
        // ����ҵ���߼�����
		String CGD_FlAG =  transferService.loadCgdMacau(bank);
        // ���뵽sub response�б�
		this.addSubResponseList(TARGET_TYPE_FIELD, "cgd_flag", CGD_FlAG);
		
	}

}
