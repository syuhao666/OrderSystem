const { createApp, ref, computed, reactive, onMounted } = Vue;

createApp({
  setup() {
    const cartItems = ref([]); // 🆕 加這個，用來存後端購物車
    const addressData = ref({});
    const selectedCity = ref("");
    const selectedDistrict = ref("");
    const selectedZip = ref("");

    const cartCount = ref(0); // 🔴 購物車紅點數量
    const checkoutData = ref({
      items: [],
      productTotal: 0,
      deliveryFee: 0,
      floorFee: 0,
      finalTotal: 0,
      
    });

    const formData = reactive({
      name: "hwgtwet",
      phone: "0987654321",
      email: "fsdfsfsfd@gmail.com",
      city: "",
      district: "",
      zip: "",
      address: "rawsrar",
      paymentMethod: "信用卡",
    });

    const fullAddress = computed(() => {
      return `${formData.zip}${formData.city}${formData.district}${formData.address}`;
    });

    // districts 是根據選定縣市動態產生的區列表
    const districts = computed(() => {
      return selectedCity.value ? addressData.value[selectedCity.value] : {};
    });
    //-----------------------------------------

    //-------------------------------------新
    // 🆕 載入購物車資料（從後端）
    function fetchCart() {
      axios
        .get("/api/items")
        .then((response) => {
          cartItems.value = response.data;
          console.log(cartItems.value);
        })
        .catch((err) => {
          console.error("抓取購物車失敗", err);
        });
    }

    function fetchCartItem() {
      axios
        .get("/cart/xa/data")
        .then((res) => {
          checkoutData.value = res.data; // 這裡就是 OrderResponse
          console.log("結帳資料", checkoutData.value);
        })
        .catch((err) => console.error("無法取得購物車明細", err));
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
      fetchCart(); // 載入購物車
      fetchCartCount();
      fetchCartItem();
      axios
        .get("./address.json")
        .then((res) => {
          addressData.value = res.data;
        })
        .catch((err) => {
          console.error("載入地址資料錯誤", err);
        });
    });
    //--------------------------------------

    function onCityChange() {
      selectedDistrict.value = "";
      selectedZip.value = "";
      formData.city = selectedCity.value;
      formData.district = "";
      formData.zip = "";
    }

    function onDistrictChange() {
      if (selectedDistrict.value && districts.value[selectedDistrict.value]) {
        selectedZip.value = districts.value[selectedDistrict.value];
      } else {
        selectedZip.value = "";
      }
      formData.district = selectedDistrict.value;
      formData.zip = selectedZip.value;
    }
    //----------------------------------------好的

    //------------------------------------------------------------
    function checkout() {
      formData.city = selectedCity.value;
      formData.district = selectedDistrict.value;
      formData.zip = selectedZip.value;

      axios
        .post(
          "/api/checkout",
          {
            cart: cartItems.value,
            name: formData.name,
            phone: formData.phone,
            email: formData.email,
            address: fullAddress.value,
            paymentMethod: formData.paymentMethod,
            totalPrice: checkoutData.value.finalTotal
          },
          {
            headers: { "Content-Type": "application/json" },
          },
        )
        .then((res) => {
          // 後端回傳的會是綠界付款表單 HTML
          //   document.open();
          //   document.write(res.data);
          //   document.close();
          document.body.innerHTML = res.data; // 把表單放進去
          document.getElementById("ecpayForm").submit();
        })
        .catch((err) => {
          console.error("結帳失敗", err);
        });
    }
    //------------------------------------------------------------

    

    return {
      addressData,
      selectedCity,
      selectedDistrict,
      selectedZip,
      districts,
      onCityChange,
      onDistrictChange,
      checkout,
      formData,
      cartItems,
      fullAddress,
      
      cartCount,
      fetchCartCount,
      fetchCartItem,
      checkoutData
    };
  },
}).mount("#app");


