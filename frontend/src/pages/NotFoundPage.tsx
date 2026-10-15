import { Link } from 'react-router-dom';
import { useI18n } from '../i18n';

export default function NotFoundPage() {
  const { t } = useI18n();
  return (
    <div className="centered-page">
      <div>
        <h1 className="display-4">404</h1>
        <p className="h5 text-muted">{t('notFound.text')}</p>
        <p className="mt-3">
          <Link to="/" className="alert-link">
            {t('nav.home')}
          </Link>
        </p>
      </div>
    </div>
  );
}
