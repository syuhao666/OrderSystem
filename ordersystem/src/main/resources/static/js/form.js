const { createApp, ref, computed, reactive, onMounted } = Vue;

createApp({
    setup() {
        const cartItems = ref([]); // 🆕 加這個，用來存後端購物車
        const addressData = ref({});
        const selectedCity = ref('');
        const selectedDistrict = ref('');
        const selectedZip = ref('');
        

        const formData = reactive({
            name: '張家綸',
            phone: '0978265991',
            email: 'ms0590341@gmail.com',
            city: '',
            district: '',
            zip: '',
            address: '文昌街95巷12號',
            paymentMethod: '信用卡',
        });


        const fullAddress = computed(() => {
            return `${formData.zip}${formData.city}${formData.district}${formData.address}`;
        });

        // districts 是根據選定縣市動態產生的區列表
        const districts = computed(() => {
            return selectedCity.value ? addressData.value[selectedCity.value] : {};
        });

        //-------------------------------------新
        // 🆕 載入購物車資料（從後端）
        function fetchCart() {
            axios.get('/api/items')
                .then(response => {
                    cartItems.value = response.data
                    console.log(cartItems.value);
                })
                .catch(err => {
                    console.error('抓取購物車失敗', err);
                });
        }
        onMounted(() => {
            fetchCart(); // 載入購物車
            axios.get('./address.json')
                .then(res => {
                    addressData.value = res.data;
                })
                .catch(err => {
                    console.error('載入地址資料錯誤', err);
                });
        });
        //--------------------------------------

        

        function onCityChange() {
            selectedDistrict.value = '';
            selectedZip.value = '';
            formData.city = selectedCity.value;
            formData.district = '';
            formData.zip = '';
        }

        function onDistrictChange() {
            if (selectedDistrict.value && districts.value[selectedDistrict.value]) {
                selectedZip.value = districts.value[selectedDistrict.value];
            } else {
                selectedZip.value = '';
            }
            formData.district = selectedDistrict.value;
            formData.zip = selectedZip.value;
        }

        function checkout() {
            formData.city = selectedCity.value;
            formData.district = selectedDistrict.value;
            formData.zip = selectedZip.value;

            console.log(cartItems.value)

                axios.post('/api/checkout', {
                    cart: cartItems.value,
                    name: formData.name,
                    phone: formData.phone,
                    email: formData.email,
                    address: fullAddress.value,
                    paymentMethod: formData.paymentMethod

                }, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(res => {
                        alert(res.data);                        
                    })
                    .catch(err => {
                        console.error("結帳失敗", err);
                    });

        }

       



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
            fullAddress
        }
    }
}).mount('#app');




   //---------------測試用的資料結構----------------
            // const postData = {
            //     cart: JSON.parse(localStorage.getItem('cart') || '[]'),
            //     name: formData.name,
            //     phone: formData.phone,
            //     email: formData.email,
            //     // city: formData.city,
            //     // district: formData.district,
            //     // zip: formData.zip,
            //     address: fullAddress.value,
            //     paymentMethod: formData.paymentMethod
            // };

            // console.log('準備送出的資料:', postData);
            //-------------------------------------------------------