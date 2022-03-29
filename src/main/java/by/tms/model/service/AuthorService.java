package by.tms.model.service;

import by.tms.model.dto.AuthorDto;
import by.tms.model.entity.Author;

import java.util.Optional;

public interface AuthorService extends GenericService<AuthorDto, Long, Author>{

    Optional<AuthorDto> findAuthorByFullName(String authorFullName);
}
