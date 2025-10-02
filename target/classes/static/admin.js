const loginBtn = document.getElementById('loginBtn');
const getUsersBtn = document.getElementById('getUsersBtn');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const userInfo = document.getElementById('userInfo');
const usersList = document.getElementById('usersList');

let jwtToken = null;

// 1. Login Logic
loginBtn.addEventListener('click', async () => {
    const email = emailInput.value;
    const password = passwordInput.value;

    if (!email || !password) {
        alert('Please enter both email and password.');
        return;
    }

    try {
        const response = await fetch('/api/v1/auth/signin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            throw new Error('Login failed. Please check credentials and ensure you are an admin.');
        }

        const data = await response.json();
        jwtToken = data.token;

        userInfo.textContent = `Logged in successfully. Token stored.`;
        getUsersBtn.disabled = false; // Enable the fetch button
        loginBtn.disabled = true;

    } catch (error) {
        userInfo.textContent = error.message;
        userInfo.style.color = 'red';
    }
});

// 2. Fetch Users Logic
getUsersBtn.addEventListener('click', async () => {
    if (!jwtToken) {
        alert('You must be logged in to fetch users.');
        return;
    }

    try {
        const response = await fetch('/api/v1/admin/users', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });

        if (response.status === 403) {
            throw new Error('Forbidden. You do not have the ADMIN role.');
        }
        if (!response.ok) {
            throw new Error('Failed to fetch users.');
        }

        const users = await response.json();
        displayUsers(users);

    } catch (error) {
        alert(error.message);
    }
});

// 3. Display Users Logic
function displayUsers(users) {
    usersList.innerHTML = ''; // Clear previous list

    if (users.length === 0) {
        usersList.innerHTML = '<li>No users found.</li>';
        return;
    }

    users.forEach(user => {
        const listItem = document.createElement('li');
        listItem.textContent = `ID: ${user.id}, Name: ${user.firstname} ${user.lastname}, Email: ${user.email}, Role: ${user.role}`;
        usersList.appendChild(listItem);
    });
}
