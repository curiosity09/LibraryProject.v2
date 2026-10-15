import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import Alert from '../../components/Alert';
import Loading from '../../components/Loading';
import { librarianApi } from '../../api/endpoints';
import { useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';

export default function FindUserOrdersPage() {
  const { t } = useI18n();
  const navigate = useNavigate();
  const { data: users, loading, error } = useApi(() => librarianApi.users(), []);
  const [username, setUsername] = useState('');

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();
    if (username) {
      navigate(`/librarian/orders/user/${encodeURIComponent(username)}`);
    }
  };

  return (
    <div className="row justify-content-center">
      <div className="col-12 col-md-5 col-lg-4">
        <h2 className="h4 mb-3">{t('nav.userOrders')}</h2>
        {error && <Alert variant="danger">{error}</Alert>}
        {loading && <Loading />}
        {users && (
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="selectUser" className="form-label">
                {t('findUser.selectUser')}
              </label>
              <select
                id="selectUser"
                className="form-select"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
              >
                <option value="">—</option>
                {users.map((user) => (
                  <option key={user.id ?? user.username} value={user.username}>
                    {user.username}
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
