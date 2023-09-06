package ru.antisessa.digitalLibrarySpringBoot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 3, max = 100, message = "Name should be between 10 and 100 characters")
    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Status status;

    @NotEmpty(message = "Author name should not be empty")
    @Size(min = 6, max = 100, message = "Author name should be between 6 and 100 characters")
    @Column(name = "author")
    private String author;

    @Column(name = "year_of_publication")
    private int yearOfPublication;

    @Column(name = "returndate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    @Column(name = "date_of_taking")
    @Temporal(TemporalType.DATE)
    private Date dateOfTaking;

    @Transient
    private boolean isExpire;

    public boolean isExpire(){
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        if(returnDate == null) {
            return false;
        } else {
            return timestamp.after(returnDate);
        }
    }

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    public Book(String name, String author, int yearOfPublication) {
        this.name = name;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.status = Status.Free;
        this.returnDate = null;
        this.owner = null;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", returnDate=" + returnDate +
                ", owner=" + owner +
                '}';
    }
}
