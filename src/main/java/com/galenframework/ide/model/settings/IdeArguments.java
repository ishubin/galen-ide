/*******************************************************************************
* Copyright 2016 Ivan Shubin http://galenframework.com
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.ide.model.settings;


import org.apache.commons.cli.*;

public class IdeArguments {
    private String profile;
    private String fileStorage;
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
        options.addOption("s", "storage", true, "Path to static file storage that will be used for storing reports");

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
        ideArguments.setFileStorage(cmd.getOptionValue("s"));

        return ideArguments;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getFileStorage() {
        return fileStorage;
    }

    public void setFileStorage(String fileStorage) {
        this.fileStorage = fileStorage;
    }
}
