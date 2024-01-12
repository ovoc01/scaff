package org.scaffolding;

import org.scaffolding.cli.ScaffoldCLI;
import org.scaffolding.database.Database;
import org.scaffolding.database.schema.column.Column;
import org.scaffolding.lang.Language;
import org.scaffolding.yml.ScaffoldProps;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        /*Yaml yaml = new Yaml();

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("settings.yml")) {
            Map<String, Object> data = yaml.load(inputStream);


            if (data != null && data.containsKey("lang") && data.get("lang") instanceof Map) {
                Map<String, Object> langMap = (Map<String, Object>) data.get("lang");
                if (langMap.containsKey("java") && langMap.get("java") instanceof Map) {
                    Language language = new Language("java",(Map<String, Object>) langMap.get("java"));
                    System.out.println(language);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/token", "postgres", "pixel");
        Database database = new Database();
        database.initAllTableSchemas(c);

        Language l = new Language();
        l.setLang("java");
        ScaffoldProps.language = l;

        String entity = database.getTableSchemas().get(0).toEntity();
        System.out.println(entity);


        /*ScaffoldCLI scaffoldCLI = new ScaffoldCLI(args);
        scaffoldCLI.run();*/
    }


}
