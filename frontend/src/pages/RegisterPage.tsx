import { useState, type FormEvent } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import Alert from '../components/Alert';
import { useAuth } from '../contexts/AuthContext';
import { useI18n } from '../i18n';
import { errorMessage } from '../hooks/useApi';
import { ApiError } from '../api/client';
import { homePathForRole } from '../utils';

export default function RegisterPage() {
  const { account, register } = useAuth();
  const { lang, setLang, t } = useI18n();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    username: '',
    password: '',
    name: '',
    surname: '',
    phoneNumber: '',
    emailAddress: '',
  });
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  if (account) {
    return <Navigate to={homePathForRole(account.role)} replace />;
  }

  const update = (key: keyof typeof form) => (event: { target: { value: string } }) => {
    setForm((current) => ({ ...current, [key]: event.target.value }));
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    setFieldErrors({});
    try {
      const current = await register(form);
      navigate(homePathForRole(current.role), { replace: true });
    } catch (caught) {
      if (caught instanceof ApiError && Object.keys(caught.fieldErrors).length > 0) {
        setFieldErrors(caught.fieldErrors);
      }
      setError(errorMessage(caught));
    } finally {
      setSubmitting(false);
    }
  };

  const fieldError = (key: string) => fieldErrors[key];

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
          <Link to="/login" className="btn btn-outline-light">
            {t('nav.login')}
          </Link>
        </div>
      </header>

      <div className="container">
        <div className="row justify-content-center">
          <div className="col-12 col-md-6 col-lg-5 p-3">
            <h2 className="h4 mb-3">{t('auth.registerTitle')}</h2>

            {error && <Alert variant="danger">{error}</Alert>}

            <form onSubmit={handleSubmit} noValidate>
              <div className="mb-3">
                <label htmlFor="inputUsername" className="form-label">
                  {t('auth.username')}
                </label>
                <input
                  id="inputUsername"
                  type="text"
                  className="form-control"
                  placeholder={t('placeholder.username')}
                  value={form.username}
                  onChange={update('username')}
                  required
                />
                {fieldError('username') && (
                  <div className="text-danger small mt-1">{fieldError('username')}</div>
                )}
              </div>

              <div className="mb-3">
                <label htmlFor="inputPassword" className="form-label">
                  {t('auth.password')}
                </label>
                <input
                  id="inputPassword"
                  type="password"
                  className="form-control"
                  placeholder={t('placeholder.password')}
                  value={form.password}
                  onChange={update('password')}
                  required
                />
                {fieldError('password') && (
                  <div className="text-danger small mt-1">{fieldError('password')}</div>
                )}
              </div>

              <div className="mb-3">
                <label htmlFor="inputName" className="form-label">
                  {t('auth.name')}
                </label>
                <input
                  id="inputName"
                  type="text"
                  className="form-control"
                  placeholder={t('placeholder.name')}
                  value={form.name}
                  onChange={update('name')}
                  required
                />
                {fieldError('name') && <div className="text-danger small mt-1">{fieldError('name')}</div>}
              </div>

              <div className="mb-3">
                <label htmlFor="inputSurname" className="form-label">
                  {t('auth.surname')}
                </label>
                <input
                  id="inputSurname"
                  type="text"
                  className="form-control"
                  placeholder={t('placeholder.surname')}
                  value={form.surname}
                  onChange={update('surname')}
                  required
                />
                {fieldError('surname') && (
                  <div className="text-danger small mt-1">{fieldError('surname')}</div>
                )}
              </div>

              <div className="mb-3">
                <label htmlFor="inputPhone" className="form-label">
                  {t('auth.phone')}
                </label>
                <input
                  id="inputPhone"
                  type="text"
                  className="form-control"
                  placeholder={t('placeholder.phone')}
                  value={form.phoneNumber}
                  onChange={update('phoneNumber')}
                  required
                />
                {fieldError('phoneNumber') && (
                  <div className="text-danger small mt-1">{fieldError('phoneNumber')}</div>
                )}
              </div>

              <div className="mb-3">
                <label htmlFor="inputEmail" className="form-label">
                  {t('auth.email')}
                </label>
                <input
                  id="inputEmail"
                  type="email"
                  className="form-control"
                  placeholder={t('placeholder.email')}
                  value={form.emailAddress}
                  onChange={update('emailAddress')}
                />
                {fieldError('emailAddress') && (
                  <div className="text-danger small mt-1">{fieldError('emailAddress')}</div>
                )}
              </div>

              <button type="submit" className="btn btn-dark" disabled={submitting}>
                {t('auth.registerButton')}
              </button>
            </form>

            <p className="mt-3 text-muted">
              {t('auth.haveAccount')}{' '}
              <Link to="/login" className="alert-link">
                {t('nav.login')}
              </Link>
            </p>
          </div>
        </div>
      </div>
    </>
  );
}
