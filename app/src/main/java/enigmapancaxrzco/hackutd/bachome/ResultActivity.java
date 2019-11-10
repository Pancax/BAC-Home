package enigmapancaxrzco.hackutd.bachome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView resultBox;
    private Button multiPurposeButton;
    private boolean isOverLimit;
    private TextView phone;
    private final String urlToCall = "https://api.twilio.com/2010-04-01/Accounts/AC2d5168c70cee421759a0f48328fb4dd5/Messages.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultBox = findViewById(R.id.result_text);
        multiPurposeButton = findViewById(R.id.result_button);
        phone = findViewById(R.id.phone_box);
        float bac =  0;//TODO: GET THIS FROM SAVEDINSTANCESTATE
        StringBuilder sb = new StringBuilder();
        sb.append("Your BAC is " + bac);
        if (bac >= .08) {
            sb.append(" and you are too drunk to drive.  Click below to get a free Uber ride sent to you!");
            setResultBox(sb.toString());
            isOverLimit = true;
        } else {
            //User is not over the legal limit
            sb.append(".  You should not rely on this as an indication of whether you should drive.  Click below to continue");
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
            String phoneNumber = phone.getText().toString();
            for (int i = 0; i < phoneNumber.length(); i++) {
                if (!Character.isDigit(phoneNumber.charAt(i))) {
                    setResultBox("That phone number is invalid.  Enter 10 digits only!");
                    return;
                }
            }
            //Our phone number is validated at this point and we can use it to craft the API call
            CallAPI caller = new CallAPI();
            String data = "To=" + phoneNumber + "&From=+14109883764&Body=Here is your free Uber voucher!  Thank you for driving safely: " + "INSERTVOUCHERHERE";
            caller.execute(urlToCall, data);
        } else {
            //TODO: RETURN TO MAINACTIVITY
        }
    }
}
