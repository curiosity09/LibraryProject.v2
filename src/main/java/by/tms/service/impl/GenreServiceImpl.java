package by.tms.service.impl;

import by.tms.dao.GenreDao;
import by.tms.dto.GenreDto;
import by.tms.entity.Genre;
import by.tms.mapper.impl.GenreMapper;
import by.tms.service.GenreService;
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
    public List<GenreDto> findAllGenre() {
        List<Genre> genres = genreDao.findAll();
        return genreMapper.mapToListDto(genres);
    }

    @Override
    @Transactional
    public Long addNewGenre(Genre genre) {
        return genreDao.save(genre);
    }

    @Override
    public Optional<GenreDto> findGenreByName(String genreName) {
        Optional<Genre> optionalGenre = genreDao.findByName(genreName);
        return Optional.ofNullable(genreMapper.mapToDto(optionalGenre.orElse(null)));
    }

    @Override
    @Transactional
    public void updateGenre(Genre genre) {
        genreDao.update(genre);
    }

    @Override
    @Transactional
    public void deleteGenre(Genre genre) {
        genreDao.delete(genre);
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
