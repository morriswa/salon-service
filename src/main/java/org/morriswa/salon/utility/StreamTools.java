package org.morriswa.salon.utility;

import java.io.*;

public class StreamTools {

    public static InputStream outputStreamToInput(ByteArrayOutputStream outputStream) throws Exception {

        // connect the pipes
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);

        // create a new thread to write output to input stream
        final var writer = new Thread(() -> {
            try {
                outputStream.writeTo(out);
                outputStream.close();
                out.close();
            } catch (IOException iox) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(iox);
            }
        });

        // start the thread
        writer.start();

        // create input stream from output
        return new ByteArrayInputStream(in.readAllBytes());
    }

}
