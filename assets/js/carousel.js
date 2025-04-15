document.addEventListener("DOMContentLoaded", () => {
    const carousel = document.querySelector(".carousel");
    const slides = Array.from(carousel.querySelectorAll(".slide"));
    const prevButton = document.querySelector(".carousel-button.prev");
    const nextButton = document.querySelector(".carousel-button.next");
    const carouselContainer = document.querySelector(".carousel-container");

    if (!carousel || !prevButton || !nextButton || slides.length === 0) {
        console.error("Carousel elements not found!");
        return;
    }

    let currentTranslate = 0;
    let prevTranslate = 0;
    let isDragging = false;
    let startPos = 0;
    let animationID;
    let maxTranslate;
    let autoSlideInterval;

    function calculateDimensions() {
        const slideWidth = slides[0].offsetWidth;
        const containerWidth = carouselContainer.offsetWidth;
        maxTranslate = -(carousel.scrollWidth - containerWidth);
        updateCarouselPosition(false);
    }

    function updateCarouselPosition(useTransition = true) {
        currentTranslate = Math.max(maxTranslate, Math.min(0, currentTranslate));
        carousel.style.transition = useTransition ? "transform 0.3s ease-out" : "none";
        carousel.style.transform = `translateX(${currentTranslate}px)`;
        updateButtonStates();
    }

    function updateButtonStates() {
        prevButton.style.display = currentTranslate < 0 ? "flex" : "none";
        nextButton.style.display = currentTranslate > maxTranslate ? "flex" : "none";
    }

    function touchStart(event) {
        isDragging = true;
        startPos = event.touches[0].clientX;
        prevTranslate = currentTranslate;
        carousel.style.transition = "none";
        cancelAnimationFrame(animationID);
        stopAutoSlide();
    }

    function touchMove(event) {
        if (!isDragging) return;
        const currentPosition = event.touches[0].clientX;
        const diff = currentPosition - startPos;
        currentTranslate = prevTranslate + diff;
        updateCarouselPosition(false);
    }

    function touchEnd() {
        isDragging = false;
        requestAnimationFrame(() => {
            updateCarouselPosition(true);
            startAutoSlide();
        });
    }

    function nextSlide() {
        currentTranslate -= carouselContainer.offsetWidth;
        updateCarouselPosition(true);
    }

    function prevSlide() {
        currentTranslate += carouselContainer.offsetWidth;
        updateCarouselPosition(true);
    }

    function startAutoSlide() {
        stopAutoSlide();
        autoSlideInterval = setInterval(() => {
            if (currentTranslate > maxTranslate) {
                nextSlide();
            } else {
                currentTranslate = 0;
                updateCarouselPosition(true);
            }
        }, 3000);
    }

    function stopAutoSlide() {
        clearInterval(autoSlideInterval);
    }

    window.addEventListener("resize", calculateDimensions);
    carouselContainer.addEventListener("touchstart", touchStart, { passive: true });
    carouselContainer.addEventListener("touchmove", touchMove, { passive: true });
    carouselContainer.addEventListener("touchend", touchEnd);
    prevButton.addEventListener("click", prevSlide);
    nextButton.addEventListener("click", nextSlide);

    calculateDimensions();
    startAutoSlide();
});
