const { createApp, ref, onMounted, computed } = Vue;

createApp({
  setup() {
    const cartItems = ref([]);
    const cartCount = ref(0); // 🔴 購物車紅點數量
    const selectedShippingMethod = ref(null);
    const selectedFloor = ref(""); // 樓層選擇

    const backendTotal = ref(0);
    const deliveryFee = ref(0);
    const floorFee = ref(0);

    // 取得購物車內容
    function fetchCart() {
      axios
        .get("/api/items") // 後端商品資料網址
        .then((response) => {
          cartItems.value = response.data;
          console.log(cartItems.value);
          cartCount.value = cartItems.value.reduce(
            (sum, item) => sum + item.quantity,
            0,
          ); // 🔴 更新紅點
        })
        .catch((error) => {
          console.error("發生錯誤", error);
        });
    }

    // 增加數量
    function increaseQuantity(cartItemId) {
      console.log("你要增加的 cartItemId 是：", cartItemId);
      axios
        .post(
          "/cart/increase",
          { cartItemId },
          {
            headers: { "Content-Type": "application/json" },
          },
        )
        .then(() => {
          console.log("數量增加");
          fetchCart();
        })
        .catch((err) => console.error("增加失敗", err));
    }

    // 減少數量（選用）
    function decreaseQuantity(cartItemId) {
      axios
        .post(
          "/cart/decrease",
          { cartItemId },
          {
            headers: { "Content-Type": "application/json" },
          },
        )
        .then(() => {
          console.log("數量減少");
          fetchCart();
        })
        .catch((err) => console.error("減少失敗", err));
    }

    //刪除
    function removeItem(cartItemId) {
      axios
        .delete("/cart/remove", {
          params: { cartItemId },
          headers: { "Content-Type": "application/json" },
        })
        .then(() => {
          console.log("刪除成功");
          fetchCart();
          // 更新購物車畫面
        })
        .catch((err) => {
          console.error("刪除失敗", err);
        });
    }

    const totalPrice = computed(() => {
      return cartItems.value.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0,
      );
    });

    function shippingMethod() {
      axios
        .post("/cart/xa", {
          deliveryMethod: selectedShippingMethod.value || "PICKUP",
          floor: Number(selectedFloor.value) || 1,
        })
        .then((res) => {
          deliveryFee.value = res.data.deliveryFee;
          floorFee.value = res.data.floorFee;
        });
    }

    // ----------------前往填寫資料頁面
    function goToForm() {
      window.location.href = "form.html";
    }

    onMounted(() => {
      fetchCart();
    });

    return {
      totalPrice,
      decreaseQuantity,
      increaseQuantity,
      goToForm,
      removeItem,
      cartItems,
      cartCount,
      shippingMethod,
      backendTotal,
      deliveryFee,
      floorFee,
      selectedShippingMethod,
      selectedFloor
    };
  },
}).mount("#app");
