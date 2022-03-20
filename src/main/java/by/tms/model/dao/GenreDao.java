package by.tms.model.dao;

import by.tms.model.entity.Genre;

import java.util.Optional;

public interface GenreDao extends GenericDao<Long, Genre>{

    Optional<Genre> findByName(String name);
}
