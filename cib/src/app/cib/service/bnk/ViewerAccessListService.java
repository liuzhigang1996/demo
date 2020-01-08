package app.cib.service.bnk;

import java.util.*;
import com.neturbo.set.exception.*;

public interface ViewerAccessListService {
	public List listViewer() throws NTBException;
    public List listSelectedCorpListByViewer(String viewerId) throws NTBException ;
    public List listSelectedCorpListByBatchId(String batchId) throws NTBException ;
    public List listCandidateCorp(List selectedList) throws NTBException ;
    public List listAllCorp() throws NTBException;
    public boolean isPending(String userId) throws NTBException;
    public String getBatchIdByViewer(String viewerId) throws NTBException;
    public String getBatchIdBeforeByBatchId(String batchId) throws NTBException;
    public List getViewerByBatchId(String batchId) throws NTBException;
    public void insertForApprove(List newAccessList)throws NTBException;
    public void updateForApprove(String batchId)throws NTBException;
    public void updateForReject(String batchId)throws NTBException;
}
