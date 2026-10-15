import { createContext, useCallback, useContext, useMemo, useState, type ReactNode } from 'react';

export type Lang = 'ru' | 'en';

type Dictionary = Record<string, string>;

const ru: Dictionary = {
  'app.title': 'Библиотека',

  'nav.home': 'Домой',
  'nav.books': 'Список книг',
  'nav.authors': 'Авторы',
  'nav.cart': 'Корзина',
  'nav.myOrders': 'Мои заказы',
  'nav.editProfile': 'Редактировать',
  'nav.profile': 'О пользователе',
  'nav.logout': 'Выйти',
  'nav.login': 'Войти',
  'nav.register': 'Регистрация',
  'nav.allOrders': 'Все заказы',
  'nav.userOrders': 'Заказ пользователя',
  'nav.addBook': 'Добавить книгу',
  'nav.addGenre': 'Добавить жанр',
  'nav.addAuthor': 'Добавить автора',
  'nav.addSection': 'Добавить секцию',
  'nav.files': 'Все файлы',
  'nav.allUsers': 'Все пользователи',
  'nav.admins': 'Администраторы',
  'nav.librarians': 'Библиотекари',
  'nav.newEmployee': 'Новый сотрудник',
  'nav.debtors': 'Блокировка',

  'auth.username': 'Имя пользователя',
  'auth.password': 'Пароль',
  'auth.name': 'Имя',
  'auth.surname': 'Фамилия',
  'auth.phone': 'Номер телефона',
  'auth.email': 'Email-адрес',
  'auth.loginTitle': 'Вход в библиотеку',
  'auth.registerTitle': 'Регистрация',
  'auth.loginButton': 'Войти',
  'auth.registerButton': 'Зарегистрироваться',
  'auth.noAccount': 'Нет аккаунта?',
  'auth.haveAccount': 'Уже есть аккаунт?',

  'common.submit': 'Подтвердить',
  'common.cancel': 'Отмена',
  'common.delete': 'Удалить',
  'common.edit': 'Редактировать',
  'common.show': 'Показать',
  'common.save': 'Сохранить',
  'common.loading': 'Загрузка…',
  'common.empty': 'Нет данных',
  'common.actions': 'Действия',
  'common.number': 'Номер',
  'common.book': 'Название книги',
  'common.author': 'Автор',
  'common.genre': 'Жанр',
  'common.section': 'Секция',
  'common.quantity': 'Кол-во',
  'common.year': 'Год',
  'common.add': 'Добавить',
  'common.user': 'Пользователь',
  'common.role': 'Роль',
  'common.level': 'Уровень',
  'common.status': 'Статус',
  'common.banned': 'В чёрном списке',
  'common.username': 'Имя пользователя',
  'common.rentalTime': 'Время заказа',
  'common.rentalPeriod': 'Срок аренды',
  'common.id': 'ID',
  'common.phone': 'Телефон',
  'common.email': 'Email',
  'common.editBook': 'Ред. книгу',
  'common.select': 'Выбрать',

  'books.caption': 'Здесь вы можете найти все доступные книги',
  'books.addToCart': 'В корзину',
  'books.select': 'Добавить',

  'authors.caption': 'Все авторы',
  'authors.showBooks': 'Показать',

  'cart.caption': 'Вы выбрали эти книги для заказа',
  'cart.empty': 'Ваша корзина пуста',
  'cart.period': 'Срок аренды',
  'cart.period.readingRoom': 'В читальный зал (рабочий день)',
  'cart.period.days14': 'Абонемент (14 суток)',
  'cart.period.days30': 'Абонемент (1 месяц)',
  'cart.create': 'Добавить заказ',

  'orders.myCaption': 'Здесь вы можете найти все свои заказы',
  'orders.allCaption': 'Здесь вы можете найти все заказы',
  'orders.userCaption': 'Здесь вы можете найти все заказы пользователя',
  'orders.finish': 'Удалить заказ',

  'status.active': 'Активен',
  'status.expired': 'Просрочен',

  'profile.caption': 'Здесь вы можете найти всю информацию о себе',

  'user.pageText': 'Вы на странице пользователя',
  'librarian.pageText': 'Вы на странице библиотекаря',
  'admin.pageText': 'Вы на странице администратора',

  'librarian.upload.title': 'Загрузка файла',
  'librarian.upload.button': 'Загрузить',
  'librarian.files.title': 'Список файлов',
  'librarian.files.empty': 'Файлы не найдены',
  'librarian.files.fileName': 'Имя файла',
  'librarian.files.link': 'Ссылка',
  'librarian.files.download': 'Скачать',

  'admin.users.caption': 'Здесь вы можете найти всех пользователей',
  'admin.employees.caption': 'Сотрудники',

  'findUser.selectUser': 'Выберите имя пользователя',

  'ban.text': 'Вы были заблокированы на этом сайте. Попробуйте войти под другим аккаунтом.',
  'error.text': 'Что-то пошло не так. Попробуйте снова.',
  'notFound.text': 'Страница не найдена.',

  'alerts.invalid': 'Неверное имя пользователя и(или) пароль',
  'alerts.loggedOut': 'Вы вышли из профиля',
  'alerts.cannotDelete': 'Невозможно удалить пользователя. У пользователя есть активные заказы.',
  'alerts.saved': 'Изменения сохранены',
  'alerts.added': 'Успешно добавлено',
  'alerts.deleted': 'Удалено',
  'alerts.orderCreated': 'Заказ успешно создан',
  'alerts.blocked': 'Пользователь заблокирован',

  'placeholder.username': 'Введите имя пользователя',
  'placeholder.password': 'Введите пароль',
  'placeholder.name': 'Введите имя',
  'placeholder.surname': 'Введите фамилию',
  'placeholder.phone': 'Введите номер телефона',
  'placeholder.email': 'Введите email',
  'placeholder.author': 'Введите имя и фамилию автора',
  'placeholder.bookName': 'Введите название книги',
  'placeholder.quantity': 'Введите количество книг',
  'placeholder.year': 'Введите год издания',
  'placeholder.genre': 'Введите название жанра',
  'placeholder.section': 'Введите название секции',
};

