window.onload = function () {
  const tabs = document.querySelectorAll(".tab");
  const propertyGrids = document.querySelectorAll(".property-grid");
  const viewMoreText = document.getElementById("view-more-text");

  function formatPrice(price) {
    return price.toLocaleString("vi-VN");
  }

  tabs.forEach((tab) => {
    tab.addEventListener("click", function () {
      tabs.forEach((t) => t.classList.remove("active"));
      this.classList.add("active");

      const selectedLocation = this.getAttribute("data-location");

      propertyGrids.forEach((grid) => {
        const gridLocation = grid.getAttribute("data-location");
        if (gridLocation === selectedLocation) {
          grid.style.display = "grid";
          grid.innerHTML = "";

          fetch(
            `http://localhost:8081/homestay/api/homestays/slide/${selectedLocation}`
          )
            .then((res) => res.json())
            .then((data) => {
              if (data.length === 0) {
                grid.innerHTML =
                  "<p>Hiện chưa có chỗ nghỉ nào ở khu vực này.</p>";
                return;
              }

              data.forEach((item) => {
                const cardHTML = `
                  <div class="property-card">
                    <div class="rating">${item.surfRating ?? "?"}</div>
                    <img src="assets/img/centre-hotel.webp" alt="${
                      item.name
                    }" />
                    <div class="property-details">
                      <h3>${item.name}</h3>
                      <div class="property-info">
                        <div class="property-rating">
                          <span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>
                        </div>
                        <div class="property-location">
                          <i class="ti-location-pin"></i>${item.street}, ${
                  item.ward
                }
                        </div>
                      </div>
                      <p class="property-price-description">
                        Giá mỗi đêm chưa gồm thuế và phí
                      </p>
                      <p class="property-price">VND ${formatPrice(
                        item.price ?? 0
                      )}</p>
                    </div>
                  </div>
                `;
                grid.insertAdjacentHTML("beforeend", cardHTML);
              });
            })
            .catch((err) => {
              grid.innerHTML =
                "<p>Lỗi khi tải dữ liệu homestay. Vui lòng thử lại.</p>";
              console.error(err);
            });
        } else {
          grid.style.display = "none";
        }
      });

      viewMoreText.textContent = this.textContent.trim();
    });
  });

  if (tabs.length > 0) {
    tabs[0].click();
  }
};
