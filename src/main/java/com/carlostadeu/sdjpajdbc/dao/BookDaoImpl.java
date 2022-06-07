package com.carlostadeu.sdjpajdbc.dao;

import com.carlostadeu.sdjpajdbc.domain.Book;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class BookDaoImpl implements BookDao {

    private final DataSource dataSource;

    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Book getBookFromRS(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthorId(resultSet.getLong("author_id"));
        return book;
    }

    @Override
    public Book getById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM book where id=?")) {
                ps.setLong(1, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return getBookFromRS(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book findBookByTitle(String title) {
        final String SELECT = "SELECT * FROM book where book.title like ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
                ps.setString(1, title);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return getBookFromRS(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book saveNewBook(Book book) {
        final String SELECT = "INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
                ps.setString(1, book.getIsbn());
                ps.setString(2, book.getPublisher());
                ps.setString(3, book.getTitle());
                ps.setLong(4, book.getAuthorId());
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
    public Book updateBook(Book book) {
        final String SELECT = "UPDATE book SET isbn=?, publisher=?, title=?, author_id=? WHERE book.id=?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
                ps.setString(1, book.getIsbn());
                ps.setString(2, book.getPublisher());
                ps.setString(3, book.getTitle());
                ps.setLong(4, book.getAuthorId());
                ps.setLong(5, book.getId());
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        final String SELECT = "DELETE FROM book WHERE id=?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT)) {
                ps.setLong(1, id);
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
