/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import database.DatabaseHelper;

/**
 *
 * @author et4m1r
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        if (args.length < 3) {
            System.err.println("Usage: java -jar AIS-R-Enhanced.jar <database_url> <username> <password>");
            System.exit(1);
        }

        String databaseUrl = args[0];
        String username = args[1];
        String password = args[2];

        DatabaseHelper databaseHelper = new DatabaseHelper(databaseUrl, username, password);

        App.main(args);

    }
}
