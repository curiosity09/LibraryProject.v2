package by.tms.model.dto;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class FileDto implements Serializable {

    private String fileName;
    private String url;

    public FileDto(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
