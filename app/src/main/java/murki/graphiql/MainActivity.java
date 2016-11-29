package murki.graphiql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Callback {

    private final HttpService httpService = new HttpService();

    private EditText graphQLUriText;
    private EditText authTokenText;
    private EditText queryText;
    private EditText resultText;
    private Button queryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphQLUriText = (EditText) findViewById(R.id.graphQLUriText);
        authTokenText = (EditText) findViewById(R.id.authTokenText);
        queryText = (EditText) findViewById(R.id.queryText);
        resultText = (EditText) findViewById(R.id.resultText);
        queryButton = (Button) findViewById(R.id.queryButton);
    }

    public void queryButtonClick(View view) {
        queryButton.setEnabled(false);
        String token = null;
        if (!TextUtils.isEmpty(authTokenText.getText().toString())) {
            token = authTokenText.getText().toString();
        }
        String graphQLQuery = "{ \"query\": \"" + queryText.getText().toString() + "\" }";
        httpService.post(graphQLUriText.getText().toString(), token, graphQLQuery, this);
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultText.setText("IOException: " + e.toString());
                queryButton.setEnabled(true);
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!response.isSuccessful()) {
                        resultText.setText("HttpError: " + response.code() + " - " + response.message());
                    } else {
                        resultText.setText(response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    resultText.setText("IOException: " + e.toString());
                } finally {
                    queryButton.setEnabled(true);
                }
            }
        });
    }
}
