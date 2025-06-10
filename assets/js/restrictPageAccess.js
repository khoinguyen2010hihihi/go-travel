function restrictPageAccess() {
    const token = localStorage.getItem("authToken");
    const user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null;
    const roles = user?.roles || [];
    const currentPage = window.location.pathname.split("/").pop().split("?")[0] || "trang-chu.html";

    function isTokenExpired(token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.exp * 1000 < Date.now();
        } catch (e) {
            return true;
        }
    }

    if (!token || isTokenExpired(token)) {
        localStorage.removeItem("authToken");
        localStorage.removeItem("user");
        if (["admin-dashboard.html", "host-dashboard.html", "my-booking.html", "payment-success.html"].includes(currentPage)
            && window.location.pathname !== "/access-denied.html") {
            window.location.href = "access-denied.html";
        }
        return;
    }

    const pageRoleMap = {
        "admin-dashboard.html": ["ROLE_ADMIN"],
        "host-dashboard.html": ["ROLE_HOST"],
        "my-booking.html": ["ROLE_USER"],
        "payment-success.html": ["ROLE_USER"]
    };

    if (pageRoleMap[currentPage] && !pageRoleMap[currentPage].some(role => roles.includes(role))
        && window.location.pathname !== "/access-denied.html") {
        window.location.href = "access-denied.html";
    }
}

export { restrictPageAccess };