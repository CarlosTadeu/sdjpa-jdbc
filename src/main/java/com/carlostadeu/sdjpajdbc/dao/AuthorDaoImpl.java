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
                        return getAuthorFromRS(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        final String SELECT = "INSERT INTO author (first_name, last_name) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
                ps.setString(1, author.getFirstName());
                ps.setString(2, author.getLastName());
                ps.execute();

                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
                    if (resultSet.next()) {
                        Long savedId = resultSet.getLong(1);
                        return this.getById(savedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Author updateAuthor(Author author) {
        final String SELECT = "UPDATE author SET first_name=?, last_name=? WHERE author.id=?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
                ps.setString(1, author.getFirstName());
                ps.setString(2, author.getLastName());
                ps.setLong(3, author.getId());
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        final String SELECT = "DELETE FROM author WHERE id=?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
                ps.setLong(1, id);
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Author getAuthorFromRS(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        return author;
    }
}
