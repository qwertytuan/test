document.addEventListener('DOMContentLoaded', function () {
    fetch('/api/posts')
        .then(response => response.json())
        .then(posts => {
            const postsDiv = document.getElementById('posts');
            posts.forEach(post => {
                const postElement = document.createElement('div');
                postElement.classList.add('post');
                postElement.innerHTML = `
                    <h3>${post.title}</h3>
                    <p>${post.content}</p>
                    <p class="author"><strong>Author:</strong> ${post.username}</p>
                    <p class="category"><strong>Category:</strong> ${post.category}</p>
                    <p class="created-date"><strong>Created Date:</strong> ${post.createdDate}</p>
                    ${post.modifiedDate ? `<p class="modified-date"><strong>Modified Date:</strong> ${post.modifiedDate}</p>` : ''}
                `;
                postsDiv.appendChild(postElement);
            });
        });
});