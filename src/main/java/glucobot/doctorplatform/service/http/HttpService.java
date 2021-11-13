package glucobot.doctorplatform.service.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class HttpService {

    private final URI baseURI;
    private final HttpAuthorizationProvider authorizationProvider;

    public HttpService(String baseURI, HttpAuthorizationProvider authorizationProvider) {
        this.baseURI = URI.create(baseURI);
        this.authorizationProvider = authorizationProvider;
    }

    public <T> T get(String action, Class<T> TClass) throws IOException {
        URL url = baseURI.resolve("/" + action).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Add parameters

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", authorizationProvider.getHeader());

        HttpResponse response = getFullResponse(connection);

        //T result = new ObjectMapper().readValue(response.getBody(), TClass);
        return null;

    }

    private HttpResponse getFullResponse(HttpURLConnection connection) throws IOException {
        HttpResponse response = new HttpResponse();

        response.setStatusCode(connection.getResponseCode());
        response.setResponseMessage(connection.getResponseMessage());

        StringBuilder headersStringBuilder = new StringBuilder();

        // read headers
        connection.getHeaderFields().entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .forEach(entry -> {
                    headersStringBuilder.append(entry.getKey()).append(": ");
                    List<String> headerValues = entry.getValue();
                    Iterator<String> it = headerValues.iterator();
                    if (it.hasNext()) {
                        headersStringBuilder.append(it.next());
                        while (it.hasNext()) {
                            headersStringBuilder.append(", ").append(it.next());
                        }
                    }
                    headersStringBuilder.append("\n");
                });

        response.setHeaders(headersStringBuilder.toString());


        // read response content
        StringBuilder bodyStringBuilder = new StringBuilder();

        Reader streamReader;

        if (response.getStatusCode() > 299) {
            streamReader = new InputStreamReader(connection.getErrorStream());
        } else {
            streamReader = new InputStreamReader(connection.getInputStream());
        }

        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            bodyStringBuilder.append(inputLine);
        }
        in.close();
        connection.disconnect();

        response.setBody(bodyStringBuilder.toString());

        return response;
    }
}
