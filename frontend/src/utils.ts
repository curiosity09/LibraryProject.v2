import type { Role } from './api/types';

/** Landing route for a role after login / when a guard rejects access. */
export function homePathForRole(role: Role | null | undefined): string {
  switch (role) {
    case 'librarian':
      return '/librarian';
    case 'admin':
      return '/admin';
    default:
      return '/books';
  }
}

/** Formats an ISO-8601 `LocalDateTime` string for display. */
export function formatDateTime(value: string | null | undefined): string {
  if (!value) {
    return '—';
  }
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return value;
  }
  return date.toLocaleString();
}

/** Whether the rental due date has already passed. */
export function isExpired(rentalPeriod: string | null | undefined): boolean {
  if (!rentalPeriod) {
    return false;
  }
  const due = new Date(rentalPeriod);
  if (Number.isNaN(due.getTime())) {
    return false;
  }
  return due.getTime() <= Date.now();
}
