package com.hawk.dilpreet.batteryoptimizer;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.LinkMovementMethod;
import android.view.View;

public class About extends AppCompatActivity {

    AppCompatTextView versionNumber,github,contact;
    String versionName= BuildConfig.VERSION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        StringBuilder stringBuilderVersion=new StringBuilder();

        versionNumber=(AppCompatTextView)findViewById(R.id.version_code);
        github=(AppCompatTextView)findViewById(R.id.text_github);
        contact=(AppCompatTextView)findViewById(R.id.contact);

        stringBuilderVersion.append("Battery Optimizer v"+versionName);
        versionNumber.setText(stringBuilderVersion.toString());

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent githubIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/amandewett"));
                startActivity(githubIntent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent=new Intent(Intent.ACTION_SEND);
                contactIntent.setType("plain/text");
                contactIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"amandewett@techie.com"});
                contactIntent.putExtra(Intent.EXTRA_SUBJECT,"Battery Optimizer v"+versionName);
                contactIntent.putExtra(Intent.EXTRA_TEXT,"");
                startActivity(Intent.createChooser(contactIntent,"Send Email"));
            }
        });
    }
}
