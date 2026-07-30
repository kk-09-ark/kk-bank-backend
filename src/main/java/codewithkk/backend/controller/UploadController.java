package codewithkk.backend.controller;

import codewithkk.backend.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload/pdf")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadPdf(file);
        return ResponseEntity.ok(Map.of(
                "message", "PDF uploaded successfully",
                "fileName", file.getOriginalFilename(),
                "url", url
        ));
    }

    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadImage(file);
        return ResponseEntity.ok(Map.of(
                "message", "Image uploaded successfully",
                "fileName", file.getOriginalFilename(),
                "url", url
        ));
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource resource = fileUploadService.getFile(filename);
        String contentType = "application/octet-stream";
        if (filename.toLowerCase().endsWith(".pdf")) {
            contentType = "application/pdf";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
