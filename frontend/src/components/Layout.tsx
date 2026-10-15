import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';

export default function Layout() {
  return (
    <div className="d-flex flex-column min-vh-100">
      <Navbar />
      <main className="container py-4 flex-grow-1">
        <Outlet />
      </main>
      <footer className="py-3 mt-auto">
        <p className="text-center text-muted mb-0">&copy; 2022 Library, Inc</p>
      </footer>
    </div>
  );
}
