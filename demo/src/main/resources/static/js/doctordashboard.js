
// Replace this with the actual doctor ID (e.g., fetched from a session or URL parameter)
const doctorId = 1; // Example ID

// Fetch the username from the backend
fetch(`/doctor/${doctorId}/username`)
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to fetch username');
        }
        return response.text(); // Parse the response as plain text
    })
    .then(username => {
        // Inject the username into the span element
        document.getElementById('doctor-name').textContent = username;
    })
    .catch(error => {
        console.error('Error fetching username:', error);
        document.getElementById('doctor-name').textContent = 'Doctor Name'; // Fallback text
    });

