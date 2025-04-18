const subLinks = document.querySelectorAll(".sub-link");
const tabContents = document.querySelectorAll(".tab-content");

subLinks.forEach((link) => {
  link.addEventListener("click", (e) => {
    e.preventDefault();
    // active menu con
    subLinks.forEach((l) => l.classList.remove("active"));
    link.classList.add("active");

    // hiện nội dung tương ứng
    const target = link.getAttribute("data-tab");
    tabContents.forEach((tc) => tc.classList.remove("active"));
    document.getElementById(target).classList.add("active");
  });
});

// Toggle mở/đóng dropdown Yêu cầu phê duyệt
function toggleApprovalMenu(event) {
  event.preventDefault();
  const parent = event.currentTarget;
  const submenu = parent.nextElementSibling;
  submenu.style.display = submenu.style.display === "block" ? "none" : "block";
  parent.classList.toggle("open");
}

// Trả về default
function returnToDefault() {
  tabContents.forEach((tc) => tc.classList.remove("active"));
  document.getElementById("default-content").classList.add("active");
  subLinks.forEach((l) => l.classList.remove("active"));
}
