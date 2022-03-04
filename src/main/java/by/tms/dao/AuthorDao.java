package by.tms.dao;

import by.tms.entity.Author;
import org.hibernate.Session;

import java.util.Optional;

public interface AuthorDao extends GenericDao<Author>{

    Optional<Author> findByFullName(Session session, String fullName);
}
