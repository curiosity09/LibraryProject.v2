package by.tms.model.service.impl;

import by.tms.model.dao.AuthorDao;
import by.tms.model.dto.AuthorDto;
import by.tms.model.entity.Author;
import by.tms.model.mapper.impl.AuthorMapper;
import by.tms.model.service.AuthorService;
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

    @Override
    @Transactional
    public Long addNewAuthor(AuthorDto authorDto) {
        return authorDao.save(authorMapper.mapToEntity(authorDto));
    }

    @Override
    public Optional<AuthorDto> findAuthorByFullName(String authorFullName) {
        Optional<Author> optionalAuthor = authorDao.findByFullName(authorFullName);
        return Optional.ofNullable(authorMapper.mapToDto(optionalAuthor.orElse(null)));
    }

    @Override
    @Transactional
    public void updateAuthor(AuthorDto authorDto) {
        authorDao.update(authorMapper.mapToEntity(authorDto));
    }

    @Override
    @Transactional
    public void deleteAuthor(AuthorDto authorDto) {
        authorDao.delete(authorMapper.mapToEntity(authorDto));
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
