window.onload = function () {
  const locationEnum = {
    "ngu-hanh-son": "Ngũ Hành Sơn",
    "son-tra": "Sơn Trà",
    "hai-chau": "Hải Châu",
    "thanh-khe": "Thanh Khê",
    "lien-chieu": "Liên Chiểu",
  };
  const resultsContainer = document.querySelector(".results");
  const endpoint = "http://localhost:8081/homestay/api/rooms";

  fetch(endpoint)
    .then((res) => {
      if (!res.ok) throw new Error("Loi truy cap");
      return res.json();
    })
    .then((data) => {
      if (!Array.isArray(data) || data.length === 0) {
        resultsContainer.innerHTML = "<p>Hiện chưa có phòng nào.</p>";
        return;
      }
      // resultsContainer.innerHTML = "";
      data.forEach((item) => {
        const locationParts = [];
        if (item.district) locationParts.push(locationEnum[item.district]);
        if (item.ward) locationParts.push(item.ward);
        if (item.street) locationParts.push(item.street);
        let locationText = locationParts.join(", ");

        const cardHTML = `
            <div class="result-card">
              <img src="assets/img/home3.webp" alt="${item.name}" />
              <div class="result-details">
                <h3>${item.homestayName}</h3>
                <div class="stars">${item.rating}/5 ★</div>
                <div class="price">Giá: ${Number(item.price).toLocaleString(
                  "vi-VN"
                )}₫ / đêm</div>
                <div class="location">${locationText}</div>
                <div class="features">${item.features}</div>
              </div>
            </div>
          `;
        resultsContainer.insertAdjacentHTML("beforeend", cardHTML);
      });
    })
    .catch((err) => {
      console.error(err);
      resultsContainer.innerHTML =
        "<p>Lỗi khi tải dữ liệu phòng. Vui lòng thử lại.</p>";
    });
};
