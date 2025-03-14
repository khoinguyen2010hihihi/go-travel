document.getElementById("openPopup").addEventListener("click", function() {
    document.getElementById("popup").style.display = "flex";
});

function closePopup() {
    document.getElementById("popup").style.display = "none";
}

function switchTab(tab) {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");
    const loginTab = document.querySelector(".tab-btn:nth-child(1)");
    const registerTab = document.querySelector(".tab-btn:nth-child(2)");

    if (tab === "login") {
        loginForm.classList.remove("hidden");
        registerForm.classList.add("hidden");
        loginTab.classList.add("active");
        registerTab.classList.remove("active");
    } else {
        registerForm.classList.remove("hidden");
        loginForm.classList.add("hidden");
        registerTab.classList.add("active");
        loginTab.classList.remove("active");
    }
}
