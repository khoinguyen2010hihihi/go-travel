window.onload = function () {
  const homestayList = document.querySelector(".homestay-list");

  const token = localStorage.getItem("authToken");

  const decodedToken = jwt_decode(token);
  const hostId = decodedToken.host_id;

  fetch(`http://localhost:8081/homestay/api/homestays/host/${hostId}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((res) => res.json())
    .then((data) => {
      homestayList.innerHTML = "";
      data.forEach((homestay) => {
        const li = document.createElement("li");
        li.innerHTML = `
          <a href="#" class="homestay-toggle" data-id="${homestay.id}">
            ${homestay.name}
          </a>
          <ul class="sub-menu" style="display: none">
            <li>
              <a href="#" class="tab-link" data-tab="view-all-room" data-hid="${homestay.id}">
                Tất cả phòng
              </a>
            </li>
            <li>
              <a href="#" class="tab-link" data-tab="add-room" data-hid="${homestay.id}">
                Thêm phòng
              </a>
            </li>
            <li>
              <a href="#" class="tab-link" data-tab="view-all-pending-room" data-hid="${homestay.id}">
                Phòng đang chờ duyệt
                </a>
            </li>
          </ul>
        `;
        li.querySelector(".homestay-toggle").addEventListener("click", (e) => {
          e.preventDefault();
          const sm = li.querySelector(".sub-menu");
          sm.style.display = sm.style.display === "block" ? "none" : "block";
        });
        homestayList.appendChild(li);
      });
      homestayList.style.display = "block";
    })
    .catch((err) => console.error("Lỗi khi load homestay:", err));

  document.addEventListener("click", function (e) {
    if (!e.target.classList.contains("tab-link")) return;
    e.preventDefault();

    const tabId = e.target.dataset.tab;
    const homestayId = e.target.dataset.hid;

    document
      .querySelectorAll(".tab-content")
      .forEach((tc) => (tc.style.display = "none"));

    if (tabId === "view-all-room") {
      showRoomList(homestayId);
    } else if (tabId == "add-room") {
      showAddForm(homestayId);
    } else {
      showPendingRoomList(homestayId);
    }
  });

  function showRoomList(homestayId) {
    const viewTab = document.getElementById("view-all-room");
    viewTab.style.display = "block";

    fetch(
      `http://localhost:8081/homestay/api/rooms/valid-homestay/${homestayId}`
    )
      .then((res) => res.json())
      .then((rooms) => {
        const tbody = viewTab.querySelector(".room-table tbody");
        tbody.innerHTML = "";
        if (!rooms.length) {
          tbody.innerHTML = '<tr><td colspan="6">Không có phòng nào.</td></tr>';
          return;
        }
        rooms.forEach((r) => {
          const tr = document.createElement("tr");
          tr.innerHTML = `
            <td>${r.roomId}</td>
            <td>${r.roomType}</td>
            <td>${Number(r.price).toLocaleString("vi-VN")}đ</td>
            <td>${r.availability ? "Còn trống" : "Đang được sử dụng"}</td>
            <td>
              <button class="btn btn-view"  data-id="${r.roomIdd}">Xem</button>
              <button class="btn btn-edit"  data-id="${r.roomId}">Sửa</button>
              <button class="btn btn-delete" data-id="${r.roomId}">Xóa</button>
            </td>
          `;
          tbody.appendChild(tr);
        });
      })
      .catch((err) => console.error("Lỗi load phòng:", err));
  }

  function showAddForm(homestayId) {
    const addTab = document.getElementById("add-room");
    addTab.style.display = "block";

    let hidden = addTab.querySelector("input[name='homestayId']");
    if (!hidden) {
      hidden = document.createElement("input");
      hidden.type = "hidden";
      hidden.name = "homestayId";
      addTab.querySelector(".card-box").appendChild(hidden);
    }
    hidden.value = homestayId;
  }

  window.returnToDefault = function () {
    document
      .querySelectorAll(".tab-content")
      .forEach((tc) => (tc.style.display = "none"));
    document.getElementById("default-content").style.display = "block";
  };

  function showPendingRoomList(homestayId) {
    const viewTab = document.getElementById("view-all-pending-room");
    viewTab.style.display = "block";

    fetch(
      `http://localhost:8081/homestay/api/rooms/pending-homestay/${homestayId}`
    )
      .then((res) => res.json())
      .then((rooms) => {
        const tbody = viewTab.querySelector(".room-table tbody");
        tbody.innerHTML = "";
        if (!rooms.length) {
          tbody.innerHTML = '<tr><td colspan="6">Không có phòng nào.</td></tr>';
          return;
        }
        rooms.forEach((r) => {
          const tr = document.createElement("tr");
          tr.innerHTML = `
            <td>${r.roomId}</td>
            <td>${r.roomType}</td>
            <td>${Number(r.price).toLocaleString("vi-VN")}đ</td>
            <td>${r.availability ? "Còn trống" : "Đang được sử dụng"}</td>
            <td>
              <button class="btn btn-view"  data-id="${r.roomIdd}">Xem</button>
              <button class="btn btn-edit"  data-id="${r.roomId}">Sửa</button>
              <button class="btn btn-delete" data-id="${r.roomId}">Xóa</button>
            </td>
          `;
          tbody.appendChild(tr);
        });
      })
      .catch((err) => console.error("Lỗi load phòng:", err));
  }

  function showAddForm(homestayId) {
    const addTab = document.getElementById("add-room");
    addTab.style.display = "block";

    let hidden = addTab.querySelector("input[name='homestayId']");
    if (!hidden) {
      hidden = document.createElement("input");
      hidden.type = "hidden";
      hidden.name = "homestayId";
      addTab.querySelector(".card-box").appendChild(hidden);
    }
    hidden.value = homestayId;
  }

  window.returnToDefault = function () {
    document
      .querySelectorAll(".tab-content")
      .forEach((tc) => (tc.style.display = "none"));
    document.getElementById("default-content").style.display = "block";
  };
  //Them

  document.getElementById("submit-room").addEventListener("click", function () {
    const roomType = document.querySelector(
      "input[name='RoomType']:checked"
    )?.value;
    const price = document.getElementById("Price").value;
    const featuresEls = document.querySelectorAll(
      "input[name='Facilities']:checked"
    );
    const features = Array.from(featuresEls)
      .map((el) => el.value)
      .join(", ");

    if (!roomType || !price || !features) {
      alert("Vui lòng điền đầy đủ thông tin bắt buộc!");
      return;
    }

    const homestayId = document.querySelector(
      "#add-room input[name='homestayId']"
    ).value;

    const roomData = {
      roomType: roomType,
      price: price,
      features: features,
    };

    const token = localStorage.getItem("authToken");

    fetch(`http://localhost:8081/homestay/api/rooms/homestay/${homestayId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(roomData),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Lỗi khi thêm phòng");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Phòng được thêm thành công:", data);
      })
      .catch((error) => {
        console.error("Lỗi:", error);
      });
  });
};
