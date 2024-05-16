
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Base64;
import java.util.Scanner;

class Node {
    String word;
    String encryptedText;
    Node next;

    public Node(String word) {
        this.word = word;
        this.encryptedText = "";
        this.next = null;
    }
}

class CircularQueue {
    private Node rear;

    public CircularQueue() {
        rear = null;
    }

    public boolean isEmpty() {
        return rear == null;
    }

    public void enqueue(String word) {
        Node newNode = new Node(word);
        if (isEmpty()) {
            newNode.next = newNode;
            rear = newNode;
        } else {
            newNode.next = rear.next;
            rear.next = newNode;
            rear = newNode;
        }
    }

    public Node getRear() {
        return rear;
    }

    public void encryptTexts(String vigenereKey, String aesKey) {
        Node temp = rear.next;
        do {
            if (temp.word.length() <= 2) {
                temp.encryptedText = caesarCipherEncrypt(temp.word, 3);
            } else if (temp.word.length() > 2 && temp.word.length() <= 5) {
                temp.encryptedText = vigenereCipherEncrypt(temp.word, vigenereKey);
            } else {
                try {
                    temp.encryptedText = aesCipherEncrypt(temp.word, aesKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            temp = temp.next;
        } while (temp != rear.next);
    }

    public String getEncryptedText() {
        StringBuilder encryptedText = new StringBuilder();
        Node temp = rear.next;
        do {
            encryptedText.append(temp.encryptedText).append(" ");
            temp = temp.next;
        } while (temp != rear.next);
        return encryptedText.toString().trim();
    }

    public static String caesarCipherEncrypt(String plainText, int shift) {
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++) {
            char ch = plainText.charAt(i);
            if (Character.isLetter(ch)) {
                char shifted = (char) (ch + shift);
                if ((Character.isUpperCase(ch) && shifted > 'Z') ||
                        (Character.isLowerCase(ch) && shifted > 'z')) {
                    shifted = (char) (ch - (26 - shift));
                }
                encryptedText.append(shifted);
            } else {
                encryptedText.append(ch);
            }
        }
        return encryptedText.toString();
    }

    public static String vigenereCipherEncrypt(String plainText, String key) {
        StringBuilder encryptedText = new StringBuilder();
        int keyIndex = 0;
        for (int i = 0; i < plainText.length(); i++) {
            char ch = plainText.charAt(i);
            if (Character.isLetter(ch)) {
                char keyChar = key.charAt(keyIndex % key.length());
                int shift = Character.toUpperCase(keyChar) - 'A';
                char shifted = (char) (ch + shift);
                if ((Character.isUpperCase(ch) && shifted > 'Z') ||
                        (Character.isLowerCase(ch) && shifted > 'z')) {
                    shifted = (char) (ch - (26 - shift));
                }
                encryptedText.append(shifted);
                keyIndex++;
            } else {
                encryptedText.append(ch);
            }
        }
        return encryptedText.toString();
    }

    public static String aesCipherEncrypt(String plainText, String aesKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptText(String encryptedText, String vigenereKey, String aesKey) throws Exception {
        String[] encryptedWords = encryptedText.split(" ");
        StringBuilder decryptedText = new StringBuilder();

        for (String word : encryptedWords) {
            if (word.length() <= 2) {
                decryptedText.append(caesarCipherDecrypt(word, 3)).append(" ");
            } else if (word.length() > 2 && word.length() <= 5) {
                decryptedText.append(vigenereCipherDecrypt(word, vigenereKey)).append(" ");
            } else {
                decryptedText.append(aesCipherDecrypt(word, aesKey)).append(" ");
            }
        }

        return decryptedText.toString().trim();
    }

    public static String caesarCipherDecrypt(String encryptedText, int shift) {
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < encryptedText.length(); i++) {
            char ch = encryptedText.charAt(i);
            if (Character.isLetter(ch)) {
                char shifted = (char) (ch - shift);
                if ((Character.isUpperCase(ch) && shifted < 'A') ||
                        (Character.isLowerCase(ch) && shifted < 'a')) {
                    shifted = (char) (ch + (26 - shift));
                }
                decryptedText.append(shifted);
            } else {
                decryptedText.append(ch);
            }
        }
        return decryptedText.toString();
    }

    public static String vigenereCipherDecrypt(String encryptedText, String key) {
        StringBuilder decryptedText = new StringBuilder();
        int keyIndex = 0;
        for (int i = 0; i < encryptedText.length(); i++) {
            char ch = encryptedText.charAt(i);
            if (Character.isLetter(ch)) {
                char keyChar = key.charAt(keyIndex % key.length());
                int shift = Character.toUpperCase(keyChar) - 'A';
                char shifted = (char) (ch - shift);
                if ((Character.isUpperCase(ch) && shifted < 'A') ||
                        (Character.isLowerCase(ch) && shifted < 'a')) {
                    shifted = (char) (ch + (26 - shift));
                }
                decryptedText.append(shifted);
                keyIndex++;
            } else {
                decryptedText.append(ch);
            }
        }
        return decryptedText.toString();
    }

    public static String aesCipherDecrypt(String encryptedText, String aesKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    public void hideTextInImage(String text, String inputImagePath, String outputImagePath) throws Exception {
        BufferedImage image = ImageIO.read(new File(inputImagePath));
        int[] bits = textToBits(text);
        int bitIndex = 0;

        for (int y = 0; y < image.getHeight() && bitIndex < bits.length; y++) {
            for (int x = 0; x < image.getWidth() && bitIndex < bits.length; x++) {
                int rgb = image.getRGB(x, y);
                int newRgb = (rgb & 0xFFFFFFFE) | bits[bitIndex];
                image.setRGB(x, y, newRgb);
                bitIndex++;
            }
        }

        ImageIO.write(image, "png", new File(outputImagePath));
    }

    public String extractTextFromImage(String imagePath) throws Exception {
        BufferedImage image = ImageIO.read(new File(imagePath));
        int[] bits = new int[image.getHeight() * image.getWidth()];
        int bitIndex = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                bits[bitIndex] = rgb & 1;
                bitIndex++;
            }
        }

        String extractedText = bitsToText(bits);
        return extractedText.split("ÿ")[0]; // Stop at the first occurrence of the padding character.
    }

    private int[] textToBits(String text) {
        int[] bits = new int[text.length() * 8];
        for (int i = 0; i < text.length(); i++) {
            int charVal = text.charAt(i);
            for (int j = 0; j < 8; j++) {
                bits[i * 8 + (7 - j)] = (charVal >> j) & 1;
            }
        }
        return bits;
    }

    private String bitsToText(int[] bits) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < bits.length / 8; i++) {
            int charVal = 0;
            for (int j = 0; j < 8; j++) {
                charVal = (charVal << 1) | bits[i * 8 + j];
            }
            text.append((char) charVal);
        }
        return text.toString();
    }
}

