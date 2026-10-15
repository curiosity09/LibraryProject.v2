import { useState, type FormEvent } from 'react';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import Alert from '../components/Alert';
import { useAuth } from '../contexts/AuthContext';
import { useI18n } from '../i18n';
import { ApiError } from '../api/client';
import { homePathForRole } from '../utils';

export default function LoginPage() {
  const { account, login } = useAuth();
  const { lang, setLang, t } = useI18n();
  const navigate = useNavigate();
  const location = useLocation();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [blocked, setBlocked] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  if (account) {
    return <Navigate to={homePathForRole(account.role)} replace />;
  }

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    setBlocked(false);
    try {
      const current = await login({ username, password });
      const from = (location.state as { from?: string } | null)?.from;
      navigate(from && from !== '/login' ? from : homePathForRole(current.role), { replace: true });
    } catch (caught) {
      if (caught instanceof ApiError && caught.status === 403) {
        setBlocked(true);
      } else {
        setError(t('alerts.invalid'));
      }
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <header className="p-3 bg-dark text-white">
        <div className="container d-flex align-items-center">
          <Link to="/" className="d-flex me-auto">
            <img src="/images/icon.png" width="50" height="50" alt={t('app.title')} />
          </Link>
          <div className="btn-group btn-group-sm me-2" role="group" aria-label="Language">
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
          <Link to="/register" className="btn btn-warning">
            {t('nav.register')}
          </Link>
        </div>
      </header>

      <div className="container">
        <div className="row justify-content-center mt-5">
          <div className="col-12 col-md-6 col-lg-5 p-3">
            <h2 className="h4 mb-3">{t('auth.loginTitle')}</h2>

            {error && <Alert variant="danger">{error}</Alert>}
            {blocked && <Alert variant="danger">{t('ban.text')}</Alert>}

            <form onSubmit={handleSubmit} noValidate>
              <div className="mb-3">
                <label htmlFor="inputUsername" className="form-label">
                  {t('auth.username')}
                </label>
                <input
                  id="inputUsername"
                  name="username"
                  type="text"
                  className="form-control"
                  placeholder={t('placeholder.username')}
                  value={username}
                  onChange={(event) => setUsername(event.target.value)}
                  required
                />
              </div>

              <div className="mb-3">
                <label htmlFor="inputPassword" className="form-label">
                  {t('auth.password')}
                </label>
                <input
                  id="inputPassword"
                  name="password"
                  type="password"
                  className="form-control"
                  placeholder={t('placeholder.password')}
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                  required
                />
              </div>

              <button type="submit" className="btn btn-dark" disabled={submitting}>
                {t('auth.loginButton')}
              </button>
            </form>

            <p className="mt-3 text-muted">
              {t('auth.noAccount')}{' '}
              <Link to="/register" className="alert-link">
                {t('nav.register')}
              </Link>
            </p>
          </div>
        </div>
      </div>
    </>
  );
}
