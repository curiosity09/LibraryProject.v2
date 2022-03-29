package by.tms.model.service.impl;

import by.tms.model.dao.GenreDao;
import by.tms.model.dto.GenreDto;
import by.tms.model.entity.Genre;
import by.tms.model.mapper.impl.GenreMapper;
import by.tms.model.service.GenreService;
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
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;
    private final GenreMapper genreMapper = GenreMapper.getInstance();

    @Override
    public List<GenreDto> findAllGenre(int limit, int offset) {
        List<Genre> genres = genreDao.findAll(limit, offset);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE, genres);
        return genreMapper.mapToListDto(genres);
    }

    @Override
    @Transactional
    public Long addNewGenre(GenreDto genreDto) {
        Long saveGenre = genreDao.save(genreMapper.mapToEntity(genreDto));
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_SERVICE, saveGenre);
        return saveGenre;
    }

    @Override
    public Optional<GenreDto> findGenreByName(String genreName) {
        Optional<Genre> optionalGenre = genreDao.findByName(genreName);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalGenre, genreName);
        return Optional.ofNullable(genreMapper.mapToDto(optionalGenre.orElse(null)));
    }

    @Override
    @Transactional
    public void updateGenre(GenreDto genreDto) {
        genreDao.update(genreMapper.mapToEntity(genreDto));
        log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_SERVICE, genreDto);
    }

    @Override
    @Transactional
    public void deleteGenre(GenreDto genreDto) {
        genreDao.delete(genreMapper.mapToEntity(genreDto));
        log.debug(LoggerUtil.ENTITY_WAS_DELETED_IN_SERVICE, genreDto);
    }

    @Override
    public boolean isGenreExist(Long id) {
        boolean exist = genreDao.isExist(id);
        log.debug(LoggerUtil.ENTITY_IS_EXIST_IN_SERVICE_BY, exist, id);
        return exist;
    }

    @Override
    public Optional<GenreDto> findGenreById(Long id) {
        Optional<Genre> optionalGenre = genreDao.findById(id);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalGenre, id);
        return Optional.ofNullable(genreMapper.mapToDto(optionalGenre.orElse(null)));
    }

    @Override
    public List<Long> getCountPages() {
        Long countRow = genreDao.getCountRow();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_SERVICE, countRow);
        return ServiceUtil.collectPages(countRow);
    }
}
