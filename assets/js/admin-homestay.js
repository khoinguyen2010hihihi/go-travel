window.onload = function () {
  const locationEnum = {
    "ngu-hanh-son": "Ngũ Hành Sơn",
    "son-tra": "Sơn Trà",
    "hai-chau": "Hải Châu",
    "thanh-khe": "Thanh Khê",
    "lien-chieu": "Liên Chiểu",
  };

  const token = localStorage.getItem("authToken");

  document.querySelector(".tab-link").addEventListener("click", function (e) {
    e.preventDefault();

    document.querySelectorAll(".tab-content").forEach((tab) => {
      tab.style.display = "none";
      tab.classList.remove("active");
    });

    const tab = document.getElementById("tab-approve-business");
    tab.style.display = "block";
    tab.classList.add("active");

    loadPendingHomestays();
  });

  function loadPendingHomestays() {
    const tableBody = document.querySelector("#homestay-request-table tbody");

    fetch("http://localhost:8080/homestay/api/homestays/pending", {
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
              <button class="btn btn-approve" onclick="approveHomestay(${
                h.id
              })">Phê duyệt</button>
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
  }

  // const roomTableBody = document.querySelector(
  //   "#tab-approve-room .room-table tbody"
  // );

  window.approveHomestay = function (homestayId) {
    fetch(
      `http://localhost:8080/homestay/api/homestays/admin/pending/${homestayId}`,
      {
        method: "PUT",
        headers: { Authorization: `Bearer ${token}` },
      }
    )
      .then((res) => {
        if (!res.ok) throw new Error(res.statusText);
        alert("Doanh nghiệp đã được phê duyệt.");
        loadPendingHomestays();
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
