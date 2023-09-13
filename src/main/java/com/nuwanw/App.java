package com.nuwanw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
/**
 * Hello world!
 *
 */
public class App 
{
    //public static String jsonKey_emplye
private static String CSV_HEADER_DATE="Date";
private static String CSV_HEADER_EMPLOYEE="Employee";
private static String CSV_HEADER_DESCRIPTION="Description";
private static String CSV_HEADER_PROJECT="Project";
private static String CSV_HEADER_TASK="Task";
private static String CSV_HEADER_QUENTITY="Quantity";
    public static void main( String[] args )
    {

        
       // Create a URL object for the REST API endpoint.
        URL url;
        try {

             



           // System.out.println(App.class.getResourceAsStream());
            url = new URL("https://api.clockwork.report/v1/worklogs?expand=issues,authors,worklogs&starting_at=2023-09-13&ending_at=2023-09-13");
         // Create an HTTP connection to the REST API endpoint.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Authorization",
    "Token wyWc+uUWY1WEYvR1kB4euMFhgj3Wx/B2Z+EbmBRssUUZAA16ilRHFPmkGqmuPSO7EpL2vgMpGn+ZwoygESa0N+pDVYqXM0R81KZwnA==--ou859rfEsaL4IFzD--6lCnR+zBJLhdk7Ys+n4Z+g==");
        // Get the response from the REST API.
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("HTTP response code: " + responseCode);
        }

        // Read the response body into a string.
        String responseBody = new Scanner(connection.getInputStream()).useDelimiter("\\A").next();

         ObjectMapper objectMapper = new ObjectMapper();
         JsonNode rootNode= objectMapper.readTree(responseBody);

         
  
        File obj = new File("myfile.csv");
        CSVWriter writer = new CSVWriter(new FileWriter(obj.getPath()));
        writer.writeNext( new String[]{CSV_HEADER_DATE,CSV_HEADER_EMPLOYEE,
                                        CSV_HEADER_DESCRIPTION,
                                        CSV_HEADER_PROJECT,CSV_HEADER_TASK,
                                        CSV_HEADER_QUENTITY});
      


    StreamSupport.stream(rootNode.spliterator(), false)
                                                .filter(node -> node.path("author").path("displayName").asText().equalsIgnoreCase( "Nuwan Walisundara"))
                                                .map(e-> new String[]{
                                                   toOOdoFormat( e.get("created").asText()),
                                                    e.path("author").path("displayName").asText(),
                                                    "["+e.path("issue").path("key").asText()+"]"+e.findValue("summary") .asText(),
                                                     "SO/202207/422-CRDB API ESB", 
                                                    mapToOOdoTask(e.get("comment").asText()),
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

}
