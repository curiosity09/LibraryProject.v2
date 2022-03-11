package by.tms.service.impl;

import by.tms.dao.AuthorDao;
import by.tms.dto.AuthorDto;
import by.tms.entity.Author;
import by.tms.mapper.impl.AuthorMapper;
import by.tms.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;
    private final AuthorMapper authorMapper = AuthorMapper.getInstance();

    @Override
    public List<AuthorDto> findAllAuthor() {
        List<Author> authors = authorDao.findAll();
        return authorMapper.mapToListDto(authors);
    }

    @Transactional
    @Override
    public Long addNewAuthor(Author author) {
        return authorDao.save(author);
    }

    @Override
    public Optional<AuthorDto> findAuthorByFullName(String authorFullName) {
        Optional<Author> optionalAuthor = authorDao.findByFullName(authorFullName);
        return Optional.ofNullable(authorMapper.mapToDto(optionalAuthor.orElse(null)));
    }

    @Transactional
    @Override
    public void updateAuthor(Author author) {
        authorDao.update(author);
    }

    @Transactional
    @Override
    public void deleteAuthor(Author author) {
        authorDao.delete(author);
    }

    @Override
    public boolean isAuthorExist(Long id) {
        return authorDao.isExist(id);
    }

    @Override
    public Optional<AuthorDto> findAuthorById(Long id) {
        Optional<Author> optionalAuthor = authorDao.findById(id);
        return Optional.ofNullable(authorMapper.mapToDto(optionalAuthor.orElse(null)));
    }
}
