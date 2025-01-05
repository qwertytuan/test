document.addEventListener('DOMContentLoaded', function () {
    fetch('/api/posts')
        .then(response => response.json())
        .then(posts => {
            const postsDiv = document.getElementById('posts');
            posts.forEach(post => {
                const postElement = document.createElement('div');
                postElement.classList.add('post');
                postElement.setAttribute('data-id', post.id);
                postElement.innerHTML = `
                    <a href="#" onclick="openPost(event, ${post.id})" class="noUnderLine">
                    <h3>${post.title}</h3>
                    <p>${post.content}</p>
                    <p class="author"><strong>Author:</strong> ${post.username}</p>
                    <p class="category"><strong>Category:</strong> ${post.category}</p>
                    <p class="created-date"><strong>Created Date:</strong> ${post.createdDate}</p>
                    ${post.modifiedDate ? `<p class="modified-date"><strong>Modified Date:</strong> ${post.modifiedDate}</p>` : ''}
                    </a>
                `;
                postsDiv.appendChild(postElement);
            });
        });
});

function openPost(event, postId) {
    event.preventDefault();
    fetch(`/api/posts/${postId}`)
        .then(response => response.json())
        .then(post => {
            document.getElementById('modalTitle').innerHTML = post.title;
            document.getElementById('modalContent').innerHTML = post.content;
            document.getElementById('modalAuthor').innerHTML = `<strong>Author:</strong> ${post.username}`;
            document.getElementById('modalCategory').innerHTML = `<strong>Category:</strong> ${post.category}`;
            document.getElementById('modalCreatedDate').innerHTML = `<strong>Created Date:</strong> ${post.createdDate}`;
            if (post.modifiedDate) {
                document.getElementById('modalModifiedDate').style.display = 'block';
                document.getElementById('modalModifiedDate').innerHTML = `<strong>Modified Date:</strong> ${post.modifiedDate}`;
            } else {
                document.getElementById('modalModifiedDate').style.display = 'none';
            }
            document.getElementById('postModal').style.display = 'block';
            document.getElementById('postId').value = postId;
            GetComments(postId); // Fetch and display comments
            Recap(post.content);
        });
}

document.querySelector('.close').addEventListener('click', function() {
    document.getElementById('postModal').style.display = 'none';
});

window.addEventListener('click', function(event) {
    if (event.target === document.getElementById('postModal')) {
        document.getElementById('postModal').style.display = 'none';
    }
});

function GetComments(postId) {
    fetch(`/api/comments/byPost?postId=${postId}`)
        .then(response => response.json())
        .then(comments => {
            const commentsDiv = document.getElementById('commentsList');
            commentsDiv.innerHTML = '';
            comments.forEach(comment => {
                const commentElement = document.createElement('div');
                commentElement.classList.add('comment');
                commentElement.innerHTML = `
                    <p>${comment.content}</p>
                    <p class="author"><strong>Author:</strong> ${comment.username}</p>
                    <p class="created-date"><strong>Created Date:</strong> ${comment.createdDate}</p>
                    ${comment.modifiedDate ? `<p class="modified-date"><strong>Modified Date:</strong> ${comment.modifiedDate}</p>` : ''}
                    <button onclick="upvoteComment(${comment.id})">Upvote</button>
                    <button onclick="downvoteComment(${comment.id})">Downvote</button>
                `;
                commentsDiv.appendChild(commentElement);
            });
        });
}



document.getElementById('commentForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const postId = document.getElementById('postId').value;
    const content = document.getElementById('commentContent').value;

    fetch(`/api/posts/${postId}/comment`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            content: content,
            post: { id: postId }
        }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(comment => {
            const commentsDiv = document.getElementById('commentsList');
            const commentElement = document.createElement('div');
            commentElement.classList.add('comment');
            commentElement.innerHTML = `
            <p>${comment.content}</p>
            <p class="author"><strong>Author:</strong> ${comment.username}</p>
            <p class="created-date"><strong>Created Date:</strong> ${comment.createdDate}</p>
            ${comment.modifiedDate ? `<p class="modified-date"><strong>Modified Date:</strong> ${comment.modifiedDate}</p>` : ''}
            <button onclick="upvoteComment(${comment.id})">Upvote</button>
            <button onclick="downvoteComment(${comment.id})">Downvote</button>
        `;
            commentsDiv.appendChild(commentElement);
            document.getElementById('commentContent').value = '';
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
});

function upvoteComment(commentId) {
    fetch(`/api/comments/${commentId}/upvote`, {
        method: 'POST',
    })
        .then(response => response.json())
        .then(updatedComment => {
            // Handle the updated comment (e.g., update the UI)
        });
}

function downvoteComment(commentId) {
    fetch(`/api/comments/${commentId}/downvote`, {
        method: 'POST',
    })
        .then(response => response.json())
        .then(updatedComment => {
            // Handle the updated comment (e.g., update the UI)
        });
}
async function Recap(postContent) {
    const chatContainer = document.getElementById('chat-container');
    const messagesDiv = document.getElementById('messages');
    const loading = document.getElementById('loading');
    const input= postContent;
    console.log(postContent);
    console.log(input);
    if (input === '') return;
    let bodyContent;
        bodyContent = JSON.stringify({
            message: `Bạn là chatbot của một trang web viết bài về công nghệ, đọc đoạn văn sau và đưa ra giải thích các thuật ngữ công nghệ thường dùng:${input}`

    });
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
    messagesDiv.innerHTML += `<p>Bot: ${reply}</p>`;
}

async function SendMessage() {
    const sendBtn = document.getElementById('send-btn');
    const chatContainer = document.getElementById('chat-container');
    const messagesDiv = document.getElementById('messages');
    const inputField = document.getElementById('input');
    const loading = document.getElementById('loading');
    const input = inputField.value.trim();
    if (input === '') return;

    // Determine the body content based on the count
    let bodyContent;
    bodyContent = JSON.stringify({message: input});


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
 function openRecapModal()
 {
     const recapbtn=document.getElementById("chatModal")
     recapbtn.style.display="block";
 }

