package com.carlostadeu.sdjpajdbc.dao;

import com.carlostadeu.sdjpajdbc.domain.Book;

public interface BookDao {

    Book getById(Long id);

    Book findBookByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);
}
