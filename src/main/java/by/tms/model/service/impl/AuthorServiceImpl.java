package by.tms.model.service.impl;

import by.tms.model.dao.AuthorDao;
import by.tms.model.dto.AuthorDto;
import by.tms.model.entity.Author;
import by.tms.model.service.AuthorService;
import by.tms.model.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
@Slf4j
public class AuthorServiceImpl extends GenericServiceImpl<AuthorDto, Long, Author> implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    public Optional<AuthorDto> findAuthorByFullName(String authorFullName) {
        Optional<Author> optionalAuthor = authorDao.findByFullName(authorFullName);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalAuthor, authorFullName);
        return Optional.ofNullable(mapper.mapToDto(optionalAuthor.orElse(null)));
    }
}
