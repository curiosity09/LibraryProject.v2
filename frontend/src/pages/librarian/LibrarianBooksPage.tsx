import { useState } from 'react';
import { Link } from 'react-router-dom';
import Alert from '../../components/Alert';
import Loading from '../../components/Loading';
import Pagination from '../../components/Pagination';
import { bookApi } from '../../api/endpoints';
import { useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';

export default function LibrarianBooksPage() {
  const { t } = useI18n();
  const [page, setPage] = useState(0);
  const { data, loading, error } = useApi(() => bookApi.list(page), [page]);

  return (
    <>
      <h2 className="h4 mb-3">{t('books.caption')}</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {loading && <Loading />}
      {data && (
        <>
          <table className="table table-bordered table-striped align-middle">
            <thead className="table-dark">
              <tr>
                <th scope="col">{t('common.number')}</th>
                <th scope="col">{t('common.book')}</th>
                <th scope="col">{t('common.author')}</th>
                <th scope="col">{t('common.genre')}</th>
                <th scope="col">{t('common.section')}</th>
                <th scope="col">{t('common.quantity')}</th>
                <th scope="col">{t('common.year')}</th>
                <th scope="col">{t('common.editBook')}</th>
              </tr>
            </thead>
            <tbody>
              {data.content.map((book, index) => (
                <tr key={book.id ?? index}>
                  <td>{data.page * data.size + index + 1}</td>
                  <td>{book.name}</td>
                  <td>{book.author.fullName}</td>
                  <td>{book.genre.name}</td>
                  <td>{book.section.name}</td>
                  <td>{book.quantity}</td>
                  <td>{book.publicationYear}</td>
                  <td>
                    <Link to={`/librarian/books/${book.id}/edit`} className="btn btn-sm btn-dark">
                      {t('common.edit')}
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
