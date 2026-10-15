import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import Alert from '../../components/Alert';
import { accountApi } from '../../api/endpoints';
import { errorMessage } from '../../hooks/useApi';
import { ApiError } from '../../api/client';
import { useAuth } from '../../contexts/AuthContext';
import { useI18n } from '../../i18n';

export default function ProfileEditPage() {
  const { t } = useI18n();
  const { account, refresh } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    password: '',
    name: account?.userData?.name ?? '',
    surname: account?.userData?.surname ?? '',
    phoneNumber: account?.userData?.phoneNumber ?? '',
    emailAddress: account?.userData?.emailAddress ?? '',
  });
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  const update = (key: keyof typeof form) => (event: { target: { value: string } }) => {
    setForm((current) => ({ ...current, [key]: event.target.value }));
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    setFieldErrors({});
    try {
      await accountApi.updateProfile(form);
      await refresh();
      navigate('/profile', { replace: true });
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
        <h2 className="h4 mb-3">{t('nav.editProfile')}</h2>
        {error && <Alert variant="danger">{error}</Alert>}

        <form onSubmit={handleSubmit} noValidate>
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
            {t('common.submit')}
          </button>
        </form>
      </div>
    </div>
  );
}
