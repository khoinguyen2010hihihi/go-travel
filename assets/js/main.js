// assets/js/main.js
import { openPopup, closePopup, setupPopupEventListeners } from './popup-login.js';
import { login, checkLoginStatus } from './login.js';
import { initializeCarousel } from './carousel.js';
import { setupTabsLocation } from './tabs-location.js';
import { initializeSlide } from './slide.js';


window.addEventListener('load', () => {
  setupPopupEventListeners();
  login();
  checkLoginStatus();
  initializeCarousel();
  setupTabsLocation();
  initializeSlide();
});