const en: Dictionary = {
  'app.title': 'Library',

  'nav.home': 'Home',
  'nav.books': 'All books',
  'nav.authors': 'Authors',
  'nav.cart': 'Cart',
  'nav.myOrders': 'My orders',
  'nav.editProfile': 'Edit profile',
  'nav.profile': 'About me',
  'nav.logout': 'Log out',
  'nav.login': 'Log in',
  'nav.register': 'Sign up',
  'nav.allOrders': 'All orders',
  'nav.userOrders': 'User orders',
  'nav.addBook': 'Add book',
  'nav.addGenre': 'Add genre',
  'nav.addAuthor': 'Add author',
  'nav.addSection': 'Add section',
  'nav.files': 'All files',
  'nav.allUsers': 'All users',
  'nav.admins': 'Admins',
  'nav.librarians': 'Librarians',
  'nav.newEmployee': 'New employee',
  'nav.debtors': 'Block user',

  'auth.username': 'Username',
  'auth.password': 'Password',
  'auth.name': 'Name',
  'auth.surname': 'Surname',
  'auth.phone': 'Phone number',
  'auth.email': 'Email address',
  'auth.loginTitle': 'Sign in to the library',
  'auth.registerTitle': 'Registration',
  'auth.loginButton': 'Sign in',
  'auth.registerButton': 'Sign up',
  'auth.noAccount': "Don't have an account?",
  'auth.haveAccount': 'Already have an account?',

  'common.submit': 'Submit',
  'common.cancel': 'Cancel',
  'common.delete': 'Delete',
  'common.edit': 'Edit',
  'common.show': 'Show',
  'common.save': 'Save',
  'common.loading': 'Loading…',
  'common.empty': 'No data',
  'common.actions': 'Actions',
  'common.number': 'Number',
  'common.book': 'Book',
  'common.author': 'Author',
  'common.genre': 'Genre',
  'common.section': 'Section',
  'common.quantity': 'Quantity',
  'common.year': 'Year',
  'common.add': 'Add',
  'common.user': 'User',
  'common.role': 'Role',
  'common.level': 'Level',
  'common.status': 'Status',
  'common.banned': 'Banned',
  'common.username': 'Username',
  'common.rentalTime': 'Rental time',
  'common.rentalPeriod': 'Rental period',
  'common.id': 'ID',
  'common.phone': 'Phone',
  'common.email': 'Email',
  'common.editBook': 'Edit book',
  'common.select': 'Select',

  'books.caption': 'You can find all available books here',
  'books.addToCart': 'Add to cart',
  'books.select': 'Add',

  'authors.caption': 'All authors',
  'authors.showBooks': 'Show',

  'cart.caption': 'You selected these books for an order',
  'cart.empty': 'Your cart is empty',
  'cart.period': 'Rental period',
  'cart.period.readingRoom': 'Reading room (working day)',
  'cart.period.days14': 'Subscription (14 days)',
  'cart.period.days30': 'Subscription (1 month)',
  'cart.create': 'Create order',

  'orders.myCaption': 'Here you can find all your orders',
  'orders.allCaption': 'Here you can find all orders',
  'orders.userCaption': "Here you can find all of the user's orders",
  'orders.finish': 'Finish order',

  'status.active': 'Active',
  'status.expired': 'Expired',

  'profile.caption': 'Here you can find all the information about yourself',

  'user.pageText': "You're on the user page",
  'librarian.pageText': "You're on the librarian page",
  'admin.pageText': "You're on the admin page",

  'librarian.upload.title': 'File upload',
  'librarian.upload.button': 'Upload',
  'librarian.files.title': 'List of files',
  'librarian.files.empty': 'No files found',
  'librarian.files.fileName': 'File name',
  'librarian.files.link': 'Link',
  'librarian.files.download': 'Download',

  'admin.users.caption': 'Here you can find all users',
  'admin.employees.caption': 'Employees',

  'findUser.selectUser': 'Select a username',

  'ban.text': 'You have been blocked on this site. Please try another account.',
  'error.text': 'Something went wrong. Please try again.',
  'notFound.text': 'Page not found.',

  'alerts.invalid': 'Invalid username and(or) password',
  'alerts.loggedOut': 'You have been logged out',
  'alerts.cannotDelete': 'The user cannot be deleted. The user still has active orders.',
  'alerts.saved': 'Changes saved',
  'alerts.added': 'Added successfully',
  'alerts.deleted': 'Deleted',
  'alerts.orderCreated': 'Order created successfully',
  'alerts.blocked': 'User blocked',

  'placeholder.username': 'Enter your username',
  'placeholder.password': 'Enter your password',
  'placeholder.name': 'Enter your name',
  'placeholder.surname': 'Enter your surname',
  'placeholder.phone': 'Enter your phone number',
  'placeholder.email': 'Enter your email',
  'placeholder.author': 'Enter the author full name',
  'placeholder.bookName': 'Enter the book name',
  'placeholder.quantity': 'Enter the book quantity',
  'placeholder.year': 'Enter the publication year',
  'placeholder.genre': 'Enter the genre name',
  'placeholder.section': 'Enter the section name',
};

