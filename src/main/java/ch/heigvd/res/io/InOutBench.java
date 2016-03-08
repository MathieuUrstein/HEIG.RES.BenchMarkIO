package ch.heigvd.res.io;

/**
 * ===============================================================
 * Project name : BufferedIOBenchmark
 * Create by    : mathieu urstein
 * On           : 06.03.16
 * ================================================================
 * Edited By    : -
 * On           : -
 * ================================================================
 * Version      : 1.0
 * ================================================================
 * Description  : Let the BufferedIOBenchmark be done
 */
public class InOutBench {
    static final int NB_METRICS = 10;
    static final String fileName = "metricsAnalysis";

    /**
     * Number of byte that will be written during the test
     */
    final static long NUMBER_OF_BYTES_TO_WRITE = 1024 * 1024 * 10; // we will write and read 10 MB files

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileRecoder recorder = new FileRecoder();
        // we are going to do 10 test to see if there is a lot of differences
        // between the tests
        for (int i = 0; i < NB_METRICS; i++) {
            recorder.init(DataRecorder.Serializers.CSV, fileName+"-"+i);
            for (InOutBenchData.Operation operation : InOutBenchData.Operation.values()) {
                recorder.record(
                    new InOutBenchData(operation, InOutBenchData.IOStrategy.BlockByBlockWithBufferedStream, NUMBER_OF_BYTES_TO_WRITE, 500)
                );
                recorder.record(
                    new InOutBenchData(operation, InOutBenchData.IOStrategy.BlockByBlockWithBufferedStream, NUMBER_OF_BYTES_TO_WRITE, 50)
                );
                recorder.record(
                    new InOutBenchData(operation, InOutBenchData.IOStrategy.BlockByBlockWithBufferedStream, NUMBER_OF_BYTES_TO_WRITE, 5)
                );
                recorder.record(
                    new InOutBenchData(operation, InOutBenchData.IOStrategy.ByteByByteWithBufferedStream, NUMBER_OF_BYTES_TO_WRITE, 0)
                );
                recorder.record(
                    new InOutBenchData(operation, InOutBenchData.IOStrategy.BlockByBlockWithoutBufferedStream, NUMBER_OF_BYTES_TO_WRITE, 500)
                );
                recorder.record(
                    new InOutBenchData(operation, InOutBenchData.IOStrategy.BlockByBlockWithoutBufferedStream, NUMBER_OF_BYTES_TO_WRITE, 50)
                );
                recorder.record(
                    new InOutBenchData(operation, InOutBenchData.IOStrategy.BlockByBlockWithoutBufferedStream, NUMBER_OF_BYTES_TO_WRITE, 5)
                );
                recorder.record(
                    new InOutBenchData(operation, InOutBenchData.IOStrategy.ByteByByteWithoutBufferedStream, NUMBER_OF_BYTES_TO_WRITE, 0)
                );
            }
            recorder.close();
        }
        System.out.println("finished");
    }
}
