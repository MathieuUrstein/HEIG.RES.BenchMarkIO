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
 * Description  : Serialize in CSV format
 */
public class CsvSerializer implements DataSerializer {

    boolean headerDone = false;

    /**
     * Serialize the data in the ps stream
     * @param ps PrintStream
     * @param data DataHandler
     */
    @Override
    public void serialize(PrintStream ps, DataHandler data) {
        if (!headerDone) {
            createHeader(ps, data);
            headerDone = true;
        }
        String values = new String("");
        for (Object value : data.getData().values()) {
            values+= value+",";
        }
        ps.println(values.substring(0,values.length()-1));
    }

    /**
     * Create the header line
     * @param ps PrintStream
     * @param data DataHandler
     */
    public void createHeader(PrintStream ps, DataHandler data) {
        String headers = new String("");
        for (String header : data.getData().keySet()) {
            headers+= header+",";
        }
        ps.println(headers.substring(0,headers.length()-1));
    }
}
