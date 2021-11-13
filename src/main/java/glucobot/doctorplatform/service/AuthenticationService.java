package glucobot.doctorplatform.service;

import glucobot.doctorplatform.model.Category;
import glucobot.doctorplatform.model.User;
import glucobot.doctorplatform.model.UserWithCategory;
import glucobot.doctorplatform.service.http.BasicAuthentication;
import glucobot.doctorplatform.service.http.HttpAuthorizationProvider;
import glucobot.doctorplatform.service.http.HttpService;
import glucobot.doctorplatform.service.http.exceptions.UnauthorizedException;

import java.io.IOException;

public class AuthenticationService {

    private final HttpService httpService;

    public AuthenticationService() {
        httpService = DI.get(HttpService.class);
    }

    public void logout() {
        DI.unregister(HttpAuthorizationProvider.class);
        DI.unregister(User.class);
    }

    public AuthenticationResult login(String username, String password) {
        DI.register(HttpAuthorizationProvider.class, new BasicAuthentication(username, password));

        try {
            UserWithCategory user = httpService.post("login", UserWithCategory.class);

            if (user.getCategory().getId() != Category.DOCTOR) {
                return failLogin(AuthenticationResult.WrongCredentials);
            }

            return successLogin(user);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return failLogin(AuthenticationResult.Fail);
        } catch (UnauthorizedException e) {
            return failLogin(AuthenticationResult.WrongCredentials);
        }
    }

    private AuthenticationResult failLogin(AuthenticationResult result) {
        DI.unregister(HttpAuthorizationProvider.class);
        return result;
    }

    private AuthenticationResult successLogin(UserWithCategory user) {
        DI.register(User.class, user);
        return AuthenticationResult.Success;
    }

    public enum AuthenticationResult {
        Success,
        Fail,
        WrongCredentials
    }

}
