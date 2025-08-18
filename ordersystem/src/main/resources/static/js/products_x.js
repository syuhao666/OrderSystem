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
            axios.get('/api/items') // 後端商品資料網址
                .then(response => {
                    cartItems.value = response.data
                    console.log(cartItems.value);
                    cartCount.value = cartItems.value.reduce((sum, item) => sum + item.quantity, 0); // 🔴 更新紅點
                })
                .catch(error => {
                    console.error('發生錯誤', error)
                })
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
        .post('/cart/add', data, { headers: { 'Content-Type': 'application/json' } })
        .then(() => {
          alert(`已成功將 ${product.name} 加入購物車！`);
          fetchCart();
        })
        .catch((error) => {
          console.error("加入購物車失敗", error);
          alert("加入購物車失敗，請稍後再試。");
        });
        
    };

    onMounted(() => {
      const urlParams = new URLSearchParams(window.location.search);
      const productId = urlParams.get('id'); // URL 範例: product.html?id=123
      fetchProductDetails(productId);
      fetchCart();
    });

    return { product, addToCart, isLoading, hasError, cartCount, cartItems, fetchCart };
  },
}).mount("#app");