// /js/change-password.js
const API_PUT_PASSWORD = '/api/profile/password';

(function () {
  const $ = (id) => document.getElementById(id);

  async function fetchJSON(url, options = {}) {
    const res = await fetch(url, {
      credentials: 'include',
      ...options,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        ...(options.headers || {})
      }
    });
    if (res.status === 401) { location.href = '/login'; return null; }
    if (!res.ok) throw new Error(await res.text());
    return res.json().catch(() => ({}));
  }

  function showMsg(ok, text) {
    const msg = $('pw-msg');
    msg.style.display = '';
    msg.className = ok ? 'form-text text-success' : 'form-text text-danger';
    msg.textContent = text;
  }

  async function onSubmit(e) {
    e.preventDefault();
    const cur = $('pw-current').value.trim();
    const n1  = $('pw-new').value.trim();
    const n2  = $('pw-confirm').value.trim();

    if (!cur || !n1 || !n2) { showMsg(false, '請完整填寫'); return; }
    if (n1.length < 6)      { showMsg(false, '新密碼至少 6 碼'); return; }
    if (n1 !== n2)          { showMsg(false, '兩次新密碼不一致'); return; }

    try {
      await fetchJSON(API_PUT_PASSWORD, {
        method: 'PUT',
        body: JSON.stringify({ currentPassword: cur, newPassword: n1 })
      });
      $('pw-current').value = $('pw-new').value = $('pw-confirm').value = '';
      showMsg(true, '密碼已變更');
    } catch (err) {
      showMsg(false, err.message || '變更失敗');
    }
  }

  document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('pw-form').addEventListener('submit', onSubmit);
  });
})();
