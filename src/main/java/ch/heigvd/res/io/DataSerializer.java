package ch.heigvd.res.io;

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
 * Description  : Serialize data
 */
public interface DataSerializer {
    void serialize(PrintStream ps, DataHandler data);
}
