// Configuration is centralized in app-config.js

function authUrl(path) {
  const apiBase = getApiBase();
  const AUTH_API = apiBase.replace(/\/api\/todos$/, "/api/auth");
  return `${AUTH_API}${path}`;
}

function setAuthStatus(message, isError = false) {
  const authStatus = document.getElementById("authStatus");
  if (!authStatus) return;
  authStatus.textContent = message;
  authStatus.classList.toggle("auth-status--error", isError);
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
      console.log("[Register] Registration successful, logging in...");
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
      console.log(
        "[Register] Login successful after registration, storing token...",
      );
      storeAuthToken(text);

      // Verify token was stored
      if (!getAuthToken()) {
        throw new Error(
          "Failed to store authentication token. Please try again.",
        );
      }
      console.log("[Register] Token stored successfully");

      setAuthStatus("Account created and logged in. Redirecting...");
      setTimeout(() => {
        const nextUrl = new URL("main.html", window.location.href);
        nextUrl.searchParams.set("token", text);
        console.log("[Register] Redirecting to:", nextUrl.toString());
        window.location.href = nextUrl.toString();
      }, 500);
      return text;
    });
}

// If already logged in, redirect to main
if (hasAuthToken()) {
  console.log("[Register] User already logged in, redirecting to main.html");
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
