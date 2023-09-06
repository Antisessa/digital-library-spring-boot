package ru.antisessa.digitalLibrarySpringBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.antisessa.digitalLibrarySpringBoot.models.Person;

import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    //Поиск людей по имени
    List<Person> findByName(String name);

    //Поиск людей по имени, отсортировав результат поиска по возрасту
    List<Person> findByNameOrderByAge(String name);

    //Поиск людей по соответствию начала имени со строкой
    List<Person> findByNameStartingWith(String startingWith);
}
