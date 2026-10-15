import { useI18n } from '../i18n';
import type { Account } from '../api/types';

interface AccountTableProps {
  accounts: Account[];
  showLevel?: boolean;
  showBanned?: boolean;
  showDelete?: boolean;
  onDelete?: (account: Account) => void;
}

export default function AccountTable({
  accounts,
  showLevel = false,
  showBanned = false,
  showDelete = false,
  onDelete,
}: AccountTableProps) {
  const { t } = useI18n();

  if (accounts.length === 0) {
    return <p className="text-muted">{t('common.empty')}</p>;
  }

  return (
    <table className="table table-bordered table-striped align-middle">
      <thead className="table-dark">
        <tr>
          <th scope="col">{t('common.id')}</th>
          <th scope="col">{t('common.username')}</th>
          <th scope="col">{t('common.role')}</th>
          <th scope="col">{t('auth.name')}</th>
          <th scope="col">{t('auth.surname')}</th>
          <th scope="col">{t('auth.phone')}</th>
          <th scope="col">{t('auth.email')}</th>
          {showLevel && <th scope="col">{t('common.level')}</th>}
          {showBanned && <th scope="col">{t('common.banned')}</th>}
          {showDelete && <th scope="col">{t('common.delete')}</th>}
        </tr>
      </thead>
      <tbody>
        {accounts.map((account, index) => (
          <tr key={account.id ?? index}>
            <td>{account.id}</td>
            <td>{account.username}</td>
            <td>{account.role}</td>
            <td>{account.userData?.name}</td>
            <td>{account.userData?.surname}</td>
            <td>{account.userData?.phoneNumber}</td>
            <td>{account.userData?.emailAddress}</td>
            {showLevel && <td>{account.level}</td>}
            {showBanned && (
              <td>
                {account.banned ? (
                  <span className="badge bg-danger">{t('common.banned')}</span>
                ) : (
                  <span className="badge bg-success">OK</span>
                )}
              </td>
            )}
            {showDelete && (
              <td>
                <button
                  type="button"
                  className="btn btn-sm btn-dark"
                  onClick={() => onDelete?.(account)}
                >
                  {t('common.delete')}
                </button>
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  );
}
