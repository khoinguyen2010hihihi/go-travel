// partner-register.js
import { showHidePassword } from "./show-hide-password.js";

// Validation logic
const regexUsername = /^[a-zA-Z]{3,}$/;
const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
const regexPassword = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;

function handleValidValue(input) {
    const small = input.parentElement.nextElementSibling;
    small.style.visibility = "hidden";
    small.textContent = "";
    input.classList.remove("error");
    input.classList.add("success");
}

function handleInValidValue(input, message) {
    const small = input.parentElement.nextElementSibling;
    small.textContent = message;
    small.style.visibility = "visible";
    input.classList.remove("success");
    input.classList.add("error");
}

function setupValidation() {
    const username = document.getElementById("username");
    const registerEmail = document.getElementById("registerEmail");
    const registerPassword = document.getElementById("registerPassword");
    const confirmPassword = document.getElementById("confirmPassword");
    const form = document.getElementById("partner-register-form");

    if (username) {
        username.addEventListener("blur", () => {
            if (regexUsername.test(username.value.trim())) {
                handleValidValue(username);
            } else {
                const message = "Tên người dùng phải có ít nhất 3 ký tự, chỉ chứa chữ cái và không có khoảng trắng.";
                handleInValidValue(username, message);
            }
        });
    }

    if (registerEmail) {
        registerEmail.addEventListener("blur", () => {
            if (regexEmail.test(registerEmail.value)) {
                handleValidValue(registerEmail);
            } else {
                const message = "Định dạng email không hợp lệ. Vui lòng nhập email hợp lệ.";
                handleInValidValue(registerEmail, message);
            }
        });
    }

    if (registerPassword) {
        registerPassword.addEventListener("blur", () => {
            if (regexPassword.test(registerPassword.value)) {
                handleValidValue(registerPassword);
            } else {
                const message = "Mật khẩu ít nhất 6 ký tự và chứa ít nhất một chữ cái và một số.";
                handleInValidValue(registerPassword, message);
            }
        });
    }

    if (confirmPassword) {
        confirmPassword.addEventListener("blur", () => {
            if (confirmPassword.value === registerPassword.value && confirmPassword.value !== "") {
                handleValidValue(confirmPassword);
            } else {
                const message = "Mật khẩu xác nhận không khớp.";
                handleInValidValue(confirmPassword, message);
            }
        });
    }

    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            let isValid = true;

            if (!regexUsername.test(username.value.trim())) {
                handleInValidValue(username, "Tên người dùng phải có ít nhất 3 ký tự, chỉ chứa chữ cái và không có khoảng trắng.");
                isValid = false;
            } else {
                handleValidValue(username);
            }

            if (!regexEmail.test(registerEmail.value)) {
                handleInValidValue(registerEmail, "Định dạng email không hợp lệ. Vui lòng nhập email hợp lệ.");
                isValid = false;
            } else {
                handleValidValue(registerEmail);
            }

            if (!regexPassword.test(registerPassword.value)) {
                handleInValidValue(registerPassword, "Mật khẩu ít nhất 6 ký tự và chứa ít nhất một chữ cái và một số.");
                isValid = false;
            } else {
                handleValidValue(registerPassword);
            }

            if (confirmPassword.value !== registerPassword.value || confirmPassword.value === "") {
                handleInValidValue(confirmPassword, "Mật khẩu xác nhận không khớp.");
                isValid = false;
            } else {
                handleValidValue(confirmPassword);
            }

            if (isValid) {
                const partnerData = {
                    username: username.value,
                    email: registerEmail.value,
                    password: registerPassword.value,
                    createdAt: new Date().toISOString().split("T")[0],
                };

                console.log("Dữ liệu gửi đi:", partnerData);

                fetch("http://localhost:8081/partner/register", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(partnerData),
                })
                    .then((response) => response.text())
                    .then((message) => {
                        alert(message);
                        form.reset();
                    })
                    .catch((error) => {
                        alert("Lỗi: " + error.message);
                        console.error("Lỗi:", error);
                    });
            }
        });
    }
}

// Slider logic
function setupSlider() {
    let slideIndex = 0;
    const sliderContent = document.querySelector(".slider-content");
    const paginationDots = document.querySelectorAll(".pagination-dot");
    const slideWidth = document.querySelector(".slide").offsetWidth;

    function goToSlide(index) {
        slideIndex = index;
        sliderContent.style.transform = `translateX(-${slideIndex * slideWidth}px)`;
        paginationDots.forEach((dot, i) => {
            dot.classList.toggle("active", i === slideIndex);
        });
    }

    paginationDots.forEach((dot) => {
        dot.addEventListener("click", () => {
            const index = parseInt(dot.getAttribute("data-slide"));
            goToSlide(index);
        });
    });

    setInterval(() => {
        slideIndex++;
        if (slideIndex >= 3) {
            slideIndex = 0;
        }
        goToSlide(slideIndex);
    }, 3000);

    goToSlide(slideIndex);
}

// Khởi tạo
document.addEventListener("DOMContentLoaded", () => {
    setupValidation();
    showHidePassword();
    setupSlider();
});
