document.addEventListener('DOMContentLoaded', () => {
    // Containers
    const loginContainer = document.getElementById('login-container');
    const dashboardContainer = document.getElementById('dashboard-container');

    // Inputs and Buttons
    const loginBtn = document.getElementById('loginBtn');
    const getUsersBtn = document.getElementById('getUsersBtn');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const passwordToggleIcon = document.getElementById('password-toggle-icon');
    
    // Display Areas
    const loginError = document.getElementById('login-error');
    const userTableBody = document.getElementById('userTableBody');
    const userCountBadge = document.getElementById('user-count-badge');

    let jwtToken = null;

    // 0. Show Password Logic
    passwordToggleIcon.addEventListener('click', () => {
        // Toggle the type of the password input
        const isPassword = passwordInput.type === 'password';
        passwordInput.type = isPassword ? 'text' : 'password';

        // Toggle the icon
        passwordToggleIcon.classList.toggle('fa-eye');
        passwordToggleIcon.classList.toggle('fa-eye-slash');
    });

    // 1. Login Logic
    loginBtn.addEventListener('click', async () => {
        const email = emailInput.value;
        const password = passwordInput.value;
        loginError.textContent = ''; // Clear previous errors

        if (!email || !password) {
            loginError.textContent = 'Please enter both email and password.';
            return;
        }

        try {
            const response = await fetch('/api/v1/auth/signin', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            if (!response.ok) {
                throw new Error('Login failed. Check credentials or admin privileges.');
            }

            const data = await response.json();
            jwtToken = data.token;

            // --- UI Transition ---
            loginContainer.style.display = 'none';
            dashboardContainer.style.display = 'block';
            
            // Automatically fetch users
            fetchUsers();

        } catch (error) {
            loginError.textContent = error.message;
        }
    });

    // 2. Fetch Users Logic
    getUsersBtn.addEventListener('click', fetchUsers);

    async function fetchUsers() {
        if (!jwtToken) {
            alert('Authentication token not found. Please log in again.');
            // Revert to login screen if token is lost
            loginContainer.style.display = 'block';
            dashboardContainer.style.display = 'none';
            return;
        }

        try {
            const response = await fetch('/api/v1/admin/users', {
                method: 'GET',
                headers: { 'Authorization': `Bearer ${jwtToken}` }
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch users. Status: ${response.status}`);
            }

            const users = await response.json();
            displayUsers(users);

        } catch (error) {
            alert(error.message);
        }
    }

    // 3. Display Users Logic
    function displayUsers(users) {
        userTableBody.innerHTML = ''; 
        userCountBadge.textContent = users.length;

        if (users.length === 0) {
            const row = userTableBody.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 7;
            cell.textContent = 'No users found.';
            cell.style.textAlign = 'center';
            return;
        }

        users.forEach(user => {
            const row = userTableBody.insertRow();

            row.insertCell().textContent = user.id;
            row.insertCell().textContent = user.firstname;
            row.insertCell().textContent = user.lastname;
            row.insertCell().textContent = user.email;

            const roleCell = row.insertCell();
            const roleBadge = document.createElement('span');
            roleBadge.className = 'role-badge';
            roleBadge.textContent = user.role;
            roleCell.appendChild(roleBadge);

            const joinedDate = user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A';
            row.insertCell().textContent = joinedDate;

            const actionsCell = row.insertCell();
            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = 'Delete';
            deleteBtn.className = 'btn btn-danger'; // Updated class
            deleteBtn.dataset.userId = user.id;
            deleteBtn.addEventListener('click', () => deleteUser(user.id));
            actionsCell.appendChild(deleteBtn);
        });
    }

    // 4. Delete User Logic
    async function deleteUser(userId) {
        if (!confirm(`Are you sure you want to delete user with ID: ${userId}?`)) {
            return;
        }

        if (!jwtToken) {
            alert('Authentication token not found. Please log in again.');
            return;
        }

        try {
            const response = await fetch(`/api/v1/admin/users/${userId}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${jwtToken}` }
            });

            if (response.ok) {
                alert('User deleted successfully.');
                fetchUsers(); // Refresh the user list
            } else {
                const errorMessage = await response.text();
                alert(errorMessage || `Error: ${response.statusText}`);
            }

        } catch (error) {
            alert(`An unexpected error occurred: ${error.message}`);
        }
    }
});
