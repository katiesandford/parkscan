package uk.co.skatey.parkscan;

import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A placeholder fragment containing a simple view.
 */
public class CameraPreviewActivityFragment extends Fragment {

    private Camera mCamera;
    private CameraPreview mPreview;

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
        mPreview = (CameraPreview) fragmentView.findViewById(R.id.camera);
        boolean qOpened = safeCameraOpen();
        if (qOpened) {
            mPreview.setCamera(mCamera);
        }
        return fragmentView;
    }

    private boolean safeCameraOpen() {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(0);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
