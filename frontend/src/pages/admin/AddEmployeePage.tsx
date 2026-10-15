import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import Alert from '../../components/Alert';
import { adminApi, type EmployeePayload } from '../../api/endpoints';
import { errorMessage } from '../../hooks/useApi';
import { ApiError } from '../../api/client';
import { useI18n } from '../../i18n';

export default function AddEmployeePage() {
  const { t } = useI18n();
  const navigate = useNavigate();

  const [form, setForm] = useState<EmployeePayload>({
    username: '',
    password: '',
    role: 'librarian',
    level: 'SECONDARY',
    name: '',
    surname: '',
    phoneNumber: '',
    emailAddress: '',
  });
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    setFieldErrors({});
    try {
      await adminApi.addEmployee(form);
      navigate('/admin', { replace: true });
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
    <div className="row justify-content-center">
      <div className="col-12 col-md-6 col-lg-5">
        <h2 className="h4 mb-3">{t('nav.newEmployee')}</h2>
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
              onChange={(event) => setForm({ ...form, username: event.target.value })}
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
              onChange={(event) => setForm({ ...form, password: event.target.value })}
              required
            />
            {fieldError('password') && (
              <div className="text-danger small mt-1">{fieldError('password')}</div>
            )}
          </div>

          <fieldset className="mb-3">
            <legend className="h6">{t('common.role')}</legend>
            <div className="form-check form-check-inline">
              <input
                className="form-check-input"
                type="radio"
                name="role"
                id="role-librarian"
                checked={form.role === 'librarian'}
                onChange={() => setForm({ ...form, role: 'librarian' })}
              />
              <label className="form-check-label" htmlFor="role-librarian">
                {t('nav.librarians')}
              </label>
            </div>
            <div className="form-check form-check-inline">
              <input
                className="form-check-input"
                type="radio"
                name="role"
                id="role-admin"
                checked={form.role === 'admin'}
                onChange={() => setForm({ ...form, role: 'admin' })}
              />
              <label className="form-check-label" htmlFor="role-admin">
                {t('nav.admins')}
              </label>
            </div>
          </fieldset>

          <fieldset className="mb-3">
            <legend className="h6">{t('common.level')}</legend>
            <div className="form-check form-check-inline">
              <input
                className="form-check-input"
                type="radio"
                name="level"
                id="level-secondary"
                checked={form.level === 'SECONDARY'}
                onChange={() => setForm({ ...form, level: 'SECONDARY' })}
              />
              <label className="form-check-label" htmlFor="level-secondary">
                SECONDARY
              </label>
            </div>
            <div className="form-check form-check-inline">
              <input
                className="form-check-input"
                type="radio"
                name="level"
                id="level-head"
                checked={form.level === 'HEAD'}
                onChange={() => setForm({ ...form, level: 'HEAD' })}
              />
              <label className="form-check-label" htmlFor="level-head">
                HEAD
              </label>
            </div>
          </fieldset>

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
              onChange={(event) => setForm({ ...form, name: event.target.value })}
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
              onChange={(event) => setForm({ ...form, surname: event.target.value })}
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
              onChange={(event) => setForm({ ...form, phoneNumber: event.target.value })}
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
              onChange={(event) => setForm({ ...form, emailAddress: event.target.value })}
            />
            {fieldError('emailAddress') && (
              <div className="text-danger small mt-1">{fieldError('emailAddress')}</div>
            )}
          </div>

          <button type="submit" className="btn btn-dark" disabled={submitting}>
            {t('common.submit')}
          </button>
        </form>
      </div>
    </div>
  );
}
