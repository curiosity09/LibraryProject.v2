package by.tms.test;

import by.tms.dao.impl.AccountDaoImpl;
import by.tms.entity.Author;
import by.tms.entity.Genre;
import by.tms.entity.Section;
import by.tms.entity.user.User;
import by.tms.entity.user.UserData;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class EntityTest {
    public static void main(String[] args) {
        @Cleanup SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        Genre roman = Genre.builder().name("Roman").build();
        Section section = Section.builder().name("Дошкольное чтение").build();

        Author author = Author.builder().fullName("Виктор Пелевин").build();
/*        Genre genre = session.get(Genre.class, 1L);
        Section section = session.get(Section.class, 1L);
        Author author = session.get(Author.class, 1L);*/
//        Book generation_p = Book.builder().name("Generation P").genre(genre).section(section).author(author).publicationYear(1999).quantity(4).build();


/*        System.out.println(OrderDaoImpl.getInstance().findAll(session));*/
        Transaction transaction = session.beginTransaction();
        List<User> allDebtors = AccountDaoImpl.getInstance().findAllDebtors(session);
        System.out.println(allDebtors);
        transaction.commit();
/*        book.setQuantity(book.getQuantity() - 1);
        session.update(book);*/

//        OrderDaoImpl.getInstance().add(session,order,List.of(book,book));

        Author author2 = Author.builder().fullName("Виктор Пелевин").build();
        Author author1 = Author.builder().fullName("Борис Акулин").build();
        author1.setId(1L);
//        List<Author> all = AuthorDaoImpl.getInstance().findById(session, 2L);

/*        Admin build = Admin.builder().username("mi11").password("pass").adminLevel(Level.HEAD).build();
        User build1 = User.builder().username("wwood").password("passs").userData(UserData.builder().surname("Heell").build()).isBanned(false).build();
        Librarian librarian = Librarian.builder().username("lib").password("pass").build();*/

    }
    private static User saveUser(Session session, String username, String password,
                          String name, String surname, String email, String phone) {
        User user = User.builder().username(username).password(password)
                .userData(UserData.builder().name(name).surname(surname)
                        .emailAddress(email).phoneNumber(phone).build()).build();
        session.save(user);
        return user;
    }
}
