package enigmapancaxrzco.hackutd.bachome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import BACtrackAPI.API.BACtrackAPI;
import BACtrackAPI.API.BACtrackAPICallbacks;
import BACtrackAPI.Constants.BACTrackDeviceType;
import BACtrackAPI.Constants.BACtrackUnit;

public class MainActivity extends AppCompatActivity {
    private static final byte PERMISSIONS_FOR_SCAN = 100;
    Context mContext;
    BACtrackAPI APIObj;
    TextView tv;
    TextView blowField;
    TextView startingInField;
    TextView secondsLeftPrompt;
    TextView getReadyField;
    ConstraintLayout countdownLayout;
    private static final int READ_REQUEST_CODE = 42;
    private Uri voucherFileLocation;
    boolean setupCompleted = false;
    private final String APIKEY = "cd2eda75f4dd42948f621ce1d02e3c";
    boolean canClickButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext= this;
        canClickButtons=true;

    }
    public void startButtonClicked(View v){
        if(canClickButtons) {
            canClickButtons = false;
            if (APIObj != null && (voucherFileLocation != null)) {
                //start off the actual process test activity stuff
                startTestActivity();
            }
            canClickButtons =true;
        }
    }
    public void SettingsButtonClicked(View v) {
        if (canClickButtons) {
            canClickButtons = false;
            if (!setupCompleted) {
                setContentView(R.layout.activity_settings);
                canClickButtons=true;
            }
        }
    }
    BACtrackAPICallbacks BACCallbacks = new
            BACtrackAPICallbacks() {
        @Override
        public void BACtrackAPIKeyDeclined(String s) {

        }

        @Override
        public void BACtrackAPIKeyAuthorized() {

        }

        @Override
        public void BACtrackConnected(BACTrackDeviceType bacTrackDeviceType) {
            canClickButtons=true;
            setStatus("Successfully Connected");
        }

        @Override
        public void BACtrackDidConnect(String s) {
            setStatus("Successfully Found Device, Connecting.");
        }

        @Override
        public void BACtrackDisconnected() {
            setStatus("Successfully Disconnected");
            canClickButtons=true;
        }

        @Override
        public void BACtrackConnectionTimeout() {

        }

        @Override
        public void BACtrackFoundBreathalyzer(BACtrackAPI.BACtrackDevice baCtrackDevice) {

        }

        @Override
        public void BACtrackCountdown(int i) {
            updateCountDown(i);
        }

        @Override
        public void BACtrackStart() {
            sayBlowNow();
        }

        @Override
        public void BACtrackBlow() {
            sayKeepBlowing();
        }

        @Override
        public void BACtrackAnalyzing() {
            sayStopBlowing();
        }

        @Override
        public void BACtrackResults(float v) {
            goToResultActivity(v);
        }

        @Override
        public void BACtrackFirmwareVersion(String s) {

        }

        @Override
        public void BACtrackSerial(String s) {

        }

        @Override
        public void BACtrackUseCount(int i) {

        }

        @Override
        public void BACtrackBatteryVoltage(float v) {

        }

        @Override
        public void BACtrackBatteryLevel(int i) {

        }

        @Override
        public void BACtrackError(int i) {

        }

        @Override
        public void BACtrackUnits(BACtrackUnit baCtrackUnit) {

        }
    };

    //Fill out the settings Activity stuff
    public void connectButtonClicked(View v){
        if(canClickButtons) {
            canClickButtons = false;
            tv = findViewById(R.id.debug_box);
            tv.setText("Searching For Devices");
            try {
                APIObj = new BACtrackAPI(this, BACCallbacks, APIKEY);
                mContext = this;
            } catch (Exception e) {
                tv.setText(e.getMessage());
            }
            if (!APIObj.isConnected()) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_FOR_SCAN);
                }
                APIObj.connectToNearestBreathalyzer();
            } else {
                tv.setText("Already Connected");
            }
        }
    }
    public void disconnectButtonClicked(View v){
        if(canClickButtons) {
            canClickButtons=false;
            if (APIObj == null) {
                tv.setText("Not Connected");
                canClickButtons=true;
                return;
            }
            APIObj.disconnect();
        }
    }
    public void finishButtonClicked(View v){

        if(canClickButtons) {
            setContentView(R.layout.activity_main);
            canClickButtons = true;
            if (APIObj != null && voucherFileLocation != null) {
                setupCompleted = true;
                findViewById(R.id.setup_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.not_connected_info).setVisibility(View.INVISIBLE);
            }
        }
    }
    //implement the document stuff

    //Fill out the Test Activity Stuff
    public void startTestActivity(){
        canClickButtons=true;
        setContentView(R.layout.activity_test);
         blowField = findViewById(R.id.blow_text);
         startingInField = findViewById(R.id.countdown_text_starting);
         secondsLeftPrompt = findViewById(R.id.countdown_text_timer);
         getReadyField = findViewById(R.id.countdown_text_getready);
         countdownLayout = findViewById(R.id.countdown_container);
         blowField.setVisibility(View.INVISIBLE);
         APIObj.startCountdown();

    }
    public void updateCountDown(int secondsLeft){
        setUpdateCount(secondsLeft+"");
    }
    public void sayBlowNow(){
        countdownLayout.setVisibility(View.INVISIBLE);
        blowField.setVisibility(View.VISIBLE);
    }
    public void sayKeepBlowing(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                blowField.setText(String.format("%s", "Keep Blowing"));
            }
        });
    }
    public void sayStopBlowing(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                blowField.setText(String.format("%s", "Stop Blowing."));
            }
        });
    }
    public void goToResultActivity(float result){
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("result",result);
        i.putExtra("voucherURI", voucherFileLocation);
        startActivity(i);
    }

    //implement file Selector
    private void setStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(String.format("%s", message));
            }
        });
    }
    private void setUpdateCount(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                secondsLeftPrompt.setText(String.format("%s", message));
            }
        });
    }
    public void selectFileButtonClicked(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //Log.d("ResultData",resultData.getData().toString());
            voucherFileLocation = resultData.getData();
            //Log.d("ResultData2",voucherFileLocation.toString());
        }
    }
}
