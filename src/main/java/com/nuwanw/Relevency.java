package com.nuwanw;

import java.util.regex.Pattern;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Relevency {
     public static void main(String[] args) {
    String sentence = "This is a test sentence.";
    String[] words = sentence.split(" ");

    List<Pattern> patterns = new ArrayList<>();
    patterns.add(Pattern.compile("test"));
    patterns.add(Pattern.compile("sentence"));

    double maxRelevancy = 0.0;
    double totalPaterns=2;
    Pattern maxPattern = null;
        double machCount=00;
    for (Pattern pattern : patterns) {
      Matcher matcher = pattern.matcher(sentence);
      if (matcher.find()) {
       machCount++;
      }
    }
System.out.println((machCount/totalPaterns)*100);
    if (maxPattern != null) {
      System.out.println("The most relevant pattern is: " + (machCount/totalPaterns)*100);
    } else {
      System.out.println("No pattern was found");
    }
  }
}
