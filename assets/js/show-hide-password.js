// show-hide-password.js
function showHidePassword() {
    const showHideButtons = document.querySelectorAll(".show-hide-btn");

    showHideButtons.forEach((button) => {
        button.addEventListener("click", () => {
            const passwordInput = button.parentElement.querySelector("input");
            if (passwordInput.type === "password") {
                passwordInput.type = "text";
                button.src = "/assets/img/icon/eye-crossed.svg";
                button.alt = "Ẩn mật khẩu";
            } else {
                passwordInput.type = "password";
                button.src = "/assets/img/icon/eye.svg";
                button.alt = "Hiện mật khẩu";
            }
        });
    });
}

export { showHidePassword };
