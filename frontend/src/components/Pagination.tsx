interface PaginationProps {
  page: number;
  totalPages: number;
  onChange: (page: number) => void;
}

export default function Pagination({ page, totalPages, onChange }: PaginationProps) {
  if (totalPages <= 1) {
    return null;
  }

  const pages = Array.from({ length: totalPages }, (_, index) => index);

  return (
    <nav aria-label="Page navigation">
      <ul className="pagination">
        <li className={`page-item${page <= 0 ? ' disabled' : ''}`}>
          <button
            type="button"
            className="page-link"
            onClick={() => onChange(page - 1)}
            disabled={page <= 0}
          >
            &lsaquo;
          </button>
        </li>
        {pages.map((index) => (
          <li key={index} className={`page-item${index === page ? ' active' : ''}`}>
            <button type="button" className="page-link" onClick={() => onChange(index)}>
              {index + 1}
            </button>
          </li>
        ))}
        <li className={`page-item${page + 1 >= totalPages ? ' disabled' : ''}`}>
          <button
            type="button"
            className="page-link"
            onClick={() => onChange(page + 1)}
            disabled={page + 1 >= totalPages}
          >
            &rsaquo;
          </button>
        </li>
      </ul>
    </nav>
  );
}
