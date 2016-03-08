package ch.heigvd.res.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * ===============================================================
 * Project name : BufferedIOBenchmark
 * Create by    : mathieuurstein
 * On           : 06.03.16
 * ================================================================
 * Edited By    : -
 * On           : -
 * ================================================================
 * Version      : 1.0
 * ================================================================
 * Description  : Record data in a specific file
 */
public class FileRecoder extends DataRecorder {

    private FileOutputStream outputFile;

    void init(DataRecorder.Serializers s, String fileName) {
        try {
            outputFile = new FileOutputStream(fileName+"."+s.toString().toLowerCase());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ps = new PrintStream(outputFile);

        super.init(s);
    }

    @Override
    void close() {
        try {
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
