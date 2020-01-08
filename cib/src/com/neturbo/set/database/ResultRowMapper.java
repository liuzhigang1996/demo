package com.neturbo.set.database;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.util.HashMap;

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
public class ResultRowMapper implements RowMapper {
    public ResultRowMapper() {
    }

    public Object mapRow(ResultSet rs, int _int) throws
            SQLException {
        HashMap res = new HashMap();
        ResultSetMetaData metaData = rs.getMetaData();
        //select name1 as label1 from table1
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String name = metaData.getColumnLabel(i);
            res.put(name, rs.getObject(i));
        }
        return res;
    }

}
