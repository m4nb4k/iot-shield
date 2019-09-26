package iot.requests;

import java.util.ArrayList;

public class UpdateWhiteList {
    public String type;
    public String model_name;
    public ArrayList<String> whitelist;
    public Long timestamp;

    public UpdateWhiteList(String type, String model_name, ArrayList<String> whitelist, Long timestamp) {
        this.type = type;
        this.model_name = model_name;
        this.whitelist = whitelist;
        this.timestamp = timestamp;
    }
}
