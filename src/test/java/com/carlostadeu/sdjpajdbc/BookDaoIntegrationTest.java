package com.carlostadeu.sdjpajdbc;

import com.carlostadeu.sdjpajdbc.dao.BookDao;
import com.carlostadeu.sdjpajdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"com.carlostadeu.sdjpajdbc.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoIntegrationTest {

    @Autowired
    BookDao bookDao;

    @Test
    void testGetAuthor() {
        Book book = bookDao.getById(1L);
        assertThat(book).isNotNull();
    }

    @Test
    void testFindBookByTitle() {
        Book book = bookDao.findBookByTitle("Domain-Driven Design");

        assertThat(book).isNotNull();
        assertThat(book.getIsbn()).isEqualTo("978-0321125217");
        assertThat(book.getPublisher()).isEqualTo("Addison Wesley");
        assertThat(book.getTitle()).isEqualTo("Domain-Driven Design");
        assertThat(book.getAuthorId()).isEqualTo(2);
    }

    @Test
    void testSaveNewBook() {
        Book book = new Book("Cálculo", "132-321", "Fictício", 4L);

        Book saved = bookDao.saveNewBook(book);
        assertThat(saved).isNotNull();
    }

    @Test
    void testUpdateBook() {
        Book book = new Book("Linguagem C", "987-789", "Fictício", 4L);
        Book saved = bookDao.saveNewBook(book);

        saved.setPublisher("Pearson");
        Book updated = bookDao.updateBook(saved);

        assertThat(updated.getPublisher()).isEqualTo("Pearson");
    }

    @Test
    void testDeleteBook() {
        Book book = new Book("Linguagem Python", "456-654", "Novatec", 4L);
        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        Book deleted = bookDao.getById(saved.getId());
        assertThat(deleted).isNull();
    }
}
