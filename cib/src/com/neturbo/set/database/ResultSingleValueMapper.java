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
public class ResultSingleValueMapper implements RowMapper {
    public ResultSingleValueMapper() {
    }

    public Object mapRow(ResultSet rs, int _int) throws
            SQLException {
        return rs.getObject(1);
    }

}
