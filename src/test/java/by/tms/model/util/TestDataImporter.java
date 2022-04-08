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
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Component("testDataImporter")
public class TestDataImporter {

    public static final int LIMIT_10 = 10;
    public static final int OFFSET_0 = 0;
    private final SessionFactory sessionFactory;

    @Autowired
    public TestDataImporter(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void importTestData() {
        Session session = sessionFactory.getCurrentSession();

        Section preSchoolLiteratureSection = saveSection(session, "Дошкольная литература");
        Section spaceSection = saveSection(session, "Космос");
        Section animalSection = saveSection(session, "Животные");

        Genre detective = saveGenre(session, "Детектив");
        Genre roman = saveGenre(session, "Роман");
        saveGenre(session, "Автобиография");

        Author akuninAuthor = saveAuthor(session, "Борис Акунин");
        Author pelevinAuthor = saveAuthor(session, "Виктор Пелевин");
        saveAuthor(session, "Чарльз Буковски");

        Book azazelloBook = saveBook(session, "Азазело", akuninAuthor, detective,
                5, animalSection, 1999);
        Book turkishGambitBook = saveBook(session, "Турецкий гамбит", akuninAuthor, roman,
                2, spaceSection, 2003);
        Book generationP = saveBook(session, "Generation P", pelevinAuthor, roman,
                4, preSchoolLiteratureSection, 1999);

        User user = saveUser(session, "user", "pass", "Misha", "Ryzgunsky",
                "ryzgunsky.mihail@gmail.com", "+375445787918");
        User newUser = saveUser(session, "newUser", "pass1", "Sasha", "Egorov",
                "ryzgunky.mihail@gmail.com", "+375445717918");
        saveAdmin(session, "admin", "pass1", "Dima", "Morozov",
                "dima@mail.com", "+375445344587", Level.HEAD);
        saveAdmin(session, "newAdmin", "pass", "Misha", "Basalay",
                "dma@mail.com", "+375445345287", Level.SECONDARY);
        saveLibrarian(session, "newLib", "pass1", "Kirill", "Anasovich",
                "kiill@mail.ru", "+375445322487", Level.HEAD);
        saveLibrarian(session, "lib", "pass", "Petya", "Lukoshko",
                "kirill@mail.ru", "+375445322457", Level.SECONDARY);

        saveOrder(session, user, List.of(azazelloBook, generationP), LocalDateTime.now().plusDays(10));
        saveOrder(session, newUser, List.of(azazelloBook, generationP, turkishGambitBook), LocalDateTime.now().plusDays(1));
        saveOrder(session, newUser, List.of(azazelloBook, turkishGambitBook), LocalDateTime.now().plusDays(2));
    }

    public void cleanTestData() {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete from User ").executeUpdate();
        session.createQuery("delete from Librarian ").executeUpdate();
        session.createQuery("delete from Admin ").executeUpdate();
        session.createQuery("delete from Order ").executeUpdate();
        session.createQuery("delete from Author ").executeUpdate();
        session.createQuery("delete from Book ").executeUpdate();
        session.createQuery("delete from Section ").executeUpdate();
        session.createQuery("delete from Genre ").executeUpdate();
    }

    private static Section saveSection(Session session, String name) {
        Section section = Section.builder().name(name).build();
        session.save(section);
        return section;
    }

    private static Genre saveGenre(Session session, String name) {
        Genre genre = Genre.builder().name(name).build();
        session.save(genre);
        return genre;
    }

    private static Author saveAuthor(Session session, String fullName) {
        Author author = Author.builder().fullName(fullName).build();
        session.save(author);
        return author;
    }

    private static Book saveBook(Session session, String name,
                                 Author author, Genre genre, int quantity, Section section, int year) {
        Book book = Book.builder().name(name).author(author).genre(genre).section(section)
                .quantity(quantity).publicationYear(year).build();
        session.save(book);
        return book;
    }

    private static User saveUser(Session session, String username, String password,
                                 String name, String surname, String email, String phone) {
        User user = User.builder().username(username).password(password).role("user")
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).isBanned(false).build();
        session.save(user);
        return user;
    }

    private static void saveAdmin(Session session, String username, String password,
                                  String name, String surname, String email, String phone, Level level) {
        Admin admin = Admin.builder().username(username).password(password)
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).adminLevel(level).build();
        session.save(admin);
    }

    private static void saveLibrarian(Session session, String username, String password,
                                      String name, String surname, String email, String phone, Level level) {
        Librarian librarian = Librarian.builder().username(username).password(password)
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).libLevel(level).build();
        session.save(librarian);
    }

    private static void saveOrder(Session session, User account, List<Book> books, LocalDateTime period) {
        Order order = Order.builder().account(account)
                .book(books).rentalTime(LocalDateTime.now()).rentalPeriod(period).build();
        session.save(order);
    }
}
