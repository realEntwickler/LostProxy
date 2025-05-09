/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:42:02
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * Property.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.utils;

import java.io.*;
import java.util.Properties;

public class Property {

    /**
     * @param file name of the file
     * @param key  the key to look for in the config
     * @return value of the given key
     */
    public String get(String file, String key) {
        try (InputStream input = new FileInputStream("LostProxy/" + file + ".properties")) {

            Properties prop = new Properties();

            // load a properties file from InputStream
            prop.load(input);

            return prop.getProperty(key);

            // Java 8 , print key and values
//            prop.forEach((key, value) -> System.out.println("Key : " + key + ", Value : " + value));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * create the default properties-file
     */
    public void setDefaultProps() {
        //create the file if not exists
        File dir = new File("LostProxy");
        if (!dir.exists()) {
            dir.mkdirs();
            try (OutputStream output = new FileOutputStream("LostProxy/cfg.properties")) {

                Properties prop = new Properties();

                // set the properties value
                prop.setProperty("db.username", "lostproxy");
                prop.setProperty("db.password", "lostproxy123");
                prop.setProperty("db.database", "lostproxy");

                prop.setProperty("ts.username", "serveradmin");
                prop.setProperty("ts.password", "password");
                prop.setProperty("ts.hostname", "localhost");
                prop.setProperty("ts.queryPort", "10011");
                prop.setProperty("ts.virtualServerPort", "9987");
                prop.setProperty("ts.nickname", "LostProxy - TeamSpeakManager");

                // save properties to project folder
                prop.store(output, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}