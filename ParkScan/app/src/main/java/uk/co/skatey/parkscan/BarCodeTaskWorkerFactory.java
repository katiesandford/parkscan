package uk.co.skatey.parkscan;

import uk.co.airsource.android.common.ui.cameraview.TaskWorker;
import uk.co.airsource.android.common.ui.cameraview.TaskWorkerFactory;

/**
 * Created by sduke on 04/10/15.
 */
public class BarCodeTaskWorkerFactory implements TaskWorkerFactory<BarCodeTaskWorker>
{
    @Override
    public BarCodeTaskWorker newWorker()
    {
        return new BarCodeTaskWorker();
    }
}
