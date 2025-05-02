const reviewListEl = document.getElementById("review-list");
const reviewForm = document.getElementById("submit-review-form");
const averageEl = document.createElement("div");
averageEl.classList.add("review-summary");
reviewListEl.before(averageEl); // gắn phía trên review list

const params = new URLSearchParams(window.location.search);
const homestayId = Number(params.get("id"));

if (!homestayId) {
  console.error("Không có homestayId trong URL");
  alert("Không xác định được homestay để hiển thị đánh giá.");
} // http://.../room.html?id=5

const apiBase = "http://localhost:8080/homestay/api/reviews";

// --- Hiển thị tổng quan Homestay ---
const nameEl = document.getElementById("homestay-name");
const addressEl = document.getElementById("homestay-address");
const descEl = document.getElementById("homestay-description");

function loadHomestayInfo() {
  fetch(`http://localhost:8080/homestay/api/homestays/${homestayId}`)
    .then((res) => res.json())
    .then((homestay) => {
      nameEl.textContent = homestay.name || "Tên homestay đang cập nhật";

      const addressParts = [homestay.street, homestay.ward, homestay.district]
        .filter(Boolean)
        .join(", ");

      addressEl.textContent = addressParts || "Địa chỉ chưa cập nhật";
      descEl.textContent =
        homestay.description?.trim() || "Chưa có mô tả cho homestay này.";
    })
    .catch((err) => {
      console.error("Lỗi khi tải thông tin tổng quan:", err);
      nameEl.textContent = "Không thể tải thông tin homestay";
    });
}

// Hiển thị danh sách + trung bình review
function loadReviews() {
  fetch(`${apiBase}/homestay/${homestayId}`)
    .then((res) => res.json())
    .then((data) => {
      reviewListEl.innerHTML = "";
      averageEl.innerHTML = "";

      if (!data.length) {
        reviewListEl.innerHTML = "<p>Chưa có đánh giá nào.</p>";
        return;
      }

            // Lấy thêm thông tin homestay để lấy surfRating
      fetch(`http://localhost:8080/homestay/api/homestays/${homestayId}`) 
      .then((res) => res.json())
      .then((homestay) => {
        const total = data.length;
        const avg = homestay.surfRating || 0;

        averageEl.innerHTML = `
          <div><strong>${total}</strong> đánh giá</div>
          <div>Trung bình: <span class="review-average">${avg.toFixed(1)} ★</span></div>
        `;
      });

      data.forEach((review) => {
        const div = document.createElement("div");
        div.classList.add("review-item");
        div.innerHTML = `
          <div class="review-header">
            <span class="review-user">${review.userName}: </span>
            <span class="review-rating">${"★".repeat(
              review.rating
            )}${"☆".repeat(5 - review.rating)}</span>
          </div>
          <p class="review-comment">${review.comment}</p>
          <span class="review-date">${new Date(
            review.createdAt
          ).toLocaleDateString("vi-VN")}</span>
          <hr>
          `;
        reviewListEl.appendChild(div);
      });
    })
    .catch((err) => {
      console.error("Lỗi khi tải review:", err);
      reviewListEl.innerHTML = "<p>Lỗi khi tải đánh giá.</p>";
    });
}

function loadMinPrice() {
  fetch(`http://localhost:8080/homestay/api/rooms/valid-homestay/${homestayId}`)
    .then(res => res.json())
    .then(rooms => {
      if (!rooms.length) {
        document.getElementById("min-price").textContent = "Không có phòng";
        return;
      }

      const minRoom = rooms.reduce((prev, curr) =>
        curr.price < prev.price ? curr : prev
      );

      const formatted = new Intl.NumberFormat("vi-VN").format(minRoom.price);
      document.getElementById("min-price").textContent = formatted;
    })
    .catch(err => {
      console.error("Lỗi khi lấy giá rẻ nhất:", err);
      document.getElementById("min-price").textContent = "Lỗi tải giá";
    });
}

// Gửi đánh giá mới
reviewForm.addEventListener("submit", (e) => {
  e.preventDefault();

  const token = localStorage.getItem("authToken");
  if (!token) {
    alert("Vui lòng đăng nhập để gửi đánh giá.");
    return;
  }

  const rating = Number(document.getElementById("rating").value);
  const comment = document.getElementById("comment").value;

  fetch(apiBase, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ rating, comment, homestayId }),
  })
    .then((res) => {
      if (!res.ok) throw new Error("Không thể gửi đánh giá");
      return res.json();
    })
    .then(() => {
      alert("Đánh giá đã được gửi!");
      reviewForm.reset();
      loadReviews();
    })
    .catch((err) => {
      console.error(err);
      alert("Lỗi khi gửi đánh giá.");
    });
});

loadReviews();
loadHomestayInfo();
loadMinPrice();