package responseHandler;

import java.io.Serializable;

public class Success extends Response {
    Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Success(Object data) {
        this.data = data;
    }

    public Success() {
    }
}
