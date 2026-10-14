package by.tms.model.config;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Seeds demo data into the in-memory H2 database on startup (profile "test" only),
 * because H2 in-mem loses all data every restart and TestDataImporter lives in test sources only.
 */
@Slf4j
@Component
@Profile("test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Long accounts = entityManager
                .createQuery("select count(a) from Account a", Long.class)
                .getSingleResult();
        if (accounts != null && accounts > 0) {
            log.info("Database already contains {} accounts, skipping data initialization", accounts);
            return;
        }

        log.info("Empty database detected - seeding demo data into the current datasource");

        Section preschool = saveSection("Дошкольная литература");
        Section space = saveSection("Космос");
        Section animals = saveSection("Животные");

        Genre detective = saveGenre("Детектив");
        Genre roman = saveGenre("Роман");
        saveGenre("Автобиография");

        Author akunin = saveAuthor("Борис Акунин");
        Author pelevin = saveAuthor("Виктор Пелевин");
        saveAuthor("Чарльз Буковски");

        Book azazello = saveBook("Азазело", akunin, detective, 5, animals, 1999);
        Book gambit = saveBook("Турецкий гамбит", akunin, roman, 2, space, 2003);
        Book generationP = saveBook("Generation P", pelevin, roman, 4, preschool, 1999);

        User user = saveUser("user", "pass", "Misha", "Ryzgunsky",
                "ryzgunsky.mihail@gmail.com", "+375445787918");
        saveUser("newUser", "pass", "Sasha", "Egorov",
                "ryzgunky.mihail@gmail.com", "+375445717918");
        saveAdmin("admin", "pass", "Dima", "Morozov", "dima@mail.com", "+375445344587", Level.HEAD);
        saveAdmin("newAdmin", "pass", "Misha", "Basalay", "dma@mail.com", "+375445345287", Level.SECONDARY);
        saveLibrarian("newLib", "pass", "Kirill", "Anasovich", "kiill@mail.ru", "+375445322487", Level.HEAD);
        saveLibrarian("lib", "pass", "Petya", "Lukoshko", "kirill@mail.ru", "+375445322457", Level.SECONDARY);

        saveOrder(user, List.of(azazello, generationP), LocalDateTime.now().plusDays(10));
        saveOrder(user, List.of(azazello, gambit), LocalDateTime.now().plusDays(30));

        log.info("Demo data seeded. Logins: user/pass, lib/pass, admin/pass");
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

    private User saveUser(String username, String rawPassword, String name, String surname, String email, String phone) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .userData(buildUserData(name, surname, email, phone))
                .isBanned(false)
                .build();
        entityManager.persist(user);
        return user;
    }

    private void saveAdmin(String username, String rawPassword, String name, String surname,
                           String email, String phone, Level level) {
        Admin admin = Admin.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .userData(buildUserData(name, surname, email, phone))
                .adminLevel(level)
                .build();
        entityManager.persist(admin);
    }

    private void saveLibrarian(String username, String rawPassword, String name, String surname,
                               String email, String phone, Level level) {
        Librarian librarian = Librarian.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .userData(buildUserData(name, surname, email, phone))
                .libLevel(level)
                .build();
        entityManager.persist(librarian);
    }

    private UserData buildUserData(String name, String surname, String email, String phone) {
        return UserData.builder()
                .name(name)
                .surname(surname)
                .emailAddress(email)
                .phoneNumber(phone)
                .build();
    }

    private void saveOrder(User account, List<Book> books, LocalDateTime period) {
        Order order = Order.builder()
                .account(account)
                .book(books)
                .rentalTime(LocalDateTime.now())
                .rentalPeriod(period)
                .build();
        entityManager.persist(order);
    }
}