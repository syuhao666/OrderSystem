document.getElementById('google-login-btn').addEventListener('click', function () {
    const clientId = '${{ secrets.CLIENT_ID }}';// 要填的 client ID
    const redirectUri = 'http://localhost:8080/oauth2/callback';
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