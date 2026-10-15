/**
 * Thin `fetch` wrapper around the JSON REST API.
 *
 * Every request is sent with `credentials: 'include'` so the `JSESSIONID`
 * session cookie is attached even when the SPA runs on the Vite dev server.
 */

const JSON_HEADERS = { 'Content-Type': 'application/json' } as const;

export class ApiError extends Error {
  readonly status: number;
  readonly fieldErrors: Record<string, string>;

  constructor(status: number, message: string, fieldErrors: Record<string, string> = {}) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.fieldErrors = fieldErrors;
  }
}

interface RequestOptions {
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
  body?: unknown;
}

export async function apiRequest<T>(url: string, options: RequestOptions = {}): Promise<T> {
  const { method = 'GET', body } = options;
  const response = await fetch(url, {
    method,
    credentials: 'include',
    headers: body === undefined ? undefined : JSON_HEADERS,
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  return parseResponse<T>(response);
}

export async function apiUpload<T>(url: string, form: FormData, method: 'POST' = 'POST'): Promise<T> {
  const response = await fetch(url, {
    method,
    credentials: 'include',
    body: form,
  });
  return parseResponse<T>(response);
}

async function parseResponse<T>(response: Response): Promise<T> {
  if (response.status === 204) {
    return undefined as T;
  }

  const text = await response.text();
  const data: unknown = text.length > 0 ? tryParse(text) : null;

  if (!response.ok) {
    const payload = data as { message?: string; errors?: Record<string, string> } | null;
    throw new ApiError(
      response.status,
      payload?.message ?? response.statusText ?? 'Request failed',
      payload?.errors ?? {},
    );
  }

  return data as T;
}

function tryParse(text: string): unknown {
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}
