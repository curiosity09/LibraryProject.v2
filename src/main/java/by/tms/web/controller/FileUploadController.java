package by.tms.web.controller;

import by.tms.model.dto.FileDto;
import by.tms.model.service.FilesStorageService;
import by.tms.web.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static by.tms.web.util.PageUtil.REDIRECT;

@Controller
public class FileUploadController {

    private FilesStorageService filesStorageService;

    @Autowired
    public void setFilesStorageService(FilesStorageService filesStorageService) {
        this.filesStorageService = filesStorageService;
    }

    @GetMapping("/librarian/files")
    public String getListFiles(Model model) {
        List<FileDto> fileInfos = filesStorageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileDto(filename, url);
        }).collect(Collectors.toList());

        model.addAttribute("files", fileInfos);

        return PageUtil.LIBRARIAN_PREFIX + "files";
    }

    @GetMapping("/librarian/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = filesStorageService.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/librarian/upload")
    public String uploadFile(Model model, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (Objects.nonNull(file)) {
            String message = "";

            try {
                filesStorageService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                model.addAttribute("message", message);
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
                model.addAttribute("message", message);
            }
        }
        return REDIRECT + "librarianPage";
    }

    @GetMapping("/librarian/files/delete/{filename:.+}")
    public String deleteFile(@PathVariable String filename, Model model, RedirectAttributes redirectAttributes) {
        try {
            boolean existed = filesStorageService.delete(filename);

            if (existed) {
                redirectAttributes.addFlashAttribute("message", "Delete the file successfully: " + filename);
            } else {
                redirectAttributes.addFlashAttribute("message", "The file does not exist!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    "Could not delete the file: " + filename + ". Error: " + e.getMessage());
        }

        return REDIRECT + "librarian/files";
    }
}
