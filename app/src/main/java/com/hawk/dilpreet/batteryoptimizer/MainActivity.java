package com.hawk.dilpreet.batteryoptimizer;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionMenu;
import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NotificationManagerCompat mNotificationManagerCompatObject;

    ImageView batteryState;
    CardView button1;
    boolean notificationState=true;
    AppCompatTextView mainActivityTemp;
    FloatingActionMenu fam;
    com.github.clans.fab.FloatingActionButton batteryStatsButton,batterySaverButton,batteryInformationButton;
    AppCompatButton settingsButton;
    TextView batteryText;
    Handler handler;
    int deviceStatus;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Method Calling
        loadBatteryInfo();

        //Variable Initialization.
        batteryText = (TextView) findViewById(R.id.batteryText);
        mainActivityTemp = (AppCompatTextView) findViewById(R.id.tempText);
        batteryState = (ImageView) findViewById(R.id.batteryImage);
        settingsButton = (AppCompatButton) findViewById(R.id.settings_button);
        button1 = (CardView) findViewById(R.id.button1);
        mNotificationManagerCompatObject = NotificationManagerCompat.from(this);
        fam = (FloatingActionMenu) findViewById(R.id.fab_menu_button);
        batteryStatsButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.batteryStatsFloatButton);
        batterySaverButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.batterySaverFloatButton);
        batteryInformationButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.batteryInformationButton);
        //Created object for Runnable class.
        runnable = new Runnable() {
            @Override
            public void run() {
                int level = (int) batteryState();
                handler.postDelayed(runnable, 0);
                batteryText.setText(level + "%");


                if (deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING) {

                    if ((level <= 100) & (level > 90)) {
                        batteryState.setImageResource(R.drawable.ic_battery_charging_full);
                    } else if ((level <= 90) & (level > 80)) {
                        batteryState.setImageResource(R.drawable.ic_battery_charging_90);
                    } else if ((level <= 80) & (level > 60)) {
                        batteryState.setImageResource(R.drawable.ic_battery_charging_80);
                    } else if ((level <= 60) & (level > 50)) {
                        batteryState.setImageResource(R.drawable.ic_battery_charging_60);
                    } else if ((level <= 50) & (level > 30)) {
                        batteryState.setImageResource(R.drawable.ic_battery_charging_50);
                    } else if ((level <= 30) & (level > 20)) {
                        batteryState.setImageResource(R.drawable.ic_battery_charging_30);
                    } else if ((level <= 20) & (level > 5)) {
                        batteryState.setImageResource(R.drawable.ic_battery_charging_20);
                    } else {
                        batteryState.setImageResource(R.drawable.ic_battery_charging_low);
                    }
                } else if ((level <= 100) & (level > 90)) {
                    batteryState.setImageResource(R.drawable.ic_batteryfull);
                } else if ((level <= 90) & (level > 80)) {
                    batteryState.setImageResource(R.drawable.ic_battery90);
                } else if ((level <= 80) & (level > 60)) {
                    batteryState.setImageResource(R.drawable.ic_battery80);
                } else if ((level <= 60) & (level > 50)) {
                    batteryState.setImageResource(R.drawable.ic_battery60);
                } else if ((level <= 50) & (level > 30)) {
                    batteryState.setImageResource(R.drawable.ic_battery50);
                } else if ((level <= 30) & (level > 20)) {
                    batteryState.setImageResource(R.drawable.ic_battery30);
                } else if ((level <= 20) & (level > 15)) {
                    batteryState.setImageResource(R.drawable.ic_battery20);
                    notificationState = false;
                }
                else if ((level <= 15) & (level > 5)) {
                    batteryState.setImageResource(R.drawable.ic_battery20);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    final boolean switch_notification = sharedPreferences.getBoolean("notification_switch", false);
                    final boolean switch_wifi = sharedPreferences.getBoolean("wifi_switch", false);
                    final boolean switch_bluetooth = sharedPreferences.getBoolean("bluetooth_switch", false);
                    final boolean switch_brightness = sharedPreferences.getBoolean("brightness_switch", false);

                    if (!notificationState) {
                        if (switch_notification) {
                            Notification notificationobject = new NotificationCompat.Builder(MainActivity.this, Notifications.CHANNEL_1_ID)
                                    .setSmallIcon(R.drawable.ic_battery_percentage_not_charging)
                                    .setContentTitle("Low Battery Warning")
                                    .setContentText("Your battery is less than 15%, please plugin the charger.")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();
                            mNotificationManagerCompatObject.notify(1, notificationobject);
                            notificationState = true;
                        }

                        //To turn off wifi
                        if (switch_wifi) {
                            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                            notificationState = true;
                            wifi.setWifiEnabled(false);
                        }

                        //To turn off Bluetooth
                        if (switch_bluetooth) {
                            BluetoothAdapter bluetoothAdapterstate1 = BluetoothAdapter.getDefaultAdapter();
                            notificationState = true;
                            if (bluetoothAdapterstate1.isEnabled()) {
                                bluetoothAdapterstate1.disable();
                            }
                        }

                        //To turn low brightness
                        if (switch_brightness) {
                            if (Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)) {
                                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

                            }
                            int mode = -1;
                            try {
                                mode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE); //this will return integer (0 or 1)
                            } catch (Exception e) {
                            }
                            if (mode == 0) {
                                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 2);
                                try {
                                    int brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);  //returns integer value 0-255
                                } catch (Exception e) {
                                }
                                try {
                                    int br = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);  //this will get the information you have just set...

                                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                                    lp.screenBrightness = (float) br / 1; //...and put it here
                                    getWindow().setAttributes(lp);
                                } catch (Exception e) {
                                }

                            }
                            notificationState = true;
                        }
                    }
                }
                else if ((level <= 5) & (level > 0)) {

                    batteryState.setImageResource(R.drawable.ic_batterylow);
                }
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 0);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsButton = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsButton);

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ApplicationInfo> packages;
                PackageManager pm;
                pm = getPackageManager();
                //get a list of installed apps.
                packages = pm.getInstalledApplications(0);

                ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                String myPackage = getApplicationContext().getPackageName();
                for (ApplicationInfo packageInfo : packages) {
                    //if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1)continue;
                    if (packageInfo.packageName.equals(myPackage)) continue;
                    mActivityManager.killBackgroundProcesses(packageInfo.packageName);
                }
                Snackbar.make(button1, "Battery optimized", Snackbar.LENGTH_LONG)
                        .setAction("Ram Information", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(MainActivity.this, RamInformation.class);
                                startActivity(i);
                            }
                        }).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isFirstTime()) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You need to provide system access to enjoy all the features.");
                alertDialog.setNegativeButton("Exit app", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        i.setData(Uri.parse("package:" + getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                });

                alertDialog.show();
            }
        }
        //Floating Action Menu
        if (fam.isOpened()) {
            fam.close(true);
        }
        fam.setClosedOnTouchOutside(true);
        batteryStatsButton.setOnClickListener(onButtonClick());
        batterySaverButton.setOnClickListener(onButtonClick());
        batteryInformationButton.setOnClickListener(onButtonClick());

        //Ongoing Notification
        SharedPreferences ongoignNotificationSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final boolean switch_ongoign_notification = ongoignNotificationSharedPreferences.getBoolean("ongoing_notification_switch", false);

        if (switch_ongoign_notification) {
            Intent serviceIntent=new Intent(this,OngoingService.class);
            startService(serviceIntent);
        }
        else if(!switch_ongoign_notification){
            Intent serviceIntent=new Intent(this,OngoingService.class);
            stopService(serviceIntent);

        }
    }

    public float batteryState(){
        Intent batteryIntent=registerReceiver(null,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryIntent != null;
        deviceStatus = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        int batteryLevel=batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int batteryScale=batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

        if(batteryLevel==-1 || batteryScale==-1){
            return 50.0f;
        }
        return ((float) batteryLevel/(float)batteryScale)*100.0f;
    }

    //Floating Action buttons listener
    private View.OnClickListener onButtonClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view==batteryStatsButton){

                    Intent intent=new Intent(getBaseContext(),BatteryStats.class);
                    intent.putExtra("tempvalueforstats",mainActivityTemp.getText().toString());
                    intent.putExtra("levelvalueforstats",batteryText.getText().toString());
                    startActivity(intent);
                }
                else if(view==batteryInformationButton){
                    startActivityForResult(new Intent(Intent.ACTION_POWER_USAGE_SUMMARY),0);
                }
                else if(view==batterySaverButton){
                    if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP){
                        Snackbar.make(button1, "Your Android version does not support this feature.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                    else{
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_BATTERY_SAVER_SETTINGS),0);
                    }

                }

            }
        };
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
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean switch_temperature= sharedPreferences.getBoolean("temperature_switch",false);

        if(batteryPresent){
            int temprature=intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            StringBuilder stringBuilderTemprature=new StringBuilder();
            //Converted celsius into fahrenheits
            double temperature_fahrenheit=(temprature/10f)*1.8+32;
            String temperature_fahrenheit_format=(new DecimalFormat("##.#").format(temperature_fahrenheit));

            if(temprature>0) {
                if (!switch_temperature)
                {
                    stringBuilderTemprature.append(((float) temprature / 10f)).append(" °C");
                }
                else
                {
                    stringBuilderTemprature.append(temperature_fahrenheit_format).append(" °F");
                }
            }
            mainActivityTemp.setText(stringBuilderTemprature.toString());

        }
        else{
            Toast toast_present = Toast.makeText(getApplicationContext(),"Battery is not available", Toast.LENGTH_LONG);
            toast_present.show();
        }

    }

    private BroadcastReceiver batteryInfoReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);
        }
    };

    public void ramInfo(View view) {
        Intent i=new Intent(MainActivity.this,RamInformation.class);
        startActivity(i);
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.apply();
        }
        return !ranBefore;
    }
}