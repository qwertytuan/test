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
                    <a href="#" onclick="openPost(event, ${post.id})">
                   
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


