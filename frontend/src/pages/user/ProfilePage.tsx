import { useAuth } from '../../contexts/AuthContext';
import { useI18n } from '../../i18n';

export default function ProfilePage() {
  const { t } = useI18n();
  const { account } = useAuth();
  const userData = account?.userData;

  return (
    <>
      <h2 className="h4 mb-3">{t('profile.caption')}</h2>
      <table className="table table-bordered table-striped align-middle">
        <thead className="table-dark">
          <tr>
            <th scope="col">{t('common.username')}</th>
            <th scope="col">{t('auth.name')}</th>
            <th scope="col">{t('auth.surname')}</th>
            <th scope="col">{t('auth.phone')}</th>
            <th scope="col">{t('auth.email')}</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{account?.username}</td>
            <td>{userData?.name}</td>
            <td>{userData?.surname}</td>
            <td>{userData?.phoneNumber}</td>
            <td>{userData?.emailAddress}</td>
          </tr>
        </tbody>
      </table>
    </>
  );
}
