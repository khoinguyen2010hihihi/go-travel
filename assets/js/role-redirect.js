document.getElementById("profileLink").addEventListener("click", function (e) {
    e.preventDefault();
    const user = JSON.parse(localStorage.getItem("user"));
    const roles = user?.roles || [];

    if (roles.includes("ROLE_ADMIN")) {
        window.location.href = "admin-dashboard.html";
    } else if (roles.includes("ROLE_HOST")) {
        window.location.href = "host-dashboard.html";
    } else if (roles.includes("ROLE_USER")) {
        window.location.href = "my-booking.html";
    } else {
        alert("Không xác định được vai trò!");
    }
});
