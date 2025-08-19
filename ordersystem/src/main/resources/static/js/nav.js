(function () {
  const $ = (id) => document.getElementById(id);

  // 導覽列元素（如果頁面沒有某些元素，不會報錯）
  const elLoginLink       = $('login-link');          // 未登入：登入圖示/連結
  const elMemberIcon      = $('member-icon');         // 已登入：頭像＋名稱（連到會員中心）
  const elMemberCenterLi  = $('member-center-link');  // 左側選單：會員中心(文字)
  const elMemberName      = $('member-name');         // 左側選單：文字內容
  const elRealLogout      = $('real-logout');         // 已登入：登出容器（li）
  const elLogoutBtn       = $('btn-logout');          // 已登入：登出按鈕
  const elMemberAvatar    = $('member-avatar');       // 導覽列頭像圓點
  const elMemberDisplay   = $('member-displayname');  // 導覽列顯示名稱

  // 會員中心內頁可選元素（若存在就一併填）
  const elMcAvatar   = $('mcAvatar');
  const elMcUsername = $('mcUsername');
  const elMcEmail    = $('mcEmail');
  const elMcJoinDate = $('mcJoinDate');
  const elMcPoints   = $('mcPoints');
  const elKpiPoints  = $('kpiPoints');
  const elKpiPending = $('kpiPending');
  const elKpiCoupons = $('kpiCoupons');

  function showLoggedIn(user) {
    if (elLoginLink)      elLoginLink.style.display = 'none';
    if (elMemberIcon)     elMemberIcon.style.display = '';
    if (elMemberCenterLi) elMemberCenterLi.style.display = '';
    if (elMemberName)     elMemberName.textContent = '會員中心';
    if (elRealLogout)     elRealLogout.style.display = '';

    // 導覽列頭像與名稱
    if (elMemberAvatar) {
      if (user.imageUrl) {
        elMemberAvatar.style.background = 'none';
        elMemberAvatar.innerHTML =
          `<img src="${user.imageUrl}" alt="頭像" style="width:100%;height:100%;object-fit:cover;">`;
      } else {
        elMemberAvatar.textContent = user.username ? user.username.charAt(0).toUpperCase() : '會';
      }
    }
    if (elMemberDisplay) elMemberDisplay.textContent = user.username || '會員';

    // 會員中心頁面資訊（若該頁面沒有這些元素，就略過）
    if (elMcUsername) elMcUsername.textContent = user.username || '';
    if (elMcEmail)    elMcEmail.textContent    = user.email || '';
    if (elMcJoinDate) elMcJoinDate.textContent = user.registerDate ? String(user.registerDate).split('T')[0] : '';
    if (elMcPoints)   elMcPoints.textContent   = user.points ?? 0;
    if (elKpiPoints)  elKpiPoints.textContent  = user.points ?? 0;

    // 其他 KPI（待你串後端時再填）
    // if (elKpiPending) elKpiPending.textContent = ...
    // if (elKpiCoupons) elKpiCoupons.textContent = ...
  }

  function showLoggedOut() {
    if (elLoginLink)      elLoginLink.style.display = '';
    if (elMemberIcon)     elMemberIcon.style.display = 'none';
    if (elMemberCenterLi) elMemberCenterLi.style.display = 'none';
    if (elRealLogout)     elRealLogout.style.display = 'none';
  }

  async function fetchCurrentUser() {
    try {
      const res = await fetch('/api/currentUser', {
        method: 'GET',
        credentials: 'include',
        headers: { 'Accept': 'application/json' }
      });
      if (!res.ok) return null;
      const user = await res.json();
      return (user && user.username) ? user : null;
    } catch {
      return null;
    }
  }

  function postLogout() {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/logout';
    document.body.appendChild(form);
    form.submit(); // 後端建議 redirect:/login
  }

  function bindLogout() {
    if (!elLogoutBtn) return;
    elLogoutBtn.addEventListener('click', function (e) {
      e.preventDefault();
      postLogout();
    });
  }

  async function init() {
    const user = await fetchCurrentUser();
    if (user) {
      showLoggedIn(user);
    } else {
      showLoggedOut();
      // 如果當前頁是會員中心頁，未登入就導回 /login
      if (document.location.pathname.toLowerCase().includes('membercenter')) {
        window.location.href = '/login';
      }
    }
    bindLogout();
  }

  document.addEventListener('DOMContentLoaded', init);
})();
