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
 * Description  : Record data into printstream with a chosen serializer
 */
public abstract class DataRecorder {

    public enum Serializers {
        CSV
    }

    PrintStream ps;
    Serializers serializerChoice;
    DataSerializer serializer;

     void init(Serializers serializerChoice) {
         this.serializerChoice = serializerChoice;
         switch (serializerChoice) {
             case CSV:
                 serializer = new CsvSerializer();
                 break;
         }
     }

    abstract void close();

    public void record(DataHandler data) {
        serializer.serialize(ps, data);
    }
}
