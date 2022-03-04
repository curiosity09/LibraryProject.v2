package by.tms.dao;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {

    List<T> findAll(Session session);

    void add(Session session, T t);

    void update(Session session, T t);

    void delete(Session session, T t);

    Optional<T> findById(Session session, Long id);
}

