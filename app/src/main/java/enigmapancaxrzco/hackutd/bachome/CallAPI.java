package enigmapancaxrzco.hackutd.bachome;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallAPI extends AsyncTask<String, Void, Void> {
    public CallAPI() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        String urlString = params[0];
        String data = params[1];
        OutputStream out = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String basicAuth = "Basic " + "QUMyZDUxNjhjNzBjZWU0MjE3NTlhMGY0ODMyOGZiNGRkNTpiOTg5MGU0ZjI3MDJiODMxNmQ5MGZjOGYzNjdlNWY5NA==";
            urlConnection.setRequestProperty ("Authorization", basicAuth);
            out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();
            urlConnection.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
