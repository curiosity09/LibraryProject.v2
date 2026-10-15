import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';
import { useI18n } from '../i18n';
import type { Role } from '../api/types';

interface NavItem {
  to: string;
  labelKey: string;
  end?: boolean;
  badge?: number;
}

function itemsForRole(role: Role, cartCount: number): NavItem[] {
  switch (role) {
    case 'user':
      return [
        { to: '/books', labelKey: 'nav.books' },
        { to: '/authors', labelKey: 'nav.authors' },
        { to: '/cart', labelKey: 'nav.cart', badge: cartCount },
        { to: '/orders', labelKey: 'nav.myOrders' },
        { to: '/profile/edit', labelKey: 'nav.editProfile' },
        { to: '/profile', labelKey: 'nav.profile' },
      ];
    case 'librarian':
      return [
        { to: '/librarian', labelKey: 'nav.home', end: true },
        { to: '/librarian/books', labelKey: 'nav.books' },
        { to: '/librarian/orders', labelKey: 'nav.allOrders' },
        { to: '/librarian/orders/user', labelKey: 'nav.userOrders' },
        { to: '/librarian/books/new', labelKey: 'nav.addBook' },
        { to: '/librarian/genres/new', labelKey: 'nav.addGenre' },
        { to: '/librarian/authors/new', labelKey: 'nav.addAuthor' },
        { to: '/librarian/sections/new', labelKey: 'nav.addSection' },
        { to: '/librarian/files', labelKey: 'nav.files' },
      ];
    case 'admin':
      return [
        { to: '/admin', labelKey: 'nav.home', end: true },
        { to: '/admin/users', labelKey: 'nav.allUsers' },
        { to: '/admin/admins', labelKey: 'nav.admins' },
        { to: '/admin/librarians', labelKey: 'nav.librarians' },
        { to: '/admin/employees/new', labelKey: 'nav.newEmployee' },
        { to: '/admin/debtors', labelKey: 'nav.debtors' },
      ];
    default:
      return [];
  }
}

export default function Navbar() {
  const { account, logout } = useAuth();
  const { count } = useCart();
  const { lang, setLang, t } = useI18n();
  const navigate = useNavigate();

  const items = account ? itemsForRole(account.role, count) : [];

  const handleLogout = async () => {
    await logout();
    navigate('/login', { replace: true });
  };

  return (
    <header className="p-3 bg-dark text-white">
      <div className="container">
        <div className="d-flex flex-wrap align-items-center">
          <Link to="/" className="d-flex align-items-center me-3">
            <img src="/images/icon.png" width="50" height="50" alt={t('app.title')} />
          </Link>

          <ul className="nav col-12 col-lg-auto me-lg-auto mb-2 mb-md-0">
            {items.map((item) => (
              <li key={item.to}>
                <NavLink
                  to={item.to}
                  end={item.end}
                  className={({ isActive }) =>
                    `nav-link px-2 ${isActive ? 'text-warning fw-semibold' : 'text-white'}`
                  }
                >
                  {t(item.labelKey)}
                  {item.badge !== undefined && item.badge > 0 && (
                    <span className="badge bg-warning text-dark ms-1">{item.badge}</span>
                  )}
                </NavLink>
              </li>
            ))}
          </ul>

          <div className="btn-group btn-group-sm me-3" role="group" aria-label="Language">
            <button
              type="button"
              className={`btn ${lang === 'en' ? 'btn-light' : 'btn-outline-light'}`}
              onClick={() => setLang('en')}
            >
              EN
            </button>
            <button
              type="button"
              className={`btn ${lang === 'ru' ? 'btn-light' : 'btn-outline-light'}`}
              onClick={() => setLang('ru')}
            >
              RU
            </button>
          </div>

          {account && (
            <button type="button" className="btn btn-outline-light" onClick={handleLogout}>
              {t('nav.logout')}
            </button>
          )}
        </div>
      </div>
    </header>
  );
}
