package iot;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import iot.requests.Event;
import iot.requests.Request;
import iot.requests.UpdateBlackList;
import iot.requests.UpdateWhiteList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SimpleShield {
    static ArrayList<Event> profileList = new ArrayList<>();
    static LinkedHashMap<String, String> protectedDeviceList = new LinkedHashMap<>();
    static LinkedHashMap<String, String> hackedDeviceList = new LinkedHashMap<>();

    public static String getRequestAction(Request request) {
        String defaultBehaviour = getDefaultBehaviour(request.model_name);

        if (defaultBehaviour.equals("allow")) {
            if (getBlackList(request.model_name).contains(request.url)) {
                return "block";
            } else {
                return "allow";
            }
        } else if (defaultBehaviour.equals("block")) {
            if (getWhiteList(request.model_name).contains(request.url)) {
                return "allow";
            }
            if (!getWhiteList(request.model_name).contains(request.url)) {
                return "quarantine";
            }
        } else {
        }
        return "none";
    }

    public static List<String> getWhiteList(String modelName) {
        for (Event e : profileList) {
            if (e.model_name.equals(modelName)) {
                return e.whitelist;
            }
        }
        return null;
    }

    public static List<String> getBlackList(String modelName) {
        for (Event e : profileList) {
            if (e.model_name.equals(modelName)) {
                return e.blacklist;
            }
        }
        return null;
    }

    public static String getDefaultBehaviour(String modelName) {
        for (Event e : profileList) {
            if (e.model_name.equals(modelName)) {
                return e.default_policy;
            }
        }
        return "none";
    }

    public static Event lineToEvent(String line) {
        try {
            Gson g = new Gson();
            Event event = g.fromJson(line, Event.class);
            return event;
        } catch (JsonSyntaxException exception) {
            System.out.println("Bad request!");
        }
        return null;
    }

    public static UpdateWhiteList lineToWhiteListEvent(String line) {
        try {
            Gson g = new Gson();
            UpdateWhiteList event = g.fromJson(line, UpdateWhiteList.class);
            return event;
        } catch (JsonSyntaxException exception) {
            System.out.println("Bad request!");
        }
        return null;
    }

    public static UpdateBlackList lineToBlackListEvent(String line) {
        try {
            Gson g = new Gson();
            UpdateBlackList event = g.fromJson(line, UpdateBlackList.class);
            return event;
        } catch (JsonSyntaxException exception) {
            System.out.println("Bad request!");
        }
        return null;
    }

    public static Request lineToRequest(String line) {
        try {
            Gson g = new Gson();
            Request event = g.fromJson(line, Request.class);
            return event;
        } catch (JsonSyntaxException exception) {
            System.out.println("Bad request!");
        }
        return null;
    }

    public static void updateWhiteList(UpdateWhiteList req) {
        for (Event e : profileList) {
            if (e.model_name.equals(req.model_name)) {
                e.whitelist = req.whitelist;
            }
        }
    }

    public static void updateBlackList(UpdateBlackList req) {
        for (Event e : profileList) {
            if (e.model_name.equals(req.model_name)) {
                e.blacklist = req.blacklist;
            }
        }
    }

    public static void addDeviceToList(LinkedHashMap<String, String> list, String deviceId, String modelName) {
        if(list.containsKey(deviceId)) {
            return;
        } else {
            list.put(deviceId, modelName);
        }
    }

    public static void shield() {
        try {
            List<String> allLines = Files.readAllLines(Paths.get("C:\\Users\\Aistis\\Desktop\\input.json"));
            for (String line : allLines) {
                if (line.contains("\"type\": \"profile_")) {
                    if (line.contains("\"type\": \"profile_create")) {
                        Event creationEvent = lineToEvent(line);
                        if (creationEvent != null) {
                            profileList.add(creationEvent);
                            System.out.println(creationEvent.model_name + " profile created");
                        }
                    } else if (line.contains("\"type\": \"profile_update")) {
                        if (line.contains("whitelist")) {
                            UpdateWhiteList updateEvent = lineToWhiteListEvent(line);
                            if (updateEvent != null) {
                                updateWhiteList(updateEvent);
                                System.out.println(updateEvent.model_name + " whitelist updated");
                            }
                        } else if (line.contains("blacklist")) {
                            UpdateBlackList updateEvent = lineToBlackListEvent(line);
                            if (updateEvent != null) {
                                updateBlackList(updateEvent);
                                System.out.println(updateEvent.model_name + " blacklist updated");
                            }
                        }
                    }
                } else if (line.contains("\"type\": \"request")) {
                    Request request = lineToRequest(line);
                    if (request != null) {
                        String action = getRequestAction(request);
                        if (action.equals("none")) {
                            System.out.println("{\"request_id\": " + request.request_id + " \"action\": \"block\"} " + request.device_id + ":deviceId, " + request.model_name + ":deviceName has no profile!");
                        } else if (action.equals("allow")) {
                            System.out.println("{\"request_id\": " + request.request_id + " \"action\": \"allow\"}");
                            addDeviceToList(protectedDeviceList, request.device_id, request.model_name);
                        } else if (action.equals("block")) {
                            System.out.println("{\"request_id\": " + request.request_id + " \"action\": \"block\"}");
                            addDeviceToList(protectedDeviceList, request.device_id, request.model_name);
                        } else if (action.equals("quarantine")) {
                            System.out.println("{\"device_id\": " + request.device_id + " \"action\": \"quarantine\"}");
                            addDeviceToList(hackedDeviceList, request.device_id, request.model_name);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Protected devices : " + protectedDeviceList);
        System.out.println("Hacked devices : " + hackedDeviceList);
    }

    public static void main(String[] args) {
        shield();
    }
}