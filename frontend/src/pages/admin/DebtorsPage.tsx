import { useState, type FormEvent } from 'react';
import Alert from '../../components/Alert';
import Loading from '../../components/Loading';
import { adminApi } from '../../api/endpoints';
import { errorMessage, useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';

export default function DebtorsPage() {
  const { t } = useI18n();
  const { data: debtors, loading, error, reload } = useApi(() => adminApi.debtors(), []);
  const [username, setUsername] = useState('');
  const [message, setMessage] = useState<string | null>(null);
  const [actionError, setActionError] = useState<string | null>(null);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    if (!username) {
      return;
    }
    setMessage(null);
    setActionError(null);
    try {
      await adminApi.blockUser(username);
      setMessage(t('alerts.blocked'));
      setUsername('');
      await reload();
    } catch (caught) {
      setActionError(errorMessage(caught));
    }
  };

  return (
    <div className="row justify-content-center">
      <div className="col-12 col-md-5 col-lg-4">
        <h2 className="h4 mb-3">{t('nav.debtors')}</h2>
        {error && <Alert variant="danger">{error}</Alert>}
        {actionError && <Alert variant="danger">{actionError}</Alert>}
        {message && <Alert variant="success">{message}</Alert>}
        {loading && <Loading />}

        {debtors && (
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="selectDebtor" className="form-label">
                {t('findUser.selectUser')}
              </label>
              <select
                id="selectDebtor"
                className="form-select"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
              >
                <option value="">—</option>
                {debtors.map((debtor) => (
                  <option key={debtor.id ?? debtor.username} value={debtor.username}>
                    {debtor.username}
                  </option>
                ))}
              </select>
            </div>
            <button type="submit" className="btn btn-dark" disabled={!username}>
              {t('common.submit')}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}
