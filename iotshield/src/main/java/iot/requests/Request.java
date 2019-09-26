package iot.requests;

public class Request {
    public String type;
    public String request_id;
    public String model_name;
    public String device_id;
    public String url;
    public Long timestamp;

    public Request(String type, String request_id, String model_name, String device_id, String url, Long timestamp) {
        this.type = type;
        this.request_id = request_id;
        this.model_name = model_name;
        this.device_id = device_id;
        this.url = url;
        this.timestamp = timestamp;
    }
}