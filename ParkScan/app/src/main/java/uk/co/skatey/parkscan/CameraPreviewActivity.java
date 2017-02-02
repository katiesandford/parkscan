package uk.co.skatey.parkscan;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.co.skatey.parkscan.databinding.ActivityCameraPreviewBinding;


public class CameraPreviewActivity extends AppCompatActivity implements CameraPreviewActivityFragment.OnBarcodeScannedListener {

    private OutputStream dataOutputStream;

    private static final String CURRENT_RUNNER_KEY = "CameraPreviewActivity.CURRENT_RUNNER_KEY";
    private static final String CURRENT_POSITION_KEY = "CameraPreviewActivity.CURRENT_POSITION_KEY";

    private static final String EMPTY_SCAN_STRING = "N/A";

    @Nullable private String currentRunner;
    @Nullable private String currentPosition;

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
        String fileName = Utils.getCurrentFilename();
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
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO handle error
        }
    }

    private void updateViews() {

        String newRunner = EMPTY_SCAN_STRING;
        String newPosition = EMPTY_SCAN_STRING;

        if (Utils.isStringRunnerId(currentRunner)) {
            newRunner = currentRunner;
        }

        Integer position = Utils.positionFromString(currentPosition);

        if (position != null) {
            newPosition = position.toString();
        }

        //update the textViews
        binding.setRunnerID(newRunner);
        binding.setPosition(newPosition);

        binding.setClearEnabled(!newPosition.equals(EMPTY_SCAN_STRING) || !newRunner.equals(EMPTY_SCAN_STRING));
        binding.setSaveEnabled(!newPosition.equals(EMPTY_SCAN_STRING) && !newRunner.equals(EMPTY_SCAN_STRING));
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

    private void positiveVibrate() {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {0, 200};

        // Vibrate
        v.vibrate(pattern, -1);
    }

    private void negativeVibrate() {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {0, 200, 500, 200};

        // Vibrate
        v.vibrate(pattern, -1);
    }

    @Override
    public void onRunnerScanned(String runnerID) {
        if (currentRunner == null) {
            positiveVibrate();
            currentRunner = runnerID;
            updateViews();
        } else {
//            negativeVibrate();
        }
    }

    @Override
    public void onPositionScanned(String position) {

        if (currentPosition == null) {
            positiveVibrate();
            currentPosition = position;
            updateViews();
        } else {
//            negativeVibrate();
        }
    }

    public void onClearClicked(View view) {
        currentRunner = null;
        currentPosition = null;
        updateViews();
    }

    public void onSaveClicked(View view) {
        if (currentPosition == null || currentRunner == null) return;
        writePair(currentRunner, currentPosition);
        currentPosition = null;
        currentRunner = null;
        updateViews();
    }
}
