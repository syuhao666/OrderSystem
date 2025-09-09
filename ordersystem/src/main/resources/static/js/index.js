const { createApp, ref, onMounted } = Vue;

createApp({
  setup() {
    const products = ref([]);
    const cartCount = ref(0); // 🔴 購物車紅點數量
    const cartItems = ref([]);

    // 🔹 漢堡選單開關狀態
    const menuOpen = ref(false);
    const toggleMenu = () => {
      menuOpen.value = !menuOpen.value;
    };

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

    function addToCart(product) {
      const data = {
        id: product.id,
        quantity: 1,
      };

      console.log("送出資料:", data);

      axios
        .post("/cart/add", data, {
          headers: {
            "Content-Type": "application/json",
          },
        })
        .then((response) => {
          console.log("加入購物車資料庫成功", response.data);
        })
        .catch((error) => {
          console.error("加入購物車失敗", error);
        });
    }

    return {
      products,
      addToCart,
      cartCount,
      fetchCart,
      cartItems,
      fetchCartCount,
      menuOpen, 
      toggleMenu
    };
  },
}).mount("#app");
