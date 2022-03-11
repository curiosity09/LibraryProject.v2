package by.tms.dao;

import by.tms.entity.Author;

import java.util.Optional;

public interface AuthorDao extends GenericDao<Long, Author>{

    Optional<Author> findByFullName(String fullName);
}
