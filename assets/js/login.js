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
            alert("Đăng nhập thành công!");
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
    const payload = JSON.parse(atob(token.split('.')[1]));
    const exp = payload.exp;
    if (!exp) return true; // Không có exp thì xem như hết hạn
    const now = Math.floor(Date.now() / 1000);
    return exp < now; // Nếu exp < now thì token đã hết hạn
  } catch (error) {
    console.error("Token invalid:", error);
    return true; // Token lỗi thì xem như hết hạn
  }
}

function checkLoginStatus() {
  const token = localStorage.getItem("authToken");
  const authButtons = document.getElementById("auth-buttons");
  const userInfo = document.getElementById("user-info");
  const userEmail = document.getElementById("user-email");
  const logoutBtn = document.getElementById("logoutBtn");

  if (authButtons && userInfo && userEmail) {
    if (token && !isTokenExpired(token)) {
      authButtons.style.display = "none";
      userInfo.style.display = "block";
      const user = decodeJWT(token);
      if (user) {
        userEmail.textContent = `Chào, ${user.sub}`;
      }

      if (logoutBtn) {
        logoutBtn.style.display = "inline-block";

        logoutBtn.onclick = () => {
          localStorage.removeItem("authToken");
          checkLoginStatus(); // Sau khi logout thì gọi lại check
        };
      }
    } else {
      localStorage.removeItem("authToken");
      authButtons.style.display = "block";
      userInfo.style.display = "none";
      if (logoutBtn) {
        logoutBtn.style.display = "none";
        logoutBtn.onclick = null; // Xóa sự kiện click cũ nếu có
      }
    }
  }
}


export { login, checkLoginStatus }