import type { Book } from '../api/types';

export default function BookList({ books }: { books: Book[] }) {
  return (
    <ul className="book-list">
      {books.map((book) => (
        <li key={book.id ?? book.name}>
          <p>{book.name}</p>
          <p className="text-muted small mb-0">{book.author.fullName}</p>
          <p className="text-muted small mb-0">{book.publicationYear}</p>
        </li>
      ))}
    </ul>
  );
}
