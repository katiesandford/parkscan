package uk.co.skatey.parkscan;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Intent mResultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up an Intent to send back to apps that request a file
        mResultIntent =
                new Intent("com.example.myapp.ACTION_RETURN_FILE");

//        setResult(Activity.RESULT_CANCELED, null);
    }

    public void onStartScanningClicked(View view) {
        startActivity(new Intent(this, CameraPreviewActivity.class));
    }

    public void onSendResultsClicked(View view) {

        String filename = this.getFilesDir() + "/" + Utils.getCurrentFilename();

        File requestFile = new File(filename);

//        Intent myIntent = new Intent(Intent.ACTION_VIEW);
////        myIntent.setData(Uri.fromFile(requestFile));
//        myIntent.setAction(Intent.ACTION_SEND);
//        myIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(requestFile));
//        myIntent.setType("text/csv");
//        Intent j = Intent.createChooser(myIntent, "Choose an application to open with:");
//        startActivity(j);

        Uri fileUri = null;

        // Use the FileProvider to get a content URI
        try {
            fileUri = FileProvider.getUriForFile(
                    MainActivity.this,
                    "uk.co.skatey.parkscan.fileprovider",
                    requestFile);
        } catch (IllegalArgumentException e) {
            Log.e("File Selector",
                    "The selected file can't be shared: " +
                            filename);
        }

        if (fileUri != null) {
            // Grant temporary read permission to the content URI
            mResultIntent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Put the Uri and MIME type in the result Intent
            mResultIntent.setDataAndType(
                    fileUri,
                    getContentResolver().getType(fileUri));

            mResultIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

            mResultIntent.setAction(Intent.ACTION_SEND);
            // Set the result
//            MainActivity.this.setResult(Activity.RESULT_OK,
//                    mResultIntent);
            startActivity(mResultIntent);
        } else {
            mResultIntent.setDataAndType(null, "");
//            MainActivity.this.setResult(RESULT_CANCELED,
//                    mResultIntent);
        }
    }
}
