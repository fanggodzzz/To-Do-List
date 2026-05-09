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
      console.log("[Login] Login successful, storing token...");
      storeAuthToken(text);

      // Verify token was stored
      if (!getAuthToken()) {
        throw new Error(
          "Failed to store authentication token. Please try again.",
        );
      }
      console.log("[Login] Token stored successfully");

      setAuthStatus("Logged in. Redirecting...");
      setTimeout(() => {
        const nextUrl = new URL("main.html", window.location.href);
        nextUrl.searchParams.set("token", text);
        console.log("[Login] Redirecting to:", nextUrl.toString());
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
if (hasAuthToken()) {
  console.log("[Login] User already logged in, redirecting to main.html");
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
