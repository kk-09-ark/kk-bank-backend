package codewithkk.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${app.upload.dir:uploads/pdfs}")
    private String uploadDir;

    @Autowired(required = false)
    private Cloudinary cloudinary;

    private Path uploadPath;
    private boolean useCloudinary;

    @PostConstruct
    public void init() {
        useCloudinary = cloudinary != null;
        if (!useCloudinary) {
            uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory: " + uploadPath, e);
            }
        }
    }

    public String uploadPdf(MultipartFile file) {
        if (useCloudinary) {
            return cloudinaryUpload(file, "raw");
        }
        return localUpload(file);
    }

    public String uploadImage(MultipartFile file) {
        if (useCloudinary) {
            return cloudinaryUpload(file, "image");
        }
        return localUpload(file);
    }

    private String cloudinaryUpload(MultipartFile file, String resourceType) {
        try {
            Map<String, Object> params = ObjectUtils.asMap(
                "resource_type", resourceType,
                "folder", "codewithkk",
                "use_filename", true,
                "unique_filename", true
            );
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), params);
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary upload failed", e);
        }
    }

    private String localUpload(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String uniqueName = UUID.randomUUID().toString() + extension;
            Path targetPath = uploadPath.resolve(uniqueName);
            file.transferTo(targetPath.toFile());
            return "/api/files/" + uniqueName;
        } catch (IOException e) {
            throw new RuntimeException("Local upload failed", e);
        }
    }

    public Resource getFile(String filename) {
        try {
            Path filePath = uploadPath.resolve(filename).normalize();
            Resource resource = new FileSystemResource(filePath);
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new RuntimeException("File not found: " + filename);
        } catch (Exception e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
    }
}
