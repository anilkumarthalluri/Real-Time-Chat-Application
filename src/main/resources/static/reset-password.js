'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const resetPasswordForm = document.querySelector('#resetPasswordForm');
    const newPasswordInput = document.querySelector('#new-password');
    const confirmPasswordInput = document.querySelector('#confirm-password');
    const successMessage = document.querySelector('#success-message');
    const errorMessage = document.querySelector('#error-message');
    const spamMessage = document.querySelector('#spam-message');

    // Get the token from the URL
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    if (!token) {
        errorMessage.textContent = 'No reset token found. Please request a new password reset link.';
        errorMessage.classList.remove('hidden');
        resetPasswordForm.classList.add('hidden');
        return;
    }

    resetPasswordForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const newPassword = newPasswordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        if (newPassword !== confirmPassword) {
            errorMessage.textContent = 'Passwords do not match.';
            errorMessage.classList.remove('hidden');
            return;
        }

        if (!newPassword || newPassword.length < 6) {
            errorMessage.textContent = 'Password must be at least 6 characters long.';
            errorMessage.classList.remove('hidden');
            return;
        }

        // Clear previous messages
        errorMessage.classList.add('hidden');

        const resetRequest = { token, newPassword };

        fetch('/api/v1/auth/reset-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(resetRequest)
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text || 'Failed to reset password.') });
            }
            return response.text();
        })
        .then(() => {
            resetPasswordForm.classList.add('hidden');
            successMessage.classList.remove('hidden');
            spamMessage.classList.remove('hidden');
        })
        .catch(error => {
            errorMessage.textContent = error.message;
            errorMessage.classList.remove('hidden');
        });
    });
});
