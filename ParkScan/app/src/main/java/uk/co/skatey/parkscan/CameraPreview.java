package uk.co.skatey.parkscan;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CAMERA_PREVIEW";

    SurfaceHolder mHolder;
    Camera mCamera;
    List<Camera.Size> mSupportedPreviewSizes;

    CameraPreview(Context context) {
        super(context);
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {
        if (mCamera == camera) { return; }

        stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            List<Camera.Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedPreviewSizes = localSizes;
            requestLayout();

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            mCamera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();
        }
    }

    /**
     * When this function returns, mCamera will be null.
     */
    private void stopPreviewAndFreeCamera() {

        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();

            mCamera = null;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        parameters.setPreviewSize(width, height);
        requestLayout();
        mCamera.setParameters(parameters);

        // Important: Call startPreview() to start updating the preview surface.
        // Preview must be started before you can take a picture.
        mCamera.startPreview();
    }
}
