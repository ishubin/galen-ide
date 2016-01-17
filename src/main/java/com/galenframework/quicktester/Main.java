package com.galenframework.quicktester;

import static spark.Spark.*;


public class Main {
    public static void main(String[] args) {
        staticFileLocation("/public");
        new TesterController();
    }
}
