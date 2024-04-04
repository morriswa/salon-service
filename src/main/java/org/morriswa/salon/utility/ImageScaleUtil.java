package org.morriswa.salon.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Provides an easy Interface to scale images
 *
 * @author William A. Morris
 * @since 2024-02-26
 */
public interface ImageScaleUtil {

    /**
     * Scales a provided image
     *
     * @param imageRequest the file containing the image to scale
     * @param pxWidth output width
     * @param pxHeight output height
     * @return a byte array containing the scaled image
     * @throws IOException if the image could not be scaled
     */
    OutputStream getScaledImage(MultipartFile imageRequest, int pxWidth, int pxHeight) throws IOException;

    /**
     * Scales a provided image
     *
     * @param imageRequest the file containing the image to scale
     * @param scale to scale the image by
     * @return a byte array containing the scaled image
     * @throws IOException if the image could not be scaled
     */
    OutputStream getScaledImage(MultipartFile imageRequest, float scale) throws IOException;
}
