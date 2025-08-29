// /js/profile.js
(function () {
  const $ = (sel) => document.querySelector(sel);

  async function fetchJSON(url, init = {}) {
    const res = await fetch(url, {
      credentials: "include",
      headers: { "Accept": "application/json", ...(init.headers || {}) },
      ...init,
    });
    if (res.status === 401) throw new Error("401");
    if (!res.ok) {
      const message = (await res.text()) || `HTTP ${res.status}`;
      throw new Error(message);
    }
    // 204 無 body
    return res.status === 204 ? null : res.json();
  }

  async function loadMe() {
    try {
      const me = await fetchJSON("/api/profile/me");
      $("#pf-username").value = me.username || "";
      $("#pf-email").value = me.email || "";
      $("#pf-remark").value = me.remark || "";
      $("#pf-phone").value = me.phone || "";
      $("#pf-address").value = me.address || "";
    } catch (e) {
      if (e.message === "401") { location.href = "/login"; return; }
      alert("載入個資失敗：" + e.message);
    }
  }

  async function saveProfile(ev) {
    ev.preventDefault();
    const payload = {
      username: $("#pf-username").value?.trim(),
      email: $("#pf-email").value?.trim(),
      remark: $("#pf-remark").value?.trim(),
      phone: $("#pf-phone").value?.trim(),
      address: $("#pf-address").value?.trim(),
    };
    try {
      await fetchJSON("/api/profile/me", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      const tip = $("#pf-msg");
      tip.style.display = "";
      tip.textContent = "已更新。";
      setTimeout(() => (tip.style.display = "none"), 2500);
    } catch (e) {
      if (e.message === "401") { location.href = "/login"; return; }
      alert("更新失敗：" + e.message);
    }
  }

  document.addEventListener("DOMContentLoaded", () => {
    $("#profile-form").addEventListener("submit", saveProfile);
    loadMe();
  });
})();
