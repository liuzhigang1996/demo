package com.neturbo.set.transaction;

import java.util.*;
import com.neturbo.set.exception.*;

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
public interface PacketHandler {
    public boolean processPacket(String transId, Map packetData) throws NTBHostException;
}
