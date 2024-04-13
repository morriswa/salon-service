package org.morriswa.salon.utility;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class StreamToolsTest {

    @Test
    void testOutputToInput() throws Exception {

        final String testStr = "Hello Pipe World!";
        final byte[] testBytes = testStr.getBytes();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.writeBytes(testBytes);

        final InputStream inputStream = StreamTools.outputStreamToInput(outputStream);

        final byte[] inputStreamBytes = inputStream.readAllBytes();
        final String inputStreamStr = new String(inputStreamBytes);

        assertEquals("output bytes == input bytes", testBytes, inputStreamBytes);
        assertEquals("output string == input string", testStr, inputStreamStr);
    }
}
