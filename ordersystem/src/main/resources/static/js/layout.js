async function loadPart(id, file) {
  const res = await fetch(file);
  document.getElementById(id).innerHTML = await res.text();
}

// 自動載入 Header & Footer
document.addEventListener("DOMContentLoaded", () => {
  loadPart("header", "header.html");
  loadPart("footer", "footer.html");
});
