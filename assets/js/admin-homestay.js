window.onload = function () {
  const locationEnum = {
    "ngu-hanh-son": "Ngũ Hành Sơn",
    "son-tra": "Sơn Trà",
    "hai-chau": "Hải Châu",
    "thanh-khe": "Thanh Khê",
    "lien-chieu": "Liên Chiểu",
  };
  const tableBody = document.querySelector("#homestay-request-table tbody");
  const token = localStorage.getItem("authToken");

  fetch("http://localhost:8081/homestay/api/homestays/pending", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((res) => res.json())
    .then((data) => {
      tableBody.innerHTML = ""; // clear 
      if (!data.length) {
        tableBody.innerHTML = `<tr><td colspan="4">Không có yêu cầu nào.</td></tr>`;
        return;
      }

      data.forEach((h) => {
        const locationParts = [];
        if (h.district) locationParts.push(locationEnum[h.district]);
        if (h.ward) locationParts.push(h.ward);
        if (h.street) locationParts.push(h.street);
        let locationText = locationParts.join(", ");
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${h.id}</td>
            <td>${h.name}</td>
            <td>
              <button
                class="btn btn-view"
                onclick='openCommonPopup("business", ${JSON.stringify({
                  name: h.name,
                  address: locationParts,
                  email: h.contactInfo,
                  note: h.description || "Không có ghi chú",
                })})'
              >
                Xem chi tiết
              </button>
            </td>
            <td>
              <button class="btn btn-approve" onclick="approveHomestay('${
                h.id
              }')">Phê duyệt</button>
              <button class="btn btn-reject" onclick="rejectHomestay('${
                h.id
              }')">Từ chối</button>
            </td>
          `;
        tableBody.appendChild(tr);
      });
    })
    .catch((err) => {
      console.error("Lỗi khi tải danh sách homestay chờ duyệt:", err);
      tableBody.innerHTML =
        "<tr><td colspan='4'>Lỗi khi tải dữ liệu.</td></tr>";
    });
  const roomTableBody = document.querySelector(
    "#tab-approve-room .room-table tbody"
  );

  function loadPendingRooms() {
    fetch("http://localhost:8081/homestay/api/rooms/pending", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Lỗi phản hồi: " + res.status);
        return res.json();
      })
      .then((rooms) => {
        roomTableBody.innerHTML = "";
        if (!rooms.length) {
          roomTableBody.innerHTML =
            "<tr><td colspan='4'>Không có phòng nào chờ duyệt.</td></tr>";
          return;
        }

        rooms.forEach((r) => {
          const popupData = {
            homestay: r.homestayName,
            roomType: r.roomType,
            features: r.features,
            price: Number(r.price).toLocaleString("vi-VN") + "đ",
          };

          const tr = document.createElement("tr");
          tr.innerHTML = `
                <td>${r.roomId}</td>
                <td>${r.homestayName}</td>
                <td>
                  <button
                    class="btn btn-view"
                    onclick='openCommonPopup("room", ${JSON.stringify(
                      popupData
                    )})'
                  >
                    Xem chi tiết
                  </button>
                </td>
                <td>
                  <button class="btn btn-approve" onclick="approveRoom('${
                    r.roomId
                  }')">Phê duyệt</button>
                  <button class="btn btn-reject" onclick="rejectRoom('${
                    r.roomId
                  }')">Từ chối</button>
                </td>
              `;
          roomTableBody.appendChild(tr);
        });
      })
      .catch((err) => {
        console.error("Lỗi khi tải danh sách phòng chờ duyệt:", err);
        roomTableBody.innerHTML =
          "<tr><td colspan='4'>Lỗi khi tải dữ liệu.</td></tr>";
      });
  }
  loadPendingRooms();

  window.approveRoom = function (roomId) {
    fetch(`http://localhost:8081/homestay/api/rooms/admin/pending/${roomId}`, {
      method: "PUT",
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => {
        if (!res.ok) throw new Error(res.statusText);
        alert("Phòng đã được phê duyệt.");
        loadPendingRooms();
      })
      .catch((e) => {
        console.error(e);
        alert("Phê duyệt thất bại.");
      });
  };

  //   window.rejectRoom = function (roomId) {
  //     fetch(`http://localhost:8081/homestay/api/rooms/${roomId}/reject`, {
  //       method: "POST",
  //       headers: { Authorization: `Bearer ${token}` },
  //     })
  //       .then((res) => {
  //         if (!res.ok) throw new Error(res.statusText);
  //         alert("Phòng đã bị từ chối.");
  //         loadPendingRooms();
  //       })
  //       .catch((e) => {
  //         console.error(e);
  //         alert("Từ chối thất bại.");
  //       });
  //   };
};
