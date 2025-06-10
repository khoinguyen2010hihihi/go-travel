// assets/js/main.js
import { openPopup, closePopup, setupPopupEventListeners, } from "./popup-login.js";
import { login, checkLoginStatus } from "./login.js";
import { initializeCarousel } from "./carousel.js";
import { setupTabsLocation } from "./tabs-location.js";
import { initializeSlide } from "./slide.js";
import { showHidePassword } from "./show-hide-password.js";
import { setupValidation } from "./register.js";
import { restrictPageAccess } from "./restrictPageAccess.js";

restrictPageAccess();

window.addEventListener("load", () => {
  setupPopupEventListeners();
  checkLoginStatus();
  login();
  initializeCarousel();
  setupTabsLocation();
  initializeSlide();
  showHidePassword();
  setupValidation();
});
