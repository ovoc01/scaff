package org.scaffolding.generator;

import lombok.Data;
import org.scaffolding.database.schema.TableSchema;
import org.scaffolding.generator.props.Properties;

import java.util.ArrayList;

@Data
public class GeneratedClasses {
    String language;
    String framework;
    String[] templates;
    ArrayList<TableSchema> tableSchemas;

    public Properties initProperties(){
        //TODO
        return null;
    }
}
