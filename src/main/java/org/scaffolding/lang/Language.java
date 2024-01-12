package org.scaffolding.lang;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Language {
    String lang;
    List<String> webFramework;
    List<String> orm;

    public Language(String lang,Map<String ,Object> langMap) {
        this.lang = lang;
        setWebFramework(langMap.get("webFramework"));
        setORM(langMap.get("orm"));
    }

    private void setWebFramework(Object webFramework) {
        String webFrameworkString = (String) webFramework;
        this.webFramework = List.of(webFrameworkString.trim().split(","));
    }

    private void setORM(Object orm) {
        String ormString = (String) orm;
        this.orm = List.of(ormString.trim().split(","));
    }

    public String getWebFramework(String webFramework){
        return this.webFramework.stream().filter(webFramework::equals).findFirst().orElse(null);
    }

    public String getORM(String orm){
        return this.orm.stream().filter(orm::equals).findFirst().orElse(null);
    }


}
