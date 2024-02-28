package org.morriswa.salon.utility;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * AUTHOR: William A. Morris
 * DATE CREATED: 2024-02-26
 * PURPOSE: Scales images using Java
 */

@Component
public class ImageScaleUtilImpl implements ImageScaleUtil {


    @Override
    public byte[] getScaledImage(MultipartFile imageRequest, int pxWidth, int pxHeight) throws IOException {

        BufferedImage retrievedImage = ImageIO.read(new ByteArrayInputStream(imageRequest.getBytes()));

        Image scaledImage = retrievedImage.getScaledInstance(
                pxWidth,
                pxHeight,
                Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(
                pxWidth,
                pxHeight,
                BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(scaledImage, 0, 0, null);

        assert imageRequest.getContentType() != null;

        // open bytestream
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        // write image to bytestream in original format
        ImageIO.write(outputImage, imageRequest.getContentType(), byteStream);
        // close bytestream
        byteStream.close();


        return byteStream.toByteArray();
    }

    @Override
    public byte[] getScaledImage(MultipartFile imageRequest, float scale) throws IOException {

        BufferedImage retrievedImage = ImageIO.read(new ByteArrayInputStream(imageRequest.getBytes()));

        final int pxWidth = (int) (retrievedImage.getWidth() * scale);
        final int pxHeight = (int) (retrievedImage.getHeight() * scale);

        Image scaledImage = retrievedImage.getScaledInstance(
                pxWidth,
                pxHeight,
                Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(
                pxWidth,
                pxHeight,
                BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(scaledImage, 0, 0, null);

        assert imageRequest.getContentType() != null;

        // open bytestream
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final String imageFormat = imageRequest.getContentType().substring(
                imageRequest.getContentType().indexOf("/") + 1
        );

        // write image to bytestream in original format
        ImageIO.write(outputImage, imageFormat, byteStream);
        // close bytestream
        byteStream.close();

        return byteStream.toByteArray();
    }
}
