package com.hawk.dilpreet.batteryoptimizer;

import android.app.ApplicationErrorReport;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.text.DecimalFormat;

public class BatteryStats  extends AppCompatActivity {

    AppCompatTextView statsLevel,statsTemp,statsHealth,statsVoltage,statsPlugged,statsChargingStatus,statsTechnology,statsCapacity,statsChargingSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_batterystats);
        Bundle valuesFromMainActivity=getIntent().getExtras();

        //Called the method we created below
        loadBatteryInfo();

        //Variable Initialization
        statsChargingSpeed=(AppCompatTextView)findViewById(R.id.current_value);
        statsLevel=(AppCompatTextView) findViewById(R.id.level_value);
        statsTemp=(AppCompatTextView)findViewById(R.id.temprature_value);
        statsHealth=(AppCompatTextView)findViewById(R.id.health_value);
        statsVoltage=(AppCompatTextView)findViewById(R.id.voltage_value);
        statsPlugged=(AppCompatTextView)findViewById(R.id.plugged_value);
        statsChargingStatus=(AppCompatTextView)findViewById(R.id.charging_status_value);
        statsTechnology=(AppCompatTextView)findViewById(R.id.technology_value);
        statsCapacity=(AppCompatTextView)findViewById(R.id.capacity_value);

        assert valuesFromMainActivity != null;
        statsTemp.setText(valuesFromMainActivity.getString("tempvalueforstats"));
        statsLevel.setText(valuesFromMainActivity.getString("levelvalueforstats"));
    }

    private void loadBatteryInfo(){
        IntentFilter intentFilterObject=new IntentFilter();
        intentFilterObject.addAction((Intent.ACTION_POWER_CONNECTED));
        intentFilterObject.addAction((Intent.ACTION_POWER_DISCONNECTED));
        intentFilterObject.addAction((Intent.ACTION_BATTERY_CHANGED));

        registerReceiver(batteryInfoReceiver, intentFilterObject);

    }

    private void updateBatteryData(Intent intent){
        boolean batteryPresent=intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT,true);
        if(batteryPresent){
            StringBuilder stringBuilderHealth=new StringBuilder();
            StringBuilder stringBuilderSpeed=new StringBuilder();
            StringBuilder stringBuilderVoltage=new StringBuilder();
            StringBuilder stringBuilderPlugged=new StringBuilder();
            StringBuilder stringBuilderChargingStatus=new StringBuilder();
            StringBuilder stringBuilderTechnology=new StringBuilder();
            StringBuilder stringBuilderCapacity=new StringBuilder();

            int chargeSpeed= (int) intent.getIntExtra(String.valueOf(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW),0);
            stringBuilderSpeed.append(chargeSpeed+" Ma");


            int health=intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            if(health==BatteryManager.BATTERY_HEALTH_COLD){
                stringBuilderHealth.append("Cold");
            }
            else if(health==BatteryManager.BATTERY_HEALTH_DEAD){
                stringBuilderHealth.append("Dead");
            }
            else if(health==BatteryManager.BATTERY_HEALTH_GOOD){
                stringBuilderHealth.append("Good");
            }
            else if(health==BatteryManager.BATTERY_HEALTH_OVERHEAT){
                stringBuilderHealth.append("Over Heat");
            }
            else if(health==BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
                stringBuilderHealth.append("Over Voltage");
            }
            else if(health==BatteryManager.BATTERY_HEALTH_UNKNOWN){
                stringBuilderHealth.append("Unknown");
            }
            else if(health==BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
                stringBuilderHealth.append("Unspecified Failure");
            }

            int plugged=intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            if(plugged==BatteryManager.BATTERY_PLUGGED_AC) {
                stringBuilderPlugged.append("AC");
            }
            else if(plugged==BatteryManager.BATTERY_PLUGGED_USB){
                stringBuilderPlugged.append("USB");
            }
            else if(plugged==BatteryManager.BATTERY_PLUGGED_WIRELESS){
                stringBuilderPlugged.append("Wireless");

            }
            else{
                stringBuilderPlugged.append("Null");
            }

            int status=intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
            if(status==BatteryManager.BATTERY_STATUS_CHARGING) {
                stringBuilderChargingStatus.append("Charging");
            }
            else if(status==BatteryManager.BATTERY_STATUS_DISCHARGING){
                stringBuilderChargingStatus.append("Discharging");
            }
            else if(status==BatteryManager.BATTERY_STATUS_FULL){
                stringBuilderChargingStatus.append("Full");
            }
            else if(status==BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                stringBuilderChargingStatus.append("Not Charging");
            }
            else if(status==BatteryManager.BATTERY_STATUS_UNKNOWN){
                stringBuilderChargingStatus.append("Unknown");
            }

            if(intent.getExtras()!=null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                if (!"".equals(technology)) {
                    stringBuilderTechnology.append(""+technology);
                }
                else{
                    stringBuilderTechnology.append("N/A");
                }
            }

            int millivolts=intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
            //to change preference
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
            boolean switch_voltage= sharedPreferences.getBoolean("voltage_switch",false);

            if(millivolts>0)
            {
                float voltage=millivolts*0.001f;
                String volt1=(new DecimalFormat("#.#").format(voltage));

                if(!switch_voltage)
                {

                    stringBuilderVoltage.append(volt1).append(" V");
                }
                else
                {
                    stringBuilderVoltage.append(millivolts).append(" mV");
                }

            }

            //Battery capacity
            long capacity = getBatteryCapacity();
            if (capacity > 0) {
                stringBuilderCapacity.append(capacity).append("mAh");
            }


            statsHealth.setText(stringBuilderHealth.toString());
            statsVoltage.setText(stringBuilderVoltage.toString());
            statsPlugged.setText(stringBuilderPlugged.toString());
            statsChargingStatus.setText(stringBuilderChargingStatus.toString());
            statsTechnology.setText(stringBuilderTechnology.toString());
            statsCapacity.setText(stringBuilderCapacity.toString());
            statsChargingSpeed.setText(stringBuilderSpeed.toString());

        }
        else{
            Toast toast_present1 = Toast.makeText(getApplicationContext(),"Battery is not available", Toast.LENGTH_LONG);
            toast_present1.show();
        }
    }

    public long getBatteryCapacity() {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            double batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
            return (long)batteryCapacity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this,About.class));
            default:
                return super.onOptionsItemSelected(item);
        }

    }




    private BroadcastReceiver batteryInfoReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);
        }
    };
}