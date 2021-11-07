package edu.bits.wilp.ir_assignment.crawler;

import edu.bits.wilp.ir_assignment.utils.JsonSerDe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ResultWriterWorker implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ResultWriterWorker.class);
    private static final int WAIT_TIME_IN_MS = 3 * 1000; // seconds

    private LinkedBlockingQueue<Data> resultQueue;
    private String outputFile;

    public ResultWriterWorker(LinkedBlockingQueue<Data> resultQueue, String outputFile) {
        this.resultQueue = resultQueue;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(outputFile, true))
                    )
            );
            LOG.info("Opened the file at " + outputFile + " for writing the output");
            while (true) {
                Data data = resultQueue.poll();
                if (data == null) {
                    try {
                        Thread.sleep(WAIT_TIME_IN_MS);
                        continue;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
                try {
                    String json = JsonSerDe.toJson(data);
                    LOG.info(json);
                    writer.println(json);
                    writer.flush();
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        } catch (IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
            throw new RuntimeException(ioe);
        }
    }
}
