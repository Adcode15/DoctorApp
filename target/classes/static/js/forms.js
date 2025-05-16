// Doctor and Patient Forms Management

// Initialize form validation and submission
document.addEventListener('DOMContentLoaded', function() {
    setupFormToggle();
    setupFormSubmission();
    setupDeleteButtons();
});

// Toggle form visibility
function setupFormToggle() {
    // Doctor form toggle
    const addDoctorBtn = document.getElementById('add-doctor-btn');
    const doctorForm = document.getElementById('doctor-form-container');
    const closeDoctorForm = document.getElementById('close-doctor-form');
    
    if (addDoctorBtn) {
        addDoctorBtn.addEventListener('click', function() {
            doctorForm.classList.add('active');
        });
    }
    
    if (closeDoctorForm) {
        closeDoctorForm.addEventListener('click', function() {
            doctorForm.classList.remove('active');
        });
    }
    
    // Patient form toggle
    const addPatientBtn = document.getElementById('add-patient-btn');
    const patientForm = document.getElementById('patient-form-container');
    const closePatientForm = document.getElementById('close-patient-form');
    
    if (addPatientBtn) {
        addPatientBtn.addEventListener('click', function() {
            patientForm.classList.add('active');
        });
    }
    
    if (closePatientForm) {
        closePatientForm.addEventListener('click', function() {
            patientForm.classList.remove('active');
        });
    }
}

// Handle form submission
function setupFormSubmission() {
    // Doctor form submission
    const doctorForm = document.getElementById('doctor-form');
    if (doctorForm) {
        doctorForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Get form data
            const doctorData = {
                name: document.getElementById('doctor-name').value,
                city: document.getElementById('doctor-city').value,
                email: document.getElementById('doctor-email').value,
                phoneNumber: document.getElementById('doctor-phone').value,
                speciality: document.getElementById('doctor-speciality').value
            };
            
            // Validate data (client-side validation)
            if (!validateDoctorData(doctorData)) {
                return;
            }
            
            // Send data to API endpoint
            fetch('/api/doctors', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(doctorData)
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Failed to add doctor');
            })
            .then(data => {
                // Add to UI
                addDoctorToUI(data);
                
                // Close the form
                document.getElementById('doctor-form-container').classList.remove('active');
                doctorForm.reset();
                
                // Reload the doctor list
                window.location.reload();
            })
            .catch(error => {
                showError('Error adding doctor: ' + error.message);
            });
        });
    }
    
    // Patient form submission
    const patientForm = document.getElementById('patient-form');
    if (patientForm) {
        patientForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Get form data
            const patientData = {
                name: document.getElementById('patient-name').value,
                city: document.getElementById('patient-city').value,
                email: document.getElementById('patient-email').value,
                phoneNumber: document.getElementById('patient-phone').value,
                symptom: document.getElementById('patient-symptom').value
            };
            
            // Validate data (client-side validation)
            if (!validatePatientData(patientData)) {
                return;
            }
            
            // Send data to API endpoint
            fetch('/api/patients', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(patientData)
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Failed to add patient');
            })
            .then(data => {
                // Add to UI
                addPatientToUI(data);
                
                // Close the form
                document.getElementById('patient-form-container').classList.remove('active');
                patientForm.reset();
                
                // Reload the patient list
                window.location.reload();
            })
            .catch(error => {
                showError('Error adding patient: ' + error.message);
            });
        });
    }
}

// Client-side validation for doctor data
function validateDoctorData(doctorData) {
    // Name validation
    if (doctorData.name.length < 3) {
        showError('Name should be at least 3 characters');
        return false;
    }
    
    // City validation
    if (!['DELHI', 'NOIDA', 'FARIDABAD'].includes(doctorData.city)) {
        showError('City must be DELHI, NOIDA, or FARIDABAD');
        return false;
    }
    
    // Email validation
    if (!validateEmail(doctorData.email)) {
        showError('Invalid email format');
        return false;
    }
    
    // Phone validation
    if (doctorData.phoneNumber.length < 10) {
        showError('Phone number should be at least 10 digits');
        return false;
    }
    
    // Speciality validation
    if (!['ORTHOPEDIC', 'GYNECOLOGY', 'DERMATOLOGY', 'ENT'].includes(doctorData.speciality)) {
        showError('Invalid speciality selected');
        return false;
    }
    
    return true;
}

// Client-side validation for patient data
function validatePatientData(patientData) {
    // Name validation
    if (patientData.name.length < 3) {
        showError('Name should be at least 3 characters');
        return false;
    }
    
    // City validation (max 20 chars)
    if (patientData.city.length > 20) {
        showError('City must not exceed 20 characters');
        return false;
    }
    
    // Email validation
    if (!validateEmail(patientData.email)) {
        showError('Invalid email format');
        return false;
    }
    
    // Phone validation
    if (patientData.phoneNumber.length < 10) {
        showError('Phone number should be at least 10 digits');
        return false;
    }
    
    // Symptom validation
    const validSymptoms = ['arthritis', 'back pain', 'tissue injuries', 'dysmenorrhea', 
                           'skin infection', 'skin burn', 'ear pain'];
    if (!validSymptoms.includes(patientData.symptom.toLowerCase())) {
        showError('Invalid symptom. Must be one of: ' + validSymptoms.join(', '));
        return false;
    }
    
    return true;
}

function validateEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

function showError(message) {
    // Create and display error message
    const errorEl = document.getElementById('form-error');
    errorEl.textContent = message;
    errorEl.style.display = 'block';
    
    // Hide after 3 seconds
    setTimeout(() => {
        errorEl.style.display = 'none';
    }, 3000);
}

// Add doctor to UI
function addDoctorToUI(doctorData) {
    const doctorList = document.getElementById('doctor-list');
    if (!doctorList) return;
    
    const doctorCard = document.createElement('div');
    doctorCard.className = 'feature-card';
    doctorCard.innerHTML = `
        <div class="feature-icon">👨‍⚕️</div>
        <h3>${doctorData.name}</h3>
        <p><strong>Speciality:</strong> ${doctorData.speciality}</p>
        <p><strong>City:</strong> ${doctorData.city}</p>
        <p><strong>Email:</strong> ${doctorData.email}</p>
        <p><strong>Phone:</strong> ${doctorData.phoneNumber}</p>
        <button class="delete-btn" data-type="doctor" data-id="${doctorData.id}">Remove</button>
    `;
    
    doctorList.prepend(doctorCard);
    setupDeleteButtons();
}

// Add patient to UI
function addPatientToUI(patientData) {
    const patientList = document.getElementById('patient-list');
    if (!patientList) return;
    
    const patientCard = document.createElement('div');
    patientCard.className = 'feature-card';
    patientCard.innerHTML = `
        <div class="feature-icon">🧑</div>
        <h3>${patientData.name}</h3>
        <p><strong>Symptom:</strong> ${patientData.symptom}</p>
        <p><strong>City:</strong> ${patientData.city}</p>
        <p><strong>Email:</strong> ${patientData.email}</p>
        <p><strong>Phone:</strong> ${patientData.phoneNumber}</p>
        <button class="delete-btn" data-type="patient" data-id="${patientData.id}">Remove</button>
    `;
    
    patientList.prepend(patientCard);
    setupDeleteButtons();
}

// Setup delete buttons
function setupDeleteButtons() {
    const deleteButtons = document.querySelectorAll('.delete-btn');
    
    deleteButtons.forEach(button => {
        // Skip if event listener is already attached
        if (button.hasListener) return;
        
        button.addEventListener('click', function(e) {
            e.stopPropagation(); // Prevent triggering card click
            
            const type = this.getAttribute('data-type');
            const id = this.getAttribute('data-id');
            
            // Call API to delete the item
            fetch(`/api/${type}s/${id}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    // Get the parent card and remove it
                    const card = this.closest('.feature-card');
                    card.classList.add('removing');
                    
                    setTimeout(() => {
                        card.remove();
                    }, 300);
                } else {
                    throw new Error(`Failed to delete ${type}`);
                }
            })
            .catch(error => {
                showError(`Error deleting ${type}: ${error.message}`);
            });
        });
        
        // Mark as having event listener
        button.hasListener = true;
    });
}

// Show doctor details in modal
function showDoctorDetails(doctor) {
    const modal = document.getElementById('detail-modal');
    const content = document.getElementById('detail-content');
    
    content.innerHTML = `
        <div class="detail-header ${doctor.speciality.toLowerCase()}">
            <h2>${doctor.name}</h2>
            <span class="detail-badge">${doctor.speciality}</span>
        </div>
        <div class="detail-body">
            <div class="detail-avatar">👨‍⚕️</div>
            <div class="detail-info">
                <p><strong>City:</strong> ${doctor.city}</p>
                <p><strong>Email:</strong> ${doctor.email}</p>
                <p><strong>Phone:</strong> ${doctor.phoneNumber}</p>
                <p><strong>Speciality:</strong> ${doctor.speciality}</p>
            </div>
        </div>
        <div class="detail-actions">
            <button class="btn" onclick="document.getElementById('detail-modal').classList.remove('active')">Close</button>
        </div>
    `;
    
    modal.classList.add('active');
}

// Show patient details in modal
function showPatientDetails(patient) {
    const modal = document.getElementById('detail-modal');
    const content = document.getElementById('detail-content');
    
    content.innerHTML = `
        <div class="detail-header patient">
            <h2>${patient.name}</h2>
            <span class="detail-badge">${patient.symptom}</span>
        </div>
        <div class="detail-body">
            <div class="detail-avatar">🧑</div>
            <div class="detail-info">
                <p><strong>City:</strong> ${patient.city}</p>
                <p><strong>Email:</strong> ${patient.email}</p>
                <p><strong>Phone:</strong> ${patient.phoneNumber}</p>
                <p><strong>Symptom:</strong> ${patient.symptom}</p>
            </div>
        </div>
        <div class="detail-actions">
            <button class="btn" onclick="document.getElementById('detail-modal').classList.remove('active')">Close</button>
        </div>
    `;
    
    modal.classList.add('active');
} 