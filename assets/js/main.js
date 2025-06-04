import { openPopup, closePopup, setupPopupEventListeners } from "./popup-login.js";
import { login, checkLoginStatus } from "./login.js";
import { initializeCarousel } from "./carousel.js";
import { setupTabsLocation } from "./tabs-location.js";
import { initializeSlide } from "./slide.js";
import { showHidePassword } from "./show-hide-password.js";
import { setupValidation } from "./register.js";

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

function restrictPageAccess() {
    const token = localStorage.getItem("authToken");
    const user = JSON.parse(localStorage.getItem("user"));
    const roles = user?.roles || [];
    const currentPage = window.location.pathname.split("/").pop() || "trang-chu.html";

    if (!token || isTokenExpired(token)) {
        localStorage.removeItem("authToken");
        localStorage.removeItem("user");
        if (["admin-dashboard.html", "host-dashboard.html", "my-booking.html", "payment-success.html"].includes(currentPage)) {
            window.location.href = "trang-chu.html";
        }
        return;
    }

    if (currentPage === "admin-dashboard.html" && !roles.includes("ROLE_ADMIN")) {
        window.location.href = "trang-chu.html";
    } else if (currentPage === "host-dashboard.html" && !roles.includes("ROLE_HOST")) {
        window.location.href = "trang-chu.html";
    } else if (["my-booking.html", "payment-success.html"].includes(currentPage) && !roles.includes("ROLE_USER")) {
        window.location.href = "trang-chu.html";
    }
}

window.addEventListener("load", () => {
    setupPopupEventListeners();
    login();
    checkLoginStatus();
    initializeCarousel();
    setupTabsLocation();
    initializeSlide();
    showHidePassword();
    setupValidation();
    restrictPageAccess();
});