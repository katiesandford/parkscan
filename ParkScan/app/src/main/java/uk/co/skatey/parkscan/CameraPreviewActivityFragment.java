package uk.co.skatey.parkscan;

import android.app.AlertDialog;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import uk.co.airsource.android.common.ui.cameraview.ASCameraView;
import uk.co.airsource.android.common.ui.cameraview.DecodeManager;
import uk.co.airsource.android.common.ui.cameraview.DecodeTask;


/**
 * A placeholder fragment containing a simple view.
 */
public class CameraPreviewActivityFragment extends Fragment implements DecodeManager.CompletedDecodeTaskListener<BarCodeTaskWorker>
{

    private ASCameraView mPreview;

    public CameraPreviewActivityFragment() {
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_camera_preview, container);
        mPreview = (ASCameraView) fragmentView.findViewById(R.id.camera);
        mPreview.enableView();
        DecodeManager<BarCodeTaskWorker> decodeManager = new DecodeManager<>(new BarCodeTaskWorkerFactory());
        decodeManager.setCompletedTaskListener(this);
        mPreview.setDecodeManager(decodeManager, true);
        mPreview.startDecoding();
        return fragmentView;
    }

    /**
     * Called when a task is completed. Called on the UI thread.
     * Note that both successful and failing tasks are delivered (the task may contain some
     * information about the failure).
     *
     * @param task The completed task
     */
    @Override
    public void decodeTaskCompleted(DecodeTask<BarCodeTaskWorker> task)
    {
        String result = task.getTaskWorker().result;
        if (result != null)
        {
            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            mPreview.startDecoding();
        }
        else
        {
            Log.d("CameraPreviewActivityFragment", "Task is completed");
        }
    }
}
