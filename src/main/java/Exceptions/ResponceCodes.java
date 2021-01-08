package Exceptions;

import java.util.HashMap;
import java.util.Map;

public class ResponceCodes {
    private Map<Integer, String> codes = new HashMap<>();


    public ResponceCodes() {
        codes.put(400, "Bad Request");
        codes.put(401, "Unauthorized - Key may be expired");
        codes.put(403, "Forbidden");
        codes.put(404, "Data not found");
        codes.put(405, "Method not allowed");
        codes.put(415, "Unsupported media type");
        codes.put(429, "Rate limit exceeded");
        codes.put(500, "Rate limit exceeded");
        codes.put(502, "Internal server error");
        codes.put(503, "Service unavailable");
        codes.put(504, "Gateway timeout");
    }

    public String getResponseReason(int code)
    {
        return codes.get(code);
    }
}
