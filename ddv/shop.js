const products = {
  1: { id: 1, name: '椅子', price: 200 },
  2: { id: 2, name: '衣櫃', price: 1500 }
};

let cart = {};

function addToCart(productId) {
  const product = products[productId];

  if (cart[productId]) {
    cart[productId].quantity += 1;
  } else {
    cart[productId] = {
      id: product.id,
      name: product.name,
      price: product.price,
      quantity: 1
    };
  }
  console.log(`加入購物車: ${product.name} - NT$${product.price}`);
  renderCart();
}

function renderCart() {
  const cartDiv = document.getElementById("cart");
  cartDiv.innerHTML = "";

  let total = 0;
  let product = 0;

  for (const id in cart) {
    const item = cart[id];
    total += item.price * item.quantity;
    product += item.quantity


    const itemDiv = document.createElement("div");
    itemDiv.innerHTML = `
  <div class="row align-items-center mb-3">
    <!-- 商品資訊 -->
    <div class="col-md-8 bg-primary text-white p-2 rounded">
      ${item.name} - 單價：$${item.price} - 數量：${item.quantity} - 小計：$${item.price * item.quantity}
    </div>

    <!-- 操作按鈕 -->
    <div class="col-md-4 d-flex align-items-center bg-light p-2 rounded gap-2">
      <button class="btn btn-primary btn-sm" type="button" onclick="changeQuantity(${id}, -1)">－</button>
      <button class="btn btn-outline-secondary btn-sm" type="button" disabled>${item.quantity}</button>
      <button class="btn btn-primary btn-sm" type="button" onclick="changeQuantity(${id}, 1)">＋</button>
      <button class="btn btn-danger btn-sm ms-auto" onclick="removeFromCart(${id})">刪除</button>
    </div>
  </div>
`;

    console.log(itemDiv);
    cartDiv.appendChild(itemDiv);
  }
  document.getElementById("total").textContent = 'NT' + total;
  document.getElementById("product").textContent = '全部商品(' + product + ')';
}

function removeFromCart(productId) {
  delete cart[productId];
  renderCart();
}

function changeQuantity(productId, delta) {
  if (cart[productId]) {
    cart[productId].quantity += delta;
    if (cart[productId].quantity <= 0) {
      delete cart[productId];
    }
  }
  renderCart();
}