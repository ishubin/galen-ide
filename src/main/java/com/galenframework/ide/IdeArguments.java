package com.galenframework.ide;


import org.apache.commons.cli.*;

public class IdeArguments {
    private String profile;
    private int port;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public static IdeArguments parse(String[] args) {
        Options options = new Options();
        options.addOption("p", "port", true, "Port for a server");
        options.addOption("P", "profile", true, "Profile that will be loaded on start");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (MissingArgumentException e) {
            throw new IllegalArgumentException("Missing value for " + e.getOption().getLongOpt(), e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        IdeArguments ideArguments = new IdeArguments();
        ideArguments.setProfile(cmd.getOptionValue("P"));
        ideArguments.setPort(Integer.parseInt(cmd.getOptionValue("p", "4567")));

        return ideArguments;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
