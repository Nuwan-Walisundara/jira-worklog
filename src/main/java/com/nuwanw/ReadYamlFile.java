package com.nuwanw;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.nuwanw.model.Configuration;

public class ReadYamlFile {

  private  static   Configuration  data ;
 static  void read() throws Exception {  
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        InputStream inputStream = ReadYamlFile.class
                                        .getClassLoader()
                                        .getResourceAsStream("config.yaml");
        // Read the YAML file into a Map
        data = yaml.load(inputStream);

    }

 public static boolean isValidUser(String desplayName)  {
   
    return data.getUsers().stream()
               .map(a-> a.getName())
               .anyMatch( desplayName.trim()::equalsIgnoreCase);
 }

 public static String clockyUrl() throws Exception{
   return    data.getClockwork().getUrl(); 

 }
 public static String token() throws Exception{
   return    data.getClockwork().getToken();
 }

 public static String project(){
  return data.getOodo().getProject();
 }

 public static String mapToOOdoTask(String desplayName,String strOrg){
AtomicReference<String> task = new AtomicReference<String>(strOrg);
   data.getUsers().stream()
                  .filter(a->desplayName.trim().equalsIgnoreCase(a.getName()))
                  .findFirst()
               
                  .ifPresent(a->{ 
                                  if( a.getTasks().stream()
                                             .noneMatch(strOrg.trim()::equalsIgnoreCase)){
                                             task.set(a.getDefaultTask().trim()); 
                                             }});
    return task.get();
}
}
