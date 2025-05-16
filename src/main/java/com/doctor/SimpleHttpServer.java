package com.doctor;

import com.doctor.entity.*;
import com.doctor.enums.*;
import com.doctor.service.*;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SimpleHttpServer {
    private static final int PORT = 8080;
    private static final String STATIC_DIR = "src/main/resources/static";
    private static SymptomSpecialityService symptomSpecialityService = new SymptomSpecialityService();
    private static List<SimpleDoctor> doctors = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        // Initialize demo data
        initializeDemoData();
        
        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Create contexts (endpoints)
        server.createContext("/", SimpleHttpServer::handleRoot);
        server.createContext("/api/doctors", SimpleHttpServer::handleDoctors);
        server.createContext("/api/patients", SimpleHttpServer::handlePatients);
        server.createContext("/api/suggestions", SimpleHttpServer::handleSuggestions);
        server.createContext("/css", SimpleHttpServer::handleStaticResource);
        server.createContext("/js", SimpleHttpServer::handleStaticResource);
        
        // Set executor
        server.setExecutor(null);
        
        // Start server
        server.start();
        
        System.out.println("Doctor-Patient Management Platform Server running on port " + PORT);
        System.out.println("Open your browser and navigate to http://localhost:" + PORT);
        System.out.println("\nAvailable endpoints:");
        System.out.println("- / (Home page with interactive UI)");
        System.out.println("- /api/doctors (GET: List all doctors, POST: Add doctor, DELETE: Remove doctor)");
        System.out.println("- /api/patients (GET: List all patients, POST: Add patient, DELETE: Remove patient)");
        System.out.println("- /api/suggestions?city=DELHI&symptom=arthritis (GET: Find doctors by city and symptom)");
    }
    
    private static void initializeDemoData() {
        // Add demo doctors
        doctors.add(new SimpleDoctor(
            "Dr. John Smith", 
            DoctorCity.DELHI, 
            "john.smith@example.com", 
            "9876543210", 
            Speciality.ORTHOPEDIC
        ));
        
        doctors.add(new SimpleDoctor(
            "Dr. Sarah Johnson", 
            DoctorCity.NOIDA, 
            "sarah.johnson@example.com", 
            "9876543211", 
            Speciality.GYNECOLOGY
        ));
        
        doctors.add(new SimpleDoctor(
            "Dr. Mike Wilson", 
            DoctorCity.DELHI, 
            "mike.wilson@example.com", 
            "9876543212", 
            Speciality.DERMATOLOGY
        ));
        
        doctors.add(new SimpleDoctor(
            "Dr. Emily Brown", 
            DoctorCity.FARIDABAD, 
            "emily.brown@example.com", 
            "9876543213", 
            Speciality.ENT
        ));
        
        // Add more doctors for better demo
        doctors.add(new SimpleDoctor(
            "Dr. Priya Sharma", 
            DoctorCity.DELHI, 
            "priya.sharma@example.com", 
            "9876543214", 
            Speciality.GYNECOLOGY
        ));
        
        doctors.add(new SimpleDoctor(
            "Dr. Rahul Verma", 
            DoctorCity.NOIDA, 
            "rahul.verma@example.com", 
            "9876543215", 
            Speciality.DERMATOLOGY
        ));
    }
    
    private static void handleRoot(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        // Default to index.html if path is "/"
        if (path.equals("/")) {
            path = "/index.html";
        }
        
        // Try to serve a static file
        Path filePath = Paths.get(STATIC_DIR, path.substring(1));
        
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            String contentType = getContentType(filePath.toString());
            exchange.getResponseHeaders().set("Content-Type", contentType);
            
            byte[] data = Files.readAllBytes(filePath);
            exchange.sendResponseHeaders(200, data.length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        } else {
            // If static file doesn't exist, send the index.html file as fallback
            Path indexPath = Paths.get(STATIC_DIR, "index.html");
            
            if (Files.exists(indexPath)) {
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                byte[] data = Files.readAllBytes(indexPath);
                exchange.sendResponseHeaders(200, data.length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(data);
                }
            } else {
                // If index.html doesn't exist, fallback to simple HTML
                String response = "<html><body>" +
                        "<h1>Doctor-Patient Management Platform</h1>" +
                        "<h2>Available Endpoints:</h2>" +
                        "<ul>" +
                        "<li><a href='/api/doctors'>List all doctors</a></li>" +
                        "<li><a href='/api/patients'>Demo patients</a></li>" +
                        "<li><a href='/api/suggestions?city=DELHI&symptom=arthritis'>Find doctors for arthritis in Delhi</a></li>" +
                        "<li><a href='/api/suggestions?city=MUMBAI&symptom=skin infection'>Find doctors for skin infection in Mumbai</a></li>" +
                        "</ul>" +
                        "</body></html>";
                
                sendResponse(exchange, response, 200);
            }
        }
    }
    
    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "text/plain";
    }
    
    private static void handleStaticResource(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String resourceType = path.split("/")[1]; // css or js
        String resourceName = path.substring(path.indexOf(resourceType) + resourceType.length() + 1);
        
        Path filePath = Paths.get(STATIC_DIR, resourceType, resourceName);
        
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            String contentType = getContentType(filePath.toString());
            exchange.getResponseHeaders().set("Content-Type", contentType);
            
            byte[] data = Files.readAllBytes(filePath);
            exchange.sendResponseHeaders(200, data.length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        } else {
            String response = "Resource not found: " + path;
            exchange.sendResponseHeaders(404, response.length());
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
    
    private static void handleDoctors(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        
        if ("GET".equals(method)) {
            // Handle GET request - list all doctors
            handleGetDoctors(exchange);
        } else if ("POST".equals(method)) {
            // Handle POST request - add a new doctor
            handleAddDoctor(exchange);
        } else if ("DELETE".equals(method)) {
            // Handle DELETE request - remove a doctor
            handleRemoveDoctor(exchange);
        } else {
            // Method not allowed
            String response = "Method not allowed";
            exchange.sendResponseHeaders(405, response.length());
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
    
    private static void handleGetDoctors(HttpExchange exchange) throws IOException {
        StringBuilder response = new StringBuilder();
        response.append("{\n  \"doctors\": [\n");
        
        for (int i = 0; i < doctors.size(); i++) {
            SimpleDoctor doctor = doctors.get(i);
            response.append("    {\n");
            response.append("      \"name\": \"").append(doctor.getName()).append("\",\n");
            response.append("      \"city\": \"").append(doctor.getCity()).append("\",\n");
            response.append("      \"email\": \"").append(doctor.getEmail()).append("\",\n");
            response.append("      \"phoneNumber\": \"").append(doctor.getPhoneNumber()).append("\",\n");
            response.append("      \"speciality\": \"").append(doctor.getSpeciality()).append("\"\n");
            response.append("    }");
            
            if (i < doctors.size() - 1) {
                response.append(",");
            }
            
            response.append("\n");
        }
        
        response.append("  ]\n}");
        
        sendResponse(exchange, response.toString(), 200);
    }
    
    private static void handleAddDoctor(HttpExchange exchange) throws IOException {
        // Read request body
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            String line;
            while ((line = br.readLine()) != null) {
                requestBody.append(line);
            }
        }
        
        // Parse JSON (simplified for demo)
        String jsonStr = requestBody.toString();
        Map<String, String> doctorData = parseJson(jsonStr);
        
        // Validate data
        String validationError = validateDoctorData(doctorData);
        if (validationError != null) {
            String errorResponse = "{\n  \"error\": \"" + validationError + "\"\n}";
            sendResponse(exchange, errorResponse, 400);
            return;
        }
        
        // Create new doctor
        SimpleDoctor newDoctor = new SimpleDoctor(
            doctorData.get("name"),
            DoctorCity.valueOf(doctorData.get("city")),
            doctorData.get("email"),
            doctorData.get("phoneNumber"),
            Speciality.valueOf(doctorData.get("speciality"))
        );
        
        // Add to list
        doctors.add(newDoctor);
        
        // Return success response
        String successResponse = "{\n  \"message\": \"Doctor added successfully\",\n" +
                "  \"doctor\": {\n" +
                "    \"name\": \"" + newDoctor.getName() + "\",\n" +
                "    \"city\": \"" + newDoctor.getCity() + "\",\n" +
                "    \"email\": \"" + newDoctor.getEmail() + "\",\n" +
                "    \"phoneNumber\": \"" + newDoctor.getPhoneNumber() + "\",\n" +
                "    \"speciality\": \"" + newDoctor.getSpeciality() + "\"\n" +
                "  }\n}";
        
        sendResponse(exchange, successResponse, 201);
    }
    
    private static void handleRemoveDoctor(HttpExchange exchange) throws IOException {
        // Get doctor email from query params
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = parseQueryParams(query);
        
        String email = params.get("email");
        if (email == null || email.isEmpty()) {
            String errorResponse = "{\n  \"error\": \"Email parameter is required\"\n}";
            sendResponse(exchange, errorResponse, 400);
            return;
        }
        
        // Find and remove doctor
        boolean removed = false;
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getEmail().equals(email)) {
                doctors.remove(i);
                removed = true;
                break;
            }
        }
        
        if (removed) {
            String successResponse = "{\n  \"message\": \"Doctor removed successfully\"\n}";
            sendResponse(exchange, successResponse, 200);
        } else {
            String errorResponse = "{\n  \"error\": \"Doctor not found\"\n}";
            sendResponse(exchange, errorResponse, 404);
        }
    }
    
    private static void handlePatients(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        
        if ("GET".equals(method)) {
            // Handle GET request - list all patients
            handleGetPatients(exchange);
        } else if ("POST".equals(method)) {
            // Handle POST request - add a new patient
            handleAddPatient(exchange);
        } else if ("DELETE".equals(method)) {
            // Handle DELETE request - remove a patient
            handleRemovePatient(exchange);
        } else {
            // Method not allowed
            String response = "Method not allowed";
            exchange.sendResponseHeaders(405, response.length());
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
    
    private static void handleGetPatients(HttpExchange exchange) throws IOException {
        String response = "{\n" +
                "  \"patients\": [\n" +
                "    {\n" +
                "      \"name\": \"Alice Cooper\",\n" +
                "      \"city\": \"DELHI\",\n" +
                "      \"email\": \"alice.cooper@example.com\",\n" +
                "      \"phoneNumber\": \"9876543214\",\n" +
                "      \"symptom\": \"arthritis\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Bob Smith\",\n" +
                "      \"city\": \"MUMBAI\",\n" +
                "      \"email\": \"bob.smith@example.com\",\n" +
                "      \"phoneNumber\": \"9876543215\",\n" +
                "      \"symptom\": \"skin infection\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Rahul Mehta\",\n" +
                "      \"city\": \"NOIDA\",\n" +
                "      \"email\": \"rahul.mehta@example.com\",\n" +
                "      \"phoneNumber\": \"9876543216\",\n" +
                "      \"symptom\": \"ear pain\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Priya Gupta\",\n" +
                "      \"city\": \"DELHI\",\n" +
                "      \"email\": \"priya.gupta@example.com\",\n" +
                "      \"phoneNumber\": \"9876543217\",\n" +
                "      \"symptom\": \"dysmenorrhea\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        
        sendResponse(exchange, response, 200);
    }
    
    private static void handleAddPatient(HttpExchange exchange) throws IOException {
        // Read request body
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            String line;
            while ((line = br.readLine()) != null) {
                requestBody.append(line);
            }
        }
        
        // Parse JSON (simplified for demo)
        String jsonStr = requestBody.toString();
        Map<String, String> patientData = parseJson(jsonStr);
        
        // Validate data
        String validationError = validatePatientData(patientData);
        if (validationError != null) {
            String errorResponse = "{\n  \"error\": \"" + validationError + "\"\n}";
            sendResponse(exchange, errorResponse, 400);
            return;
        }
        
        // In a real application, we would add the patient to the database
        // For demo purposes, we just return a success response
        
        String successResponse = "{\n  \"message\": \"Patient added successfully\",\n" +
                "  \"patient\": {\n" +
                "    \"name\": \"" + patientData.get("name") + "\",\n" +
                "    \"city\": \"" + patientData.get("city") + "\",\n" +
                "    \"email\": \"" + patientData.get("email") + "\",\n" +
                "    \"phoneNumber\": \"" + patientData.get("phoneNumber") + "\",\n" +
                "    \"symptom\": \"" + patientData.get("symptom") + "\"\n" +
                "  }\n}";
        
        sendResponse(exchange, successResponse, 201);
    }
    
    private static void handleRemovePatient(HttpExchange exchange) throws IOException {
        // Get patient email from query params
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = parseQueryParams(query);
        
        String email = params.get("email");
        if (email == null || email.isEmpty()) {
            String errorResponse = "{\n  \"error\": \"Email parameter is required\"\n}";
            sendResponse(exchange, errorResponse, 400);
            return;
        }
        
        // In a real application, we would remove the patient from the database
        // For demo purposes, we just return a success response
        
        String successResponse = "{\n  \"message\": \"Patient removed successfully\"\n}";
        sendResponse(exchange, successResponse, 200);
    }
    
    // Simple JSON parser (for demo purposes only)
    private static Map<String, String> parseJson(String json) {
        Map<String, String> result = new HashMap<>();
        
        // Remove { } and split by commas
        json = json.trim();
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }
        
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                
                // Remove quotes
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length() - 1);
                }
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                
                result.put(key, value);
            }
        }
        
        return result;
    }
    
    // Validate doctor data
    private static String validateDoctorData(Map<String, String> doctorData) {
        // Check required fields
        if (!doctorData.containsKey("name") || doctorData.get("name").isEmpty()) {
            return "Name is required";
        }
        if (!doctorData.containsKey("city") || doctorData.get("city").isEmpty()) {
            return "City is required";
        }
        if (!doctorData.containsKey("email") || doctorData.get("email").isEmpty()) {
            return "Email is required";
        }
        if (!doctorData.containsKey("phoneNumber") || doctorData.get("phoneNumber").isEmpty()) {
            return "Phone number is required";
        }
        if (!doctorData.containsKey("speciality") || doctorData.get("speciality").isEmpty()) {
            return "Speciality is required";
        }
        
        // Validate name (at least 3 characters)
        if (doctorData.get("name").length() < 3) {
            return "Name must be at least 3 characters";
        }
        
        // Validate city (must be one of the allowed values)
        try {
            DoctorCity.valueOf(doctorData.get("city"));
        } catch (IllegalArgumentException e) {
            return "City must be one of: DELHI, NOIDA, FARIDABAD";
        }
        
        // Validate email (simple validation)
        if (!doctorData.get("email").matches(".+@.+\\..+")) {
            return "Invalid email format";
        }
        
        // Validate phone number (at least 10 digits)
        if (!doctorData.get("phoneNumber").matches("\\d{10,}")) {
            return "Phone number must be at least 10 digits";
        }
        
        // Validate speciality (must be one of the allowed values)
        try {
            Speciality.valueOf(doctorData.get("speciality"));
        } catch (IllegalArgumentException e) {
            return "Speciality must be one of: ORTHOPEDIC, GYNECOLOGY, DERMATOLOGY, ENT";
        }
        
        return null; // No validation errors
    }
    
    // Validate patient data
    private static String validatePatientData(Map<String, String> patientData) {
        // Check required fields
        if (!patientData.containsKey("name") || patientData.get("name").isEmpty()) {
            return "Name is required";
        }
        if (!patientData.containsKey("city") || patientData.get("city").isEmpty()) {
            return "City is required";
        }
        if (!patientData.containsKey("email") || patientData.get("email").isEmpty()) {
            return "Email is required";
        }
        if (!patientData.containsKey("phoneNumber") || patientData.get("phoneNumber").isEmpty()) {
            return "Phone number is required";
        }
        if (!patientData.containsKey("symptom") || patientData.get("symptom").isEmpty()) {
            return "Symptom is required";
        }
        
        // Validate name (at least 3 characters)
        if (patientData.get("name").length() < 3) {
            return "Name must be at least 3 characters";
        }
        
        // Validate city (max 20 characters)
        if (patientData.get("city").length() > 20) {
            return "City must not exceed 20 characters";
        }
        
        // Validate email (simple validation)
        if (!patientData.get("email").matches(".+@.+\\..+")) {
            return "Invalid email format";
        }
        
        // Validate phone number (at least 10 digits)
        if (!patientData.get("phoneNumber").matches("\\d{10,}")) {
            return "Phone number must be at least 10 digits";
        }
        
        // Validate symptom (must be one of the allowed values)
        String symptom = patientData.get("symptom").toLowerCase();
        List<String> validSymptoms = Arrays.asList(
            "arthritis", "back pain", "tissue injuries",
            "dysmenorrhea",
            "skin infection", "skin burn",
            "ear pain"
        );
        
        if (!validSymptoms.contains(symptom)) {
            return "Invalid symptom. Must be one of: " + String.join(", ", validSymptoms);
        }
        
        return null; // No validation errors
    }
    
    private static void handleSuggestions(HttpExchange exchange) throws IOException {
        // Parse query parameters
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = parseQueryParams(query);
        
        String city = params.get("city");
        String symptom = params.get("symptom");
        
        if (city == null || symptom == null) {
            String errorResponse = "{\n  \"error\": \"Missing required parameters: city and symptom\"\n}";
            sendResponse(exchange, errorResponse, 400);
            return;
        }
        
        // URL decode the symptom
        symptom = URLDecoder.decode(symptom, StandardCharsets.UTF_8);
        
        try {
            DoctorCity doctorCity = DoctorCity.valueOf(city);
            Speciality speciality = symptomSpecialityService.getSpecialityForSymptom(symptom);
            
            if (speciality != null) {
                // Find matching doctors
                List<SimpleDoctor> matchingDoctors = new ArrayList<>();
                for (SimpleDoctor doctor : doctors) {
                    if (doctor.getCity() == doctorCity && doctor.getSpeciality() == speciality) {
                        matchingDoctors.add(doctor);
                    }
                }
                
                if (matchingDoctors.isEmpty()) {
                    String noDocResponse = "{\n  \"message\": \"There isn't any doctor present at your location for your symptom\"\n}";
                    sendResponse(exchange, noDocResponse, 200);
                } else {
                    StringBuilder doctorResponse = new StringBuilder();
                    doctorResponse.append("{\n  \"doctors\": [\n");
                    
                    for (int i = 0; i < matchingDoctors.size(); i++) {
                        SimpleDoctor doctor = matchingDoctors.get(i);
                        doctorResponse.append("    {\n");
                        doctorResponse.append("      \"name\": \"").append(doctor.getName()).append("\",\n");
                        doctorResponse.append("      \"city\": \"").append(doctor.getCity()).append("\",\n");
                        doctorResponse.append("      \"email\": \"").append(doctor.getEmail()).append("\",\n");
                        doctorResponse.append("      \"phoneNumber\": \"").append(doctor.getPhoneNumber()).append("\",\n");
                        doctorResponse.append("      \"speciality\": \"").append(doctor.getSpeciality()).append("\"\n");
                        doctorResponse.append("    }");
                        
                        if (i < matchingDoctors.size() - 1) {
                            doctorResponse.append(",");
                        }
                        
                        doctorResponse.append("\n");
                    }
                    
                    doctorResponse.append("  ],\n");
                    doctorResponse.append("  \"symptom\": \"").append(symptom).append("\",\n");
                    doctorResponse.append("  \"speciality\": \"").append(speciality).append("\"\n");
                    doctorResponse.append("}");
                    sendResponse(exchange, doctorResponse.toString(), 200);
                }
            } else {
                String invalidSymptomResponse = "{\n  \"error\": \"Invalid symptom: " + symptom + "\"\n}";
                sendResponse(exchange, invalidSymptomResponse, 400);
            }
        } catch (IllegalArgumentException e) {
            String notServicedResponse = "{\n  \"message\": \"We are still waiting to expand to your location\"\n}";
            sendResponse(exchange, notServicedResponse, 200);
        }
    }
    
    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        
        if (query == null || query.isEmpty()) {
            return params;
        }
        
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = pair.substring(0, idx);
                String value = pair.substring(idx + 1);
                params.put(key, value);
            }
        }
        
        return params;
    }
    
    private static void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        if (response.startsWith("<html>")) {
            exchange.getResponseHeaders().set("Content-Type", "text/html");
        }
        
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
} 