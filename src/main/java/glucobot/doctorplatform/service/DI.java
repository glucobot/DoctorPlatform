package glucobot.doctorplatform.service;

import java.util.HashMap;
import java.util.Map;

public class DI {

    private static final Map<Class, Object> registeredServices = new HashMap<>();

    public static <T> void register(Class<T> clazz, T implementation) {
        if (registeredServices.containsKey(clazz)) {
            registeredServices.replace(clazz, implementation);
        } else {
            registeredServices.put(clazz, implementation);
        }
    }

    public static <T> void unregister(Class<T> clazz) {
        registeredServices.remove(clazz);
    }

    public static <T> T get(Class<T> clazz) {
        return (T) registeredServices.get(clazz);
    }

}
