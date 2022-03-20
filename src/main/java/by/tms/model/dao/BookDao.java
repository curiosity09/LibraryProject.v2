package by.tms.model.dao;

import by.tms.model.entity.Book;

import java.util.List;

public interface BookDao extends GenericDao<Long, Book>{

    List<Book> findByName(String name);
}
