// /js/profile.js
const API_GET_USER    = '/api/user';
const API_PUT_PROFILE = '/api/user/profile';

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

  async function loadProfile() {
    const u = await fetchJSON(API_GET_USER);
    if (!u) return;
    $('pf-username').value = u.username || '';
    $('pf-email').value    = u.email || '';
    $('pf-remark').value   = u.remark || '';
    $('pf-phone').value    = u.phone || '';
    $('pf-address').value  = u.address || '';
  }

  async function submitProfile(e) {
    e.preventDefault();
    const payload = {
      username: $('pf-username').value.trim(),
      email:    $('pf-email').value.trim(),
      remark:   $('pf-remark').value.trim(),
      phone:    $('pf-phone').value.trim(),
      address:  $('pf-address').value.trim()
    };
    if (!payload.username || !payload.email) {
      alert('請填寫暱稱與 Email');
      return;
    }
    await fetchJSON(API_PUT_PROFILE, { method: 'PUT', body: JSON.stringify(payload) });
    const msg = document.getElementById('pf-msg');
    msg.style.display = '';
    msg.textContent = '已更新。';
    setTimeout(() => msg.style.display = 'none', 2000);
  }

  document.addEventListener('DOMContentLoaded', () => {
    loadProfile();
    document.getElementById('profile-form').addEventListener('submit', submitProfile);
  });
})();
