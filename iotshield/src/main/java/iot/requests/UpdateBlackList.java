package iot.requests;

import java.util.ArrayList;

public class UpdateBlackList {
    public String type;
    public String model_name;
    public ArrayList<String> blacklist;
    public Long timestamp;

    public UpdateBlackList(String type, String model_name, ArrayList<String> blacklist, Long timestamp) {
        this.type = type;
        this.model_name = model_name;
        this.blacklist = blacklist;
        this.timestamp = timestamp;
    }
}
