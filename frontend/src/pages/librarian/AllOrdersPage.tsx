import { useState } from 'react';
import Alert from '../../components/Alert';
import BookList from '../../components/BookList';
import Loading from '../../components/Loading';
import Pagination from '../../components/Pagination';
import { orderApi } from '../../api/endpoints';
import { useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';
import { formatDateTime, isExpired } from '../../utils';

export default function AllOrdersPage() {
  const { t } = useI18n();
  const [page, setPage] = useState(0);
  const { data, loading, error } = useApi(() => orderApi.all(page), [page]);

  return (
    <>
      <h2 className="h4 mb-3">{t('orders.allCaption')}</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {loading && <Loading />}
      {data && data.content.length === 0 && <p className="text-muted">{t('common.empty')}</p>}
      {data && data.content.length > 0 && (
        <>
          <table className="table table-bordered table-striped align-middle">
            <thead className="table-dark">
              <tr>
                <th scope="col">{t('common.number')}</th>
                <th scope="col">{t('common.book')}</th>
                <th scope="col">{t('common.user')}</th>
                <th scope="col">{t('common.rentalTime')}</th>
                <th scope="col">{t('common.rentalPeriod')}</th>
                <th scope="col">{t('common.status')}</th>
              </tr>
            </thead>
            <tbody>
              {data.content.map((order, index) => (
                <tr key={order.id ?? index}>
                  <td>{data.page * data.size + index + 1}</td>
                  <td>
                    <BookList books={order.bookList} />
                  </td>
                  <td>
                    <p className="mb-0">{order.user.username}</p>
                    <p className="text-muted small mb-0">{order.user.userData?.name}</p>
                    <p className="text-muted small mb-0">{order.user.userData?.surname}</p>
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
