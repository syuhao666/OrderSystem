(async function(){
  const $  = (sel) => document.querySelector(sel);

  function fmtMoney(n){
    return 'NT$' + Number(n).toLocaleString('zh-TW', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }
  function fmtDate(str){
    if (!str) return '';
    return String(str).replace('T',' ').slice(0,16);
  }
  async function fetchJSON(url){
    const res = await fetch(url, { credentials:'include', headers:{'Accept':'application/json'} });
    if (!res.ok) throw new Error(String(res.status));
    return res.json();
  }

  async function loadOrders(){
    try{
      const orders = await fetchJSON('/api/orders');
      const tbody = document.querySelector('#tbl-orders tbody');
      tbody.innerHTML = '';

      if (!orders || orders.length === 0){
        document.getElementById('order-empty').style.display = '';
        return;
      }
      document.getElementById('order-empty').style.display = 'none';

      orders.forEach(o=>{
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${o.orderNo}</td>
          <td>${o.status}</td>
          <td>${fmtMoney(o.totalAmount)}</td>
          <td>${fmtDate(o.createdAt)}</td>
        `;
        tbody.appendChild(tr);
      });
    }catch(e){
      if (e.message === '401'){ location.href = '/login'; return; }
      alert('載入訂單失敗');
    }
  }

  await loadOrders();
})();
