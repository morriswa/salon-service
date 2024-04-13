package org.morriswa.salon.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * interacts with Amazon S3 bucket to store and retrieve application content
 *
 * @author William A. Morris
 * @since 2024-01-26
 */
public interface AmazonS3Client {

    void uploadToS3(OutputStream content, String contentType, String destination) throws Exception;

    boolean doesObjectExist(String path);

    URL getSignedObjectUrl(String path, int expirationMinutes);

    void deleteObject(String path);
}
