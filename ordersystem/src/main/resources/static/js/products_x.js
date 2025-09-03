const { createApp, ref, onMounted } = Vue;

createApp({
  setup() {
    const product = ref(null);
    const isLoading = ref(false);
    const hasError = ref(false);
    const cartCount = ref(0); // 🔴 購物車紅點數量
    const cartItems = ref([]);

    //----------------
    // 取得購物車內容
    function fetchCart() {
      axios
        .get("/api/items") // 後端商品資料網址
        .then((response) => {
          cartItems.value = response.data;
          console.log(cartItems.value);
        })
        .catch((error) => {
          console.error("發生錯誤", error);
        });
    }

    function fetchCartCount() {
      axios
        .get("/cart/count")
        .then((res) => {
          cartCount.value = res.data; // 後端算好的總數量
          updateCartCount(cartCount.value); // ← 加這行，確保 header 也同步
        })
        .catch((err) => console.error("無法取得購物車數量", err));
    }
    //----------------
    const fetchProductDetails = (productId) => {
      if (!productId) {
        console.error("未提供商品 ID");
        hasError.value = true;
        return;
      }

      isLoading.value = true;
      hasError.value = false;

      axios
        .get(`/api/product/${productId}`)
        .then((response) => {
          product.value = response.data;
          console.log("成功獲取商品詳細內容:", product.value);
        })
        .catch((error) => {
          console.error("獲取商品詳細內容時發生錯誤", error);
          hasError.value = true;
          product.value = null;
        })
        .finally(() => {
          isLoading.value = false;
        });
    };

    const addToCart = (product) => {
      const data = { id: product.id, quantity: 1 }; //-----
      axios
        .post("/cart/add", data, {
          headers: { "Content-Type": "application/json" },
        })
        .then(() => {
          alert(`已成功將 ${product.name} 加入購物車！`);
          fetchCart();
          fetchCartCount(); // ← 這裡會自動更新 header 數量
        })
        .catch((error) => {
          console.error("加入購物車失敗", error);
          alert("加入購物車失敗，請稍後再試。");
        });
    };

    function updateCartCount(count) {
      const badge = document.getElementById("cart-count-badge");
      if (badge) {
        badge.textContent = count;
        badge.style.display = count > 0 ? "" : "none";
      }
    }

    // 假設你有一個方法可以取得購物車商品數量
    function getCartCount() {
      // 例如從 localStorage 取
      const cart = JSON.parse(localStorage.getItem("cart") || "[]");
      return cart.length;
    }

    // 頁面載入時自動更新
    document.addEventListener("DOMContentLoaded", function () {
      updateCartCount(getCartCount());
    });

    onMounted(() => {
      const urlParams = new URLSearchParams(window.location.search);
      const productId = urlParams.get("id"); // URL 範例: product.html?id=123
      fetchProductDetails(productId);
      fetchCart();
      fetchCartCount(); // 頁面載入時也同步 header 數量
    });

    return {
      product,
      addToCart,
      isLoading,
      hasError,
      cartCount,
      cartItems,
      fetchCart,
      fetchCartCount,
    };
  },
}).mount("#app");
