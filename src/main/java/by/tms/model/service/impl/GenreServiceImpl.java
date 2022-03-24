package by.tms.model.service.impl;

import by.tms.model.dao.GenreDao;
import by.tms.model.dto.GenreDto;
import by.tms.model.entity.Genre;
import by.tms.model.mapper.impl.GenreMapper;
import by.tms.model.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;
    private final GenreMapper genreMapper = GenreMapper.getInstance();

    @Override
    public List<GenreDto> findAllGenre(int limit, int offset) {
        List<Genre> genres = genreDao.findAll(limit, offset);
        return genreMapper.mapToListDto(genres);
    }

    @Override
    @Transactional
    public Long addNewGenre(GenreDto genreDto) {
        return genreDao.save(genreMapper.mapToEntity(genreDto));
    }

    @Override
    public Optional<GenreDto> findGenreByName(String genreName) {
        Optional<Genre> optionalGenre = genreDao.findByName(genreName);
        return Optional.ofNullable(genreMapper.mapToDto(optionalGenre.orElse(null)));
    }

    @Override
    @Transactional
    public void updateGenre(GenreDto genreDto) {
        genreDao.update(genreMapper.mapToEntity(genreDto));
    }

    @Override
    @Transactional
    public void deleteGenre(GenreDto genreDto) {
        genreDao.delete(genreMapper.mapToEntity(genreDto));
    }

    @Override
    public boolean isGenreExist(Long id) {
        return genreDao.isExist(id);
    }

    @Override
    public Optional<GenreDto> findGenreById(Long id) {
        Optional<Genre> optionalGenre = genreDao.findById(id);
        return Optional.ofNullable(genreMapper.mapToDto(optionalGenre.orElse(null)));
    }
}
