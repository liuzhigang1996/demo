package app.cib.service.bnk;

import java.util.*;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.bnk.CorporationHis;

import com.neturbo.set.exception.*;
import com.neturbo.set.core.NTBUser;

public interface CorporationService {
    public void add(Corporation corpObj)throws NTBException;
    public void remove(Corporation corpObj)throws NTBException;
    public void update(Corporation corpObj)throws NTBException;
    public List list(Corporation corpObj) throws NTBException;
    public Corporation view(String corpId) throws NTBException ;
    public String getCorporationStatus(String corpId, NTBUser user,Locale lang) throws NTBException ;
    public void addHis(CorporationHis corpHis) throws NTBException ;
    public void updateHis(CorporationHis corpHis) throws NTBException ;
    public CorporationHis loadPendingHis(String corpId)throws NTBException ;
    public CorporationHis loadHisBySeqNo(String seqNo) throws NTBException;
    public List listCorpHisByAfterId(String prefId) throws NTBException;
    public List listMerchantGroup(String merchantGroup) throws NTBException;

}
