package org.morriswa.salon.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * AUTHOR: William A. Morris
 * DATE CREATED: 2024-02-26
 * PURPOSE: Provides an easy Interface to scale images
 */
public interface ImageScaleUtil {

    /**
     *
     *
     * @param imageRequest
     * @param pxWidth
     * @param pxHeight
     * @return
     * @throws IOException
     */
    byte[] getScaledImage(MultipartFile imageRequest, int pxWidth, int pxHeight) throws IOException;

    /**
     *
     *
     * @param imageRequest
     * @param scale
     * @return
     * @throws IOException
     */
    byte[] getScaledImage(MultipartFile imageRequest, float scale) throws IOException;
}
