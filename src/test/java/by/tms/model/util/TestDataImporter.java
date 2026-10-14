package by.tms.model.util;

import by.tms.model.entity.Author;
import by.tms.model.entity.Book;
import by.tms.model.entity.Genre;
import by.tms.model.entity.Order;
import by.tms.model.entity.Section;
import by.tms.model.entity.user.Admin;
import by.tms.model.entity.user.Level;
import by.tms.model.entity.user.Librarian;
import by.tms.model.entity.user.User;
import by.tms.model.entity.user.UserData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Component("testDataImporter")
public class TestDataImporter {

    public static final int LIMIT_10 = 10;
    public static final int OFFSET_0 = 0;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void importTestData() {
        Section preSchoolLiteratureSection = saveSection("Дошкольная литература");
        Section spaceSection = saveSection("Космос");
        Section animalSection = saveSection("Животные");

        Genre detective = saveGenre("Детектив");
        Genre roman = saveGenre("Роман");
        saveGenre("Автобиография");

        Author akuninAuthor = saveAuthor("Борис Акунин");
        Author pelevinAuthor = saveAuthor("Виктор Пелевин");
        saveAuthor("Чарльз Буковски");

        Book azazelloBook = saveBook("Азазело", akuninAuthor, detective,
                5, animalSection, 1999);
        Book turkishGambitBook = saveBook("Турецкий гамбит", akuninAuthor, roman,
                2, spaceSection, 2003);
        Book generationP = saveBook("Generation P", pelevinAuthor, roman,
                4, preSchoolLiteratureSection, 1999);

        User user = saveUser("user", "pass", "Misha", "Ryzgunsky",
                "ryzgunsky.mihail@gmail.com", "+375445787918");
        User newUser = saveUser("newUser", "pass1", "Sasha", "Egorov",
                "ryzgunky.mihail@gmail.com", "+375445717918");
        saveAdmin("admin", "pass1", "Dima", "Morozov",
                "dima@mail.com", "+375445344587", Level.HEAD);
        saveAdmin("newAdmin", "pass", "Misha", "Basalay",
                "dma@mail.com", "+375445345287", Level.SECONDARY);
        saveLibrarian("newLib", "pass1", "Kirill", "Anasovich",
                "kiill@mail.ru", "+375445322487", Level.HEAD);
        saveLibrarian("lib", "pass", "Petya", "Lukoshko",
                "kirill@mail.ru", "+375445322457", Level.SECONDARY);

        saveOrder(user, List.of(azazelloBook, generationP), LocalDateTime.now().plusDays(10));
        saveOrder(newUser, List.of(azazelloBook, generationP, turkishGambitBook), LocalDateTime.now().plusDays(1));
        saveOrder(newUser, List.of(azazelloBook, turkishGambitBook), LocalDateTime.now().plusDays(2));
    }

    @Transactional
    public void cleanTestData() {
        entityManager.createQuery("delete from User").executeUpdate();
        entityManager.createQuery("delete from Librarian").executeUpdate();
        entityManager.createQuery("delete from Admin").executeUpdate();
        entityManager.createQuery("delete from Order").executeUpdate();
        entityManager.createQuery("delete from Author").executeUpdate();
        entityManager.createQuery("delete from Book").executeUpdate();
        entityManager.createQuery("delete from Section").executeUpdate();
        entityManager.createQuery("delete from Genre").executeUpdate();
    }

    private Section saveSection(String name) {
        Section section = Section.builder().name(name).build();
        entityManager.persist(section);
        return section;
    }

    private Genre saveGenre(String name) {
        Genre genre = Genre.builder().name(name).build();
        entityManager.persist(genre);
        return genre;
    }

    private Author saveAuthor(String fullName) {
        Author author = Author.builder().fullName(fullName).build();
        entityManager.persist(author);
        return author;
    }

    private Book saveBook(String name, Author author, Genre genre, int quantity, Section section, int year) {
        Book book = Book.builder().name(name).author(author).genre(genre).section(section)
                .quantity(quantity).publicationYear(year).build();
        entityManager.persist(book);
        return book;
    }

    private User saveUser(String username, String password, String name, String surname, String email, String phone) {
        User user = User.builder().username(username).password(password).role("user")
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).isBanned(false).build();
        entityManager.persist(user);
        return user;
    }

    private void saveAdmin(String username, String password, String name, String surname, String email, String phone, Level level) {
        Admin admin = Admin.builder().username(username).password(password)
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).adminLevel(level).build();
        entityManager.persist(admin);
    }

    private void saveLibrarian(String username, String password, String name, String surname, String email, String phone, Level level) {
        Librarian librarian = Librarian.builder().username(username).password(password)
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).libLevel(level).build();
        entityManager.persist(librarian);
    }

    private void saveOrder(User account, List<Book> books, LocalDateTime period) {
        Order order = Order.builder().account(account)
                .book(books).rentalTime(LocalDateTime.now()).rentalPeriod(period).build();
        entityManager.persist(order);
    }
}
