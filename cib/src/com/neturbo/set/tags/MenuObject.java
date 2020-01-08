package com.neturbo.set.tags;

import com.neturbo.set.core.NTBUser;
import com.neturbo.set.xml.XMLElement;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface MenuObject {
    public void setUser(NTBUser user);
    public NTBUser getUser();
    public void setXmlElement(XMLElement xml);
    public XMLElement getXmlElement();
    public boolean isShowMenu();
}
