function showHidePassword() {
    const passwordInputs = document.querySelectorAll("#registerPassword, #loginPassword");
    const showHideButtons = document.querySelectorAll(".show-hide-btn");

    showHideButtons.forEach((button, index) => {
        button.addEventListener("click", () => {
            const passwordInput = passwordInputs[index];
            if (passwordInput.type === "password") {
                passwordInput.type = "text";
                button.textContent = "Hide";
            } else {
                passwordInput.type = "password";
                button.textContent = "Show";
            }
        });
    });
}

// Initialize when the popup is loaded
document.addEventListener("DOMContentLoaded", () => {
    showHidePassword();
});

export { showHidePassword };
