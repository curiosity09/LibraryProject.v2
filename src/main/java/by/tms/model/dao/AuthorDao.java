package by.tms.model.dao;

import by.tms.model.entity.Author;

import java.util.Optional;

public interface AuthorDao extends GenericDao<Long, Author>{

    Optional<Author> findByFullName(String fullName);
}
