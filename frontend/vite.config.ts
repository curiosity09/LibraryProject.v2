import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Dev server runs on :5173 and proxies every /api call to Spring Boot on :8080
// (same-origin from the browser's point of view, so the JSESSIONID cookie just works).
// `npm run build` writes the SPA straight into the Spring Boot static resources.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: '../src/main/resources/static',
    emptyOutDir: true,
  },
});
