(async function(){
  const $ = (sel) => document.querySelector(sel);

  function q(name){
    return new URLSearchParams(location.search).get(name);
  }
  function fmtMoney(n){
    if (n == null) return 'NT$0.00';
    return 'NT$' + Number(n).toLocaleString('zh-TW', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  async function fetchJSON(url){
    const res = await fetch(url, { credentials:'include', headers:{'Accept':'application/json'} });
    if (res.status === 401) throw new Error('401');
    if (!res.ok) throw new Error(String(res.status));
    return res.json();
  }

  async function load(){
    const id = q('id');
    if (!id){ location.href = '/front/orders.html'; return; }
    try{
      const od = await fetchJSON(`/api/orders/${id}`);

      const code = od.merchantTradeNo || ('OD'+String(od.id).padStart(6,'0'));
      $('#od-title').textContent = `前台｜訂單明細：${code}（${od.status}）`;

      const hasReceiver = od.name || od.phone || od.email || od.address || od.paymentMethod || od.merchantTradeNo;
      if (hasReceiver) {
        $('#receiver-box').style.display = '';
        $('#od-name').textContent = od.name || '';
        $('#od-phone').textContent = od.phone || '';
        $('#od-email').textContent = od.email || '';
        $('#od-address').textContent = od.address || '';
        $('#od-pay').textContent = od.paymentMethod || '';
        $('#od-merchant').textContent = od.merchantTradeNo || code;
      }

      const tbody = document.querySelector('#tbl-items tbody');
      tbody.innerHTML = '';
      let total = 0;
      (od.items || []).forEach(it=>{
        const sub = Number(it.unitPrice || 0) * Number(it.quantity || 0);
        total += sub;
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${it.name || ''}</td>
          <td>${fmtMoney(it.unitPrice)}</td>
          <td>${it.quantity}</td>
          <td>${fmtMoney(sub)}</td>
        `;
        tbody.appendChild(tr);
      });
      $('#od-total').textContent = fmtMoney(od.totalAmount ?? total);

    }catch(e){
      if (e.message === '401'){ location.href = '/login'; return; }
      alert('訂單不存在或讀取失敗');
      location.href='/front/orders.html';
    }
  }

  document.addEventListener('DOMContentLoaded', load);
})();
