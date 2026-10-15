import { useState } from 'react';
import Alert from '../../components/Alert';
import AccountTable from '../../components/AccountTable';
import Loading from '../../components/Loading';
import Pagination from '../../components/Pagination';
import { adminApi } from '../../api/endpoints';
import { errorMessage, useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';

export default function UsersPage() {
  const { t } = useI18n();
  const [page, setPage] = useState(0);
  const { data, loading, error, reload } = useApi(() => adminApi.users(page), [page]);
  const [actionError, setActionError] = useState<string | null>(null);

  const handleDelete = async (id: number | null) => {
    if (id === null) {
      return;
    }
    setActionError(null);
    try {
      await adminApi.deleteUser(id);
      await reload();
    } catch (caught) {
      setActionError(errorMessage(caught));
    }
  };

  return (
    <>
      <h2 className="h4 mb-3">{t('admin.users.caption')}</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {actionError && <Alert variant="danger">{actionError}</Alert>}
      {loading && <Loading />}
      {data && (
        <>
          <AccountTable
            accounts={data.content}
            showBanned
            showDelete
            onDelete={(account) => void handleDelete(account.id)}
          />
          <Pagination page={data.page} totalPages={data.totalPages} onChange={setPage} />
        </>
      )}
    </>
  );
}
