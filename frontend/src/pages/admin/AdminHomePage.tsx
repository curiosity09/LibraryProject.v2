import { Link } from 'react-router-dom';
import { useI18n } from '../../i18n';

export default function AdminHomePage() {
  const { t } = useI18n();
  return (
    <>
      <h2 className="h4 mb-3">{t('admin.pageText')}</h2>
      <div className="list-group" style={{ maxWidth: 420 }}>
        <Link className="list-group-item list-group-item-action" to="/admin/users">
          {t('nav.allUsers')}
        </Link>
        <Link className="list-group-item list-group-item-action" to="/admin/admins">
          {t('nav.admins')}
        </Link>
        <Link className="list-group-item list-group-item-action" to="/admin/librarians">
          {t('nav.librarians')}
        </Link>
        <Link className="list-group-item list-group-item-action" to="/admin/employees/new">
          {t('nav.newEmployee')}
        </Link>
        <Link className="list-group-item list-group-item-action" to="/admin/debtors">
          {t('nav.debtors')}
        </Link>
      </div>
    </>
  );
}
