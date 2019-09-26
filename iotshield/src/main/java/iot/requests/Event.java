package iot.requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event {
    public String type;
    public String model_name;
    @SerializedName("default")
    public String default_policy;
    public List<String> whitelist;
    public List<String> blacklist;
    public Long timestamp;

    public Event(String type, String model_name, String default_policy, List<String> whitelist, List<String> blacklist, Long timestamp) {
        this.type = type;
        this.model_name = model_name;
        this.default_policy = default_policy;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
        this.timestamp = timestamp;
    }
}