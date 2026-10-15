import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import type { Book } from '../api/types';

interface CartValue {
  items: Book[];
  count: number;
  add: (book: Book) => void;
  remove: (bookId: number) => void;
  clear: () => void;
  has: (bookId: number | null) => boolean;
}

const CartContext = createContext<CartValue | null>(null);
const STORAGE_KEY = 'library.cart';

function loadCart(): Book[] {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return [];
    }
    const parsed: unknown = JSON.parse(raw);
    return Array.isArray(parsed) ? (parsed as Book[]) : [];
  } catch {
    return [];
  }
}

export function CartProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<Book[]>(loadCart);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
  }, [items]);

  const add = useCallback((book: Book) => {
    setItems((current) => {
      if (current.some((item) => item.id === book.id)) {
        return current;
      }
      return [...current, book];
    });
  }, []);

  const remove = useCallback((bookId: number) => {
    setItems((current) => current.filter((item) => item.id !== bookId));
  }, []);

  const clear = useCallback(() => setItems([]), []);

  const has = useCallback(
    (bookId: number | null) => bookId !== null && items.some((item) => item.id === bookId),
    [items],
  );

  const value = useMemo<CartValue>(
    () => ({ items, count: items.length, add, remove, clear, has }),
    [items, add, remove, clear, has],
  );

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart(): CartValue {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used inside a CartProvider');
  }
  return context;
}
