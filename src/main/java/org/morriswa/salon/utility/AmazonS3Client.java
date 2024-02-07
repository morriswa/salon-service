package org.morriswa.salon.utility;

import java.io.IOException;
import java.net.URL;

public interface AmazonS3Client {

    void uploadToS3(byte[] content, String destination) throws IOException;

    boolean doesObjectExist(String path);

    URL getSignedObjectUrl(String path, int expirationMinutes);

    void deleteObject(String path);
}
