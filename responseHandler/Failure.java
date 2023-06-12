package responseHandler;

public class Failure extends Response {
    Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Failure(Error error2) {
        this.error = error2;
    }

    public Failure() {
    }
    
}
