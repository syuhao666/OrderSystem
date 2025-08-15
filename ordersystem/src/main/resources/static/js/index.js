const { createApp, ref, onMounted } = Vue

createApp({
    setup() {
        const products = ref([])
        const cartCount = ref(0); // 🔴 購物車紅點數量
        const cartItems = ref([]);

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

        onMounted(() => {
            fetchCart();
            axios.get('/api/products') // 後端商品資料網址
                .then(response => {
                    products.value = response.data
                    console.log(products.value);
                    
                })
                .catch(error => {
                    console.error('發生錯誤', error)
                })
            

        })

        function addToCart(product) {
            
            const data = {
                id: product.id,
                quantity: 1
            }

            console.log('送出資料:', data)

            axios.post('/cart/add', data, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    console.log('加入購物車資料庫成功', response.data)
                })
                .catch(error => {
                    console.error('加入購物車失敗', error)
                })

        }

        return { products, addToCart, cartCount, fetchCart, cartItems }

    }
}).mount('#app')



