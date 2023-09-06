package ru.antisessa.digitalLibrarySpringBoot.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antisessa.digitalLibrarySpringBoot.models.Book;
import ru.antisessa.digitalLibrarySpringBoot.models.Person;
import ru.antisessa.digitalLibrarySpringBoot.models.Status;
import ru.antisessa.digitalLibrarySpringBoot.repositories.BooksRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("dd\\MM\\yyyy");

    private final BooksRepository booksRepository;

    @Autowired
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    //найти книги по названию
    public List<Book> findByName(String name){
        return booksRepository.findByName(name);
    }

    //найти книгу по началу названия
    public List<Book> findByNameStartingWith(String search) {
        if (Objects.equals(search, "")) {
            return null;
        } else {
            return booksRepository.findByNameStartingWithIgnoreCase(search);
        }
    }

    //найти книги по владельцу
    public List<Book> findByOwner(Person owner){
        return booksRepository.findByOwner(owner);
    }

    //найти книгу по дате издания
    public List<Book> findBookByDateOfPublicationEquals(int year){
        return booksRepository.findBookByYearOfPublicationEquals(year);
    }

    //найти книгу по первым буквам ФИО автора
    public List<Book> findBookByAuthorStartingWith(String startingWith){
        return booksRepository.findBookByAuthorStartingWith(startingWith);
    }

    //найти книгу по статусу
    public List<Book> findBookByStatusEquals(Status status){
        return booksRepository.findBookByStatusEquals(status);
    }

    // CRUD - поиск, сохранение, изменение, удаление
    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAllWithParam(int page, int itemsPerPage, boolean sort_by_year) {
        if (itemsPerPage == 0 && !sort_by_year) {
            return booksRepository.findAll();
        } else if (itemsPerPage == 0 && sort_by_year) {
            return booksRepository.findAll(Sort.by("yearOfPublication"));
        } else if (itemsPerPage != 0 && !sort_by_year) {
            return booksRepository.findAll(PageRequest.of(page-1, itemsPerPage)).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page-1, itemsPerPage,
                    Sort.by("yearOfPublication"))).getContent();
        }
    }

    @Transactional
    public void save(Book book) {
        book.setStatus(Status.Free);
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void setOwner(int book_id, Person person){
        Book book = booksRepository.findById(book_id).orElse(null);

        if(book != null){
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            book.setOwner(person);
            book.setStatus(Status.Busy);

            book.setDateOfTaking(date);

            Timestamp returnTimestamp = new Timestamp(timestamp.getTime() + 60000);
            book.setReturnDate(returnTimestamp);

            booksRepository.save(book);
        }
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public void deleteOwner(int id){
        Optional<Book> foundBook = booksRepository.findById(id);
        Book book = foundBook.orElse(null);

        if(book != null){
            book.setOwner(null);
            book.setStatus(Status.Free);
            book.setReturnDate(null);
            book.setDateOfTaking(null);
        }
    }
}
