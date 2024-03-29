package enigmapancaxrzco.hackutd.bachome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ResultActivity extends AppCompatActivity {

    private TextView resultBox;
    private TextView caseBox;
    private Button multiPurposeButton;
    private boolean isOverLimit;
    private TextView phone;
    private Uri uri;
    private final String urlToCall = "https://api.twilio.com/2010-04-01/Accounts/AC2d5168c70cee421759a0f48328fb4dd5/Messages.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uri = (Uri) getIntent().getParcelableExtra("voucherURI");
        setContentView(R.layout.activity_result);
        resultBox = findViewById(R.id.result_text);
        caseBox = findViewById(R.id.result_text_filler2);
        multiPurposeButton = findViewById(R.id.result_button);
        phone = findViewById(R.id.phone_box);
        float bac = getIntent().getFloatExtra("result",1);
        if (bac >= -.01) {
            setResultBox(bac+"");
            caseBox.setText("You are too drunk to drive.  Click below to get a free Lyft ride sent to you!");
            isOverLimit = true;
        } else {
            //User is not over the legal limit
            caseBox.setText("You should not rely on this as an indication of whether you should drive.  Click below to continue");
            isOverLimit = false;
        }
    }
    public void setResultBox(String s) {
        resultBox.setText(s);
    }

    public void setButtonText(String s) {
        multiPurposeButton.setText(s);
    }

    public void onButtonPress(View v) {
        if (isOverLimit) {
            String voucher;
            try {
                InputStream is = getContentResolver().openInputStream(uri);
                Log.d("InputStream is ", is.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)));
                Log.d("BufferedReader is ", br.toString());
                StringBuilder fileContentSB = new StringBuilder();
                String line = br.readLine();
                Log.d("line is",line);
                while (line != null) {
                    fileContentSB.append(line);
                    line = br.readLine();
                }
                Log.d("fileContentSB is ",fileContentSB.toString());
                String[] voucherAr = fileContentSB.toString().split(",");
                Log.d("voucherAr[0] is ",voucherAr[0]);
                StringBuilder newContents = new StringBuilder();
                voucher = voucherAr[0];
                for (int i = 1; i < voucherAr.length; i++) {
                    newContents.append(voucherAr[i] + ",");
                }
                OutputStream os = getContentResolver().openOutputStream(uri);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                bw.write(newContents.toString());
                String phoneNumber = phone.getText().toString();
                for (int i = 0; i < phoneNumber.length(); i++) {
                    if (!Character.isDigit(phoneNumber.charAt(i))) {
                        setResultBox("That phone number is invalid.  Enter 10 digits only!");
                        return;
                    }
                }
                br.close();
                bw.close();
                is.close();
                os.close();
                //Our phone number is validated at this point and we can use it to craft the API call
                CallAPI caller = new CallAPI();
                String data = "To=+1" + phoneNumber + "&From=+14109883764&Body=Here%20is%20your%20free%20Lyft%20ride!%20%20Thank%20you%20for%20driving%20safely:%20" + voucher;
                caller.execute(urlToCall, data);
            } catch (IOException e) {
                resultBox.setText(e.getMessage());
            }
        }
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
