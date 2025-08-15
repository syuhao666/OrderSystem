const { createApp, ref, onMounted } = Vue;

createApp({
  setup() {
    const product = ref(null);
    const isLoading = ref(false);
    const hasError = ref(false);

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
      const data = { id: product.id, quantity: 1 };
      axios
        .post('/cart/add', data, { headers: { 'Content-Type': 'application/json' } })
        .then(() => {
          alert(`已成功將 ${product.name} 加入購物車！`);
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
    });

    return { product, addToCart, isLoading, hasError };
  },
}).mount("#app");