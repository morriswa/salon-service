package org.morriswa.salon.utility;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

@Component
public class AmazonS3ClientImpl implements AmazonS3Client {
    private final AmazonS3 s3;

    private final String ACTIVE_BUCKET;
    private final String FILE_DEST_PREFIX;

    @Autowired
    AmazonS3ClientImpl(Environment e) {
        Logger log = LoggerFactory.getLogger(AmazonS3Client.class);
        this.s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        this.ACTIVE_BUCKET = e.getRequiredProperty("aws.s3.bucket");
        this.FILE_DEST_PREFIX = e.getRequiredProperty("aws.s3.apppath");
        try { // attempt to access test file within S3 bucket
            s3.doesObjectExist(this.ACTIVE_BUCKET, "eecs447/hello-world.txt");
            log.info("Successfully started Amazon S3 Client!");
        } catch (Exception ex) { // if file is inaccessible, S3 client has not been properly configured
            // report error
            log.error("Could not create Amazon S3 Client, shutting down. Please double check your AWS config.");
            // and shutdown application
            throw new UnsatisfiedDependencyException(
                    this.getClass().getCanonicalName(),
                    "amazonS3ClientImpl",
                    this.s3.getClass().getCanonicalName(),
                    ex.getMessage());
        }
    }

    @Override
    public void uploadToS3(OutputStream content, String contentType, String destination) throws IOException, InterruptedException {

        final ByteArrayOutputStream outputStream = (ByteArrayOutputStream) content;
        final int contentLength = outputStream.size();

        final ObjectMetadata imageInfo; {
            var info = new ObjectMetadata();
            info.setContentLength(contentLength);
            info.setContentType(contentType);
            imageInfo = info;
        }

        // connect the pipes
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);

        // create a new thread to write output to input stream
        final var myThread = new Thread(() -> {
            try {
                outputStream.writeTo(out);
                out.close();
            } catch (IOException iox) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(iox);
            }
        });

        // start the thread
        myThread.start();

        // create input stream from output
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(in.readAllBytes());

        s3.putObject(new PutObjectRequest(ACTIVE_BUCKET,
                this.FILE_DEST_PREFIX+destination,
                inputStream, imageInfo));
    }

    @Override
    public boolean doesObjectExist(String path) {
        boolean objectExists;
        try {
            objectExists = s3.doesObjectExist(this.ACTIVE_BUCKET, this.FILE_DEST_PREFIX+path);
        } catch (Exception e) {
            objectExists = false;
        }

        return objectExists;
    }

    @Override
    public URL getSignedObjectUrl(String path, int expirationMinutes) {

        Date expiration = new Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000L * 60 * expirationMinutes;
        expiration.setTime(expTimeMillis);

        return s3.generatePresignedUrl(
                new GeneratePresignedUrlRequest(
                        ACTIVE_BUCKET,
                        this.FILE_DEST_PREFIX+path)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration));
    }

    @Override
    public void deleteObject(String path) {
        s3.deleteObject(new DeleteObjectRequest(ACTIVE_BUCKET, this.FILE_DEST_PREFIX+path));
    }
}
