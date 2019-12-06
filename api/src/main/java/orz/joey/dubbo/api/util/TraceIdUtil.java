package orz.joey.dubbo.api.util;

import java.util.UUID;

public class TraceIdUtil {
    private static ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

    public static void setTraceId(String value){
        TRACE_ID.set(value);
    }

    public static String getTraceId() {
        return TRACE_ID.get();
    }

    public static void removeTraceId() {
        TRACE_ID.remove();
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
