package org.morriswa.eecs447.utility;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class AmazonS3ClientImpl implements AmazonS3Client {
    private final String FILE_DEST_PREFIX;
    private final String INTERNAL_FILE_CACHE_PATH;
    private final Environment e;
    private final AmazonS3 s3;
    private final String ACTIVE_BUCKET;

    @Autowired
    AmazonS3ClientImpl(Environment e) {
        this.e = e;
        this.s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        this.ACTIVE_BUCKET = e.getRequiredProperty("aws.s3.bucket");
        this.FILE_DEST_PREFIX = e.getRequiredProperty("aws.s3.apppath");
        this.INTERNAL_FILE_CACHE_PATH = e.getRequiredProperty("server.cache");
    }

    @Override
    public void uploadToS3(byte[] content, String destination) throws IOException {

        final UUID cachePath = UUID.randomUUID();

        File temp = new File(this.INTERNAL_FILE_CACHE_PATH + cachePath);

        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(temp));
        outputStream.write(content);

        if (!temp.exists())
            throw new FileSystemException("Failed to cache provided file for upload!");

        s3.putObject(new PutObjectRequest(ACTIVE_BUCKET,
                this.FILE_DEST_PREFIX+destination,
                temp));

        if (!temp.delete())
            throw new FileSystemException("Failed to delete cached file after upload!");
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
