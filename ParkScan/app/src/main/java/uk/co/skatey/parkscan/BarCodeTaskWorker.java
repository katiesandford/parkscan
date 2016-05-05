package uk.co.skatey.parkscan;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.airsource.android.common.ui.cameraview.TaskWorker;
import uk.co.airsource.android.common.ui.cameraview.TaskWorkerImageProvider;
import uk.co.airsource.android.common.ui.cameraview.TaskWorkerProgressHandler;

/**
 * Created by sduke on 04/10/15.
 */
public class BarCodeTaskWorker implements TaskWorker
{
    @Nullable
    public String result;

    @Override
    public void runDecode(TaskWorkerProgressHandler handler, TaskWorkerImageProvider provider)
    {
        handler.setDecodeStateStarted();
        byte[] byteArray = provider.getFrameBuffer();
        LuminanceSource source = new PlanarYUVLuminanceSource(
                byteArray,
                provider.getWidth(),
                provider.getHeight(),
                0,
                0,
                provider.getWidth(),
                provider.getHeight(),
                false
        );
        HybridBinarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        ArrayList<BarcodeFormat> possibleFormats= new ArrayList<>();
        possibleFormats.add(BarcodeFormat.CODE_128);
        Map<DecodeHintType, ArrayList> hints = new HashMap<>();
        hints.put(DecodeHintType.POSSIBLE_FORMATS, possibleFormats);
        multiFormatReader.setHints(hints);
        Result contents;
        try
        {
            contents = multiFormatReader.decode(binaryBitmap);
            Log.d("BarCodeTaskWorker", contents.getText());
            result = contents.getText();
            handler.setDecodeStateCompleted();
        }
        catch (NotFoundException e)
        {
            e.printStackTrace();
            handler.setDecodeStateFailed();
        }
    }

    @Override
    public void initializeDecodeTask()
    {

    }

    /**
     * Clear any fields that may have been set in a previous use of this task
     */
    @Override
    public void recycleTask()
    {

    }
}
