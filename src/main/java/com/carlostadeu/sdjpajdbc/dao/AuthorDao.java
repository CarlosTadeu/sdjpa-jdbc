package com.carlostadeu.sdjpajdbc.dao;

import com.carlostadeu.sdjpajdbc.domain.Author;

public interface AuthorDao {

    Author getById(Long id);
    Author findAuthorByName(String firstName, String lastName);
}
