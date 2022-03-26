package by.tms.model.service;

import by.tms.model.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<AuthorDto> findAllAuthor(int limit, int offset);

    Long addNewAuthor(AuthorDto authorDto);

    Optional<AuthorDto> findAuthorByFullName(String authorFullName);

    void updateAuthor(AuthorDto authorDto);

    void deleteAuthor(AuthorDto authorDto);

    boolean isAuthorExist(Long id);

    Optional<AuthorDto> findAuthorById(Long id);

    List<Long> getCountPages();
}
