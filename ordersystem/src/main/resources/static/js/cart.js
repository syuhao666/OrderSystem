const { createApp, ref, onMounted, computed } = Vue;

createApp({
  setup() {
    const cartItems = ref([]);
    const cartCount = ref(0);
    const selectedDelivery = ref(""); // 預設空字串
    const selectedFloor = ref(""); // 樓層選擇

    const productTotal = ref(0);
    const deliveryFee = ref(0);
    const floorFee = ref(0);
    const finalTotal = ref(0);

    const showDeliveryWarning = ref(false);
    const showFloorWarning = ref(false);

    // 取得購物車內容
    function fetchCart() {
      axios
        .get("/api/items")
        .then((response) => {
          cartItems.value = response.data;
          console.log(cartItems.value);
        })
        .catch((error) => console.error("發生錯誤", error));
    }

    function fetchCartCount() {
      axios
        .get("/cart/count")
        .then((res) => {
          cartCount.value = res.data;
        })
        .catch((err) => console.error("無法取得購物車數量", err));
    }

    function increaseQuantity(cartItemId) {
      axios
        .post("/cart/increase", { cartItemId }, { headers: { "Content-Type": "application/json" } })
        .then(() => {
          fetchCart();
          shippingMethod();
          fetchCartCount();
        })
        .catch((err) => console.error("增加失敗", err));
    }

    function decreaseQuantity(cartItemId) {
      axios
        .post("/cart/decrease", { cartItemId }, { headers: { "Content-Type": "application/json" } })
        .then(() => {
          fetchCart();
          shippingMethod();
          fetchCartCount();
        })
        .catch((err) => console.error("減少失敗", err));
    }

    function removeItem(cartItemId) {
      axios
        .delete("/cart/remove", { params: { cartItemId }, headers: { "Content-Type": "application/json" } })
        .then(() => {
          fetchCart();
          shippingMethod();
          fetchCartCount();
        })
        .catch((err) => console.error("刪除失敗", err));
    }

    // 計算運費、樓層費與總額
    function shippingMethod() {
      axios
        .post("/cart/xa", {
          deliveryMethod: selectedDelivery.value || "PICKUP",
          floor: Number(selectedFloor.value) || 1,
        })
        .then((res) => {
          deliveryFee.value = res.data.deliveryFee;
          floorFee.value = res.data.floorFee;
          productTotal.value = res.data.productTotal;
          finalTotal.value = res.data.finalTotal;
        });
    }

    // 驗證運送方式與樓層
    function validateSelection() {
      showDeliveryWarning.value = !selectedDelivery.value;
      showFloorWarning.value = !selectedFloor.value;
      shippingMethod();
  
    }

    // 判斷是否可以進入下一步
    const canProceed = computed(() => !!selectedDelivery.value && !!selectedFloor.value);

    // 下一步按鈕
    function goToForm() {
      validateSelection();
      if (canProceed.value) {
        window.location.href = "form.html";
      }
    }

    onMounted(() => {
      fetchCart();
      shippingMethod();
      fetchCartCount();
      validateSelection();
    });

    return {
      decreaseQuantity,
      increaseQuantity,
      goToForm,
      removeItem,
      cartItems,
      cartCount,
      shippingMethod,
      selectedDelivery,
      selectedFloor,
      floorFee,
      deliveryFee,
      productTotal,
      finalTotal,
      fetchCartCount,
      canProceed,
      validateSelection,
      showDeliveryWarning,
      showFloorWarning
    };
  },
}).mount("#app");
