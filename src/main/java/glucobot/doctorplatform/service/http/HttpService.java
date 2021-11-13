package glucobot.doctorplatform.service.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import glucobot.doctorplatform.service.DI;
import glucobot.doctorplatform.service.http.exceptions.NotFoundException;
import glucobot.doctorplatform.service.http.exceptions.UnauthorizedException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class HttpService {

    private static final int NOT_FOUND = 404;
    private static final int UNAUTHORIZED = 401;

    private final URI baseURI;
    private final ObjectMapper objectMapper;

    public HttpService(String baseURI) {
        this.baseURI = URI.create(baseURI);
        objectMapper = new ObjectMapper();
    }

    public <T> T get(String action, Map<String, String> params, Class<T> TClass) throws IOException, InterruptedException {
        HttpClient client = createHttpClient();

        HttpRequest request = createRequestBuilder(action, params)
                .build();

        HttpResponse<String> response = sendRequest(client, request);

        return objectMapper.readValue(response.body(), TClass);
    }

    public <T> T get(String action, Class<T> TClass) throws IOException, InterruptedException {
        return this.get(action, new HashMap<>(), TClass);
    }

    public <T> T post(String action, Object data, Class<T> TClass) throws IOException, InterruptedException {
        HttpClient client = createHttpClient();

        HttpRequest request = createRequestBuilder(action)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(data)))
                .build();

        HttpResponse<String> response = sendRequest(client, request);

        return new ObjectMapper().readValue(response.body(), TClass);
    }

    public <T> T post(String action, Class<T> TClass) throws IOException, InterruptedException {
        return this.post(action, null, TClass);
    }

    private HttpResponse<String> sendRequest(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<java.lang.String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        switch (response.statusCode()) {
            case NOT_FOUND:
                throw new NotFoundException(response.body());
            case UNAUTHORIZED:
                throw new UnauthorizedException(response.body());

            default:
                return response;
        }
    }

    private HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    private HttpRequest.Builder createRequestBuilder(String action) {
        return createRequestBuilder(action, new HashMap<>());
    }

    private HttpRequest.Builder createRequestBuilder(String action, Map<String, String> params) {
        HttpAuthorizationProvider authorizationProvider = DI.get(HttpAuthorizationProvider.class);

        URI uri = baseURI.resolve("/" + action + "/");

        if (params.size() != 0) {
            String paramsQuery = evaluateQueryParams(params);
            uri = uri.resolve("?" + paramsQuery);
        }

        return HttpRequest.newBuilder()
                .uri(uri.normalize())
                .header("Content-Type", "application/json")
                .header("Authorization", authorizationProvider.getHeader());
    }

    private String evaluateQueryParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (var param : params.entrySet()) {
            sb.append(param.getKey()).append("=").append(param.getValue());
            if (i < params.size() - 1)
                sb.append("&");
            i++;
        }

        return sb.toString();
    }

}
