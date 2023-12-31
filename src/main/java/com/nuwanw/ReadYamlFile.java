/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package com.nuwanw;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.nuwanw.model.Configuration;

public class ReadYamlFile {

  private  static   Configuration  data ;
 static  void read(  InputStream inputStream) throws Exception {  
  //System.setProperty("config.file.path", "/Users/nuwanwalisundara/personal/config.yaml");
     
       
      
 Yaml yaml = new Yaml(new Constructor(Configuration.class));
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

 public static String mapToOOdoTask(String desplayName,String strOrg, String jiraKey){
AtomicReference<String> task = new AtomicReference<String>(strOrg);
if(data.getClockwork().getDailyStandupJira()!=null&&
data.getClockwork().getDailyStandupJira().getOodoTask() !=null && jiraKey.trim().equalsIgnoreCase( data.getClockwork().getDailyStandupJira().getKey().trim())){
  task.set( data.getClockwork().getDailyStandupJira().getOodoTask());
}else{
   data.getUsers().stream()
                  .filter(a->desplayName.trim().equalsIgnoreCase(a.getName()))
                  .findFirst()
               
                  .ifPresent(a->{ 
                                  if( a.getTasks().stream()
                                             .noneMatch(strOrg.trim()::equalsIgnoreCase)){
                                             task.set(a.getDefaultTask().trim()); 
                                             }});
                                            }
    return task.get().trim();
}
}
