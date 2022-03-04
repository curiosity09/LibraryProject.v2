package by.tms.dao;

import by.tms.entity.Book;
import org.hibernate.Session;

import java.util.List;

public interface BookDao extends GenericDao<Book>{

    List<Book> findByName(Session session, String name);
}
