import { useState, type FormEvent } from 'react';
import { Link } from 'react-router-dom';
import Alert from '../../components/Alert';
import { fileApi } from '../../api/endpoints';
import { errorMessage } from '../../hooks/useApi';
import { useI18n } from '../../i18n';

export default function LibrarianHomePage() {
  const { t } = useI18n();
  const [file, setFile] = useState<File | null>(null);
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [uploading, setUploading] = useState(false);

  const handleUpload = async (event: FormEvent) => {
    event.preventDefault();
    if (!file) {
      return;
    }
    setUploading(true);
    setError(null);
    setMessage(null);
    try {
      const uploaded = await fileApi.upload(file);
      setMessage(`${t('alerts.added')}: ${uploaded.fileName}`);
      setFile(null);
    } catch (caught) {
      setError(errorMessage(caught));
    } finally {
      setUploading(false);
    }
  };

  return (
    <>
      <h2 className="h4 mb-3">{t('librarian.pageText')}</h2>

      <div className="card" style={{ maxWidth: 520 }}>
        <div className="card-body">
          <h3 className="h6 card-title">{t('librarian.upload.title')}</h3>
          {error && <Alert variant="danger">{error}</Alert>}
          {message && <Alert variant="success">{message}</Alert>}
          <form onSubmit={handleUpload}>
            <input
              type="file"
              className="form-control mb-2"
              onChange={(event) => setFile(event.target.files?.[0] ?? null)}
            />
            <button type="submit" className="btn btn-dark" disabled={!file || uploading}>
              {t('librarian.upload.button')}
            </button>
          </form>
          <p className="mt-3 mb-0">
            <Link to="/librarian/files">{t('nav.files')}</Link>
          </p>
        </div>
      </div>
    </>
  );
}
