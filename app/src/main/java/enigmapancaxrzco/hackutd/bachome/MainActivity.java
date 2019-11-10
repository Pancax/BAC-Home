package enigmapancaxrzco.hackutd.bachome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import BACtrackAPI.API.BACtrackAPI;
import BACtrackAPI.API.BACtrackAPICallbacks;
import BACtrackAPI.Constants.BACTrackDeviceType;
import BACtrackAPI.Constants.BACtrackUnit;
import BACtrackAPI.Exceptions.BluetoothLENotSupportedException;
import BACtrackAPI.Exceptions.BluetoothNotEnabledException;
import BACtrackAPI.Exceptions.LocationServicesNotEnabledException;

public class MainActivity extends AppCompatActivity {
    private static final byte PERMISSIONS_FOR_SCAN = 100;
    Context mContext;
    BACtrackAPI APIObj;
    ArrayList<String> vouchers;
    TextView tv;
    TextView blowField;
    TextView startingInField;
    TextView secondsLeftPrompt;
    TextView getReadyField;
    ConstraintLayout countdownLayout;
    private final String APIKEY = "cd2eda75f4dd42948f621ce1d02e3c";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext= this;

    }
    public void startButtonClicked(View v){
        if(!(APIObj==null)&&!(vouchers==null)){
            //start off the actual process test activity stuff
            startTestActivity();
        }

    }
    public void SettingsButtonClicked(View v){
        //Handle settings shit
        setContentView(R.layout.activity_settings);
    }
    BACtrackAPICallbacks BACCallbacks = new BACtrackAPICallbacks() {
        @Override
        public void BACtrackAPIKeyDeclined(String s) {

        }

        @Override
        public void BACtrackAPIKeyAuthorized() {

        }

        @Override
        public void BACtrackConnected(BACTrackDeviceType bacTrackDeviceType) {

        }

        @Override
        public void BACtrackDidConnect(String s) {

        }

        @Override
        public void BACtrackDisconnected() {

        }

        @Override
        public void BACtrackConnectionTimeout() {

        }

        @Override
        public void BACtrackFoundBreathalyzer(BACtrackAPI.BACtrackDevice baCtrackDevice) {

        }

        @Override
        public void BACtrackCountdown(int i) {

        }

        @Override
        public void BACtrackStart() {

        }

        @Override
        public void BACtrackBlow() {

        }

        @Override
        public void BACtrackAnalyzing() {

        }

        @Override
        public void BACtrackResults(float v) {

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
        tv = findViewById(R.id.debug_box);
        try {
            APIObj = new BACtrackAPI(this, BACCallbacks, APIKEY);
            mContext = this;
        }catch(Exception e){
            tv.setText(e.getMessage());
        }
        if(!APIObj.isConnected()){
            //update text
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_FOR_SCAN);
            }
            APIObj.connectToNearestBreathalyzer();
        } else{
            //it is already connected
        }
    }
    public void disconnectButtonClicked(View v){
        if(APIObj ==null){
            //it was not connected
            return;
        }
        APIObj.disconnect();
        //update the text
    }
    public void finishButtonClicked(View v){
        setContentView(R.layout.activity_main);
    }
    //implement the document stuff

    //Fill out the Test Activity Stuff
    public void startTestActivity(){
        setContentView(R.layout.activity_test);
         blowField = findViewById(R.id.blow_text);
         startingInField = findViewById(R.id.countdown_text_starting);
         secondsLeftPrompt = findViewById(R.id.countdown_text_timer);
         getReadyField = findViewById(R.id.countdown_text_getready);
         countdownLayout = findViewById(R.id.countdown_container);
         blowField.setVisibility(View.INVISIBLE);

    }
    public void updateCountDown(int secondsLeft){
        //update text stuff
    }
    public void sayBlowNow(){
        countdownLayout.setVisibility(View.INVISIBLE);
    }
    public void sayKeepBlowing(){
        //say KeepBlowing
    }
    public void sayStopBlowing(){
        //say to stop blowing
    }
    public void goToResultActivity(float result){
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("result",result);

    }
    //Call result activity

    //implement file Selector
}
