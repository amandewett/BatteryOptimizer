package com.hawk.dilpreet.batteryoptimizer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsPreference extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Preference for temperature value switch
        android.preference.SwitchPreference temperature_switch = (android.preference.SwitchPreference) findPreference("temperature_switch");
        temperature_switch.setSummaryOff("Switch on to show temperature in °F");
        temperature_switch.setSummaryOn("Switch off to show Temperature in °C");
        //Preference for voltage value switch
        android.preference.SwitchPreference voltage_switch = (android.preference.SwitchPreference) findPreference("voltage_switch");
        voltage_switch.setSummaryOff("Switch on to show voltage in mV");
        voltage_switch.setSummaryOn("Switch off to show voltage in V");
        //Preference for memory value switch
        android.preference.SwitchPreference memory_switch = (android.preference.SwitchPreference) findPreference("memory_switch");
        memory_switch.setSummaryOff("Switch on to show available and used memory in %");
        memory_switch.setSummaryOn("Switch off to show available and used memory in GB");
        //Preference for nitofication value switch
        android.preference.SwitchPreference notification_switch = (android.preference.SwitchPreference) findPreference("notification_switch");
        notification_switch.setSummaryOff("Switch on to show low battery notification @15%");
        notification_switch.setSummaryOn("Switch off to hide low battery notification");
        //Preference for wifi switch
        android.preference.SwitchPreference wifi_switch = (android.preference.SwitchPreference) findPreference("wifi_switch");
        wifi_switch.setSummaryOff("Switch on to turn off wifi when battery is low");
        wifi_switch.setSummaryOn("Switch off to do not turn off wifi when battery is low");
        //Preference for bluetooth switch
        android.preference.SwitchPreference bluetooth_switch = (android.preference.SwitchPreference) findPreference("bluetooth_switch");
        bluetooth_switch.setSummaryOff("Switch on to turn off bluetooth when battery is low");
        bluetooth_switch.setSummaryOn("Switch off to do not turn off bluetooth when battery is low");
        //Preference for brightness switch
        final android.preference.SwitchPreference brightness_switch = (android.preference.SwitchPreference) findPreference("brightness_switch");
        brightness_switch.setSummaryOff("Switch on to dim screen brightness when battery is low");
        brightness_switch.setSummaryOn("Switch off to do not dim screen brightness when battery is low");
        //Preference for Ongoing notification
        android.preference.SwitchPreference ongoing_notification_switch = (android.preference.SwitchPreference) findPreference("ongoing_notification_switch");
        ongoing_notification_switch.setSummaryOff("Switch on to show ongoing notification");
        ongoing_notification_switch.setSummaryOn("Switch off to do not show ongoing notification");
        ongoing_notification_switch.setEnabled(true);
        ongoing_notification_switch.setDefaultValue(true);
    }
}
