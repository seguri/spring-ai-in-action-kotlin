document.addEventListener('DOMContentLoaded', () => {
  const chatMessages = document.getElementById('chat-messages');
  const chatContainer = document.getElementById('chat-container');
  const messageInput = document.getElementById('message-input');
  const sendButton = document.getElementById('send-button');

  let eventSource = null;

  function appendUserMessage(message) {
    const messageElement = document.createElement('div');
    messageElement.classList.add('message', 'user-message');
    messageElement.textContent = message;
    chatMessages.appendChild(messageElement);
    chatMessages.scrollTop = chatMessages.scrollHeight;
  }

  function sendMessage() {
    const message = messageInput.value;
    if (message) {
      appendUserMessage(message);
      messageInput.value = '';

      if (eventSource) {
        eventSource.close();
      }

      eventSource = new EventSource(`https://localhost:8443/translate-stream/it-de?message=${encodeURIComponent(message)}`);

      let botMessageElement = null;
      let botMessage = '';

      eventSource.onmessage = (event) => {
        botMessage += event.data;

        if (!botMessageElement) {
          botMessageElement = document.createElement('div');
          botMessageElement.classList.add('message', 'bot-message');
          chatMessages.appendChild(botMessageElement);
        }

        botMessageElement.innerHTML = marked.parse(botMessage);
        chatContainer.scrollTop = chatContainer.scrollHeight;
      };

      eventSource.onerror = (error) => {
        console.error('EventSource failed:', error);
        eventSource.close();
      };

      eventSource.onopen = () => {
        console.log('EventSource connection opened');
      };
    }
  }

  sendButton.addEventListener('click', sendMessage);
  messageInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
      sendMessage();
    }
  });
});
