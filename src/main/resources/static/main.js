'use strict';

// Page selectors
var loginPage = document.querySelector('#login-page');
var chatPage = document.querySelector('#chat-page');

// Form selectors
var loginForm = document.querySelector('#loginForm');
var signupForm = document.querySelector('#signupForm');
var messageForm = document.querySelector('#messageForm');

// Input selectors
var messageInput = document.querySelector('#message');

// Other selectors
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var typingIndicator = document.querySelector('#typing-indicator');

// Toggles
var showSignup = document.querySelector('#show-signup');
var showLogin = document.querySelector('#show-login');

var stompClient = null;
var username = null;
var jwtToken = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// Typing indicator state
var typingTimer = null;
var isTyping = false;
var activeTypers = [];

// --- Event Listeners ---

document.addEventListener('DOMContentLoaded', () => {
    const savedToken = localStorage.getItem('jwt');
    if (savedToken) {
        try {
            const tokenPayload = parseJwt(savedToken);
            // Check if token is expired
            if (tokenPayload.exp * 1000 < Date.now()) {
                localStorage.removeItem('jwt');
                return; // Token is expired, do nothing
            }

            jwtToken = savedToken;
            username = tokenPayload.firstname + ' ' + tokenPayload.lastname;

            loginPage.classList.add('hidden');
            chatPage.classList.remove('hidden');
            
            fetchChatHistory();
            connect();
        } catch (e) {
            // If token is invalid for any reason, clear it
            localStorage.removeItem('jwt');
        }
    }
});

showSignup.addEventListener('click', () => {
    document.getElementById('login-form').classList.add('hidden');
    document.getElementById('signup-form').classList.remove('hidden');
});

showLogin.addEventListener('click', () => {
    document.getElementById('signup-form').classList.add('hidden');
    document.getElementById('login-form').classList.remove('hidden');
});

loginForm.addEventListener('submit', login, true);
signupForm.addEventListener('submit', signup, true);
messageForm.addEventListener('submit', sendMessage, true);

messageInput.addEventListener('input', handleTyping);

// --- Typing Functions ---

function handleTyping() {
    if (!isTyping) {
        isTyping = true;
        stompClient.send("/app/chat.typing", {}, JSON.stringify({ sender: username, type: 'TYPING' }));
    }
    clearTimeout(typingTimer);
    typingTimer = setTimeout(() => {
        isTyping = false;
        stompClient.send("/app/chat.typing", {}, JSON.stringify({ sender: username, type: 'STOP_TYPING' }));
    }, 1000); // 1 second timeout
}

function updateTypingIndicator() {
    const filteredTypers = activeTypers.filter(typer => typer !== username);
    if (filteredTypers.length === 0) {
        typingIndicator.textContent = '';
    } else if (filteredTypers.length === 1) {
        typingIndicator.textContent = `${filteredTypers[0]} is typing...`;
    } else if (filteredTypers.length === 2) {
        typingIndicator.textContent = `${filteredTypers[0]} and ${filteredTypers[1]} are typing...`;
    } else {
        typingIndicator.textContent = 'Multiple users are typing...';
    }
}

// --- Authentication Functions ---

function signup(event) {
    event.preventDefault();
    const firstname = document.querySelector('#signup-firstname').value.trim();
    const lastname = document.querySelector('#signup-lastname').value.trim();
    const email = document.querySelector('#signup-email').value.trim();
    const password = document.querySelector('#signup-password').value.trim();

    if (!firstname || !lastname || !email || !password) {
        alert('All fields are required.');
        return;
    }

    const signupRequest = { firstname, lastname, email, password };

    fetch('/api/v1/auth/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(signupRequest)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Signup failed');
        }
        alert('Signup successful! Please login.');
        showLogin.click();
    })
    .catch(error => alert(error.message));
}

function login(event) {
    event.preventDefault();
    const email = document.querySelector('#login-email').value.trim();
    const password = document.querySelector('#login-password').value.trim();

    if (!email || !password) {
        alert('Email and password are required.');
        return;
    }

    const loginRequest = { email, password };

    fetch('/api/v1/auth/signin', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(loginRequest)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Login failed');
        }
        return response.json();
    })
    .then(data => {
        jwtToken = data.token;
        localStorage.setItem('jwt', jwtToken);
        
        const tokenPayload = parseJwt(jwtToken);
        username = tokenPayload.firstname + ' ' + tokenPayload.lastname;

        loginPage.classList.add('hidden');
        chatPage.classList.remove('hidden');
        
        fetchChatHistory();
        connect();
    })
    .catch(error => alert(error.message));
}

// --- Chat History ---
function fetchChatHistory() {
    fetch('/messages', {
        headers: {
            'Authorization': `Bearer ${jwtToken}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Could not fetch chat history.');
        }
        return response.json();
    })
    .then(messages => {
        messages.forEach(message => {
            displayMessage(message, false);
        });
        messageArea.scrollTop = 0;
    })
    .catch(error => console.error(error));
}

// --- WebSocket Functions ---

function connect() {
    if (username) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        var headers = {
            'Authorization': `Bearer ${jwtToken}`
        };
        stompClient.connect(headers, onConnected, onError);
    }
}

function onConnected() {
    stompClient.subscribe('/topic/public', onMessageReceived);
    stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
    connectingElement.style.display = 'none';
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    if (message.type === 'TYPING') {
        if (!activeTypers.includes(message.sender) && message.sender !== username) {
            activeTypers.push(message.sender);
        }
    } else if (message.type === 'STOP_TYPING') {
        activeTypers = activeTypers.filter(typer => typer !== message.sender);
    } else {
        displayMessage(message, true);
    }
    updateTypingIndicator();
}

function displayMessage(message, shouldScroll) {
    const isScrolledToBottom = messageArea.scrollTop === 0;

    var messageElement = document.createElement('li');

    if (message.type === 'JOIN' || message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        var text = message.sender + (message.type === 'JOIN' ? ' joined!' : ' left!');
        messageElement.innerHTML = `<p>${text}</p>`;
    } else if (message.type === 'CHAT') {
        messageElement.classList.add('chat-message');
        if (message.sender === username) {
            messageElement.classList.add('sender');
        } else {
            messageElement.classList.add('receiver');
        }

        var avatarColor = getAvatarColor(message.sender);
        var avatarChar = message.sender[0];

        messageElement.innerHTML = 
            `<i style="background-color: ${avatarColor}">${avatarChar}</i>
            <span>${message.sender}</span>
            <p>${message.content}</p>`;
    }

    messageArea.insertBefore(messageElement, messageArea.firstChild);

    if (shouldScroll && isScrolledToBottom) {
        messageArea.scrollTop = 0;
    }
}


// --- Helper Functions ---

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function parseJwt(token) {
    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
}
