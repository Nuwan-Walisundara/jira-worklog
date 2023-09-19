package com.nuwanw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import static com.nuwanw.ReadYamlFile.*;

public class WorkLogManager {
     //public static String jsonKey_emplye
private static String CSV_HEADER_DATE="Date";
private static String CSV_HEADER_EMPLOYEE="Employee";
private static String CSV_HEADER_DESCRIPTION="Description";
private static String CSV_HEADER_PROJECT="Project";
private static String CSV_HEADER_TASK="Task";
private static String CSV_HEADER_QUENTITY="Quantity";
private static StringBuilder urlstr =  new StringBuilder();
private static String CLOCKY_APPENDER = "/v1/worklogs?expand=issues,authors,worklogs&";
    public static void main( String[] args ) throws Exception
    {
        InputStream inputStream =null;
        try {
          inputStream=   new FileInputStream(
                (System.getProperty("user.home") + File.separator + ".jira-work" + File.separator + "config.yaml"));

        } catch (FileNotFoundException e) {
            
           System.out.println();
        }
        
        read(inputStream);
        urlstr.append(clockyUrl()).append(CLOCKY_APPENDER );
        
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        String starting_at, ending_at;
         starting_at= ending_at = formatter.format(  LocalDate.now());
         
         urlstr.append("starting_at").append("=").append( starting_at);
         urlstr.append("&" ).append("ending_at").append("=").append(ending_at);
         
       // Create a URL object for the REST API endpoint.
        URL url;
        try {


           // System.out.println(App.class.getResourceAsStream());
            url = new URL(urlstr.toString());
         // Create an HTTP connection to the REST API endpoint.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Authorization",  "Token " + token());
        // Get the response from the REST API.
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("HTTP response code: " + responseCode);
        }

        // Read the response body into a string.
        String responseBody = new Scanner(connection.getInputStream()).useDelimiter("\\A").next();

         ObjectMapper objectMapper = new ObjectMapper();
         JsonNode rootNode= objectMapper.readTree(responseBody);

         
  
        File obj = new File("myfile-"+starting_at+".csv");
        CSVWriter writer = new CSVWriter(new FileWriter(obj.getPath()));
        writer.writeNext( new String[]{CSV_HEADER_DATE,CSV_HEADER_EMPLOYEE,
                                        CSV_HEADER_DESCRIPTION,
                                        CSV_HEADER_PROJECT,CSV_HEADER_TASK,
                                        CSV_HEADER_QUENTITY});
      //https://www.chakray.com/how-to-write-a-synapse-handler-wso2-integrator/


    StreamSupport.stream(rootNode.spliterator(), false)
                                                .filter(node -> isValidUser(node.path("author").path("displayName").asText() ))
                                                .map(e-> new String[]{
                                                   toOOdoFormat( e.get("created").asText()),
                                                    e.path("author").path("displayName").asText(),
                                                    "["+e.path("issue").path("key").asText()+"] - "+e.findValue("summary") .asText(),
                                                    project(), 
                                                    mapToOOdoTask( e.path("author").path("displayName").asText(),e.get("comment").asText(),
                                                    e.path("issue").path("key").asText()),
                                                    String.valueOf( e.path("timeSpentSeconds") .asDouble()/3600)}
                                                   ).forEach(a -> writer.writeNext(a));;


  
        // Close the CSV writer object.
        writer.close();
        
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       
    }

    public static String toOOdoFormat( String args ){
      


        Pattern pattern = Pattern.compile("\\+0300");
        Matcher matcher = pattern.matcher(args);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "+03:00");
        }
        matcher.appendTail(sb);

//System.out.println(matcher.toString());

ZonedDateTime  dateDarsalam = ZonedDateTime.parse(sb.toString());

 //ZoneId sourceTimeZone = ZoneId.of("Africa/Dar_es_Salaam");
        ZoneId targetTimeZone = ZoneId.of("Asia/Colombo");
         ZonedDateTime convertedDateTime = dateDarsalam.withZoneSameInstant(targetTimeZone);
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");

         return convertedDateTime.format(formatter);
    }
    /*
private static String mapToOOdoTask(String strOrg){

   String str=  strOrg.trim();
    if(!str.equalsIgnoreCase("Internal discussions") &&
       !str.equalsIgnoreCase("CRDB design review")&&
       !str.equalsIgnoreCase("WSO2 Design review")   ){
        return "Design";
       }else{
        return str;
       }
}
 */
}
