import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { homePathForRole } from '../utils';
import type { Role } from '../api/types';
import Loading from './Loading';

/** Blocks anonymous visitors and banned accounts before rendering the app shell. */
export function RequireAuth() {
  const { account, initializing } = useAuth();
  const location = useLocation();

  if (initializing) {
    return <Loading />;
  }
  if (!account) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }
  if (account.banned) {
    return <Navigate to="/banned" replace />;
  }
  return <Outlet />;
}

/** Restricts a nested route group to the given roles. */
export function RoleGuard({ roles }: { roles: Role[] }) {
  const { account } = useAuth();

  if (!account) {
    return <Navigate to="/login" replace />;
  }
  if (!roles.includes(account.role)) {
    return <Navigate to={homePathForRole(account.role)} replace />;
  }
  return <Outlet />;
}
