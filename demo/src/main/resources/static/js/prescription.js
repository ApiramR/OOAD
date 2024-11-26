// Base URL for the backend API
const BASE_URL = "http://localhost:8000/doctor/prescription"; // Ensure your backend is running on this port

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

// // Fetch and display prescriptions based on patient ID
// async function searchPrescription() {
//     const patientId = document.getElementById('searchInput').value;

//     if (!patientId) {
//         alert('Please enter a valid Patient ID!');
//         return;
//     }

//     try {
//         const response = await fetch(`${BASE_URL}/patient/${patientId}`);
//         if (response.ok) {
//             const prescription = await response.json();
//             displayPrescription(prescription);
//         } else {
//             alert('Prescription not found!');
//         }
//     } catch (error) {
//         console.error('Error fetching prescription:', error);
//         alert('Failed to retrieve prescription. Try again later.');
//     }
// }

// // Display prescription details dynamically
// function displayPrescription(prescription) {
//     const panel = document.querySelector('.prescription-panel');
//     panel.innerHTML = `
//         <div class="prescription-card">
//             <div class="prescription-details">
//                 <h3>Patient: ${prescription.patient}</h3>
//                 <p>Medicine: ${prescription.meds}</p>
//                 <p>Dosage: ${prescription.dosage}</p>
//                 <p>Description: ${prescription.description}</p>
//                 <p>Date Issued: ${prescription.dateIssued}</p>
//             </div>
//             <div class="prescription-actions">
//                 <button onclick="openForm('update', ${JSON.stringify(prescription)})">Update</button>
//                 <button onclick="deletePrescription(${prescription.id})">Delete</button>
//             </div>
//         </div>
//     `;
// }

// // Handle prescription creation
// document.getElementById('createPrescriptionForm').addEventListener('submit', async function (e) {
//     e.preventDefault();
//     const newPrescription = {
//         patient: document.getElementById('createPatientName').value,
//         meds: document.getElementById('createPrescriptionDetails').value,
//         dosage: document.getElementById('createDosage').value,
//         description: document.getElementById('createDescription').value,
//         dateIssued: document.getElementById('createPrescriptionDate').value,
//     };

//     try {
//         const response = await fetch(BASE_URL, {
//             method: 'POST',
//             headers: { 'Content-Type': 'application/json' },
//             body: JSON.stringify(newPrescription),
//         });

//         if (response.ok) {
//             alert('Prescription created successfully!');
//             closeForm();
//         } else {
//             alert('Failed to create prescription!');
//         }
//     } catch (error) {
//         console.error('Error creating prescription:', error);
//         alert('Error creating prescription!');
//     }
// });

// // Handle prescription update
// document.getElementById('updatePrescriptionForm').addEventListener('submit', async function (e) {
//     e.preventDefault();
//     const prescriptionId = document.getElementById('updatePrescriptionId').value;
//     const updatedPrescription = {
//         patient: document.getElementById('updatePatientName').value,
//         meds: document.getElementById('updatePrescriptionDetails').value,
//         dosage: document.getElementById('updateDosage').value,
//         description: document.getElementById('updateDescription').value,
//         dateIssued: document.getElementById('updatePrescriptionDate').value,
//     };

//     try {
//         const response = await fetch(`${BASE_URL}/${prescriptionId}`, {
//             method: 'PUT',
//             headers: { 'Content-Type': 'application/json' },
//             body: JSON.stringify(updatedPrescription),
//         });

//         if (response.ok) {
//             alert('Prescription updated successfully!');
//             closeForm();
//         } else {
//             alert('Failed to update prescription!');
//         }
//     } catch (error) {
//         console.error('Error updating prescription:', error);
//         alert('Error updating prescription!');
//     }
// });

// // Handle prescription deletion
// async function deletePrescription(prescriptionId) {
//     const confirmDelete = confirm('Are you sure you want to delete this prescription?');
//     if (confirmDelete) {
//         try {
//             const response = await fetch(`${BASE_URL}/${prescriptionId}`, {
//                 method: 'DELETE',
//             });

//             if (response.ok) {
//                 alert('Prescription deleted successfully!');
//                 document.getElementById('deleteMessage').style.display = 'block';
//             } else {
//                 alert('Failed to delete prescription!');
//             }
//         } catch (error) {
//             console.error('Error deleting prescription:', error);
//             alert('Error deleting prescription!');
//         }
//     }
// }

// function searchPrescription() {
//     const presId = document.getElementById('prescriptionSearchInput').value;

