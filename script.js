const carousel = document.querySelector(".carousel");
const slides = document.querySelectorAll(".slide");
const prevButton = document.querySelector(".carousel-button.prev");
const nextButton = document.querySelector(".carousel-button.next");
let slideIndex = 0;
const slidesPerMove = 5;

function nextSlides(n) {
    slideIndex += n;
    if (slideIndex >= slides.length - 1) {
        slideIndex = slides.length - 1;
    }
    updateButtonsVisibility();
    updateCarousel();
}

function prevSlides(n) {
    slideIndex -= n;
    if (slideIndex < 0) {
        slideIndex = 0;
    }
    updateButtonsVisibility();
    updateCarousel();
}

function updateCarousel() {
    const slideWidth = slides[0].offsetWidth + 10;
    let translateX = -slideIndex * slideWidth;

    const carouselWidth = carousel.offsetWidth;
    const lastSlideWidth = slides[slides.length - 1].offsetWidth;
    const remainingSpace = carouselWidth - lastSlideWidth;

    if (slideIndex === slides.length - 1) {
        translateX = -(slideIndex * slideWidth - remainingSpace);
    }

    carousel.style.transform = `translateX(${translateX}px)`;
}

function updateButtonsVisibility() {
    if (slideIndex === 0) {
        prevButton.style.display = "none";
    } else {
        prevButton.style.display = "block";
    }

    if (slideIndex >= slides.length - 6) {
        nextButton.style.display = "none";
    } else {
        nextButton.style.display = "block";
    }
}

updateButtonsVisibility();
