package org.morriswa.salon.utility;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * AUTHOR: William A. Morris
 * DATE CREATED: 2024-02-26
 * PURPOSE: Scales images using Java
 */

@Component
public class ImageScaleUtilImpl implements ImageScaleUtil {

    private OutputStream getScaledImage(BufferedImage originalImage, int pxWidth, int pxHeight, String contentType) throws IOException {

        Image scaledImage = originalImage.getScaledInstance(
                pxWidth,
                pxHeight,
                Image.SCALE_FAST);
        BufferedImage outputImage = new BufferedImage(
                pxWidth,
                pxHeight,
                BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(scaledImage, 0, 0, null);

        assert contentType != null;
        final String imageFormat = contentType.substring(
                contentType.indexOf("/") + 1
        );

        // open bytestream
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        // write image to bytestream in original format
        ImageIO.write(outputImage, imageFormat, byteStream);
        // close bytestream
        byteStream.close();

        return byteStream;
    }

    @Override
    public OutputStream getScaledImage(MultipartFile imageRequest, int pxWidth, int pxHeight) throws IOException {

        BufferedImage retrievedImage = ImageIO.read(imageRequest.getInputStream());

        final String contentType = imageRequest.getContentType();

        return getScaledImage(retrievedImage, pxWidth, pxHeight, contentType);
    }

    @Override
    public OutputStream getScaledImage(MultipartFile imageRequest, float scale) throws IOException {

        BufferedImage retrievedImage = ImageIO.read(imageRequest.getInputStream());

        final int pxWidth = (int) (retrievedImage.getWidth() * scale);
        final int pxHeight = (int) (retrievedImage.getHeight() * scale);
        final String contentType = imageRequest.getContentType();

        return getScaledImage(retrievedImage, pxWidth, pxHeight, contentType);
    }
}
