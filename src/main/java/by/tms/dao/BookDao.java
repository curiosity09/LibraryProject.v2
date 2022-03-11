package by.tms.dao;

import by.tms.entity.Book;

import java.util.List;

public interface BookDao extends GenericDao<Long, Book>{

    List<Book> findByName(String name);
}
