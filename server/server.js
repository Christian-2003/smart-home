const https = require('https');
const fs = require('fs');
const express = require('express');

const app = express();

app.get('/', (req, res) => {
    if (!req.socket.authorized) {
        console.error("Client certificate validation failed!");
        return res.status(401).send("Client certificate validation failed!");
    }
    res.send('Hello, World!');
});

const options = {
    key: fs.readFileSync('server.key'),
    cert: fs.readFileSync('server.crt'),
    ca: fs.readFileSync('ca.crt'),
    requestCert: true,
    rejectUnauthorized: false
}

const server = https.createServer(options, app)

server.on('secureConnection', (tlsSocket) => {
    if (!tlsSocket.authorized) {
        console.error("Client certificate error:", tlsSocket.authorizationError);
    }
});

server.listen(443, '0.0.0.0', () => {
    console.log('HTTPS Server running on port 443');
});
