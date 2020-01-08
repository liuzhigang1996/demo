package com.neturbo.set.core;

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
public interface NTBPermission {
    public String getPermissionId();
    public String getPermissionType();
    public String getPermissionResource();
    void setPermissionId(String permissionId);
    void setPermissionType(String permissionType);
    void setPermissionResource(String permissionResource);
}
