package app.cib.service.txn;

import java.util.List;
import java.util.Map;

import com.neturbo.set.exception.NTBException;


import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;


/**
 * @author mxl
 * 2006-07-16
 */

public interface TransferTemplateService {
	/* add a template in BANK */
	public TransferBank addTemplateBANK(TransferBank pojoTemplate) throws NTBException;
	/* edit a template in BANK by mxl */
	public TransferBank editTemplateBANK(TransferBank pojoTemplate) throws NTBException;
	/* delete a template in BANK edit by mxl 0123*/
	public TransferBank deleteTemplateBANK(String transId) throws NTBException;
	/* list all template in BANK * edit by mxl 0123 */
	public List listTemplateBANK(String corpID) throws NTBException;
	
	/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
    public List listTemplateBANK(String corpID,String ownFlag) throws NTBException;
    /* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */

    /* Execute template in BANK */
	public TransferBank execTemplateBANK(TransferBank pojoTemplate) throws NTBException;
	/* view template in BANK */
	public TransferBank viewTemplate(String transID) throws NTBException ;
	
	
	/* add a template in Macau */
	public TransferMacau addTemplateMacau(TransferMacau pojoTemplate) throws NTBException;
	/* edit a template in Macau edit by mxl 0123*/
	public TransferMacau editTemplateMacau(TransferMacau pojoTemplate) throws NTBException;
	/* delete a template in Macau */
	public TransferMacau deleteTemplateMacau(String transId) throws NTBException;
	/* list all template in Macau edit by mxl 0123*/
	public List listTemplateMacau(String corpID) throws NTBException;
	/* Execute template in Macau */
	public TransferMacau execTemplateMacau(TransferMacau pojoTemplate) throws NTBException;
	/* view template in Macau */
	public TransferMacau viewTemplateMacau(String transID) throws NTBException ;
	
	/* add a template in Oversea */
	public TransferOversea addTemplateOversea(TransferOversea pojoTemplate) throws NTBException;
	/* edit a template in Oversea by mxl 0123*/
	public TransferOversea editTemplateOversea(TransferOversea pojoTemplate) throws NTBException;
	/* delete a template in Oversea by mxl 0123*/
	public TransferOversea deleteTemplateOversea(String transId) throws NTBException;
	/* list all template in Oversea edit by mxl 0123*/
	public List listTemplateOversea(String corpID) throws NTBException;
	/* Execute template in Oversea */
	public TransferOversea execTemplateOversea(TransferOversea pojoTemplate) throws NTBException;
	/* view template in Oversea */
	public TransferOversea viewTemplateOversea(String transID) throws NTBException ;
	
}
