package com.moonface.schoolix;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        android.support.v7.widget.Toolbar _toolbar = findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        _toolbar.setNavigationOnClickListener(_v -> onBackPressed());
        TextView author_view = findViewById(R.id.author_view);
        TextView version_view = findViewById(R.id.version_view);
        TextView whats_new_view = findViewById(R.id.whats_new_view);
        View licences_block = findViewById(R.id.licences_block);
        View contact_block = findViewById(R.id.contact_block);

        author_view.setText(getString(R.string.author_label).concat(getString(R.string.colon_char)).concat(getString(R.string.space_char)).concat(getString(R.string.author)));
        try {
            version_view.setText(getString(R.string.version_label).concat(getString(R.string.colon_char)).concat(getString(R.string.space_char)).concat(getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        whats_new_view.setText(getString(R.string.whats_new));
        licences_block.setOnClickListener(v -> startActivity(new Intent(this, OssLicensesMenuActivity.class)));
        contact_block.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getString(R.string.mailto)+getString(R.string.email)));
            startActivity(intent);
        });
    }
}
