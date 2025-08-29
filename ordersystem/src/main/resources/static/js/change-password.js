// /src/main/resources/static/js/change-password.js
(function(){
  const $ = (sel) => document.querySelector(sel);

  function showMsg(text, ok){
    const el = $('#pw-msg');
    el.textContent = text;
    el.style.display = '';
    el.className = 'form-text ' + (ok ? 'text-success' : 'text-danger');
  }

  async function changePassword(e){
    e.preventDefault();
    const current = $('#pw-current').value.trim();
    const npw     = $('#pw-new').value.trim();
    const confirm = $('#pw-confirm').value.trim();

    if (!current){ showMsg('請輸入目前密碼', false); return; }
    if (!npw || npw.length < 6){ showMsg('新密碼至少 6 碼', false); return; }
    if (npw !== confirm){ showMsg('新密碼與確認不一致', false); return; }

    try{
      const res = await fetch('/api/profile/password', {
        method: 'PUT',
        credentials: 'include',
        headers: {'Content-Type':'application/json', 'Accept':'application/json'},
        body: JSON.stringify({ oldPassword: current, newPassword: npw })
      });

      if (res.status === 204){
        showMsg('密碼已更新，下次登入請使用新密碼。', true);
        // 清空欄位
        $('#pw-current').value = '';
        $('#pw-new').value = '';
        $('#pw-confirm').value = '';
        return;
      }
      if (res.status === 401){
        // 未登入
        location.href = '/login';
        return;
      }

      // 其他狀態：盡量把後端訊息顯示出來
      let msg = '變更失敗';
      try {
        const data = await res.json();
        msg = typeof data === 'string' ? data : (data.message || JSON.stringify(data));
      } catch(_) {
        msg = await res.text();
      }
      showMsg(msg || '變更失敗', false);

    }catch(err){
      console.error(err);
      showMsg('系統錯誤，請稍後再試', false);
    }
  }

  document.addEventListener('DOMContentLoaded', function(){
    const form = document.getElementById('pw-form');
    form.addEventListener('submit', changePassword);
  });
})();
