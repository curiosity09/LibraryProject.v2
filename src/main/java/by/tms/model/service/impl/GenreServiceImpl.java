package by.tms.model.service.impl;

import by.tms.model.dao.GenreDao;
import by.tms.model.dto.GenreDto;
import by.tms.model.entity.Genre;
import by.tms.model.service.GenreService;
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
public class GenreServiceImpl extends GenericServiceImpl<GenreDto, Long, Genre> implements GenreService {

    private final GenreDao genreDao;

    @Override
    public Optional<GenreDto> findGenreByName(String genreName) {
        Optional<Genre> optionalGenre = genreDao.findByName(genreName);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalGenre, genreName);
        return Optional.ofNullable(mapper.mapToDto(optionalGenre.orElse(null)));
    }
}
