package by.tms.web.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public final class PageUtil {

    private PageUtil() {
        throw new UnsupportedOperationException();
    }

    public static final String USER_PREFIX = "page/user/";
    public static final String ADMIN_PREFIX = "page/admin/";
    public static final String LIBRARIAN_PREFIX = "page/librarian/";
    public static final String ERROR_PAGE_PREFIX = "page/errorPage";
    public static final String BAN_PAGE_PREFIX = "page/banPage";
    public static final String BAN_PAGE = "banPage";
    public static final String START_PAGE = "/";
    public static final String USER_ATTRIBUTE = "user";
    public static final String ACCOUNT_ATTRIBUTE = "account";
    public static final String SHOPPING_CART_ATTRIBUTE = "shoppingCart";
    public static final String BOOK_ATTRIBUTE = "book";
    public static final String COUNT_PAGES_ATTRIBUTE = "countPages";
    public static final String OFFSET_PARAMETER = "offset";
    public static final String ALL_USERS_ATTRIBUTE = "allUsers";
    public static final String ALL_USERS_SUFFIX = ALL_USERS_ATTRIBUTE;
    public static final String ALL_ADMINS_ATTRIBUTE = "allAdmins";
    public static final String CANNOT_DELETE_ATTRIBUTE = "cannotDel";
    public static final String ALL_ADMINS_SUFFIX = ALL_ADMINS_ATTRIBUTE;
    public static final String ALL_LIBRARIANS_ATTRIBUTE = "allLibrarians";
    public static final String ALL_LIBRARIANS_SUFFIX = ALL_LIBRARIANS_ATTRIBUTE;
    public static final String VALUE_ZERO = "0";
    public static final String ALL_AUTHORS_ATTRIBUTE = "allAuthors";
    public static final String ALL_SECTIONS_ATTRIBUTE = "allSections";
    public static final String ID_PATH_VARIABLE = "id";
    public static final String ALL_BOOKS_PAGE = "allBooks";
    public static final String ALL_BOOKS_ATTRIBUTE = ALL_BOOKS_PAGE;
    public static final String ALL_GENRES_ATTRIBUTE = "allGenres";
    public static final String ALL_AUTHOR_SUFFIX = "allAuthor";
    public static final String AUTHOR_ATTRIBUTE = "author";
    public static final String ADD_AUTHOR_SUFFIX = "addAuthor";
    public static final String ADD_SECTION_SUFFIX = "addSection";
    public static final String ADD_GENRE_SUFFIX = "addGenre";
    public static final String ALL_ORDERS_ATTRIBUTE = "allOrders";
    public static final String ADD_EMPLOYEE_SUFFIX = "addEmployee";
    public static final String DEBTORS_ATTRIBUTE = "debtors";
    public static final String LIBRARIAN_PAGE_SUFFIX = "librarian";
    public static final String LIBRARIAN_ROLE = LIBRARIAN_PAGE_SUFFIX;
    public static final String USER_PAGE_SUFFIX = "user";
    public static final String USER_ROLE = USER_PAGE_SUFFIX;
    public static final String SUCCESS_PAGE = "/success";
    public static final String USER_PAGE = "user/userPage";
    public static final String LIBRARIAN_PAGE = "librarian/librarianPage";
    public static final String ADMIN_PAGE = "admin/adminPage";
    public static final String REGISTER_PAGE = "registerPage";
    public static final String ERROR_PAGE = "errorPage";
    public static final String INDEX_PAGE_SUFFIX = "index";
    public static final String GENRE_ATTRIBUTE = "genre";
    public static final String SECTION_ATTRIBUTE = "section";
    public static final String RENTAL_PERIOD_PARAMETER = "rentalPeriod";
    public static final String BOOK_ID_PARAMETER = "bookId";
    public static final String REFERER_HEADER = "referer";
    public static final String REDIRECT = "redirect:";
    public static final String USERNAME_PARAMETER = "username";
    public static final String BLOCK_USER_PAGE = "blockUser";
    public static final String ADMIN_PAGE_SUFFIX = "admin";
    public static final String REGISTER_PAGE_SUFFIX = "register";
    public static final String ADMIN_ROLE = ADMIN_PAGE_SUFFIX;
    public static final String SHOPPING_CART_SUFFIX = "shoppingCart";
    public static final String SHOPPING_CART_PAGE = "/user/shoppingCart";
    public static final String EDIT_BOOK_SUFFIX = "editBook";
    public static final String EDIT_USER_SUFFIX = "editUser";
    public static final String ADD_BOOK_SUFFIX = "addBook";
    public static final String AUTHOR_BOOK_SUFFIX = "authorBook";
    public static final String AUTHOR_BOOK_ATTRIBUTE = AUTHOR_BOOK_SUFFIX;
    public static final String SHOW_ALL_BOOKS_USER_PAGE = "user/showAllBooks";
    public static final String SHOW_ALL_BOOKS_LIB_PAGE = "librarian/showAllBooks";
    public static final String FIND_USER_ORDER_SUFFIX = "findUserOrder";
    public static final String USER_ORDER_PAGE = "userOrder";
    public static final String USER_ORDER_ATTRIBUTE = "userOrder";
    public static final String EXCEPTION_MESSAGE = "exceptionMessage";
    public static final String ALL_ORDERS_PAGE = "allOrders";
    public static final String ALL_USERS_PAGE = "/admin/allUsersPage";
    public static final String USER_INFO_PAGE_SUFFIX = "userInfo";
    public static final int OFFSET_ZERO = 0;
    public static final int ONE_BOOK = 1;
    public static final int LIMIT_TEN = 10;

    public static LocalDateTime setRentalPeriod(String period) {
        LocalDateTime now = LocalDateTime.now();
        int rentalPeriod = Integer.parseInt(period);
        if (rentalPeriod == 1) {
            long seconds = LocalDateTime.now().compareTo(LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0))) > 0 ?
                    ChronoUnit.SECONDS.between(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(19, 0)), now)
                    : ChronoUnit.SECONDS.between(LocalTime.now(), LocalTime.of(19, 0));
            return now.plusSeconds(Math.abs(seconds));
        } else {
            return now.plusDays(rentalPeriod);
        }
    }
}
