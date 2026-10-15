import { useEffect, useState, type FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Alert from '../../components/Alert';
import { authorApi, bookApi, genreApi, sectionApi } from '../../api/endpoints';
import { errorMessage, useApi } from '../../hooks/useApi';
import { ApiError } from '../../api/client';
import { useI18n } from '../../i18n';
import type { Book } from '../../api/types';

interface FormState {
  name: string;
  authorId: string;
  genreId: string;
  sectionId: string;
  quantity: string;
  publicationYear: string;
}

const emptyForm: FormState = {
  name: '',
  authorId: '',
  genreId: '',
  sectionId: '',
  quantity: '1',
  publicationYear: String(new Date().getFullYear()),
};

export default function BookFormPage() {
  const { t } = useI18n();
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const bookId = id === undefined ? null : Number(id);

  const { data: authors } = useApi(() => authorApi.all(), []);
  const { data: genres } = useApi(() => genreApi.all(), []);
  const { data: sections } = useApi(() => sectionApi.all(), []);

  const [form, setForm] = useState<FormState>(emptyForm);
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (bookId === null) {
      return;
    }
    bookApi
      .byId(bookId)
      .then((book) =>
        setForm({
          name: book.name,
          authorId: String(book.author.id),
          genreId: String(book.genre.id),
          sectionId: String(book.section.id),
          quantity: String(book.quantity),
          publicationYear: String(book.publicationYear),
        }),
      )
      .catch((caught) => setError(errorMessage(caught)));
  }, [bookId]);

  const update = (key: keyof FormState) => (event: { target: { value: string } }) => {
    setForm((current) => ({ ...current, [key]: event.target.value }));
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const author = authors?.find((item) => String(item.id) === form.authorId);
    const genre = genres?.find((item) => String(item.id) === form.genreId);
    const section = sections?.find((item) => String(item.id) === form.sectionId);
    if (!author || !genre || !section) {
      setError(t('common.empty'));
      return;
    }

    const book: Book = {
      id: bookId,
      name: form.name,
      author,
      genre,
      section,
      quantity: Number(form.quantity),
      publicationYear: Number(form.publicationYear),
    };

    setSubmitting(true);
    setError(null);
    setFieldErrors({});
    try {
      if (bookId === null) {
        await bookApi.create(book);
      } else {
        await bookApi.update(bookId, book);
      }
      navigate('/librarian/books', { replace: true });
    } catch (caught) {
      if (caught instanceof ApiError && Object.keys(caught.fieldErrors).length > 0) {
        setFieldErrors(caught.fieldErrors);
      }
      setError(errorMessage(caught));
    } finally {
      setSubmitting(false);
    }
  };

  const fieldError = (key: string) => fieldErrors[key];

  return (
    <div className="row justify-content-center">
      <div className="col-12 col-md-6 col-lg-5">
        <h2 className="h4 mb-3">{bookId === null ? t('nav.addBook') : t('common.editBook')}</h2>
        {error && <Alert variant="danger">{error}</Alert>}

        <form onSubmit={handleSubmit} noValidate>
          <div className="mb-3">
            <label htmlFor="inputNameBook" className="form-label">
              {t('common.book')}
            </label>
            <input
              id="inputNameBook"
              type="text"
              className="form-control"
              placeholder={t('placeholder.bookName')}
              value={form.name}
              onChange={update('name')}
              required
            />
            {fieldError('name') && <div className="text-danger small mt-1">{fieldError('name')}</div>}
          </div>

          <div className="mb-3">
            <label htmlFor="selectAuthor" className="form-label">
              {t('common.author')}
            </label>
            <select
              id="selectAuthor"
              className="form-select"
              value={form.authorId}
              onChange={update('authorId')}
              required
            >
              <option value="">—</option>
              {authors?.map((author) => (
                <option key={author.id ?? 0} value={String(author.id)}>
                  {author.fullName}
                </option>
              ))}
            </select>
          </div>

          <div className="mb-3">
            <label htmlFor="selectGenre" className="form-label">
              {t('common.genre')}
            </label>
            <select
              id="selectGenre"
              className="form-select"
              value={form.genreId}
              onChange={update('genreId')}
              required
            >
              <option value="">—</option>
              {genres?.map((genre) => (
                <option key={genre.id ?? 0} value={String(genre.id)}>
                  {genre.name}
                </option>
              ))}
            </select>
          </div>

          <div className="mb-3">
            <label htmlFor="selectSection" className="form-label">
              {t('common.section')}
            </label>
            <select
              id="selectSection"
              className="form-select"
              value={form.sectionId}
              onChange={update('sectionId')}
              required
            >
              <option value="">—</option>
              {sections?.map((section) => (
                <option key={section.id ?? 0} value={String(section.id)}>
                  {section.name}
                </option>
              ))}
            </select>
          </div>

          <div className="mb-3">
            <label htmlFor="inputQuantity" className="form-label">
              {t('common.quantity')}
            </label>
            <input
              id="inputQuantity"
              type="number"
              min={0}
              max={99}
              className="form-control"
              placeholder={t('placeholder.quantity')}
              value={form.quantity}
              onChange={update('quantity')}
              required
            />
            {fieldError('quantity') && (
              <div className="text-danger small mt-1">{fieldError('quantity')}</div>
            )}
          </div>

          <div className="mb-3">
            <label htmlFor="inputYear" className="form-label">
              {t('common.year')}
            </label>
            <input
              id="inputYear"
              type="number"
              min={1800}
              max={2100}
              className="form-control"
              placeholder={t('placeholder.year')}
              value={form.publicationYear}
              onChange={update('publicationYear')}
              required
            />
            {fieldError('publicationYear') && (
              <div className="text-danger small mt-1">{fieldError('publicationYear')}</div>
            )}
          </div>

          <button type="submit" className="btn btn-dark" disabled={submitting}>
            {t('common.submit')}
          </button>
        </form>
      </div>
    </div>
  );
}
