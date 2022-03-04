package by.tms.util;

import by.tms.entity.Author;
import by.tms.entity.Book;
import by.tms.entity.Genre;
import by.tms.entity.Order;
import by.tms.entity.Section;
import by.tms.entity.user.Account;
import by.tms.entity.user.Admin;
import by.tms.entity.user.Level;
import by.tms.entity.user.Librarian;
import by.tms.entity.user.User;
import by.tms.entity.user.UserData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestDataImporter {

    private static final TestDataImporter INSTANCE = new TestDataImporter();

    public static TestDataImporter getInstance() {
        return INSTANCE;
    }

    public void importTestData(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Section preSchoolLiteratureSection = saveSection(session, "Дошкольная литература");
            Section spaceSection = saveSection(session, "Космос");
            Section animalSection = saveSection(session, "Животные");

            Genre detective = saveGenre(session, "Детектив");
            Genre roman = saveGenre(session, "Роман");

            Author akuninAuthor = saveAuthor(session, "Борис Акунин");
            Author pelevinAuthor = saveAuthor(session, "Виктор Пелевин");

            Book azazelloBook = saveBook(session, "Азазело", akuninAuthor, detective,
                    5, animalSection, 1999);
            Book turkishGambitBook = saveBook(session, "Турецкий гамбит", akuninAuthor, roman,
                    2, spaceSection, 2003);
            Book generationP = saveBook(session, "Generation P", pelevinAuthor, roman,
                    4, preSchoolLiteratureSection, 1999);

            Account user = saveUser(session, "user", "pass", "Misha", "Ryzgunsky",
                    "ryzgunsky.mihail@gmail.com", "+375445717918");
            saveAdmin(session, "admin", "pass", "Dima", "Basalay",
                    "dima@mail.com", "+375445345287", Level.HEAD);
            saveLibrarian(session, "lib", "pass", "Kirill", "Lukoshko",
                    "kirill@mail.ru", "+375445322457", Level.SECONDARY);
            Transaction transaction = session.beginTransaction();
            saveOrder(session, user, List.of(azazelloBook, generationP), LocalDateTime.now().plusDays(1));
            saveOrder(session, user, List.of(azazelloBook, generationP,turkishGambitBook), LocalDateTime.now().plusDays(1));
            transaction.commit();
        }
    }

    private Section saveSection(Session session, String name) {
        Section section = Section.builder().name(name).build();
        session.save(section);
        return section;
    }

    private Genre saveGenre(Session session, String name) {
        Genre genre = Genre.builder().name(name).build();
        session.save(genre);
        return genre;
    }

    private Author saveAuthor(Session session, String fullName) {
        Author author = Author.builder().fullName(fullName).build();
        session.save(author);
        return author;
    }

    private Book saveBook(Session session, String name,
                          Author author, Genre genre, int quantity, Section section, int year) {
        Book book = Book.builder().name(name).author(author).genre(genre).section(section)
                .quantity(quantity).publicationYear(year).build();
        session.save(book);
        return book;
    }

    private Account saveUser(Session session, String username, String password,
                             String name, String surname, String email, String phone) {
        User user = User.builder().username(username).password(password)
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).isBanned(false).build();
        session.save(user);
        return user;
    }

    private Admin saveAdmin(Session session, String username, String password,
                            String name, String surname, String email, String phone, Level level) {
        Admin admin = Admin.builder().username(username).password(password)
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).adminLevel(level).build();
        session.save(admin);
        return admin;
    }

    private Librarian saveLibrarian(Session session, String username, String password,
                                    String name, String surname, String email, String phone, Level level) {
        Librarian librarian = Librarian.builder().username(username).password(password)
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).libLevel(level).build();
        session.save(librarian);
        return librarian;
    }

    private Order saveOrder(Session session, Account account, List<Book> books, LocalDateTime period) {
        Order order = Order.builder().account(account)
                .book(books).rentalTime(LocalDateTime.now()).rentalPeriod(period).build();
        session.save(order);
        return order;
    }
}
