package org.scaffolding.database.schema;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.scaffolding.database.schema.column.Column;
import org.scaffolding.database.schema.column.ForeignKey;
import org.scaffolding.database.schema.column.PrimaryKey;
import org.scaffolding.misc.Utilities;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TableSchema {
    String name;
    PrimaryKey primaryKey;
    ArrayList<ForeignKey> foreignKeys;
    ArrayList<Column> columns;
    ArrayList<String> columnNames;
    String database;

    public TableSchema(ResultSet resultSet) throws SQLException {
        name = resultSet.getString("TABLE_NAME");
        primaryKey = new PrimaryKey();
        foreignKeys = new ArrayList<ForeignKey>();
        columns = new ArrayList<Column>();
        columnNames = new ArrayList<String>();
    }

    public String getNameUpperScale() {
        return convertToUpperScale(this.getEntityName());
    }

    public static String convertToUpperScale(String name) {
        String[] values = name.split("_");
        String value = values[0];
        if (values.length > 1) {
            value += toUpperCase(values[1]);
        }
        return value;
    }

    public static void main(String[] args) {
        System.out.println(convertToUpperScale("Article"));
    }

    public static String toUpperCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public void initPrimaryKey(ResultSet rs) throws SQLException {
        while (rs.next()) {
            primaryKey = new PrimaryKey();
            primaryKey.init(rs);
            columnNames.add(primaryKey.getName());
        }
    }

    public void initForeignKeys(ResultSet rs, DatabaseMetaData data, Connection c) throws SQLException {
        while (rs.next()) {
            ForeignKey foreignKey = new ForeignKey();
            foreignKey.init(rs, data, c);
            foreignKeys.add(foreignKey);
            columnNames.add(foreignKey.getName());
        }
    }

    public void initColumns(ResultSet rs) throws SQLException {
        while (rs.next()) {

            Column column = new Column();
            column.init(rs);

            if (!column.getName().equals(primaryKey.getName()) && !columnNames.contains(column.getName())) {
                columnNames.add(column.getName());
                columns.add(column);
            }
        }
    }

    public String upperFirstChar() {
        return this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
    }

    public String getServiceName() {
        return upperFirstChar() + "Service";
    }

    public String getEntityName() {
        return upperFirstChar();
    }

    public String getRepositoryName() {
        return upperFirstChar() + "Repository";
    }

    public String getControllerName() {
        return upperFirstChar() + "Controller";
    }

    public ArrayList<String> classToImport() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Column column : this.getColumns()) {
            if (!column.getTypeClz().getName().contains("java.lang"))
                arrayList.add(column.getClass().getName());
        }
        return arrayList;
    }




    public void drawTableSchema(){
        StringBuilder sb = new StringBuilder();
        sb.append("Table: ").append(this.getName()).append(",Column: ");
        if (primaryKey != null) {
            sb.append(primaryKey.getName()).append(", ");
        }

        // Append foreign keys if exist
        if (foreignKeys != null && !foreignKeys.isEmpty()) {
            for (ForeignKey foreignKey : foreignKeys) {
                sb.append(foreignKey.getName()).append(", ");
            }
        }

        // Append columns
        for (Column column : columns) {
            sb.append(column.getName()).append(", ");
        }
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        System.out.println(sb.toString());
    }

    public String getAllGettersAndSetters() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (primaryKey != null) {
            sb.append(primaryKey.formatGetterAndSetter()).append("\n");
        }
        if (foreignKeys != null && !foreignKeys.isEmpty()) {
            for (ForeignKey foreignKey : this.getForeignKeys()) {
                sb.append(foreignKey.formatGetterAndSetter()).append("\n");
            }
        }
        for (Column column : this.getColumns()) {
            sb.append(column.formatGetterAndSetter()).append("\n");
        }
        return sb.toString();
    }

    public String getAllFields() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (primaryKey != null) {
            sb.append(primaryKey.formatFields());
        }
        if (foreignKeys != null && !foreignKeys.isEmpty()) {
            for (ForeignKey foreignKey : this.getForeignKeys()) {
                sb.append(foreignKey.formatFields());
            }
        }
        for (Column column : this.getColumns()) {
            sb.append(column.formatFields());
        }
        return sb.toString();
    }

    public String toEntity() throws IOException {
        String entityTemplate = Utilities.loadEntityTemplate();
        entityTemplate = entityTemplate.replaceAll("##name", this.getEntityName());
        entityTemplate = entityTemplate.replaceAll("##fields", this.getAllFields());
        entityTemplate = entityTemplate.replaceAll("##getters&setters", this.getAllGettersAndSetters());

        return entityTemplate;
    }
}
