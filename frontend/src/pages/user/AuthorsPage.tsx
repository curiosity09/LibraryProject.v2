import { useState } from 'react';
import { Link } from 'react-router-dom';
import Alert from '../../components/Alert';
import Loading from '../../components/Loading';
import Pagination from '../../components/Pagination';
import { authorApi } from '../../api/endpoints';
import { useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';

export default function AuthorsPage() {
  const { t } = useI18n();
  const [page, setPage] = useState(0);
  const { data, loading, error } = useApi(() => authorApi.list(page), [page]);

  return (
    <>
      <h2 className="h4 mb-3">{t('authors.caption')}</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {loading && <Loading />}
      {data && (
        <>
          <table className="table table-bordered table-striped align-middle">
            <thead className="table-dark">
              <tr>
                <th scope="col">{t('common.number')}</th>
                <th scope="col">{t('common.author')}</th>
                <th scope="col">{t('nav.books')}</th>
              </tr>
            </thead>
            <tbody>
              {data.content.map((author, index) => (
                <tr key={author.id ?? index}>
                  <td>{data.page * data.size + index + 1}</td>
                  <td>{author.fullName}</td>
                  <td>
                    <Link to={`/authors/${author.id}/books`} className="btn btn-sm btn-dark">
                      {t('authors.showBooks')}
                    </Link>
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
