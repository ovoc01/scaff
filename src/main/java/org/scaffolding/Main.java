package org.scaffolding;

import org.scaffolding.database.schema.column.Column;
import org.scaffolding.lang.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
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

        Column column = new Column();
        column.setName("id");
        column.formatGetterAndSetter();
    }


}
