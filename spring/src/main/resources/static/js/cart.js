const { createApp, ref, computed } = Vue

createApp({
    setup() {

        const cart = ref(JSON.parse(localStorage.getItem('cart') || '[]'));
        console.log(cart);
        console.log(cart.value);
        console.log(JSON.stringify(cart.value));
        console.log(localStorage);
        // ----------------購物車功能
        function increaseQuantity(item) {
            item.quantity++;
            localStorage.setItem('cart', JSON.stringify(cart.value))
        }


        // 減少商品數量
        function decreaseQuantity(item) {
            if (item.quantity > 1) {
                item.quantity--;
            } else {
                // 如果數量減到 1 以下，可以選擇刪除商品
                removeFromCart(item.id);
            }
            localStorage.setItem('cart', JSON.stringify(cart.value))
        }

        // 清空購物車
        function clearCart() {
            cart.value = []
            localStorage.setItem('cart', JSON.stringify(cart.value))
        }

        // 刪除購物車中的商品
        function removeFromCart(id) {
            cart.value = cart.value.filter(item => item.id !== id)
            localStorage.setItem('cart', JSON.stringify(cart.value))
        }

        // 計算總價
        const totalPrice = computed(() => {
            return cart.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
        })

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

        
        // ----------------前往填寫資料頁面
        function goToForm() {
            localStorage.setItem('cart', JSON.stringify(cart.value));
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













        return { cart, totalPrice, removeFromCart, decreaseQuantity, increaseQuantity, clearCart, addressData, goToForm}

    }
}).mount('#app')
