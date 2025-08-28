const { createApp, ref, onMounted } = Vue;

createApp({
  setup() {
    const products = ref([]);
    const cartCount = ref(0); // 🔴 購物車紅點數量
    const cartItems = ref([]);

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
        })
        .catch((err) => console.error("無法取得購物車數量", err));
    }

    onMounted(() => {
      fetchCart();
      fetchCartCount();
      axios
        .get("/api/products") // 後端商品資料網址
        .then((response) => {
          products.value = response.data;
          console.log(products.value);
        })
        .catch((error) => {
          console.error("發生錯誤", error);
        });
    });

    

    return { products, cartCount, fetchCart, cartItems, fetchCartCount };
  },
}).mount("#app");