//     if (presId) {
//         fetch(`http://localhost:8080/doctor/prescription/${presId}`)
//             .then(response => {
//                 if (!response.ok) {
//                     throw new Error("Prescription not found");
//                 }
//                 return response.json();
//             })
//             .then(data => {
//                 // Check if the response has prescription details and patient data
//                 if (data) {
//                     // Populate prescription details
//                     document.getElementById('prescriptionMedication').textContent = data.meds;
//                     document.getElementById('prescriptionDosage').textContent = data.dosage;
//                     document.getElementById('prescriptionDate').textContent = data.dateIssued;
//                     document.getElementById('prescriptionDescription').textContent = data.description || 'No description available';

//                     // Populate patient details (ensure patient fields are available in the response)
//                     if (data.patient) {
//                         document.getElementById('patientName').textContent = data.patient.patientName || 'Unknown';
//                         document.getElementById('patientAge').textContent = data.patient.patientAge || 'Unknown';
//                     } else {
//                         document.getElementById('patientName').textContent = 'No patient data available';
//                         document.getElementById('patientAge').textContent = 'Unknown';
//                     }

//                     // Show the prescription details panel
//                     document.getElementById('prescriptionDetails').style.display = 'block';
//                 }
//             })
//             .catch(error => {
//                 console.error('Error fetching prescription data:', error);
//                 alert('Prescription not found. Please check the ID and try again.');
//             });
//     } else {
//         alert("Please enter a valid Prescription ID.");
//     }
// }





// function createPrescription() {
//     // Collect form data
//     const prescriptionData = {
//         PID: {
//             id: document.getElementById('createPatientId').value
//         },
//         meds: document.getElementById('createMedication').value,
//         dosage: document.getElementById('createDosage').value,
//         dateIssued: document.getElementById('createPrescriptionDate').value,
//         description: document.getElementById('createDescription').value
//     };
//     // Check if all fields are populated
//     if (!prescriptionData.patient.id || !prescriptionData.meds || !prescriptionData.dosage || !prescriptionData.dateIssued) {
//         alert("Please fill all required fields.");
//         return;
//     }

//     // Send the prescription data to the backend
//     fetch('http://localhost:8000/doctor/prescription', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(prescriptionData),
//     })
//     .then((response) => {
//         if (response.ok) {
//             return response.json();
//         } else {
//             throw new Error('Failed to create prescription');
//         }
//     })
//     .then((data) => {
//         // Show success message or handle success
//         alert('Prescription created successfully!');
//         console.log('Created prescription:', data);

//         // Optionally close the modal or reset the form
//         closeForm();  // assuming closeForm() is a function to close the modal
//         document.getElementById('createPrescriptionForm').reset(); // Reset the form fields
//     })
//     .catch((error) => {
//         console.error('Error:', error);
//         alert('Error creating prescription.');
//     });
// }





// document.getElementById('createFormSubmitBtn').addEventListener('click', (e) => {
//     e.preventDefault(); // Prevent form's default submit behavior
//     console.log("Create button clicked!");

//     const prescriptionData = {
//         patient: {
//             id: document.getElementById('createPatientName').value,
//         },
//         meds: document.getElementById('createMedication').value,
//         dosage: document.getElementById('createDosage').value,
//         dateIssued: document.getElementById('createPrescriptionDate').value,
//         description: document.getElementById('createDescription').value,
//     };

//     console.log("Payload to be sent:", prescriptionData);

//     fetch('http://localhost:8080/doctor/prescription', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(prescriptionData),
//     })
//         .then((response) => {
//             if (response.ok) {
//                 return response.json();
//             }
//             throw new Error('Failed to create prescription');
//         })
//         .then((data) => {
//             alert('Prescription created successfully!');
//             console.log('Created prescription:', data);
//         })
//         .catch((error) => {
//             console.error('Error:', error);
//             alert('Error creating prescription.');
//         });
// });

