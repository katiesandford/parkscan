package uk.co.skatey.parkscan;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by samduke on 29/01/2017.
 */

class Utils {

    private static final String RECORDINGS_STORAGE_PREFIX = "recordings";
    private static final String RECORDINGS_FILE_EXTENSION = ".csv";

    static String getCurrentFilename() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        String date = sdf.format(new Date());
        return RECORDINGS_STORAGE_PREFIX + "_" + date + RECORDINGS_FILE_EXTENSION;
    }

    static boolean isStringRunnerId(@Nullable String runnerId) {

        if (runnerId == null) {
            return false;
        }

        if (!runnerId.startsWith("A")) {
            return false;
        }

        String numbersOnlyString = runnerId.substring(1);

        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(numbersOnlyString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Nullable
    static Integer positionFromString(@Nullable   String positionString) {

        if (positionString == null) {
            return null;
        }

        if (!positionString.startsWith("P")) {
            return null;
        }

        String numbersOnlyString = positionString.substring(1);

        try {
            return Integer.parseInt(numbersOnlyString);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
