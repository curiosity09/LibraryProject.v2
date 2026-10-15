import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Alert from '../../components/Alert';
import { orderApi } from '../../api/endpoints';
import { errorMessage } from '../../hooks/useApi';
import { useCart } from '../../contexts/CartContext';
import { useI18n } from '../../i18n';

const PERIODS = [
  { value: 1, labelKey: 'cart.period.readingRoom' },
  { value: 14, labelKey: 'cart.period.days14' },
  { value: 30, labelKey: 'cart.period.days30' },
];

export default function CartPage() {
  const { t } = useI18n();
  const { items, remove, clear } = useCart();
  const navigate = useNavigate();

  const [rentalPeriod, setRentalPeriod] = useState(1);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const handleCreate = async () => {
    const bookIds = items
      .map((item) => item.id)
      .filter((id): id is number => id !== null);
    if (bookIds.length === 0) {
      return;
    }
    setSubmitting(true);
    setError(null);
    try {
      await orderApi.create(bookIds, rentalPeriod);
      clear();
      navigate('/orders', { replace: true });
    } catch (caught) {
      setError(errorMessage(caught));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <h2 className="h4 mb-3">{t('cart.caption')}</h2>
      {error && <Alert variant="danger">{error}</Alert>}

      {items.length === 0 ? (
        <p className="text-muted">{t('cart.empty')}</p>
      ) : (
        <>
          <table className="table table-bordered table-striped align-middle">
            <thead className="table-dark">
              <tr>
                <th scope="col">{t('common.number')}</th>
                <th scope="col">{t('common.book')}</th>
                <th scope="col">{t('common.author')}</th>
                <th scope="col">{t('common.year')}</th>
                <th scope="col">{t('common.delete')}</th>
              </tr>
            </thead>
            <tbody>
              {items.map((book, index) => (
                <tr key={book.id ?? index}>
                  <td>{index + 1}</td>
                  <td>{book.name}</td>
                  <td>{book.author.fullName}</td>
                  <td>{book.publicationYear}</td>
                  <td>
                    <button
                      type="button"
                      className="btn btn-sm btn-dark"
                      onClick={() => book.id !== null && remove(book.id)}
                    >
                      {t('common.delete')}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <fieldset className="mb-3">
            <legend className="h6">{t('cart.period')}</legend>
            {PERIODS.map((period) => (
              <div className="form-check" key={period.value}>
                <input
                  className="form-check-input"
                  type="radio"
                  name="rentalPeriod"
                  id={`period-${period.value}`}
                  value={period.value}
                  checked={rentalPeriod === period.value}
                  onChange={() => setRentalPeriod(period.value)}
                />
                <label className="form-check-label" htmlFor={`period-${period.value}`}>
                  {t(period.labelKey)}
                </label>
              </div>
            ))}
          </fieldset>

          <button
            type="button"
            className="btn btn-dark"
            onClick={handleCreate}
            disabled={submitting}
          >
            {t('cart.create')}
          </button>
        </>
      )}
    </>
  );
}
