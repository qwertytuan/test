
async function SendMessage() {
    let count = 0;
    const sendBtn = document.getElementById('send-btn');
    const chatContainer = document.getElementById('chat-container');
    const messagesDiv = document.getElementById('messages');
    const inputField = document.getElementById('input');
    const loading = document.getElementById('loading');
    const input = inputField.value.trim();
    if (input === '') return;

    // Determine the body content based on the count
    let bodyContent;
    if (count === 1) {
        bodyContent = JSON.stringify({
            message: "Bạn là chatbot của một trang web viết bài về công nghệ, đọc đoạn văn sau và đưa ra giải thích các thuật ngữ công nghệ thường dùng:"
        });
        count++;
    } else {
        bodyContent = JSON.stringify({
            message: input
        });
    }
    loading.style.display = 'block';
    const response = await fetch('/api/chatbot/message', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: bodyContent
    });
    loading.style.display = 'none';
    const reply = await response.text();
    messagesDiv.innerHTML += `<p>You: ${input}</p><p>Bot: ${reply}</p>`;
    inputField.value = '';
}
