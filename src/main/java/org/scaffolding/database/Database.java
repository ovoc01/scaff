package org.scaffolding.database;

import lombok.Getter;
import lombok.Setter;
import org.scaffolding.database.schema.TableSchema;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Getter
@Setter
public class Database implements Serializable {
    private ArrayList<TableSchema> tableSchemas;


    public Database() {
        tableSchemas = new ArrayList<TableSchema>();
    }

    public void initAllTableSchemas(Connection connection) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet tablesResultSet = databaseMetaData.getTables(null, null, null, new String[] { "TABLE" });
        while (tablesResultSet.next()) {
            TableSchema tableSchema = new TableSchema(tablesResultSet);
            tableSchema.setDatabase(connection.getCatalog());
            ResultSet primaryKeysResultSet = databaseMetaData.getPrimaryKeys(null, null, tableSchema.getName());

            tableSchema.initPrimaryKey(primaryKeysResultSet);
            ResultSet foreignKeysResultSet = databaseMetaData.getImportedKeys(null, null, tableSchema.getName());
            tableSchema.initForeignKeys(foreignKeysResultSet,databaseMetaData,connection);

            ResultSet columnsResultSet = databaseMetaData.getColumns(null, null, tableSchema.getName(), null);
            tableSchema.initColumns(columnsResultSet);
            tableSchemas.add(tableSchema);
        }
    }


    public void showTables(){
        for (TableSchema tableSchema : this.tableSchemas){
            tableSchema.drawTableSchema();
        }
    }

}