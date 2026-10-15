import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './styles.css';
import App from './App';
import { AuthProvider } from './contexts/AuthContext';
import { CartProvider } from './contexts/CartContext';
import { I18nProvider } from './i18n';

const container = document.getElementById('root');
if (!container) {
  throw new Error('Root container #root was not found');
}

ReactDOM.createRoot(container).render(
  <React.StrictMode>
    <I18nProvider>
      <AuthProvider>
        <CartProvider>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </CartProvider>
      </AuthProvider>
    </I18nProvider>
  </React.StrictMode>,
);
