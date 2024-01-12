package org.scaffolding.database.schema.column;


import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.scaffolding.database.schema.TableSchema;
import org.scaffolding.misc.Utilities;

@Getter
@Setter
public class Column {
    String name;
    String type;
    Class<?> typeClz;
    boolean nullable;

    public String getNameUpperScale() {
        return TableSchema.convertToUpperScale(this.getName());
    }

    private static final Map<String, Class<?>> wrapperClassMap;
    static {
        wrapperClassMap = new HashMap<>();
        wrapperClassMap.put("int4", Integer.class);
        wrapperClassMap.put("serial", Integer.class);
        wrapperClassMap.put("float8", Double.class);
        wrapperClassMap.put("date", java.sql.Date.class);
        wrapperClassMap.put("varchar", String.class);
        wrapperClassMap.put("time", java.sql.Time.class);
        wrapperClassMap.put("timestamp", java.sql.Timestamp.class);
    }

    public void init(ResultSet rs) throws SQLException {
        setName(rs.getString("COLUMN_NAME"));
        setType(rs.getString("TYPE_NAME"));
        setTypeClz(rs.getString("TYPE_NAME").getClass());
        setNullable(rs);
    }

    public String getClassString() {
        return wrapperClassMap.get(getType()).getName();
    }

    void setNullable(boolean bo) {
        this.nullable = bo;
    }

    public void setNullable(ResultSet rs) throws SQLException {
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int columnIndex = rs.findColumn("COLUMN_NAME");
        int isNullable = resultSetMetaData.isNullable(columnIndex);
        setNullable(ResultSetMetaData.columnNoNulls == isNullable);
    }

    public String upperFirstChar() {
        return this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
    }

    public String getUpperFirstChar() {
        return TableSchema.convertToUpperScale(this.upperFirstChar());
    }


    public String formatToGetter(){
        //TODO
       return null;
    }

    public String formatToSetter(){
        //TODO
       return null;
    }

    public String formatToField(){
        return "\tprivate " + getClassString() + " " + getName() + ";";
    }


    public String formatGetterAndSetter() throws IOException {
        InputStream inputStream = Column.class.getClassLoader().getResourceAsStream("backend/java/default.temp");
        String getterSetterTemplate = Utilities.readFile(inputStream);
        String pattern = "##getters&setters(.*?)!!getters&setters";
        String content = Utilities.getContentBetweenPattern(pattern,getterSetterTemplate);
        content = content.replaceAll("##type","String");
        content = content.replaceAll("##nameUpperscale",upperFirstChar());
        content = content.replaceAll("##name",getName());
        System.out.println(content);
        return null;
    }
}
