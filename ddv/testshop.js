
        const { createApp, ref, onMounted, computed } = Vue

        createApp({
            setup() {
                const products = ref([])
                const cart = ref([])

                onMounted(() => {
                    axios.get('products.json') // 後端商品資料網址
                        .then(response => {
                            products.value = response.data
                        })
                        .catch(error => {
                            console.error('發生錯誤', error)
                        })
                })
              


                function addToCart(product) {
                    // 檢查購物車中是否已經有這個商品
                    const found = cart.value.find(item => item.id === product.id)
                    if (found) {
                        found.quantity++;
                    } else {
                        cart.value.push({ ...product, quantity: 1 });
                    }
                }

                function increaseQuantity(item) {
                    item.quantity++;
                }

                function decreaseQuantity(item) {
                    if (item.quantity > 1) {
                        item.quantity--;
                    } else {
                        // 如果數量減到 1 以下，可以選擇刪除商品
                        removeFromCart(item.id);
                    }
                }

                function clearCart() {
                    cart.value = []
                }

                function removeFromCart(id) {
                    cart.value = cart.value.filter(item => item.id !== id)
                }

                const totalPrice = computed(() => {
                    return cart.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
                })

                return { products, cart, addToCart, totalPrice, removeFromCart, decreaseQuantity, increaseQuantity, clearCart }
                
            }
        }).mount('#app')


