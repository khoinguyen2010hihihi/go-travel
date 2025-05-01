// assets/js/room-list.js

const resultsContainer = document.querySelector(".results");

// Tạo pagination container
const paginationContainer = document.createElement("div");
paginationContainer.classList.add("pagination");
resultsContainer.after(paginationContainer);

// API endpoint
const endpoint = "http://localhost:8080/homestay/api/homestays";
const ITEMS_PER_PAGE = 20;

let currentPage = 1;
let allHomestays = [];

async function fetchPrimaryImageUrl(homestayId) {
    try {
        const res = await fetch(`http://localhost:8080/homestay/api/homestays/${homestayId}/images/primary`);
        if (!res.ok) throw new Error("No primary image");
        const data = await res.json();
        return data.primaryImageUrl; // Đường link ảnh chính
    } catch (error) {
        console.error("Không lấy được ảnh chính cho homestay ID:", homestayId, error);
        return "assets/img/default-thumbnail.webp"; // fallback ảnh mặc định
    }
}

async function renderPage(page) {
    resultsContainer.innerHTML = "";

    const start = (page - 1) * ITEMS_PER_PAGE;
    const end = start + ITEMS_PER_PAGE;
    const currentItems = allHomestays.slice(start, end);

    for (const item of currentItems) {
        const primaryImageUrl = await fetchPrimaryImageUrl(item.id);

        const cardHTML = `
      <div class="result-card">
        <img src="${primaryImageUrl}" alt="${item.name}" />
        <div class="result-details">
          <h3>${item.name}</h3>
          <div class="stars">${item.surfRating ?? "?"}/5 ★</div>
          <div class="location">
            <i class="ti-location-pin"></i> ${item.street ?? ""}, ${item.ward ?? ""}
          </div>
          <div class="amenities">
            ${item.description ?? "Chỗ nghỉ lý tưởng cho bạn."}
          </div>
        </div>
      </div>
    `;
        resultsContainer.insertAdjacentHTML("beforeend", cardHTML);
    }
}

function renderPagination() {
    paginationContainer.innerHTML = "";

    const totalPages = Math.ceil(allHomestays.length / ITEMS_PER_PAGE);

    if (currentPage > 1) {
        const prevBtn = document.createElement("button");
        prevBtn.textContent = "Previous";
        prevBtn.addEventListener("click", () => {
            currentPage--;
            renderPage(currentPage);
            renderPagination();
        });
        paginationContainer.appendChild(prevBtn);
    }

    for (let i = 1; i <= totalPages; i++) {
        const pageBtn = document.createElement("button");
        pageBtn.textContent = i;
        if (i === currentPage) {
            pageBtn.classList.add("active");
        }
        pageBtn.addEventListener("click", () => {
            currentPage = i;
            renderPage(currentPage);
            renderPagination();
        });
        paginationContainer.appendChild(pageBtn);
    }

    if (currentPage < totalPages) {
        const nextBtn = document.createElement("button");
        nextBtn.textContent = "Next";
        nextBtn.addEventListener("click", () => {
            currentPage++;
            renderPage(currentPage);
            renderPagination();
        });
        paginationContainer.appendChild(nextBtn);
    }
}

// Fetch dữ liệu homestay
async function loadHomestays() {
    try {
        const response = await fetch(endpoint);
        if (!response.ok) throw new Error("Lỗi khi truy cập API homestays");

        const data = await response.json();
        if (!Array.isArray(data) || data.length === 0) {
            resultsContainer.innerHTML = "<p>Hiện chưa có homestay nào.</p>";
            return;
        }

        allHomestays = data;
        await renderPage(currentPage);
        renderPagination();
    } catch (error) {
        console.error(error);
        resultsContainer.innerHTML = "<p>Lỗi khi tải dữ liệu. Vui lòng thử lại sau.</p>";
    }
}

// Khởi động
loadHomestays();
