import { useI18n } from '../i18n';

export default function Loading() {
  const { t } = useI18n();
  return (
    <div className="text-center py-5 text-muted">
      <div className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true" />
      {t('common.loading')}
    </div>
  );
}
