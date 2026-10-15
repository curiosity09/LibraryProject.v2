import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import { authApi, type LoginPayload, type RegisterPayload } from '../api/endpoints';
import type { Account } from '../api/types';

interface AuthValue {
  account: Account | null;
  initializing: boolean;
  login: (payload: LoginPayload) => Promise<Account>;
  register: (payload: RegisterPayload) => Promise<Account>;
  logout: () => Promise<void>;
  refresh: () => Promise<void>;
}

const AuthContext = createContext<AuthValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [account, setAccount] = useState<Account | null>(null);
  const [initializing, setInitializing] = useState(true);

  const refresh = useCallback(async () => {
    try {
      setAccount(await authApi.me());
    } catch {
      setAccount(null);
    }
  }, []);

  useEffect(() => {
    let active = true;
    authApi
      .me()
      .then((current) => {
        if (active) {
          setAccount(current);
        }
      })
      .catch(() => {
        if (active) {
          setAccount(null);
        }
      })
      .finally(() => {
        if (active) {
          setInitializing(false);
        }
      });
    return () => {
      active = false;
    };
  }, []);

  const login = useCallback(async (payload: LoginPayload) => {
    const current = await authApi.login(payload);
    setAccount(current);
    return current;
  }, []);

  const register = useCallback(async (payload: RegisterPayload) => {
    const current = await authApi.register(payload);
    setAccount(current);
    return current;
  }, []);

  const logout = useCallback(async () => {
    try {
      await authApi.logout();
    } finally {
      setAccount(null);
    }
  }, []);

  const value = useMemo<AuthValue>(
    () => ({ account, initializing, login, register, logout, refresh }),
    [account, initializing, login, register, logout, refresh],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthValue {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used inside an AuthProvider');
  }
  return context;
}
