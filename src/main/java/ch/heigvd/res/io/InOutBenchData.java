package ch.heigvd.res.io;

import ch.heigvd.res.io.util.Timer;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ===============================================================
 * Project name : BufferedIOBenchmarkAllInOne
 * Create by    : mathieuurstein
 * On           : 06.03.16
 * ================================================================
 * Edited By    : -
 * On           : -
 * ================================================================
 * Version      : 1.0
 * ================================================================
 * Description  : Let people generate data of an io benchmark
 * with different different strategies, operations, blocksize, filesize
 *
 * also persist the duration of this test
 */

public class InOutBenchData extends DataHandler {

    private void setOperation(String operation) {
        getData().put("Operation", operation);
    }
    private void setStrategy(String strategy) {
        getData().put("Strategy", strategy);
    }
    private void setBlockSize(Integer blockSize) {
        getData().put("BlockSize", blockSize);
    }
    private void setFileSizeInBytes(Long fileSizeInBytes) {
        getData().put("FileSizeInBytes", fileSizeInBytes);
    }
    private void setDurationInMs(Long durationInMs) {
        getData().put("DurationInMs", durationInMs);
    }

    final static String FILENAME_PREFIX = "test-data"; // we will write and read test files at this location
    static final Logger LOG = Logger.getLogger(BufferedIOBenchmarkAllInOne.class.getName());

    /**
     * This enum is used to describe the 4 different strategies for doing the IOs
     */
    public enum IOStrategy {
        ByteByByteWithoutBufferedStream,
        ByteByByteWithBufferedStream,
        BlockByBlockWithoutBufferedStream,
        BlockByBlockWithBufferedStream
    }

    /**
     * This enum lists the different oprations
     */
    public enum Operation {
        WRITE,
        READ
    }