public class stego {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CircularQueue queue = new CircularQueue();

        System.out.println("Enter a sentence:");
        String sentence = scanner.nextLine();
        String[] words = sentence.split(" ");

        for (String word : words) {
            queue.enqueue(word);
        }

        System.out.println("Enter Vigenere key:");
        String vigenereKey = scanner.nextLine();

        System.out.println("Enter AES key (16 characters):");
        String aesKey = scanner.nextLine();

        queue.encryptTexts(vigenereKey, aesKey);

        String encryptedText = queue.getEncryptedText();
        System.out.println("Encrypted Texts: " + encryptedText);

        System.out.println("Enter the path to the input image:");
        String inputImagePath = "trial.png";

        System.out.println("Enter the path to save the output image:");
        String outputImagePath = "trial1.png";

        try {
            queue.hideTextInImage(encryptedText, inputImagePath, outputImagePath);

            System.out.println("Text hidden in image successfully.");

            System.out.println("Enter the path to the image to decode:");
            String imagePath = scanner.nextLine();

            System.out.println("Enter Vigenere key for decryption:");
            String decryptVigenereKey = scanner.nextLine();

            System.out.println("Enter AES key (16 characters) for decryption:");
            String decryptAesKey = scanner.nextLine();

            String extractedText = queue.extractTextFromImage(imagePath);
            System.out.println("Extracted Encrypted Texts: " + extractedText);

            String decryptedText = CircularQueue.decryptText(extractedText, decryptVigenereKey, decryptAesKey);
            System.out.println("Decrypted Texts: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
