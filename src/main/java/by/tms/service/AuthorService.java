package by.tms.service;

import by.tms.dto.AuthorDto;
import by.tms.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<AuthorDto> findAllAuthor();

    Long addNewAuthor(Author author);

    Optional<AuthorDto> findAuthorByFullName(String authorFullName);

    void updateAuthor(Author author);

    void deleteAuthor(Author author);

    boolean isAuthorExist(Long id);

    Optional<AuthorDto> findAuthorById(Long id);
}
