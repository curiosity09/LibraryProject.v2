import { Link } from 'react-router-dom';
import { useI18n } from '../i18n';

export default function ErrorPage() {
  const { t } = useI18n();
  return (
    <div className="centered-page">
      <div>
        <h1 className="h3">{t('error.text')}</h1>
        <p className="mt-3">
          <Link to="/" className="alert-link">
            {t('nav.home')}
          </Link>
        </p>
      </div>
    </div>
  );
}
