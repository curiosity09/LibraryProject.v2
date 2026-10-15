import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import Alert from './Alert';
import { errorMessage } from '../hooks/useApi';
import { ApiError } from '../api/client';
import { useI18n } from '../i18n';

interface NamedEntityFormProps {
  labelKey: string;
  placeholderKey: string;
  onSubmit: (value: string) => Promise<unknown>;
  redirectTo: string;
}

export default function NamedEntityForm({
  labelKey,
  placeholderKey,
  onSubmit,
  redirectTo,
}: NamedEntityFormProps) {
  const { t } = useI18n();
  const navigate = useNavigate();
  const [value, setValue] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [fieldError, setFieldError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    setFieldError(null);
    try {
      await onSubmit(value);
      navigate(redirectTo, { replace: true });
    } catch (caught) {
      if (caught instanceof ApiError) {
        setFieldError(caught.fieldErrors.name ?? caught.fieldErrors.fullName ?? null);
      }
      setError(errorMessage(caught));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="row justify-content-center">
      <div className="col-12 col-md-6 col-lg-5">
        <h2 className="h4 mb-3">{t(labelKey)}</h2>
        {error && <Alert variant="danger">{error}</Alert>}

        <form onSubmit={handleSubmit} noValidate>
          <div className="mb-3">
            <label htmlFor="inputName" className="form-label">
              {t(labelKey)}
            </label>
            <input
              id="inputName"
              type="text"
              className="form-control"
              placeholder={t(placeholderKey)}
              value={value}
              onChange={(event) => setValue(event.target.value)}
              required
            />
            {fieldError && <div className="text-danger small mt-1">{fieldError}</div>}
          </div>

          <button type="submit" className="btn btn-dark" disabled={submitting}>
            {t('common.submit')}
          </button>
        </form>
      </div>
    </div>
  );
}
