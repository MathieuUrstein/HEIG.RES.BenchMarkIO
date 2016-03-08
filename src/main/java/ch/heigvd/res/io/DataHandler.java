package ch.heigvd.res.io;

import java.util.HashMap;

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
 * Description  : Let people map data
 */
public class DataHandler {
    private HashMap<String, Object> data;

    public DataHandler() {
        data = new HashMap<>();
    }

    public HashMap<String, Object> getData() {
        return data;
    }
}
