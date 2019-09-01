package com.hawk.dilpreet.batteryoptimizer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.text.DecimalFormat;
import java.util.List;

public class RamInformation extends AppCompatActivity {

    AppCompatTextView totalRam, usedRam, availableRam;
    CardView button_optimize;
    long totalMemory = 0;
    float usedMemory = 0;
    long availMemory = 0;
    private static final int BYTEFACTOR = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram_information);

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean switch_memory= sharedPreferences.getBoolean("memory_switch",false);

        StringBuilder stringBuilderTotalRam = new StringBuilder();
        StringBuilder stringBuilderUsedRam = new StringBuilder();
        StringBuilder stringBuilderAvailableRam = new StringBuilder();

        totalRam = (AppCompatTextView) findViewById(R.id.total_ram_value);
        usedRam = (AppCompatTextView) findViewById(R.id.used_ram_value);
        availableRam = (AppCompatTextView) findViewById(R.id.avail_ram_value);
        button_optimize=(CardView)findViewById(R.id.button_optimize);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            final ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            final ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                activityManager.getMemoryInfo(mi);
                totalMemory = mi.totalMem;
                availMemory=mi.availMem;
                float totalMemoryGb=(float) totalMemory / (RamInformation.BYTEFACTOR * RamInformation.BYTEFACTOR * RamInformation.BYTEFACTOR);
                String totalmemoryFormat=(new DecimalFormat("#.#").format(totalMemoryGb));
                stringBuilderTotalRam.append(totalmemoryFormat).append(" GB");

                float availMemoryGb=(float) availMemory / (RamInformation.BYTEFACTOR * RamInformation.BYTEFACTOR * RamInformation.BYTEFACTOR);
                String availmemoryFormat=(new DecimalFormat("#.#").format(availMemoryGb));
                double availMemoryPercent=((double)availMemoryGb/totalMemoryGb) * 100;
                String availMemoryPercentFormat=(new DecimalFormat("#").format(availMemoryPercent));

                if(!switch_memory)
                {
                    stringBuilderAvailableRam.append(availmemoryFormat).append(" GB");
                }
                else
                {
                    stringBuilderAvailableRam.append(availMemoryPercentFormat).append(" %");
                }


                usedMemory=totalMemoryGb-availMemoryGb;
                String usedMemoryFormat=(new DecimalFormat("#.#").format(usedMemory));
                double usedMemoryPercent=((double)usedMemory/totalMemoryGb)*100;
                String usedMemoryPercentFormat=(new DecimalFormat("#").format(usedMemoryPercent));

                if(!switch_memory)
                {
                    stringBuilderUsedRam.append(usedMemoryFormat).append(" GB");
                }
                else
                {
                    stringBuilderUsedRam.append(usedMemoryPercentFormat).append(" %");
                }





                totalRam.setText(stringBuilderTotalRam.toString());
                availableRam.setText(stringBuilderAvailableRam.toString());
                usedRam.setText(stringBuilderUsedRam.toString());
            }
        }

        button_optimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ApplicationInfo> packages;
                PackageManager pm;
                pm = getPackageManager();
                //get a list of installed apps.
                packages = pm.getInstalledApplications(0);

                ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                String myPackage = getApplicationContext().getPackageName();
                for (ApplicationInfo packageInfo : packages) {
                    if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1)continue;
                    if(packageInfo.packageName.equals(myPackage)) continue;
                    mActivityManager.killBackgroundProcesses(packageInfo.packageName);
                }
                Snackbar.make(button_optimize, "Battery optimized", Snackbar.LENGTH_LONG).show();

                finish();
                startActivity(getIntent());
            }
        });


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

}