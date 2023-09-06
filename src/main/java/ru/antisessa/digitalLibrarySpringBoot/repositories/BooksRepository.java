package ru.antisessa.digitalLibrarySpringBoot.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.antisessa.digitalLibrarySpringBoot.models.Book;
import ru.antisessa.digitalLibrarySpringBoot.models.Person;
import ru.antisessa.digitalLibrarySpringBoot.models.Status;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByName(String name);

    List<Book> findByNameStartingWithIgnoreCase(String search);

    List<Book> findByOwner(Person owner);

    List<Book> findBookByYearOfPublicationEquals(int year);

    List<Book> findBookByAuthorStartingWith(String startingWith);

    List<Book> findBookByStatusEquals(Status status);
}
