// Welcome page - simple redirect to login

document.getElementById("loginToUseBtn").addEventListener("click", () => {
  window.location.href = "login.html";
});

// If already logged in, redirect to main
if (hasAuthToken()) {
  console.log("[Welcome] User already logged in, redirecting to main.html");
  window.location.href = "main.html";
} else {
  console.log("[Welcome] User not logged in, showing welcome screen");
}
