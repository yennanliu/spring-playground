'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var privateMessageArea = document.querySelector('#privateMessageArea');  // Added for private message area

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

var senderUsername = null; // Global variable to store the sender's username
var targetUsername = null; // Global variable to store the receiver's username

// Function to initialize the sender's username
function initializeSender(username) {
    senderUsername = username; // Set the sender's username
    console.log('>>> Sender initialized as:', senderUsername);
}

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}

function onError(error) {
    console.log(">>> error = " + error)
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            receiver: targetUsername,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function sendPrivateMessage(event, receiver) {
    var messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        var privateMessage = {
            sender: username,
            receiver: receiver,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendPrivateMessage", {}, JSON.stringify(privateMessage));  // Send private message
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

// Private message handler
function onPrivateMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');
    messageElement.classList.add('private-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(message.sender);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    privateMessageArea.appendChild(messageElement);
    privateMessageArea.scrollTop = privateMessageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

// Online user functions
function fetchUserList() {
    fetch('/user/online_user')
        .then(response => response.json())
        .then(data => {
            updateOnlineUsers(data);
        })
        .catch(error => {
            console.error('Error fetching user list: ', error);
        });
}

// Function to update the online users list and add chat functionality
function updateOnlineUsers(users) {
    const userList = document.getElementById('userList');
    userList.innerHTML = ''; // Clear the list first

    users.forEach(user => {
        const listItem = document.createElement('li');

        // Create a span for the green light
        const greenLight = document.createElement('span');
        greenLight.classList.add('green-light'); // Define CSS for this class
        listItem.appendChild(greenLight);

        // Create a span for the username
        const usernameSpan = document.createElement('span');
        usernameSpan.textContent = user;
        listItem.appendChild(usernameSpan);

        // Create a "Chat" button
        const chatButton = document.createElement('button');
        chatButton.textContent = 'Chat';

        // Add event listener to update targetUsername and start private chat
        chatButton.addEventListener('click', () => {
            targetUsername = user; // Set the target user for private chat
            console.log('>>> Selected targetUsername:', targetUsername);
            console.log('>>> Sender username:', senderUsername);
            startPrivateChat(user); // Initiate private chat with the clicked user
        });

        // Append the chat button to the list item
        listItem.appendChild(chatButton);

        // Append the list item to the user list
        userList.appendChild(listItem);
    });
}

function startPrivateChat(username) {
    // Open a popup window for the private chat
    const popupWindow = window.open('', '_blank', 'width=400,height=300');
    popupWindow.document.write('<html><head><title>Chat with ' + username + '</title></head><body>');
    popupWindow.document.write('<h2>Chat with ' + username + '</h2>');
    popupWindow.document.write('<div id="privateChatMessages"></div>');
    popupWindow.document.write('<input type="text" id="privateMessageInput" placeholder="Type a message..."/>');
    popupWindow.document.write('<button id="sendButton">Send</button>');
    popupWindow.document.write('</body></html>');

    console.log(">>> startPrivateChat username = " + username);

    // Once the popup loads, attach the send message logic
    popupWindow.unload = function () {
        const sendButton = popupWindow.document.getElementById('sendButton');
        const messageInput = popupWindow.document.getElementById('privateMessageInput');
        const privateChatMessages = popupWindow.document.getElementById('privateChatMessages');

        sendButton.addEventListener('click', () => {
            const message = messageInput.value.trim();
            if (message !== '') {
                // Extract receiver (username) from the popup title
                const receiver = popupWindow.document.title.replace('Chat with ', '').trim();

                privateChatMessages.innerHTML += '<p><strong>You:</strong> ' + message + '</p>';
                console.log('>>> Sending private message to:', receiver);

                // Send the message via STOMP
                stompClient.send('/app/chat.sendPrivateMessage', {}, JSON.stringify({
                    sender: senderUsername, // Global sender username
                    receiver: 'zzzz', // username // Extracted receiver from popup title
                    content: message,
                    type: 'CHAT'
                }));

                messageInput.value = ''; // Clear the input field
            }
        });
    };
}

// Periodically update the online users list
setInterval(fetchUserList, 5000);

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);