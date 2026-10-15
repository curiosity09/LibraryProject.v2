import { useState } from 'react';
import Alert from '../../components/Alert';
import Loading from '../../components/Loading';
import { fileApi } from '../../api/endpoints';
import { errorMessage, useApi } from '../../hooks/useApi';
import { useI18n } from '../../i18n';

export default function FilesPage() {
  const { t } = useI18n();
  const { data: files, loading, error, reload } = useApi(() => fileApi.list(), []);
  const [actionError, setActionError] = useState<string | null>(null);
  const [uploading, setUploading] = useState(false);

  const handleUpload = async (list: FileList | null) => {
    const file = list?.[0];
    if (!file) {
      return;
    }
    setUploading(true);
    setActionError(null);
    try {
      await fileApi.upload(file);
      await reload();
    } catch (caught) {
      setActionError(errorMessage(caught));
    } finally {
      setUploading(false);
    }
  };

  const handleDelete = async (fileName: string) => {
    setActionError(null);
    try {
      await fileApi.remove(fileName);
      await reload();
    } catch (caught) {
      setActionError(errorMessage(caught));
    }
  };

  return (
    <>
      <h2 className="h4 mb-3">{t('librarian.files.title')}</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {actionError && <Alert variant="danger">{actionError}</Alert>}

      <div className="mb-3" style={{ maxWidth: 480 }}>
        <input
          type="file"
          className="form-control"
          disabled={uploading}
          onChange={(event) => {
            void handleUpload(event.target.files);
            event.target.value = '';
          }}
        />
      </div>

      {loading && <Loading />}
      {files && files.length === 0 && <p className="text-muted">{t('librarian.files.empty')}</p>}
      {files && files.length > 0 && (
        <table className="table table-hover align-middle">
          <thead className="table-dark">
            <tr>
              <th scope="col">{t('librarian.files.fileName')}</th>
              <th scope="col">{t('librarian.files.link')}</th>
              <th scope="col">{t('common.actions')}</th>
            </tr>
          </thead>
          <tbody>
            {files.map((file) => (
              <tr key={file.fileName}>
                <td>{file.fileName}</td>
                <td>
                  <a href={file.url}>{t('librarian.files.download')}</a>
                </td>
                <td>
                  <button
                    type="button"
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => handleDelete(file.fileName)}
                  >
                    {t('common.delete')}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  );
}