const dictionaries: Record<Lang, Dictionary> = { ru, en };
const STORAGE_KEY = 'library.lang';

interface I18nValue {
  lang: Lang;
  setLang: (lang: Lang) => void;
  t: (key: string, vars?: Record<string, string | number>) => string;
}

const I18nContext = createContext<I18nValue | null>(null);

function initialLang(): Lang {
  const stored = localStorage.getItem(STORAGE_KEY);
  return stored === 'en' || stored === 'ru' ? stored : 'ru';
}

export function I18nProvider({ children }: { children: ReactNode }) {
  const [lang, setLangState] = useState<Lang>(initialLang);

  const setLang = useCallback((next: Lang) => {
    localStorage.setItem(STORAGE_KEY, next);
    setLangState(next);
  }, []);

  const t = useCallback(
    (key: string, vars?: Record<string, string | number>) => {
      let text = dictionaries[lang][key] ?? en[key] ?? key;
      if (vars) {
        for (const [name, value] of Object.entries(vars)) {
          text = text.split(`{${name}}`).join(String(value));
        }
      }
      return text;
    },
    [lang],
  );

  const value = useMemo<I18nValue>(() => ({ lang, setLang, t }), [lang, setLang, t]);

  return <I18nContext.Provider value={value}>{children}</I18nContext.Provider>;
}

export function useI18n(): I18nValue {
  const context = useContext(I18nContext);
  if (!context) {
    throw new Error('useI18n must be used inside an I18nProvider');
  }
  return context;
}
