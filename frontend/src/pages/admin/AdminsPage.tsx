import { useState } from 'react';
import Alert from '../../components/Alert';
import AccountTable from '../../components/AccountTable';
import Loading from '../../components/Loading';
import Pagination from '../../components/Pagination';
import { adminApi } from '../../api/endpoints';
import { useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';

export default function AdminsPage() {
  const { t } = useI18n();
  const [page, setPage] = useState(0);
  const { data, loading, error } = useApi(() => adminApi.admins(page), [page]);

  return (
    <>
      <h2 className="h4 mb-3">{t('nav.admins')}</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {loading && <Loading />}
      {data && (
        <>
          <AccountTable accounts={data.content} showLevel />
          <Pagination page={data.page} totalPages={data.totalPages} onChange={setPage} />
        </>
      )}
    </>
  );
}
