const { createApp, ref, onMounted } = Vue

createApp({
    setup() {
        const products = ref([])


        onMounted(() => {
            axios.get('/api/productsA') // 後端商品資料網址
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

        return { products, addToCart }

    }
}).mount('#app')



// const cart = JSON.parse(localStorage.getItem('cart') || '[]')
// // 檢查購物車中是否已經有這個商品
// const found = cart.find(item => item.id === product.id)
// if (found) {
//     found.quantity++;
// } else {
//     cart.push({ ...product, quantity: 1 });
// }
// localStorage.setItem('cart', JSON.stringify(cart));


// function increaseQuantity(cartItemId) {
//     axios.post('/cart/increase', { cartItemId })
//         .then(() => {
//             console.log('數量增加成功');
//             // 這裡可以重新抓購物車列表或更新本地狀態
//         })
//         .catch(err => {
//             console.error('增加失敗', err);
//         });
// }

// function decreaseQuantity(cartItemId) {
//     axios.post('/cart/decrease', { cartItemId })
//         .then(() => {
//             console.log('數量減少成功');
//             // 更新購物車畫面
//         })
//         .catch(err => {
//             console.error('減少失敗', err);
//         });
// }

// function removeItem(cartItemId) {
//     axios.delete('/cart/remove', { params: { cartItemId } })
//         .then(() => {
//             console.log('刪除成功');
//             // 更新購物車畫面
//         })
//         .catch(err => {
//             console.error('刪除失敗', err);
//         });
// }