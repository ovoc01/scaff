package org.scaffolding.database.schema.column;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class PrimaryKey extends Column {

    @Override
    public void init(ResultSet rs) throws SQLException {
        setName(rs.getString("COLUMN_NAME"));
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        setType(getClassColumnType(resultSetMetaData.getColumnType(1)).getSimpleName());
        setTypeClz(getClassColumnType(1));
    }
    private static Class<?> getClassColumnType(int columnType) {
        switch (columnType) {
            case Types.INTEGER:
                return Integer.class;
            case Types.BIGINT:
                return Long.class;
            case Types.VARCHAR:
                return String.class;
            // Add more cases as needed based on your database's data types
            default:
                return null;
        }
    }
}
