// Base URL for the backend API
const BASE_URL = "http://localhost:8001/api/prescriptions"; // Ensure your backend is running on this port

// Open the form modal
function openForm(action, prescriptionData = null) {
    const createForm = document.getElementById('createForm');
    const updateForm = document.getElementById('updateForm');

    // Hide both forms initially
    createForm.style.display = 'none';
    updateForm.style.display = 'none';

    if (action === 'create') {
        // Show the create form and reset fields
        createForm.style.display = 'block';
        document.getElementById('createPrescriptionForm').reset();
    } else if (action === 'update' && prescriptionData) {
        // Show the update form and populate fields with existing data
        updateForm.style.display = 'block';
        document.getElementById('updatePrescriptionId').value = prescriptionData.id;
        document.getElementById('updatePatientName').value = prescriptionData.patient;
        document.getElementById('updatePrescriptionDetails').value = prescriptionData.meds;
        document.getElementById('updatePrescriptionDate').value = prescriptionData.dateIssued;
        document.getElementById('updateDosage').value = prescriptionData.dosage;
        document.getElementById('updateDescription').value = prescriptionData.description;
    }
}

// Close the form modal
function closeForm() {
    document.getElementById('createForm').style.display = 'none';
    document.getElementById('updateForm').style.display = 'none';
}

// Fetch and display prescriptions based on patient ID
async function searchPrescription() {
    const patientId = document.getElementById('searchInput').value;

    if (!patientId) {
        alert('Please enter a valid Patient ID!');
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/patient/${patientId}`);
        if (response.ok) {
            const prescription = await response.json();
            displayPrescription(prescription);
        } else {
            alert('Prescription not found!');
        }
    } catch (error) {
        console.error('Error fetching prescription:', error);
        alert('Failed to retrieve prescription. Try again later.');
    }
}

// Display prescription details dynamically
function displayPrescription(prescription) {
    const panel = document.querySelector('.prescription-panel');
    panel.innerHTML = `
        <div class="prescription-card">
            <div class="prescription-details">
                <h3>Patient: ${prescription.patient}</h3>
                <p>Medicine: ${prescription.meds}</p>
                <p>Dosage: ${prescription.dosage}</p>
                <p>Description: ${prescription.description}</p>
                <p>Date Issued: ${prescription.dateIssued}</p>
            </div>
            <div class="prescription-actions">
                <button onclick="openForm('update', ${JSON.stringify(prescription)})">Update</button>
                <button onclick="deletePrescription(${prescription.id})">Delete</button>
            </div>
        </div>
    `;
}

// Handle prescription creation
document.getElementById('createPrescriptionForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    const newPrescription = {
        patient: document.getElementById('createPatientName').value,
        meds: document.getElementById('createPrescriptionDetails').value,
        dosage: document.getElementById('createDosage').value,
        description: document.getElementById('createDescription').value,
        dateIssued: document.getElementById('createPrescriptionDate').value,
    };

    try {
        const response = await fetch(BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newPrescription),
        });

        if (response.ok) {
            alert('Prescription created successfully!');
            closeForm();
        } else {
            alert('Failed to create prescription!');
        }
    } catch (error) {
        console.error('Error creating prescription:', error);
        alert('Error creating prescription!');
    }
});

// Handle prescription update
document.getElementById('updatePrescriptionForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    const prescriptionId = document.getElementById('updatePrescriptionId').value;
    const updatedPrescription = {
        patient: document.getElementById('updatePatientName').value,
        meds: document.getElementById('updatePrescriptionDetails').value,
        dosage: document.getElementById('updateDosage').value,
        description: document.getElementById('updateDescription').value,
        dateIssued: document.getElementById('updatePrescriptionDate').value,
    };

    try {
        const response = await fetch(`${BASE_URL}/${prescriptionId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedPrescription),
        });

        if (response.ok) {
            alert('Prescription updated successfully!');
            closeForm();
        } else {
            alert('Failed to update prescription!');
        }
    } catch (error) {
        console.error('Error updating prescription:', error);
        alert('Error updating prescription!');
    }
});

// Handle prescription deletion
async function deletePrescription(prescriptionId) {
    const confirmDelete = confirm('Are you sure you want to delete this prescription?');
    if (confirmDelete) {
        try {
            const response = await fetch(`${BASE_URL}/${prescriptionId}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                alert('Prescription deleted successfully!');
                document.getElementById('deleteMessage').style.display = 'block';
            } else {
                alert('Failed to delete prescription!');
            }
        } catch (error) {
            console.error('Error deleting prescription:', error);
            alert('Error deleting prescription!');
        }
    }
}