// Function to delete a prescription
function deletePrescription() {
    // Retrieve the Prescription ID (this can be fetched dynamically; replace with actual implementation)
    const prescriptionId = document.getElementById('prescriptionSearchInput').value.trim();

    if (!prescriptionId) {
        alert('Please provide a valid Prescription ID to delete.');
        return;
    }

    // Confirmation dialog
    const confirmation = confirm('Are you sure you want to delete this prescription?');
    if (!confirmation) return;

    // API call to delete the prescription
    fetch(`http://localhost:8000/doctor/prescription/${prescriptionId}`, {
        method: 'DELETE',
    })
        .then((response) => {
            if (response.ok) {
                // Show success message
                document.getElementById('deleteMessage').style.display = 'block';
                setTimeout(() => {
                    document.getElementById('deleteMessage').style.display = 'none';
                }, 3000);

                // Optionally, clear prescription details from UI
                clearPrescriptionDetails();
            } else {
                throw new Error('Failed to delete the prescription. Please try again.');
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('Error deleting prescription: ' + error.message);
        });
}

// Function to clear prescription details from the UI after deletion
function clearPrescriptionDetails() {
    document.getElementById('prescriptionMedication').textContent = '';
    document.getElementById('prescriptionDosage').textContent = '';
    document.getElementById('prescriptionDate').textContent = '';
    document.getElementById('prescriptionSearchInput').value = '';
}

// Function to search and retrieve a prescription (optional, modify based on your setup)
function searchPrescription() {
    const prescriptionId = document.getElementById('prescriptionSearchInput').value.trim();

    if (!prescriptionId) {
        alert('Please enter a Prescription ID to search.');
        return;
    }

    // Fetch prescription details (modify the endpoint as per your backend)
    fetch(`http://localhost:8000/doctor/prescription/${prescriptionId}`)
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Prescription not found.');
            }
        })
        .then((data) => {
            // Populate prescription details in the UI
            document.getElementById('prescriptionMedication').textContent = data.meds || 'N/A';
            document.getElementById('prescriptionDosage').textContent = data.dosage || 'N/A';
            document.getElementById('prescriptionDate').textContent = data.dateIssued || 'N/A';

            if (data.patient) {
                document.getElementById('patientName').textContent = data.patient.patientName || 'Unknown';
                document.getElementById('patientAge').textContent = data.patient.patientAge || 'Unknown';
            } else {
                document.getElementById('patientName').textContent = 'No patient data available';
                document.getElementById('patientAge').textContent = 'Unknown';
            }
        })
        
        .catch((error) => {
            console.error('Error:', error);
            alert('Error fetching prescription: ' + error.message);
        });
}


// Function to open the update form with prescription data pre-filled
function openFormUpdate(prescriptionId) {
    fetch(`http://localhost:8000/doctor/prescription/${prescriptionId}`)
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Prescription not found.");
            }
        })
        .then((data) => {
            populateUpdateForm(data);
            document.getElementById("updateForm").style.display = "block";
        })
        .catch((error) => {
            console.error("Error fetching prescription:", error);
            alert("Error fetching prescription: " + error.message);
        });
}

// Populate the update form with the fetched prescription data
function populateUpdateForm(data) {
    document.getElementById("updatePatientId").value = data.patientId || "";
    document.getElementById("updateMedication").value = data.meds || "";
    document.getElementById("updateDosage").value = data.dosage || "";
    document.getElementById("updateDescription").value = data.description || "";
    document.getElementById("updatePrescriptionDate").value = data.dateIssued || "";
    document.getElementById("updatePrescriptionForm").dataset.prescriptionId = data.presID; // Store the ID for reference
}

// Function to update a prescription
function updatePrescription() {
    const prescriptionId = document.getElementById("updatPrescriptionId").value.trim();

    if (!prescriptionId) {
        alert("Please enter a valid Prescription ID.");
        return;
    }

    const updatedData = {
        patient: { PatientID: document.getElementById("updatePatientId").value.trim() || null },
        meds: document.getElementById("updateMedication").value.trim(),
        dosage: document.getElementById("updateDosage").value.trim(),
        description: document.getElementById("updateDescription").value.trim(),
        dateIssued: document.getElementById("updatePrescriptionDate").value.trim(),
    };

    // Ensure at least one field is updated
    const hasUpdates = Object.values(updatedData).some((value) => value && value !== null);
    if (!hasUpdates) {
        alert("No changes detected. Please modify at least one field to update.");
        return;
    }

    fetch(`http://localhost:8000/doctor/prescription/${prescriptionId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedData),
    })
        .then((response) => {
            if (response.ok) {
                alert("Prescription updated successfully!");
                document.getElementById("updatePrescriptionForm").reset();
                closeForm();
            } else {
                throw new Error("Failed to update prescription.");
            }
        })
        .catch((error) => {
            console.error("Error updating prescription:", error);
            alert("Error updating prescription: " + error.message);
        });
}

// Attach event listener to the update button
document.getElementById("updateFormSubmitBtn").addEventListener("click", (event) => {
    event.preventDefault();
    updatePrescription();
});




// Make the Update button functional
document.getElementById("updateFormSubmitBtn").addEventListener("click", (event) => {
    event.preventDefault(); // Prevent default form submission
    updatePrescription();
});


 

