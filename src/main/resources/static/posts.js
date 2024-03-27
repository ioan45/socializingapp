function getTimeFormatted(millis) {
    let date = new Date(millis);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // getMonth() is zero-based
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

function toggleLike(postId, likeButton) {
    // Toggle like button
    likeButton.classList.toggle('liked');
    const buttonStatus = likeButton.classList.contains('liked') ? 'liked' : 'unliked';

    // Increment/Decrement the counter.
    const likesCount = Number(likeButton.innerHTML.slice(6, -1));
    if (buttonStatus === "liked")
        likeButton.innerHTML = `Like (${likesCount + 1})`;
    else
        likeButton.innerHTML = `Like (${likesCount - 1})`;

    // Send like status to backend server
    const postData = {
        postId: postId,
        likeStatus: buttonStatus
    };
    fetch("http://localhost:8080/post/like", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(postData)
    });
}

function postComment(postId, commentButton) {
    // Get the comment input element
    const inputElem = commentButton.parentElement.querySelector('.comment-input');
    if (inputElem.value.trim() === "")
        return;

    // Send the comment to backend server
    const currentTime = Date.now();
    const postData = {
        postId: postId,
        username: "",
        content: inputElem.value,
        creationDate: currentTime
    };
    fetch("http://localhost:8080/post/comment", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(postData)
    })
    .then(response => {
        return response.text();
    })
    .then(responseTxt => {
        // Append the comment to the comments section.
        let commentDiv = document.createElement('div');
        commentDiv.className = 'comment';
        commentDiv.innerHTML = "" +
            "<span>[" + getTimeFormatted(currentTime) + "] </span>" +
            "<strong>" + responseTxt + " : </strong>" +
            "<span>" + inputElem.value + "</span>";
        inputElem.parentElement.parentElement.querySelector('.comments').appendChild(commentDiv);
        inputElem.value = ""
    });
}

function deletePost(postElem) {
    // Send the delete request to the backend server
    const postData = Number(postElem.getAttribute('id'))
    fetch("http://localhost:8080/post/delete", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(postData)
    });

    // Delete the post from the client page
    postElem.remove();
}

function createPost(textAreaElem) {
    if (textAreaElem.value.trim() === "")
        return;

    // Send the create request to the backend server
    const postData = {
        content: textAreaElem.value,
        creationTime: Date.now()
    };
    fetch("http://localhost:8080/post/create", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(postData)
    })
    .then(response => {
        // Reload the page so the new post is shown
        location.reload();
    });
}

// Event listener for like button click
document.querySelectorAll('.like-btn').forEach(button => {
    button.addEventListener('click', () => {
        const postId = Number(button.closest('.post').getAttribute('id'));
        toggleLike(postId, button);
    });
});

// Event listener for comment button click
document.querySelectorAll('.comment-btn').forEach(button => {
    button.addEventListener('click', () => {
        const postId = Number(button.closest('.post').getAttribute('id'));
        postComment(postId, button);
    });
});

// Event listener for delete post button click
document.querySelectorAll('.delete-btn').forEach(button => {
    button.addEventListener('click', () => {
        const postElem = button.closest('.post');
        deletePost(postElem);
    });
});

// Event listener for create post button click
document.querySelector('.post-button').addEventListener('click', () => {
        const textAreaElem = document.querySelector('.post-textarea');
        createPost(textAreaElem);
});
