const DEFAULT_API_BASE = "http://localhost:8080/api/todos";
const AUTH_TOKEN_STORAGE_KEY = "todoAuthToken";

function normalizeApiBase(value) {
  if (!value) {
    return DEFAULT_API_BASE;
  }
  return value.replace(/\/api\/todo?s?$/, "/api/todos");
}

const API = normalizeApiBase(
  localStorage.getItem("todoApiBase") || DEFAULT_API_BASE,
);
const AUTH_API = API.replace(/\/api\/todos$/, "/api/auth");

function authUrl(path) {
  return `${AUTH_API}${path}`;
}

function setAuthStatus(message, isError = false) {
  const authStatus = document.getElementById("authStatus");
  if (!authStatus) return;
  authStatus.textContent = message;
  authStatus.classList.toggle("auth-status--error", isError);
}

function storeAuthToken(token) {
  if (token) {
    localStorage.setItem(AUTH_TOKEN_STORAGE_KEY, token);
  }
}

function loginWithCredentials(userName, userPassword) {
  return fetch(authUrl("/login"), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ userName, userPassword }),
  })
    .then((res) => res.text().then((text) => ({ res, text: text.trim() })))
    .then(({ res, text }) => {
      if (!res.ok) {
        throw new Error(text || "Unable to log in");
      }
      storeAuthToken(text);
      setAuthStatus("Logged in. Redirecting...");
      setTimeout(() => {
        const nextUrl = new URL("main.html", window.location.href);
        nextUrl.searchParams.set("token", text);
        window.location.href = nextUrl.toString();
      }, 500);
      return text;
    })
    .catch((error) => {
      setAuthStatus(error.message, true);
      return Promise.reject(error);
    });
}

// If already logged in, redirect to main
if (localStorage.getItem(AUTH_TOKEN_STORAGE_KEY)) {
  window.location.href = "main.html";
}

// Handle login form
const loginForm = document.getElementById("loginForm");
const loginUserName = document.getElementById("loginUserName");
const loginPassword = document.getElementById("loginPassword");

if (loginForm) {
  loginForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const userName = loginUserName.value.trim();
    const userPassword = loginPassword.value.trim();

    if (!userName || !userPassword) {
      setAuthStatus("Username and password are required.", true);
      return;
    }

    loginWithCredentials(userName, userPassword).catch(() => {
      // Error already displayed by the function
    });
  });
}
