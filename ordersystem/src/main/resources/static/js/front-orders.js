(async function(){
  const $  = (sel) => document.querySelector(sel);
  const $$ = (sel) => document.querySelectorAll(sel);

  function fmtMoney(n){
    if (n == null) return 'NT$0.00';
    return 'NT$' + Number(n).toLocaleString('zh-TW', {
      minimumFractionDigits: 2, maximumFractionDigits: 2
    });
  }
  function fmtDate(str){
    if (!str) return '';
    return String(str).replace('T',' ').slice(0,16);
  }
  function badge(status){
    const map = {
      CREATED:   'secondary',
      PAID:      'primary',
      SHIPPED:   'info',
      COMPLETED: 'success',
      CANCELLED: 'danger'
    };
    const cls = map[status] || 'secondary';
    return `<span class="badge bg-${cls} badge-status">${status || ''}</span>`;
  }
  async function fetchJSON(url){
    const res = await fetch(url, { credentials:'include', headers:{'Accept':'application/json'} });
    if (res.status === 401) throw new Error('401'); // 未登入
    if (!res.ok) throw new Error(String(res.status));
    return res.json();
  }

  let rawOrders = [];  // 原始列表（未篩選）

  function applyFilter(){
    const status = $('#filter-status')?.value || '';
    const from   = $('#filter-from')?.value || '';
    const to     = $('#filter-to')?.value || '';

    let list = [...rawOrders];

    if (status) list = list.filter(o => (o.status === status));
    if (from)   list = list.filter(o => (o.createdAt && o.createdAt.slice(0,10) >= from));
    if (to)     list = list.filter(o => (o.createdAt && o.createdAt.slice(0,10) <= to));

    renderTable(list);
  }

  function renderTable(orders){
    const tbody = $('#tbl-orders tbody');
    tbody.innerHTML = '';

    if (!orders || orders.length === 0){
      $('#order-empty').style.display = '';
      return;
    }
    $('#order-empty').style.display = 'none';

    orders.forEach((o, idx) => {
      const code = o.merchantTradeNo || o.id;
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${idx + 1}</td>
        <td>${code}</td>
        <td>${badge(o.status)}</td>
        <td>${fmtMoney(o.totalAmount)}</td>
        <td>${o.paymentMethod || ''}</td>
        <td>${fmtDate(o.createdAt)}</td>
        <td class="text-nowrap">
          <button class="btn btn-sm btn-outline-secondary btn-detail" data-id="${o.id}">
            查看明細
          </button>
          
        </td>
      `;
      tbody.appendChild(tr);
    });

    // 綁定「查看明細」
    $$('.btn-detail').forEach(btn => {
      btn.addEventListener('click', async (e) => {
        const id = e.currentTarget.getAttribute('data-id');
        await showDetail(id);
      });
    });
  }

  async function showDetail(orderId){
    try{
      const dto = await fetchJSON(`/api/orders/${orderId}`);
      // 基本資料
      $('#detail-basic').innerHTML = `
        <div class="row">
          <div class="col-md-6">
            <div><strong>訂單編號：</strong>${dto.merchantTradeNo || dto.id}</div>
            <div><strong>狀態：</strong>${dto.status}</div>
            <div><strong>建立時間：</strong>${fmtDate(dto.createdAt)}</div>
            <div><strong>付款方式：</strong>${dto.paymentMethod || ''}</div>
          </div>
          <div class="col-md-6">
            <div><strong>收件人：</strong>${dto.name || ''}</div>
            <div><strong>電話：</strong>${dto.phone || ''}</div>
            <div><strong>Email：</strong>${dto.email || ''}</div>
            <div><strong>地址：</strong>${dto.address || ''}</div>
          </div>
        </div>
      `;

      // 明細
      const tbody = $('#detail-items');
      tbody.innerHTML = '';
      let sum = 0;
      (dto.items || []).forEach((it, i) => {
        const sub = Number(it.unitPrice || 0) * Number(it.quantity || 0);
        sum += sub;
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${i+1}</td>
          <td>${it.name || ''}</td>
          <td>${fmtMoney(it.unitPrice)}</td>
          <td>${it.quantity}</td>
          <td>${fmtMoney(sub)}</td>
        `;
        tbody.appendChild(tr);
      });

      $('#detail-total').textContent = '總計：' + fmtMoney(dto.totalAmount ?? sum);

      // 開啟 Modal
      const modal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
      modal.show();
    }catch(e){
      if (e.message === '401'){ location.href = '/login'; return; }
      alert('載入訂單明細失敗');
    }
  }

  async function init(){
    try{
      rawOrders = await fetchJSON('/api/orders');
      renderTable(rawOrders);
    }catch(e){
      if (e.message === '401'){ location.href = '/login'; return; }
      alert('載入訂單失敗');
    }

    $('#btn-apply-filter')?.addEventListener('click', applyFilter);
  }

  document.addEventListener('DOMContentLoaded', init);
})();
