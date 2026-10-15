package by.tms.web.rest;

import by.tms.model.dto.FileDto;
import by.tms.model.service.FilesStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * File storage endpoints (upload / download / list / delete) used by the librarian area.
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('librarian','admin')")
@Slf4j
public class FileRestController {

    private static final String DOWNLOAD_PREFIX = "/api/files/";

    private final FilesStorageService filesStorageService;

    @GetMapping
    public List<FileDto> list() {
        return filesStorageService.loadAll()
                .map(path -> {
                    String filename = path.getFileName().toString();
                    return new FileDto(filename, DOWNLOAD_PREFIX + filename);
                })
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileDto upload(@RequestParam("file") MultipartFile file) {
        filesStorageService.save(file);
        log.debug("File {} uploaded", file.getOriginalFilename());
        return new FileDto(file.getOriginalFilename(), DOWNLOAD_PREFIX + file.getOriginalFilename());
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        Resource resource = filesStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{filename:.+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String filename) {
        if (!filesStorageService.delete(filename)) {
            throw new NoSuchElementException("File not found: " + filename);
        }
        log.debug("File {} deleted", filename);
    }
}
