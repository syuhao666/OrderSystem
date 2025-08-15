const { createApp, ref, onMounted, computed } = Vue

createApp({
    setup() {
        const cartItems = ref([]);
        const cartCount = ref(0); // 🔴 購物車紅點數量
        
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
        
        // 增加數量
        function increaseQuantity(cartItemId) {
            console.log('你要增加的 cartItemId 是：', cartItemId);
            axios.post('/cart/increase', { cartItemId }, {
                headers: { 'Content-Type': 'application/json' }
            }).then(() => {
                console.log('數量增加')
                fetchCart()
            }).catch(err => console.error('增加失敗', err))
        }
        

        // 減少數量（選用）
        function decreaseQuantity(cartItemId) {
            axios.post('/cart/decrease', { cartItemId }, {
                headers: { 'Content-Type': 'application/json' }
            }).then(() => {
                console.log('數量減少')
                fetchCart()
            }).catch(err => console.error('減少失敗', err))
        }
        
        //刪除
        function removeItem(cartItemId) {
            axios.delete('/cart/remove', { params: { cartItemId },
            headers: {'Content-Type': 'application/json'}
                }).then(() => {
                    console.log('刪除成功');
                    fetchCart()
                    // 更新購物車畫面
                }).catch(err => {
                    console.error('刪除失敗', err);
                });
        }
        
        const totalPrice = computed(() => {
            return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
        })
        
        

        // ----------------前往填寫資料頁面
        function goToForm() {            
            window.location.href = 'form.html';
        }

        // ---------------------------縣市資料
        const addressData = ref({});
        axios.get('./address.json')
            .then(res => {
                addressData.value = res.data;
            })
            .catch(err => {
                console.error('載入地址資料錯誤', err);
            });

        // ------------------------------------------

         onMounted(() => {           
            fetchCart()
        })

        return { totalPrice, decreaseQuantity, increaseQuantity, addressData, goToForm, removeItem, cartItems, cartCount}

    }
}).mount('#app')







//-----------------------------------------------前端暫存方法
        // const cart = ref(JSON.parse(localStorage.getItem('cart') || '[]'));
        
        // ----------------購物車功能
        // function increaseQuantity(item) {
        //     item.quantity++;
        //     localStorage.setItem('cart', JSON.stringify(cart.value))
        // }


        // 減少商品數量
        // function decreaseQuantity(item) {
        //     if (item.quantity > 1) {
        //         item.quantity--;
        //     } else {
        //         // 如果數量減到 1 以下，可以選擇刪除商品
        //         removeFromCart(item.id);
        //     }
        //     localStorage.setItem('cart', JSON.stringify(cart.value))
        // }

        // // 清空購物車
        // function clearCart() {
        //     cart.value = []
        //     localStorage.setItem('cart', JSON.stringify(cart.value))
        // }

        // // 刪除購物車中的商品
        // function removeFromCart(id) {
        //     cart.value = cart.value.filter(item => item.id !== id)
        //     localStorage.setItem('cart', JSON.stringify(cart.value))
        // }

        // // 計算總價
        // const totalPrice = computed(() => {
        //     return cart.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
        // })
        //----------------------------------------------------



        // ----------------結帳傳給後端

        // function checkout() {
        //     axios.post('/api/checkout', cart.value, {
        //         headers: {
        //             'Content-Type': 'application/json'
        //         }
        //     })
        //         .then(res => {
        //             alert(res.data); // 顯示「訂單已建立！」
        //             clearCart();
        //         })
        //         .catch(err => {
        //             console.error("結帳失敗", err);
        //         });
        // }
        //------------------------------------