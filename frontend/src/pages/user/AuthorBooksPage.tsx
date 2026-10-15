import { useState } from 'react';
import { useParams } from 'react-router-dom';
import Alert from '../../components/Alert';
import BookTable from '../../components/BookTable';
import Loading from '../../components/Loading';
import Pagination from '../../components/Pagination';
import { bookApi } from '../../api/endpoints';
import { useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';

export default function AuthorBooksPage() {
  const { t } = useI18n();
  const { id } = useParams<{ id: string }>();
  const authorId = Number(id);
  const [page, setPage] = useState(0);

  const { data, loading, error } = useApi(
    () => bookApi.byAuthor(authorId, page),
    [authorId, page],
  );

  return (
    <>
      <h2 className="h4 mb-3">{t('books.caption')}</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {loading && <Loading />}
      {data && (
        <>
          <BookTable books={data.content} offset={data.page * data.size} showAdd />
          <Pagination page={data.page} totalPages={data.totalPages} onChange={setPage} />
        </>
      )}
    </>
  );
}
