package com.nuwanw;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeConvert { 
    public static void main( String[] args )
    {
       /*  String inputTime ="2011-12-03T10:15:30+01:00";// "2023-09-08T08:53:24.132+03:00";
        ZoneId sourceTimeZone = ZoneId.of("Africa/Dar_es_Salaam");
        ZoneId targetTimeZone = ZoneId.of("Asia/Colombo");

        Instant instant = Instant.parse(inputTime);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, sourceTimeZone);

        ZonedDateTime convertedDateTime = zonedDateTime.withZoneSameInstant(targetTimeZone);

        System.out.println(convertedDateTime); 

        ZonedDateTime  date = ZonedDateTime .now();
  DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
  String text = date.format(formatter);
  LocalDate parsedDate = LocalDate.parse(text, formatter);
System.out.println(parsedDate);*/


        Pattern pattern = Pattern.compile("\\+0300");
        Matcher matcher = pattern.matcher("2023-09-08T08:53:24.132+0300");

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "+03:00");
        }
        matcher.appendTail(sb);

//System.out.println(matcher.toString());

ZonedDateTime  dateDarsalam = ZonedDateTime.parse(sb.toString());

 ZoneId sourceTimeZone = ZoneId.of("Africa/Dar_es_Salaam");
        ZoneId targetTimeZone = ZoneId.of("Asia/Colombo");
         ZonedDateTime convertedDateTime = dateDarsalam.withZoneSameInstant(targetTimeZone);

        System.out.println(convertedDateTime); 


        
System.out.println(dateDarsalam);

DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YYYY");
convertedDateTime.format(formatter);

        
System.out.println(convertedDateTime.format(formatter));



    }
}