    /**
     * This method drives the generation of test data file, based on the parameters passed. The method opens a
     * FileOutputStream. Depending on the strategy, it wraps a BufferedOutputStream around it, or not. The method
     * then delegates the actual production of bytes to another method, passing it the stream.
     */
    private void produceTestData(IOStrategy ioStrategy, long numberOfBytesToWrite, int blockSize) {

        OutputStream os = null;
        try {
            // Let's connect our stream to a file data sink
            os = new FileOutputStream(FILENAME_PREFIX + "-" + ioStrategy + "-" + blockSize + ".bin");

            // If the strategy dictates to use a buffered stream, then let's wrap one around our file output stream
            if ((ioStrategy == IOStrategy.BlockByBlockWithBufferedStream) || (ioStrategy == IOStrategy.ByteByByteWithBufferedStream)) {
                os = new BufferedOutputStream(os);
            }

            // Now, let's call the method that does the actual work and produces bytes on the stream
            produceDataToStream(os, ioStrategy, numberOfBytesToWrite, blockSize);

            // We are done, so we only have to close the output stream
            os.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

    }

    /**
     * This method produces bytes on the passed stream (the method does not know this stream is buffered or not)
     * Depending on the strategy, the method either writes bytes one by one OR in chunks (the size of the chunk
     * is passed in parameter)
     */
    private void produceDataToStream(OutputStream os, IOStrategy ioStrategy, long numberOfBytesToWrite, int blockSize) throws IOException {
        // If the strategy dictates to write byte by byte, then it's easy to write the loop; but let's just hope that our client has
        // given us a buffered output stream, otherwise the performance will be really bad
        if ((ioStrategy == IOStrategy.ByteByByteWithBufferedStream) || (ioStrategy == IOStrategy.ByteByByteWithoutBufferedStream)) {
            for (int i = 0; i < numberOfBytesToWrite; i++) {
                os.write('h');
            }

            // If the strategy dictates to write block by block, then the loop is a bit longer to write
        } else {
            long remainder = numberOfBytesToWrite % blockSize;
            long numberOfBlocks = (numberOfBytesToWrite / blockSize);
            byte[] block = new byte[blockSize];

            // we start by writing a number of entire blocks
            for (int i = 0; i < numberOfBlocks; i++) {
                for (int j = 0; j < blockSize; j++) {
                    block[j] = 'b';
                }
                os.write(block);
            }

            // and we write a partial block at the end
            if (remainder != 0) {
                for (int j = 0; j < remainder; j++) {
                    block[j] = 'B';
                }
                os.write(block, 0, (int) remainder);
            }
        }
    }

    /**
     * This method drives the consumption of test data file, based on the parameters passed. The method opens a
     * FileInputStream. Depending on the strategy, it wraps a BufferedInputStream around it, or not. The method
     * then delegates the actual consumption of bytes to another method, passing it the stream.
     */
    private void consumeTestData(IOStrategy ioStrategy, int blockSize) {
        InputStream is = null;
        try {
            // Let's connect our stream to a file data sink
            is = new FileInputStream(FILENAME_PREFIX + "-" + ioStrategy + "-" + blockSize + ".bin");

            // If the strategy dictates to use a buffered stream, then let's wrap one around our file input stream
            if ((ioStrategy == IOStrategy.BlockByBlockWithBufferedStream) || (ioStrategy == IOStrategy.ByteByByteWithBufferedStream)) {
                is = new BufferedInputStream(is);
            }

            // Now, let's call the method that does the actual work and produces bytes on the stream
            consumeDataFromStream(is, ioStrategy, blockSize);

            // We are done, so we only have to close the input stream
            is.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    /**
     * This method consumes bytes on the passed stream (the method does not know this stream is buffered or not)
     * Depending on the strategy, the method either reads bytes one by one OR in chunks (the size of the chunk
     * is passed in parameter). The method does not do anything with the read bytes, except counting them.
     */
    private void consumeDataFromStream(InputStream is, IOStrategy ioStrategy, int blockSize) throws IOException {
        long totalBytes = 0;
        // If the strategy dictates to write byte by byte, then it's easy to write the loop; but let's just hope that our client has
        // given us a buffered output stream, otherwise the performance will be really bad
        if ((ioStrategy == IOStrategy.ByteByByteWithBufferedStream) || (ioStrategy == IOStrategy.ByteByByteWithoutBufferedStream)) {
            int c;
            while ((c = is.read()) != -1) {
                // here, we could cast c to a byte and process it
                totalBytes++;
            }

            // If the strategy dictates to write block by block, then the loop is a bit longer to write
        } else {
            byte[] block = new byte[blockSize];
            int bytesRead;
            while ((bytesRead = is.read(block)) != -1) {
                // here, we can process bytes block[0..bytesRead]
                totalBytes += bytesRead;
            }
        }

        setFileSizeInBytes(totalBytes);
    }

    /**
     * Produces the data of the bench.
     * For read strategy the fileSizeInBytes is useless. Just set it to 0
     * @param operation Operation type
     * @param strategy Strategy type
     * @param blockSize Block size
     * @param fileSizeInBytes Size of the write file
     */
    public InOutBenchData(Operation operation, IOStrategy strategy, long fileSizeInBytes, int blockSize) {
        // set known values
        setOperation(operation.toString());
        setStrategy(strategy.toString());
        setBlockSize(blockSize);
        // the bench start here
        Timer.start();
        LOG.log(Level.INFO, "Generating test data ({0}, {1}, {2})",
                new Object[]{
                        operation,
                        strategy,
                        blockSize,
                }
        );
        if (operation == Operation.READ) {
            consumeTestData(strategy, blockSize);
        } else if (operation == Operation.WRITE) {
            setFileSizeInBytes(fileSizeInBytes);
            produceTestData(strategy, fileSizeInBytes, blockSize);
        }
        // the bench finish here
        long duration = Timer.takeTime();
        setDurationInMs(duration);
        LOG.log(Level.INFO, "Finished in {0}", duration);
    }
}
