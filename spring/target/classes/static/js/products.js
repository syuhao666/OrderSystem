const { createApp, ref, onMounted } = Vue

createApp({
    setup() {
        const products = ref([])


        onMounted(() => {
            axios.get('http://localhost:8080/api/products') // 後端商品資料網址
                .then(response => {
                    products.value = response.data
                    console.log(products.value);
                })
                .catch(error => {
                    console.error('發生錯誤', error)
                })
                
        })

        function addToCart(product) {

            const cart = JSON.parse(localStorage.getItem('cart') || '[]')
            // 檢查購物車中是否已經有這個商品
            const found = cart.find(item => item.id === product.id)
            if (found) {
                found.quantity++;
            } else {
                cart.push({ ...product, quantity: 1 });
            }
            localStorage.setItem('cart', JSON.stringify(cart))

            console.log(cart);
            
            
            console.log(localStorage);
        }

        console.log(products.value);
        console.log(JSON.stringify(products.value));
        


        return { products , addToCart}

    }
}).mount('#app')