package uk.co.skatey.parkscan;

import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.airsource.android.common.ui.cameraview.ASCameraView;


/**
 * A placeholder fragment containing a simple view.
 */
public class CameraPreviewActivityFragment extends Fragment {

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
        return fragmentView;
    }
}
