     const { createApp, ref, computed } = Vue

        createApp({
            setup() {
                
                const cart = ref(JSON.parse(localStorage.getItem('cart') || '[]'))

                function increaseQuantity(item) {
                    item.quantity++;
                    localStorage.setItem('cart', JSON.stringify(cart.value))
                }

                function decreaseQuantity(item) {
                    if (item.quantity > 1) {
                        item.quantity--;
                    } else {
                        // 如果數量減到 1 以下，可以選擇刪除商品
                        removeFromCart(item.id);
                    }
                    localStorage.setItem('cart', JSON.stringify(cart.value))
                }

                function clearCart() {
                    cart.value = []
                    localStorage.setItem('cart', JSON.stringify(cart.value))
                }

                function removeFromCart(id) {
                    cart.value = cart.value.filter(item => item.id !== id)
                    localStorage.setItem('cart', JSON.stringify(cart.value))
                }

                const totalPrice = computed(() => {
                    return cart.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
                })

                return {  cart, totalPrice, removeFromCart, decreaseQuantity, increaseQuantity, clearCart }
                
            }
        }).mount('#app')
