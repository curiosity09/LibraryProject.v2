import { useCart } from '../contexts/CartContext';
import { useI18n } from '../i18n';
import type { Book } from '../api/types';

interface BookTableProps {
  books: Book[];
  offset?: number;
  showAdd?: boolean;
}

export default function BookTable({ books, offset = 0, showAdd = false }: BookTableProps) {
  const { t } = useI18n();
  const { add, has } = useCart();

  if (books.length === 0) {
    return <p className="text-muted">{t('common.empty')}</p>;
  }

  return (
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
          {showAdd && <th scope="col">{t('common.add')}</th>}
        </tr>
      </thead>
      <tbody>
        {books.map((book, index) => (
          <tr key={book.id ?? index}>
            <td>{offset + index + 1}</td>
            <td>{book.name}</td>
            <td>{book.author.fullName}</td>
            <td>{book.genre.name}</td>
            <td>{book.section.name}</td>
            <td>{book.quantity}</td>
            <td>{book.publicationYear}</td>
            {showAdd && (
              <td>
                <button
                  type="button"
                  className="btn btn-sm btn-dark"
                  onClick={() => add(book)}
                  disabled={has(book.id) || book.quantity <= 0}
                >
                  {has(book.id) ? t('books.addToCart') : t('books.select')}
                </button>
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  );
}
