package uk.co.skatey.parkscan;

//import android.media.Image;
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

//import uk.co.airsource.android.common.ui.cameraview.TaskWorker;
//import uk.co.airsource.android.common.ui.cameraview.TaskWorkerImageProvider;
//import uk.co.airsource.android.common.ui.cameraview.TaskWorkerProgressHandler;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

/**
 * Created by sduke on 04/10/15.
 */
public class BarCodeTaskWorker //implements TaskWorker
{
    @Nullable
    public String result;

//    @Override
    public static String runDecode(android.media.Image image/*TaskWorkerProgressHandler handler, TaskWorkerImageProvider provider*/)
    {
//        handler.setDecodeStateStarted();
//        byte[] byteArray = provider.getFrameBuffer();

        LuminanceSource source = new PlaneLuminanceSource(
                image.getPlanes()[0],
                image.getWidth(),
                image.getHeight()
        );
//        HybridBinarizer binarizer = new HybridBinarizer(source);
//        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
//        MultiFormatReader multiFormatReader = new MultiFormatReader();
//        ArrayList<BarcodeFormat> possibleFormats= new ArrayList<>();
//        possibleFormats.add(BarcodeFormat.CODE_128);
//        Map<DecodeHintType, ArrayList> hints = new HashMap<>();
//        hints.put(DecodeHintType.POSSIBLE_FORMATS, possibleFormats);
//        multiFormatReader.setHints(hints);
//        Result contents;
//        try
//        {
//            contents = multiFormatReader.decode(binaryBitmap);
//            Log.d("BarCodeTaskWorker", contents.getText());
//            return contents.getText();
////            handler.setDecodeStateCompleted();
//        }
//        catch (NotFoundException e)
//        {
////            e.printStackTrace();
////            handler.setDecodeStateFailed();
//
//        }
        Image barcode = new Image(image.getWidth(), image.getHeight(), "Y800");
        barcode.setData(source.getMatrix());

        ImageScanner scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        int result = scanner.scanImage(barcode);

        if (result != 0) {

            SymbolSet syms = scanner.getResults();
            for (Symbol sym : syms) {
                return sym.getData();
            }
        }
        return null;
    }

//    @Override
//    public void initializeDecodeTask()
//    {
//
//    }
//
//    /**
//     * Clear any fields that may have been set in a previous use of this task
//     */
//    @Override
//    public void recycleTask()
//    {
//
//    }
}
