package uk.co.skatey.parkscan;

import android.media.Image;

import com.google.zxing.LuminanceSource;

import java.nio.ByteBuffer;

/**
 * Created by Katie on 19/05/2016.
 */
public class PlaneLuminanceSource extends LuminanceSource {

    private final Image.Plane plane;

    protected PlaneLuminanceSource(Image.Plane plane, int width, int height) {
        super(width, height);
        this.plane = plane;
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        if (row == null || row.length < getWidth()) {
            row = new byte[getWidth()];
        }
        plane.getBuffer().position(y*plane.getRowStride());
        plane.getBuffer().get(row, 0, getWidth());
        return row;
    }

    @Override
    public byte[] getMatrix() {
        byte[] matrix = new byte[getWidth()*getHeight()];
        ByteBuffer planeBuffer = plane.getBuffer();
        for (int y = 0; y < getHeight(); y++) {
            planeBuffer.position(y*plane.getRowStride());
            planeBuffer.get(matrix, y*getWidth(), getWidth());
        }
        return matrix;
    }
}
