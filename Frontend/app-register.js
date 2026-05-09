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

function registerAccount(userName, userEmail, userPassword) {
  return fetch(authUrl("/register"), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ userName, userEmail, userPassword }),
  })
    .then((res) => res.text().then((text) => ({ res, text: text.trim() })))
    .then(({ res, text }) => {
      if (!res.ok) {
        throw new Error(text || "Unable to register");
      }
      setAuthStatus(text || "Account created. Logging in...");
      // After registration, log in automatically
      return loginWithCredentials(userName, userPassword);
    })
    .catch((error) => {
      setAuthStatus(error.message, true);
      return Promise.reject(error);
    });
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
      setAuthStatus("Account created and logged in. Redirecting...");
      setTimeout(() => {
        const nextUrl = new URL("main.html", window.location.href);
        nextUrl.searchParams.set("token", text);
        window.location.href = nextUrl.toString();
      }, 500);
      return text;
    });
}

// If already logged in, redirect to main
if (localStorage.getItem(AUTH_TOKEN_STORAGE_KEY)) {
  window.location.href = "main.html";
}

// Handle register form
const registerForm = document.getElementById("registerForm");
const registerUserName = document.getElementById("registerUserName");
const registerEmail = document.getElementById("registerEmail");
const registerPassword = document.getElementById("registerPassword");

if (registerForm) {
  registerForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const userName = registerUserName.value.trim();
    const userEmail = registerEmail.value.trim();
    const userPassword = registerPassword.value.trim();

    if (!userName || !userEmail || !userPassword) {
      setAuthStatus("All fields are required.", true);
      return;
    }

    registerAccount(userName, userEmail, userPassword).catch(() => {
      // Error already displayed by the function
    });
  });
}
