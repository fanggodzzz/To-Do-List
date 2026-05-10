/**
 * Server Configuration Manager
 * To switch between servers, change the line below:
 *   "localhost" = Development (http://localhost:8080/api/todos)
 *   "render" = Production (https://to-do-list-eki9.onrender.com/api/todos)
 */

const ACTIVE_SERVER = "localhost"; // Change this to "localhost" or "render"
const AUTH_TOKEN_KEY = "todoAuthToken";

const SERVERS = {
  localhost: "http://localhost:8080/api/todos",
  render: "https://to-do-list-eki9.onrender.com/api/todos",
};

/**
 * Get the API base URL for the current server
 */
function getApiBase() {
  return SERVERS[ACTIVE_SERVER];
}

/**
 * Get the current server mode ("localhost" or "render")
 */
function getServerMode() {
  return ACTIVE_SERVER;
}

/**
 * Build API URL for a given endpoint path
 */
function apiUrl(path) {
  const base = getApiBase();
  // Ensure path starts with /
  const normalizedPath = path.startsWith("/") ? path : `/${path}`;
  return `${base}${normalizedPath}`;
}

/**
 * Build Authorization headers with JWT token
 */
function buildAuthHeaders(extraHeaders = {}) {
  const headers = { ...extraHeaders };
  const token = getAuthToken();
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }
  return headers;
}

/**
 * Check whether the JWT has expired based on its exp claim
 */
function isTokenExpired(token) {
  const payload = decodeJwtPayload(token);
  if (!payload || !payload.exp) {
    return true;
  }

  const expiresAtMs = payload.exp * 1000;
  return Date.now() >= expiresAtMs;
}

/**
 * Check if user has a valid auth token
 */
function hasAuthToken() {
  const token = localStorage.getItem(AUTH_TOKEN_KEY);
  if (!token) {
    return false;
  }

  if (isTokenExpired(token)) {
    clearAuthToken();
    return false;
  }

  return true;
}

/**
 * Store auth token (called after successful login/register)
 */
function storeAuthToken(token) {
  localStorage.setItem(AUTH_TOKEN_KEY, token);
}

/**
 * Get auth token
 */
function getAuthToken() {
  const token = localStorage.getItem(AUTH_TOKEN_KEY);
  if (!token) {
    return null;
  }

  if (isTokenExpired(token)) {
    clearAuthToken();
    return null;
  }

  return token;
}

/**
 * Clear auth token (called on logout)
 */
function clearAuthToken() {
  localStorage.removeItem(AUTH_TOKEN_KEY);
}

/**
 * Extract token from URL parameter (used after login redirect)
 * Removes the token from URL after storing it
 */
function bootstrapAuthTokenFromUrl() {
  const url = new URL(window.location.href);
  const token = url.searchParams.get("token");

  if (!token) {
    console.log("[Auth] No token param in URL");
    return;
  }

  if (isTokenExpired(token)) {
    console.warn("[Auth] Ignoring expired token from URL");
    url.searchParams.delete("token");
    window.history.replaceState(
      {},
      "",
      `${url.pathname}${url.search}${url.hash}`,
    );
    return;
  }

  console.log("[Auth] Found token in URL, storing in localStorage");
  localStorage.setItem(AUTH_TOKEN_KEY, token);

  // Verify it was actually stored
  if (!localStorage.getItem(AUTH_TOKEN_KEY)) {
    console.error("[Auth] ERROR: Failed to store token in localStorage!");
    return;
  }
  console.log("[Auth] Token stored successfully");

  // Clean up URL
  url.searchParams.delete("token");
  window.history.replaceState(
    {},
    "",
    `${url.pathname}${url.search}${url.hash}`,
  );
}

/**
 * Decode JWT payload (without verification - client-side only)
 */
function decodeJwtPayload(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join(""),
    );
    return JSON.parse(jsonPayload);
  } catch (err) {
    console.error("Failed to decode JWT:", err);
    return null;
  }
}

/**
 * Get user info from token
 */
function getUserFromToken() {
  const token = getAuthToken();
  if (!token) return null;
  return decodeJwtPayload(token);
}
