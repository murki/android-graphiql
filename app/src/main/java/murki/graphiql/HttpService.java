package murki.graphiql;

import android.support.annotation.Nullable;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpService {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;

    public HttpService() {
        client = new OkHttpClient();
    }

    public void post(String graphQLUri, @Nullable String authToken, String jsonBody, Callback responseCallback){
        RequestBody body = RequestBody.create(JSON, jsonBody);
        Request.Builder requestBuilder = new Request.Builder()
                .url(graphQLUri)
                .post(body);

        if (authToken != null) {
            requestBuilder.header("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(responseCallback);
    }
}
