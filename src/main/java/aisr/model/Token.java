package aisr.model;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 *
 * @author gauravdahal
 */
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    private String recruit_email;
    private String generated_token;

    // Default constructor
    public Token() {
        // Generate a random token
        this.generated_token = generateRandomToken();
    }

    // Constructor with parameters
    public Token(String recruit_email) {
        this.recruit_email = recruit_email;
        // If a generated token is not provided, generate one
        if (generated_token == null || generated_token.isEmpty()) {
            this.generated_token = generateRandomToken();
        }
    }

    public String getRecruit_email() {
        return recruit_email;
    }

    public void setRecruit_email(String recruit_email) {
        this.recruit_email = recruit_email;
    }

    public String getGenerated_token() {
        return generated_token;
    }

    public void setGenerated_token(String generated_token) {
        this.generated_token = generated_token;
    }

    // Method to generate a random token of fixed length
    private String generateRandomToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }
}
