package com.carlostadeu.sdjpajdbc.dao;

import com.carlostadeu.sdjpajdbc.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource dataSource;

    public AuthorDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Author getById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM author where id = ?")) {
                ps.setLong(1, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        Author author = new Author();
                        author.setId(id);
                        author.setFirstName(resultSet.getString("first_name"));
                        author.setLastName(resultSet.getString("last_name"));
                        return author;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        final String SELECT = "SELECT * FROM author where author.first_name like ? and author.last_name like ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        Author author = new Author();
                        author.setId(resultSet.getLong("id"));
                        author.setFirstName(resultSet.getString("first_name"));
                        author.setLastName(resultSet.getString("last_name"));
                        return author;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
