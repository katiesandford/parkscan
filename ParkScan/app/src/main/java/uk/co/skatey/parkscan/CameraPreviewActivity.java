package uk.co.skatey.parkscan;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.co.skatey.parkscan.databinding.ActivityCameraPreviewBinding;


public class CameraPreviewActivity extends AppCompatActivity {

    private static final String RECORDINGS_STORAGE_FOLDER = "recordings";
    private static final String RECORDINGS_FILE_EXTENSION = ".csv";

    private OutputStream dataOutputStream;

    private static final String CURRENT_RUNNER_KEY = "CameraPreviewActivity.CURRENT_RUNNER_KEY";
    private static final String CURRENT_POSITION_KEY = "CameraPreviewActivity.CURRENT_POSITION_KEY";
    private String currentRunner;
    private String currentPosition;

    private TextView runnerIDTextView;
    private TextView positionTextView;

    private ActivityCameraPreviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            currentRunner = savedInstanceState.getString(CURRENT_RUNNER_KEY);
            currentPosition = savedInstanceState.getString(CURRENT_POSITION_KEY);
        }
        updateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //open the file
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        String date = sdf.format(new Date());
        String fileName = RECORDINGS_STORAGE_FOLDER + "/" + date + RECORDINGS_FILE_EXTENSION;
        try {
            dataOutputStream = openFileOutput(fileName, Context.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //TODO show an error and leave the activity
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close the file
        if (dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dataOutputStream = null;
        }
    }

    private void writePair(@NonNull String runner, @NonNull String position) {
        String pairString = position + "," + runner + "\n";
        try {
            dataOutputStream.write(pairString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO handle error
        }
    }

    private void updateViews() {
        //update the buttons
        binding.setRunnerID(currentRunner);
        binding.setPosition(currentPosition);
    }

    private final CameraPreviewActivityFragment.OnBarcodeScannedListener onBarcodeScannedListener =
            new CameraPreviewActivityFragment.OnBarcodeScannedListener() {
                @Override
                public void onRunnerScanned(String runnerID) {
                    currentRunner = runnerID;
                }

                @Override
                public void onPositionScanned(String position) {
                    currentPosition = position;
                }
            };
}


