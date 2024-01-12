package org.scaffolding.misc;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static String readFile(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            String line;
            while((line=br.readLine())!=null){
                sb.append(line);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static String getContentBetweenPattern(String pattern,String input){
        Pattern pattern1 = Pattern.compile(pattern,Pattern.DOTALL);
        Matcher matcher = pattern1.matcher(input);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

    public static Properties loadColumnTypeFieldMap(String filepath) throws IOException {
        Properties properties = new Properties();

        try(FileInputStream fl = new FileInputStream(filepath)){
            properties.load(fl);
        }
        return properties;
    }
}
