const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

/**
 * Reads the current JWT from browser storage.
 */
export function getToken() {
  return localStorage.getItem('redditCloneToken');
}

/**
 * Persists or clears the JWT used by authenticated API requests.
 */
export function setToken(token) {
  if (token) {
    localStorage.setItem('redditCloneToken', token);
  } else {
    localStorage.removeItem('redditCloneToken');
  }
}

/**
 * Sends an API request with JSON headers and optional bearer authentication.
 */
export async function apiRequest(path, options = {}) {
  const token = getToken();
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {})
    }
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({ error: 'Request failed' }));
    throw new Error(error.error || 'Request failed');
  }

  if (response.status === 204) {
    return null;
  }
  return response.json();
}

/**
 * Convenience helper for JSON POST requests.
 */
export function postJson(path, body) {
  return apiRequest(path, {
    method: 'POST',
    body: JSON.stringify(body)
  });
}
