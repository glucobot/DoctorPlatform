package glucobot.doctorplatform.service.http;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthentication implements HttpAuthorizationProvider {

    private final String username;
    private final String password;

    public BasicAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getHeader() {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }
}
