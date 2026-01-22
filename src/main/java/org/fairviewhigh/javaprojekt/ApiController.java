package org.fairviewhigh.javaprojekt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ApiController {
    VocabularyTrainerWeb trainer = new VocabularyTrainerWeb();

    @GetMapping("/spanish")
    public ResponseEntity<String> getSpanish() {
        try {
            String word = trainer.getWord(2);
            if (word == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("No Spanish word found");
            }
            return ResponseEntity.ok(word);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while fetching the word");
        }
    }

    @GetMapping("/english")
    public ResponseEntity<String> getEnglish() {
        try {
            String word = trainer.getWord(1);  // Getting the English word
            if (word == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("No English word found");
            }
            return ResponseEntity.ok(word);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while fetching the word");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList() {
        try {
            trainer.getWord(0);
            String[] list = trainer.getList();
            if (list == null || list.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No English word found");
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the word");
        }
    }

    @PostMapping("/checkEnglish")
    public ResponseEntity<Map<String, String>> checkEnglish(@RequestBody Map<String, Object> requestData) {
        Map<String, String> response = new HashMap<>();

        if (requestData.containsKey("message") && requestData.get("message") != null &&
            requestData.containsKey("id") && requestData.get("id") instanceof Number) {
            String word = requestData.get("message").toString();
            int id = ((Number) requestData.get("id")).intValue();
            String result = VocabularyTrainerWeb.isCorrect(word, id, 2);
            
            response.put("result", result);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Missing or invalid 'message' or 'id' parameter");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/checkSpanish")
    public ResponseEntity<Map<String, String>> checkSpanish(@RequestBody Map<String, Object> requestData) {
        Map<String, String> response = new HashMap<>();

        if (requestData.containsKey("message") && requestData.get("message") != null &&
            requestData.containsKey("id") && requestData.get("id") instanceof Number) {
            String word = requestData.get("message").toString();
            int id = ((Number) requestData.get("id")).intValue();
            String result = VocabularyTrainerWeb.isCorrect(word, id, 1);
            
            // Return the result in the response
            response.put("result", result);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Missing or invalid 'message' or 'id' parameter");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/addWord")
    public ResponseEntity<Map<String, String>> addWord(@RequestBody Map<String, String> requestData) {
        Map<String, String> response = new HashMap<>();
        
        // Check if 'message' key exists in the requestData
        if (requestData.containsKey("En") && requestData.get("En") != null) {
            // Call the static method directly using the class name
            VocabularyTrainerWeb.addVocabCard(requestData.get("En"), requestData.get("Spa"));
            
            response.put("En", "Word added: " + requestData.get("En"));
            return ResponseEntity.ok(response); // Return success response
        } else {
            // Handle case where 'message' is missing or null
            response.put("error", "Missing or invalid 'message' parameter");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // Return error response with 400 Bad Request
        }
    }

    //doesn't work
    
    @PostMapping("/changeFile")
    public ResponseEntity<Map<String, String>> changeFile(@RequestBody Map<String, String> requestData) {
        Map<String, String> response = new HashMap<>();

        if (requestData.containsKey("File") && requestData.get("File") != null) {
            String fileName = requestData.get("File").trim();

            boolean isFileChanged = VocabularyTrainerWeb.setFilePath(fileName);

            if (isFileChanged) {
                response.put("message", "File successfully changed to: " + fileName);
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Failed to change the file. Please check the file name.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            response.put("error", "Missing or invalid 'File' parameter");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
