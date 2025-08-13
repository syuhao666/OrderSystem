document.getElementById('google-login-btn').addEventListener('click', function () {
    const clientId = "1097626042096-kvaim9a40uijmsst8o8cejqm5eufq1qm.apps.googleusercontent.com";// 要填的 client ID
    const redirectUri = 'https://accurately-enhanced-raven.ngrok-free.app/webhook/google';
    const scope = 'openid profile email';
    const authUrl =
      `https://accounts.google.com/o/oauth2/v2/auth` +
      `?client_id=${clientId}` +
      `&redirect_uri=${encodeURIComponent(redirectUri)}` +
      `&response_type=code` +
      `&scope=${encodeURIComponent(scope)}` +
      `&access_type=offline` +
      `&prompt=consent`;

    window.location.href = authUrl; // 觸發 redirect
  });