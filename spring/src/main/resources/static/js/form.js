const { createApp, ref, computed, reactive } = Vue;

createApp({
    setup() {
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

        // districts 是根據選定縣市動態產生的區列表
        const districts = computed(() => {
            return selectedCity.value ? addressData.value[selectedCity.value] : {};
        });

        // 載入 JSON 檔案
        axios.get('./address.json')
            .then(res => {
                addressData.value = res.data;
            })
            .catch(err => {
                console.error('載入地址資料錯誤', err);
            });

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

            // const postData = {
            //     cart: JSON.parse(localStorage.getItem('cart') || '[]'),
            //     name: formData.name,
            //     phone: formData.phone,
            //     email: formData.email,
            //     city: formData.city,
            //     district: formData.district,
            //     zip: formData.zip,
            //     address: formData.address,
            //     paymentMethod: formData.paymentMethod
            // };

            // console.log('準備送出的資料:', postData);


            axios.post('/api/checkout', {
                cart: JSON.parse(localStorage.getItem('cart') || '[]'),
                name: formData.name,
                phone: formData.phone,
                email: formData.email,
                city: formData.city,
                district: formData.district,
                zip: formData.zip,
                address: formData.address,
                paymentMethod: formData.paymentMethod

            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(res => {
                    alert(res.data);
                    clearCart();
                })
                .catch(err => {
                    console.error("結帳失敗", err);
                });

        }

        function clearCart() {
            localStorage.removeItem('cart');
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
            clearCart
        }
    }
}).mount('#app');