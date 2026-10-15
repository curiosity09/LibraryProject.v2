import { useState } from 'react';
import { useParams } from 'react-router-dom';
import Alert from '../../components/Alert';
import BookList from '../../components/BookList';
import Loading from '../../components/Loading';
import Pagination from '../../components/Pagination';
import { orderApi } from '../../api/endpoints';
import { errorMessage, useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';
import { formatDateTime, isExpired } from '../../utils';

export default function UserOrdersPage() {
  const { t } = useI18n();
  const { username = '' } = useParams<{ username: string }>();
  const [page, setPage] = useState(0);
  const { data, loading, error, reload } = useApi(
    () => orderApi.byUser(username, page),
    [username, page],
  );
  const [actionError, setActionError] = useState<string | null>(null);

  const handleFinish = async (orderId: number | null) => {
    if (orderId === null) {
      return;
    }
    setActionError(null);
    try {
      await orderApi.finish(orderId);
      await reload();
    } catch (caught) {
      setActionError(errorMessage(caught));
    }
  };

  return (
    <>
      <h2 className="h4 mb-3">
        {t('orders.userCaption')}: <span className="text-muted">{username}</span>
      </h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {actionError && <Alert variant="danger">{actionError}</Alert>}
      {loading && <Loading />}
      {data && data.content.length === 0 && <p className="text-muted">{t('common.empty')}</p>}
      {data && data.content.length > 0 && (
        <>
          <table className="table table-bordered table-striped align-middle">
            <thead className="table-dark">
              <tr>
                <th scope="col">{t('common.number')}</th>
                <th scope="col">{t('common.book')}</th>
                <th scope="col">{t('common.rentalTime')}</th>
                <th scope="col">{t('common.rentalPeriod')}</th>
                <th scope="col">{t('common.status')}</th>
                <th scope="col">{t('common.delete')}</th>
              </tr>
            </thead>
            <tbody>
              {data.content.map((order, index) => (
                <tr key={order.id ?? index}>
                  <td>{data.page * data.size + index + 1}</td>
                  <td>
                    <BookList books={order.bookList} />
                  </td>
                  <td>{formatDateTime(order.rentalTime)}</td>
                  <td>{formatDateTime(order.rentalPeriod)}</td>
                  <td>
                    {isExpired(order.rentalPeriod) ? (
                      <span className="badge bg-secondary">{t('status.expired')}</span>
                    ) : (
                      <span className="badge bg-success">{t('status.active')}</span>
                    )}
                  </td>
                  <td>
                    <button
                      type="button"
                      className="btn btn-sm btn-dark"
                      onClick={() => handleFinish(order.id)}
                    >
                      {t('orders.finish')}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <Pagination page={data.page} totalPages={data.totalPages} onChange={setPage} />
        </>
      )}
    </>
  );
}
