import type { ReactNode } from 'react';

type Variant = 'success' | 'danger' | 'warning' | 'info' | 'dark';

interface AlertProps {
  variant?: Variant;
  children: ReactNode;
  onClose?: () => void;
}

export default function Alert({ variant = 'dark', children, onClose }: AlertProps) {
  return (
    <div
      className={`alert alert-${variant}${onClose ? ' alert-dismissible' : ''}`}
      role="alert"
    >
      {children}
      {onClose && (
        <button type="button" className="btn-close" aria-label="Close" onClick={onClose} />
      )}
    </div>
  );
}
