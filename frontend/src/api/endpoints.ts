import { apiRequest, apiUpload } from './client';
import type { Account, Author, Book, FileInfo, Genre, Order, Page, Section } from './types';

export interface LoginPayload {
  username: string;
  password: string;
}

export interface RegisterPayload {
  username: string;
  password: string;
  name: string;
  surname: string;
  phoneNumber: string;
  emailAddress: string;
}

export interface UpdateProfilePayload {
  password?: string;
  name: string;
  surname: string;
  phoneNumber: string;
  emailAddress: string;
}

export interface EmployeePayload {
  username: string;
  password: string;
  role: 'librarian' | 'admin';
  level: 'HEAD' | 'SECONDARY';
  name: string;
  surname: string;
  phoneNumber: string;
  emailAddress: string;
}

const page = (value: number) => `page=${value}`;
const enc = encodeURIComponent;

export const authApi = {
  login: (payload: LoginPayload) =>
    apiRequest<Account>('/api/auth/login', { method: 'POST', body: payload }),
  register: (payload: RegisterPayload) =>
    apiRequest<Account>('/api/auth/register', { method: 'POST', body: payload }),
  logout: () => apiRequest<void>('/api/auth/logout', { method: 'POST' }),
  me: () => apiRequest<Account>('/api/auth/me'),
};

export const accountApi = {
  updateProfile: (payload: UpdateProfilePayload) =>
    apiRequest<Account>('/api/account/me', { method: 'PUT', body: payload }),
};

export const bookApi = {
  list: (pageIndex = 0) => apiRequest<Page<Book>>(`/api/books?${page(pageIndex)}`),
  byId: (id: number) => apiRequest<Book>(`/api/books/${id}`),
  byAuthor: (authorId: number, pageIndex = 0) =>
    apiRequest<Page<Book>>(`/api/books/by-author/${authorId}?${page(pageIndex)}`),
  create: (book: Book) => apiRequest<Book>('/api/books', { method: 'POST', body: book }),
  update: (id: number, book: Book) =>
    apiRequest<Book>(`/api/books/${id}`, { method: 'PUT', body: book }),
};

export const authorApi = {
  list: (pageIndex = 0) => apiRequest<Page<Author>>(`/api/authors?${page(pageIndex)}`),
  all: () => apiRequest<Author[]>('/api/authors/all'),
  create: (author: Author) => apiRequest<Author>('/api/authors', { method: 'POST', body: author }),
};

export const genreApi = {
  all: () => apiRequest<Genre[]>('/api/genres'),
  create: (genre: Genre) => apiRequest<Genre>('/api/genres', { method: 'POST', body: genre }),
};

export const sectionApi = {
  all: () => apiRequest<Section[]>('/api/sections'),
  create: (section: Section) =>
    apiRequest<Section>('/api/sections', { method: 'POST', body: section }),
};

export const orderApi = {
  all: (pageIndex = 0) => apiRequest<Page<Order>>(`/api/orders?${page(pageIndex)}`),
  my: (pageIndex = 0) => apiRequest<Page<Order>>(`/api/orders/my?${page(pageIndex)}`),
  byUser: (username: string, pageIndex = 0) =>
    apiRequest<Page<Order>>(`/api/orders/user/${enc(username)}?${page(pageIndex)}`),
  create: (bookIds: number[], rentalPeriod: number) =>
    apiRequest<Order>('/api/orders', { method: 'POST', body: { bookIds, rentalPeriod } }),
  finish: (id: number) => apiRequest<void>(`/api/orders/${id}`, { method: 'DELETE' }),
};

export const librarianApi = {
  users: () => apiRequest<Account[]>('/api/librarian/users'),
};

export const adminApi = {
  users: (pageIndex = 0) => apiRequest<Page<Account>>(`/api/admin/users?${page(pageIndex)}`),
  admins: (pageIndex = 0) => apiRequest<Page<Account>>(`/api/admin/admins?${page(pageIndex)}`),
  librarians: (pageIndex = 0) =>
    apiRequest<Page<Account>>(`/api/admin/librarians?${page(pageIndex)}`),
  debtors: () => apiRequest<Account[]>('/api/admin/debtors'),
  blockUser: (username: string) =>
    apiRequest<void>(`/api/admin/users/${enc(username)}/block`, { method: 'POST' }),
  deleteUser: (id: number) => apiRequest<void>(`/api/admin/users/${id}`, { method: 'DELETE' }),
  addEmployee: (payload: EmployeePayload) =>
    apiRequest<Account>('/api/admin/employees', { method: 'POST', body: payload }),
};

export const fileApi = {
  list: () => apiRequest<FileInfo[]>('/api/files'),
  upload: (file: File) => {
    const form = new FormData();
    form.append('file', file);
    return apiUpload<FileInfo>('/api/files', form);
  },
  remove: (filename: string) => apiRequest<void>(`/api/files/${enc(filename)}`, { method: 'DELETE' }),
};
