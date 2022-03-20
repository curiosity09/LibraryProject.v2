package by.tms.dao;

import by.tms.entity.Genre;

import java.util.Optional;

public interface GenreDao extends GenericDao<Long, Genre>{

    Optional<Genre> findByName(String name);
}
