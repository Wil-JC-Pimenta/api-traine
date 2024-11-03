const API_URL = 'http://localhost:8081';

// Login function
async function login(username, password) {
    try {
        const response = await fetch(`${API_URL}/auth/login`, {