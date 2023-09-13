package com.nuwanw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;


import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.nuwanw.model.Configuration;



public class SnakeYml {
   // private  static   Map<String, Object> data ;
   public static void main( String[] args ){

     
    
            // Yaml yaml = new Yaml(new Constructor(Configuration.class));
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        InputStream inputStream = SnakeYml.class
                                        .getClassLoader()
                                        .getResourceAsStream("config.yaml");
       Configuration config= yaml.loadAs(inputStream,Configuration.class);

        System.out.println(config);
    }
}
