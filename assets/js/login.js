// assets/js/login.js
import { closePopup } from "./popup-login.js";

function decodeJWT(token) {
  const base64Url = token.split(".")[1];
  const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split("")
      .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
      .join("")
  );
  return JSON.parse(jsonPayload);
}

function login() {
  const loginBtn = document.querySelector(".submit-login-btn");

  if (loginBtn) {
    loginBtn.addEventListener("click", function () {
      const email = document.getElementById("loginEmail").value;
      const password = document.getElementById("loginPassword").value;

      if (!email.trim() || !password.trim()) {
        alert("Vui lòng nhập đầy đủ email và mật khẩu.");
        return;
      }

      fetch("http://localhost:8080/homestay/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      })
        .then((response) => response.json())
        .then((data) => {
          if (data.token) {
            localStorage.setItem("authToken", data.token);

            const roles = data.roles || [];
            localStorage.setItem(
              "user",
              JSON.stringify({
                email: data.email,
                roles: roles,
              })
            );

            checkLoginStatus();
            closePopup();
          } else {
            alert("Đăng nhập thất bại.");
          }
        })
        .catch(() => alert("Đã có lỗi xảy ra. Thử lại sau."));
    });
  }
}

function isTokenExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    const exp = payload.exp;
    if (!exp) return true;
    const now = Math.floor(Date.now() / 1000);
    return exp < now;
  } catch (error) {
    console.error("Token invalid:", error);
    return true;
  }
}

function checkLoginStatus() {
  const token = localStorage.getItem("authToken");
  const authButtons = document.getElementById("auth-buttons");
  const userInfo = document.getElementById("user-info");
  const logoutBtn = document.getElementById("logoutBtn");
  const userIcon = document.getElementById("user-icon");
  const userMenu = document.getElementById("user-menu");

  if (authButtons && userInfo && userIcon && userMenu && logoutBtn) {
    if (token && !isTokenExpired(token)) {
      authButtons.style.display = "none";
      userInfo.style.display = "inline-block";

      logoutBtn.onclick = () => {
        localStorage.removeItem("authToken");
        localStorage.removeItem("user");
        checkLoginStatus();
        window.location.href = "trang-chu.html";
      };

      userIcon.onclick = () => {
        userMenu.style.display = userMenu.style.display === "block" ? "none" : "block";
      };

      document.addEventListener("click", function (event) {
        if (!userInfo.contains(event.target)) {
          userMenu.style.display = "none";
        }
      });
    } else {
      localStorage.removeItem("authToken");
      localStorage.removeItem("user");
      authButtons.style.display = "flex";
      userInfo.style.display = "none";
      userMenu.style.display = "none";
      logoutBtn.onclick = null;
    }
  }
}

export { login, checkLoginStatus };
