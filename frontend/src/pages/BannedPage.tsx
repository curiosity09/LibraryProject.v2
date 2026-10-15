import { Link } from 'react-router-dom';
import { useI18n } from '../i18n';

export default function BannedPage() {
  const { t } = useI18n();
  return (
    <div className="centered-page">
      <div>
        <img src="/images/banCat.jpg" alt={t('common.banned')} className="mb-4" style={{ maxWidth: 240 }} />
        <h1 className="h3">{t('ban.text')}</h1>
        <p className="mt-3">
          <Link to="/login" className="alert-link">
            {t('nav.login')}
          </Link>
        </p>
      </div>
    </div>
  );
}
