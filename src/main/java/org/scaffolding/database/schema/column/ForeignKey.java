package org.scaffolding.database.schema.column;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForeignKey extends Column {
    String referenceTable;
    String referenceColumn;

    public void init(ResultSet rs, DatabaseMetaData metaData, Connection connection) throws SQLException {
        setReferenceTable(rs.getString("PKTABLE_NAME"));
        setReferenceColumn(rs.getString("PKCOLUMN_NAME"));



        ResultSet columnResultSet = metaData.getColumns(connection.getCatalog(), null, getReferenceTable(),
                getReferenceColumn());
        if (columnResultSet.next()) {
            super.init(columnResultSet);
            setName(rs.getString("FKCOLUMN_NAME"));
            //System.out.println(getName());
        }
    }

    public String upperFirstChar() {
        return this.referenceTable.substring(0, 1).toUpperCase() + this.referenceTable.substring(1);
    }
}
