package by.tms.model.service.impl;

import by.tms.model.dao.AuthorDao;
import by.tms.model.dto.AuthorDto;
import by.tms.model.entity.Author;
import by.tms.model.mapper.impl.AuthorMapper;
import by.tms.model.service.AuthorService;
import by.tms.model.util.LoggerUtil;
import by.tms.model.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;
    private final AuthorMapper authorMapper = AuthorMapper.getInstance();

    @Override
    public List<AuthorDto> findAllAuthor(int limit, int offset) {
        List<Author> authors = authorDao.findAll(limit, offset);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE, authors);
        return authorMapper.mapToListDto(authors);
    }

    @Override
    @Transactional
    public Long addNewAuthor(AuthorDto authorDto) {
        Long saveAuthor = authorDao.save(authorMapper.mapToEntity(authorDto));
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_SERVICE, saveAuthor);
        return saveAuthor;
    }

    @Override
    public Optional<AuthorDto> findAuthorByFullName(String authorFullName) {
        Optional<Author> optionalAuthor = authorDao.findByFullName(authorFullName);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalAuthor, authorFullName);
        return Optional.ofNullable(authorMapper.mapToDto(optionalAuthor.orElse(null)));
    }

    @Override
    @Transactional
    public void updateAuthor(AuthorDto authorDto) {
        authorDao.update(authorMapper.mapToEntity(authorDto));
        log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_SERVICE, authorDto);
    }

    @Override
    @Transactional
    public void deleteAuthor(AuthorDto authorDto) {
        authorDao.delete(authorMapper.mapToEntity(authorDto));
        log.debug(LoggerUtil.ENTITY_WAS_DELETED_IN_SERVICE, authorDto);
    }

    @Override
    public boolean isAuthorExist(Long id) {
        boolean exist = authorDao.isExist(id);
        log.debug(LoggerUtil.ENTITY_IS_EXIST_IN_SERVICE_BY, exist, id);
        return exist;
    }

    @Override
    public Optional<AuthorDto> findAuthorById(Long id) {
        Optional<Author> optionalAuthor = authorDao.findById(id);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalAuthor, id);
        return Optional.ofNullable(authorMapper.mapToDto(optionalAuthor.orElse(null)));
    }

    @Override
    public List<Long> getCountPages() {
        Long countRow = authorDao.getCountRow();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_SERVICE, countRow);
        return ServiceUtil.collectPages(countRow);
    }
}
