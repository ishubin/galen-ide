package com.galenframework.ide.devices.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.devices.commands.DeviceClearCookiesCommand;
import com.galenframework.ide.devices.commands.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DeviceTaskParser {

    public static DeviceTask parseTask(ObjectMapper mapper, JsonNode jsonNode) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        DeviceTask deviceTask = new DeviceTask();
        deviceTask.setName(jsonNode.get("name").asText());
        deviceTask.setCommands(parseCommands(mapper, jsonNode.get("commands")));
        return deviceTask;
    }

    private static List<DeviceCommand> parseCommands(ObjectMapper mapper, JsonNode commandsNode) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (commandsNode.isArray()) {
            Iterator<JsonNode> it = commandsNode.iterator();

            List<DeviceCommand> commands = new LinkedList<>();
            while (it.hasNext()) {
                commands.add(parseCommand(mapper, it.next()));
            }
            return commands;
        } else {
            throw new RuntimeException("\"commands\" should be an array");
        }
    }

    private static final Map<String, Class<? extends DeviceCommand>> commandClasses = new HashMap<String, Class<? extends DeviceCommand>>(){{
        put("openUrl", DeviceOpenUrlCommand.class);
        put("resize", DeviceResizeCommand.class);
        put("checkLayout", DeviceCheckLayoutCommand.class);
        put("inject", DeviceInjectCommand.class);
        put("runJs", DeviceRunJavaScriptCommand.class);
        put("restart", DeviceRestartCommand.class);
        put("clearCookies", DeviceClearCookiesCommand.class);
    }};

    private static DeviceCommand parseCommand(ObjectMapper mapper, JsonNode commandNode) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String commandName = commandNode.get("name").asText();
        if (commandClasses.containsKey(commandName)) {
            Class<? extends DeviceCommand> commandClass = commandClasses.get(commandName);
            if (commandNode.has("parameters")) {
                return mapper.convertValue(commandNode.get("parameters"), commandClasses.get(commandName));
            } else {
                return commandClass.getConstructor().newInstance();
            }

        } else {
            throw new RuntimeException("Unknown command type: " + commandName);
        }
    }
}
