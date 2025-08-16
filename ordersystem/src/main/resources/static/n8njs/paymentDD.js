// ✅ 適用 n8n 的「Function 節點」的綠界 CheckMacValue 計算器（同步）

function encodeSpecialUrl(str) {
  return encodeURIComponent(str)
    .toLowerCase()
    .replace(/%20/g, '+')
    .replace(/%21/g, '!')
    .replace(/%28/g, '(')
    .replace(/%29/g, ')')
    .replace(/%2a/g, '*');
}

// ✅ SHA256 實作（同步）
function sha256(str) {
  function rotateRight(n, x) {
    return (x >>> n) | (x << (32 - n));
  }

  function toUtf8Bytes(str) {
    const utf8 = [];
    for (let i = 0; i < str.length; i++) {
      const charcode = str.charCodeAt(i);
      if (charcode < 0x80) utf8.push(charcode);
      else if (charcode < 0x800) {
        utf8.push(0xc0 | (charcode >> 6), 0x80 | (charcode & 0x3f));
      } else if (charcode < 0xd800 || charcode >= 0xe000) {
        utf8.push(0xe0 | (charcode >> 12), 0x80 | ((charcode >> 6) & 0x3f), 0x80 | (charcode & 0x3f));
      } else {
        i++;
        const code = 0x10000 + (((charcode & 0x3ff) << 10) | (str.charCodeAt(i) & 0x3ff));
        utf8.push(
          0xf0 | (code >> 18),
          0x80 | ((code >> 12) & 0x3f),
          0x80 | ((code >> 6) & 0x3f),
          0x80 | (code & 0x3f)
        );
      }
    }
    return utf8;
  }

  function toHex(n) {
    return n.toString(16).padStart(8, '0');
  }

  const K = [...Array(64)].map((_, i) => Math.floor(Math.abs(Math.sin(i + 1)) * 2 ** 32));
  const H = [
    0x6a09e667, 0xbb67ae85,
    0x3c6ef372, 0xa54ff53a,
    0x510e527f, 0x9b05688c,
    0x1f83d9ab, 0x5be0cd19
  ];

  const msg = toUtf8Bytes(str);
  msg.push(0x80);
  while ((msg.length % 64) !== 56) msg.push(0);
  const bitLen = str.length * 8;
  for (let i = 7; i >= 0; i--) {
    msg.push((bitLen >>> (i * 8)) & 0xff);
  }

  const w = new Array(64);
  for (let i = 0; i < msg.length; i += 64) {
    for (let j = 0; j < 16; j++) {
      w[j] = (msg[i + (j * 4)] << 24) | (msg[i + (j * 4) + 1] << 16) | (msg[i + (j * 4) + 2] << 8) | (msg[i + (j * 4) + 3]);
    }

    for (let j = 16; j < 64; j++) {
      const s0 = rotateRight(7, w[j - 15]) ^ rotateRight(18, w[j - 15]) ^ (w[j - 15] >>> 3);
      const s1 = rotateRight(17, w[j - 2]) ^ rotateRight(19, w[j - 2]) ^ (w[j - 2] >>> 10);
      w[j] = (w[j - 16] + s0 + w[j - 7] + s1) >>> 0;
    }

    let [a, b, c, d, e, f, g, h] = H;

    for (let j = 0; j < 64; j++) {
      const S1 = rotateRight(6, e) ^ rotateRight(11, e) ^ rotateRight(25, e);
      const ch = (e & f) ^ (~e & g);
      const temp1 = (h + S1 + ch + K[j] + w[j]) >>> 0;
      const S0 = rotateRight(2, a) ^ rotateRight(13, a) ^ rotateRight(22, a);
      const maj = (a & b) ^ (a & c) ^ (b & c);
      const temp2 = (S0 + maj) >>> 0;

      [h, g, f, e, d, c, b, a] = [
        g, f, e, (d + temp1) >>> 0,
        c, b, a, (temp1 + temp2) >>> 0
      ];
    }

    H[0] = (H[0] + a) >>> 0;
    H[1] = (H[1] + b) >>> 0;
    H[2] = (H[2] + c) >>> 0;
    H[3] = (H[3] + d) >>> 0;
    H[4] = (H[4] + e) >>> 0;
    H[5] = (H[5] + f) >>> 0;
    H[6] = (H[6] + g) >>> 0;
    H[7] = (H[7] + h) >>> 0;
  }

  return H.map(toHex).join('').toUpperCase();
}

// ✅ 商店資訊
const hashKey = 'pwFHCqoQZGmho4w6';
const hashIV = 'EkRm7iFT261dpevs';
const merchantID = '3002607';

// ✅ 模擬前端傳來的資料
const body = $json.body;
const now = new Date();
const merchantTradeDate = now.toISOString().slice(0, 19).replace('T', ' ').replace(/-/g, '/');

const orderId = 'TEST' + now.getTime();
const totalAmount = 1000;
const itemNames = body.cart.map(item => item.name).join('#');

const params = {
  MerchantID: merchantID,
  MerchantTradeNo: orderId,
  MerchantTradeDate: merchantTradeDate,
  PaymentType: 'aio',
  TotalAmount: totalAmount.toString(),
  TradeDesc: '訂單測試',
  ItemName: itemNames,
  ReturnURL: 'https://accurately-enhanced-raven.ngrok-free.app/webhook-test/callback',
  ChoosePayment: 'Credit',
  ClientBackURL: 'http://localhost:8080/success',
};

const sortedKeys = Object.keys(params).sort();
const rawQuery = sortedKeys.map(key => `${key}=${params[key]}`).join('&');
const rawString = `HashKey=${hashKey}&${rawQuery}&HashIV=${hashIV}`;
const encoded = encodeSpecialUrl(rawString);
const checkMacValue = sha256(encoded);

params.CheckMacValue = checkMacValue;

return params;
