package org.morriswa.salon.validation;

import org.morriswa.salon.exception.ValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageValidator {

    public static final List<String> permittedContentTypes = List.of("image/jpg","image/jpeg","image/png");

    public static void validateUploadedImage(MultipartFile image) throws ValidationException {
        ValidationException ve = new ValidationException();

        if (image.getContentType()==null) ve.addValidationError("image/content-type", true, null,
            "Image File MUST specify a Content Type!");
        else if (!permittedContentTypes.contains(image.getContentType().toLowerCase()))
            ve.addValidationError("image/content-type", true, image.getContentType(),
                "Image File MUST be of type jpg OR png...");

        if (ve.containsErrors()) throw ve;
    }
}
