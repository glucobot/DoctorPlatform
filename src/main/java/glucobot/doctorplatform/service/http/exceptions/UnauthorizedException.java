package glucobot.doctorplatform.service.http.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
