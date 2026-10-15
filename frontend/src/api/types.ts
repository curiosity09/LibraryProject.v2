export type Role = 'user' | 'librarian' | 'admin';

export type Level = 'HEAD' | 'SECONDARY';

export interface UserData {
  name: string;
  surname: string;
  phoneNumber: string;
  emailAddress: string | null;
}

export interface Account {
  id: number | null;
  username: string;
  role: Role;
  userData: UserData | null;
  /** Serialized from the boolean field `isBanned` (Jackson bean naming). */
  banned: boolean;
  level: Level | null;
  ordersAmount: number | null;
}

export interface Author {
  id: number | null;
  fullName: string;
}

export interface Genre {
  id: number | null;
  name: string;
}

export interface Section {
  id: number | null;
  name: string;
}

export interface Book {
  id: number | null;
  name: string;
  author: Author;
  genre: Genre;
  section: Section;
  quantity: number;
  publicationYear: number;
}

/** `rentalTime` / `rentalPeriod` are ISO-8601 `LocalDateTime` strings. */
export interface Order {
  id: number | null;
  bookList: Book[];
  user: Account;
  rentalTime: string;
  rentalPeriod: string;
}

export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface FileInfo {
  fileName: string;
  url: string;
}
