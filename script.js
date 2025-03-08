const carousel = document.querySelector(".carousel");
const slides = document.querySelectorAll(".slide");
const prevButton = document.querySelector(".carousel-button.prev");
const nextButton = document.querySelector(".carousel-button.next");
let slideIndex = 0;

function nextSlide() {
    slideIndex++;
    if (slideIndex >= slides.length) {
        slideIndex = slides.length - 1;
    }
    updateCarousel();
    updateButtonsVisibility();
}

function prevSlide() {
    slideIndex--;
    if (slideIndex < 0) {
        slideIndex = 0;
    }
    updateCarousel();
    updateButtonsVisibility();
}

function updateCarousel() {
    const slideWidth = slides[0].offsetWidth + 10; // Kích thước slide + margin
    let translateX = -slideIndex * slideWidth;

    // Tính số lượng slide hiển thị trên màn hình
    const containerWidth = document.querySelector(".carousel-container").offsetWidth;
    const slidesVisible = Math.floor(containerWidth / slideWidth);

    // Nếu đang ở gần cuối, căn chỉnh slide cuối cùng về bên phải
    if (slideIndex >= slides.length - slidesVisible) {
        translateX = -(slides.length - slidesVisible) * slideWidth;
    }

    const maxTranslateX = 0; // Giữ nguyên ở 0 để slide cuối luôn ở bên phải
    const minTranslateX = -Math.max(0, (slides.length - slidesVisible) * slideWidth);

    translateX = Math.max(translateX, minTranslateX); // giới hạn translateX

    carousel.style.transform = `translateX(${translateX}px)`;
}

function updateButtonsVisibility() {
    if (slideIndex === 0) {
        prevButton.style.display = "none";
    } else {
        prevButton.style.display = "block";
    }

    if (slideIndex >= slides.length - 1) {
        nextButton.style.display = "none";
    } else {
        nextButton.style.display = "block";
    }
}

// Gọi hàm này khi trang tải lần đầu để ẩn nút "Trước" nếu đang ở slide đầu tiên
updateButtonsVisibility();
