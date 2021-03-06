package kr.hs.entrydsm.service.Image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.hs.entrydsm.payload.response.ImageResponse;
import kr.hs.entrydsm.service.Image.AdminImageService;
import kr.hs.entrydsm.service.exception.InvalidFileExtensionException;
import kr.hs.entrydsm.exception.BadRequestException;
import kr.hs.entrydsm.security.AuthMiddleware;
import kr.hs.entrydsm.enitity.Image;
import kr.hs.entrydsm.enitity.repository.AdminImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AdminImageServiceImpl implements AdminImageService {
    private final AdminImageRepository adminImageRepository;

    private final AmazonS3 s3;

    private final ArrayList<String> allowedExtensions = new ArrayList<>(Arrays.asList(".JPG", ".JPEG", ".HEIC", ".PNG"));

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Override
    public ImageResponse createImage(Optional<MultipartFile> file) throws IOException {
        MultipartFile parsedFile = file.orElseThrow(BadRequestException::new);

        String originalFilename = parsedFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + ext;

        if (allowedExtensions.stream().noneMatch(e -> e.equals(ext.toUpperCase()))) { throw new InvalidFileExtensionException(); }

        s3.putObject(new PutObjectRequest(bucket, "images/" + filename, parsedFile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.AuthenticatedRead));

        Image image = adminImageRepository.save(Image.builder()
                .club(AuthMiddleware.currentClub())
                .path("https://" + bucket + "/images/" + filename)
                .build());

        return ImageResponse.builder().imagePath(image.getPath()).imageId(image.getId()).build();
    }
}
