window.onload = function () {
  const homestayList = document.querySelector(".homestay-list");

  fetch("http://localhost:8080/homestay/api/homestays") // endpoint ví dụ
    .then((res) => res.json())
    .then((data) => {
      homestayList.innerHTML = ""; // clear nếu có cũ
      data.forEach((homestay) => {
        const li = document.createElement("li");
        li.innerHTML = `
            <a href="#" class="homestay-toggle" data-id="${homestay.id}">${homestay.name}</a>
            <ul class="sub-menu">
              <li>
                <a href="#" class="tab-link" data-tab="view-all-room" data-hid="${homestay.id}">Tất cả phòng</a>
              </li>
              <li>
                <a href="#" class="tab-link" data-tab="add-room" data-hid="${homestay.id}">Thêm phòng</a>   
              </li> 
            </ul>
          `;
        homestayList.appendChild(li);
        li.querySelector(".homestay-toggle").addEventListener(
          "click",
          function (e) {
            e.preventDefault();
            const submenu = this.nextElementSibling;
            if (submenu) {
              submenu.style.display =
                submenu.style.display === "block" ? "none" : "block";
            }
          }
        );
      });

      homestayList.style.display = "block";
    })
    .catch((err) => {
      console.error("Lỗi khi load homestay:", err);
    });

  // Bắt sự kiện click vào "Tất cả phòng"
  document.addEventListener("click", function (e) {
    if (e.target.classList.contains("tab-link")) {
      e.preventDefault();
      const tab = e.target.dataset.tab;
      const homestayId = e.target.dataset.hid;
      console.log("Tab:", tab, "| Homestay ID:", homestayId);

      if (tab === "view-all-room") {
        // Gọi API để lấy danh sách phòng thuộc homestay này
        fetchRoomsByHomestay(homestayId);
      } else if (tab === "add-room") {
        // Hiển thị form thêm phòng và gán sẵn homestayId
        showAddRoomForm(homestayId);
      }
    }
  });

  function fetchRoomsByHomestay(homestayId) {
    console.log("Fetch phòng của homestay:", homestayId);
    // Gọi API & render bảng như ảnh bạn gửi
  }

  function showAddRoomForm(homestayId) {
    console.log("Mở form thêm phòng cho homestay:", homestayId);
    // Hiện form thêm phòng, gán hidden input chứa homestayId
  }
};
