

import java.io.*;
import java.util.*;

class Piece {
    String pieceType;
    String color;
    String x; // Column letter (A–H)
    int y;    // Row number (1–8)

    Piece(String pieceType, String color, String x, int y) {
        this.pieceType = pieceType.toLowerCase();
        this.color = color.toLowerCase();
        this.x = x.toUpperCase();
        this.y = y;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Piece> pieces = new ArrayList<>();

        // Read file
        try {
            File file = new File("./pieces.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                Piece piece = new Piece(parts[0].trim(),parts[1].trim(),parts[2].trim(),Integer.parseInt(parts[3].trim()));
                pieces.add(piece);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return;
        }

        try (// Ask for target position
        Scanner input = new Scanner(System.in)) {
            System.out.print("Enter the target position (e.g., E2): ");
            String pos = input.nextLine().trim().toUpperCase();
            String targetX = pos.substring(0, 1);
            int targetY = Integer.parseInt(pos.substring(1));

            // Validate for each piece
            for (Piece p : pieces) {
                boolean valid = isValidMove(p, targetX, targetY);
                if (valid) {
                    System.out.println((p.pieceType) + " at " + p.x + p.y +
                            " can move to " + targetX + targetY);
                } else {
                    System.out.println((p.pieceType) + " at " + p.x + p.y +
                            " can NOT move to " + targetX + targetY);
                }
            }
        } catch (NumberFormatException e) {
            
            e.printStackTrace();
        }
    }

    // Validate move rules
    static boolean isValidMove(Piece p, String targetX, int targetY) {
        int currX = p.x.charAt(0) - 'A'; // 0–7
        int currY = p.y - 1;             // 0–7
        int newX = targetX.charAt(0) - 'A';
        int newY = targetY - 1;

        // Check board bounds
        if (newX < 0 || newX > 7 || newY < 0 || newY > 7) return false;
        if (currX == newX && currY == newY) return false; // no move

        int dx = Math.abs(newX - currX);
        int dy = Math.abs(newY - currY);

        switch (p.pieceType) {
            case "rook":
                return (dx == 0 || dy == 0);
            case "bishop":
                return (dx == dy);
            case "queen":
                return (dx == dy || dx == 0 || dy == 0);
            case "king":
                return (dx <= 1 && dy <= 1);
            case "knight":
                return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
            case "pawn":
                if (p.color.equals("white")) {
                    return (dx == 0 && newY == currY + 1);
                } else { // black pawn moves "down"
                    return (dx == 0 && newY == currY - 1);
                }
            default:
                return false;
        }
    }

}
