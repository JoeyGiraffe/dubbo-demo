package orz.joey.dubbo.api.dto;

import java.io.Serializable;

public class MessageDto implements Serializable {
    private static final long serialVersionUID = -2003845148389933914L;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
