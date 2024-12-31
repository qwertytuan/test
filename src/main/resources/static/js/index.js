document.addEventListener('DOMContentLoaded', function () {
    fetch('/api/posts')
        .then(response => response.json())
        .then(posts => {
            const postsDiv = document.getElementById('posts');
            posts.forEach(post => {
                const postElement = document.createElement('div');
                postElement.innerHTML = `
                        <h3>${post.title}</h3>
                        <p>${post.content}</p>
                        <p><strong>Author:</strong> ${post.username}</p>
                        <p><strong>Category:</strong> ${post.category}</p>
                        <p><strong>Created Date:</strong> ${post.createdDate}</p>
                        ${post.modifiedDate ? `<p><strong>Modified Date:</strong> ${post.modifiedDate}</p>` : ''}
                    `;
                postsDiv.appendChild(postElement);
            });
        });
});
