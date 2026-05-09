// Welcome page - simple redirect to login
const AUTH_TOKEN_STORAGE_KEY = "todoAuthToken";

document.getElementById("loginToUseBtn").addEventListener("click", () => {
  window.location.href = "login.html";
});

// If already logged in, redirect to main
if (localStorage.getItem(AUTH_TOKEN_STORAGE_KEY)) {
  window.location.href = "main.html";
}
