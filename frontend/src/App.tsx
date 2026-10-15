import { Navigate, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import { RequireAuth, RoleGuard } from './components/ProtectedRoute';
import { useAuth } from './contexts/AuthContext';
import { homePathForRole } from './utils';

import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import BannedPage from './pages/BannedPage';
import ErrorPage from './pages/ErrorPage';
import NotFoundPage from './pages/NotFoundPage';

import BooksPage from './pages/user/BooksPage';
import AuthorsPage from './pages/user/AuthorsPage';
import AuthorBooksPage from './pages/user/AuthorBooksPage';
import CartPage from './pages/user/CartPage';
import MyOrdersPage from './pages/user/MyOrdersPage';
import ProfilePage from './pages/user/ProfilePage';
import ProfileEditPage from './pages/user/ProfileEditPage';

import LibrarianHomePage from './pages/librarian/LibrarianHomePage';
import LibrarianBooksPage from './pages/librarian/LibrarianBooksPage';
import BookFormPage from './pages/librarian/BookFormPage';
import AddAuthorPage from './pages/librarian/AddAuthorPage';
import AddGenrePage from './pages/librarian/AddGenrePage';
import AddSectionPage from './pages/librarian/AddSectionPage';
import AllOrdersPage from './pages/librarian/AllOrdersPage';
import FindUserOrdersPage from './pages/librarian/FindUserOrdersPage';
import UserOrdersPage from './pages/librarian/UserOrdersPage';
import FilesPage from './pages/librarian/FilesPage';

import AdminHomePage from './pages/admin/AdminHomePage';
import UsersPage from './pages/admin/UsersPage';
import AdminsPage from './pages/admin/AdminsPage';
import LibrariansPage from './pages/admin/LibrariansPage';
import DebtorsPage from './pages/admin/DebtorsPage';
import AddEmployeePage from './pages/admin/AddEmployeePage';

function HomeRedirect() {
  const { account } = useAuth();
  return <Navigate to={homePathForRole(account?.role)} replace />;
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/banned" element={<BannedPage />} />
      <Route path="/error" element={<ErrorPage />} />

      <Route element={<RequireAuth />}>
        <Route element={<Layout />}>
          <Route path="/" element={<HomeRedirect />} />

          <Route element={<RoleGuard roles={['user']} />}>
            <Route path="/books" element={<BooksPage />} />
            <Route path="/authors" element={<AuthorsPage />} />
            <Route path="/authors/:id/books" element={<AuthorBooksPage />} />
            <Route path="/cart" element={<CartPage />} />
            <Route path="/orders" element={<MyOrdersPage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/profile/edit" element={<ProfileEditPage />} />
          </Route>

          <Route element={<RoleGuard roles={['librarian', 'admin']} />}>
            <Route path="/librarian" element={<LibrarianHomePage />} />
            <Route path="/librarian/books" element={<LibrarianBooksPage />} />
            <Route path="/librarian/books/new" element={<BookFormPage />} />
            <Route path="/librarian/books/:id/edit" element={<BookFormPage />} />
            <Route path="/librarian/authors/new" element={<AddAuthorPage />} />
            <Route path="/librarian/genres/new" element={<AddGenrePage />} />
            <Route path="/librarian/sections/new" element={<AddSectionPage />} />
            <Route path="/librarian/orders" element={<AllOrdersPage />} />
            <Route path="/librarian/orders/user" element={<FindUserOrdersPage />} />
            <Route path="/librarian/orders/user/:username" element={<UserOrdersPage />} />
            <Route path="/librarian/files" element={<FilesPage />} />
          </Route>

          <Route element={<RoleGuard roles={['admin']} />}>
            <Route path="/admin" element={<AdminHomePage />} />
            <Route path="/admin/users" element={<UsersPage />} />
            <Route path="/admin/admins" element={<AdminsPage />} />
            <Route path="/admin/librarians" element={<LibrariansPage />} />
            <Route path="/admin/debtors" element={<DebtorsPage />} />
            <Route path="/admin/employees/new" element={<AddEmployeePage />} />
          </Route>
        </Route>
      </Route>

      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}
