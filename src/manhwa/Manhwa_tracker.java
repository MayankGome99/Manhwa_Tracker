package manhwa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Manhwa_tracker {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/manhwa_tracker";
        String user = "root";
        String password = "YOUR_DATABASE_PASSWORD";

        String insertSQL = "INSERT INTO my_list (title, chapter, rating) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement info = conn.prepareStatement(insertSQL);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("--- WELCOME TO MANHWA TRACKER ---");

            while (true) {
                System.out.println("\n1. Add a New Manhwa");
                System.out.println("2. View All Manhwa");
                System.out.println("3. Update Chapter Count");
                System.out.println("4. Delete a Manhwa");
                System.out.println("5. Exit Program");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer

                switch (choice) {
                    case 1: {
                        System.out.print("Please enter the Manhwa title: ");
                        String title = scanner.nextLine();

                        System.out.print("Please enter the chapter count (e.g., 88 or 88+): ");
                        String chapter = scanner.nextLine();

                        System.out.print("Enter Rating (1-10): ");
                        double rating = scanner.nextDouble();
                        scanner.nextLine(); // Clear the buffer

                        info.setString(1, title);
                        info.setString(2, chapter);
                        info.setDouble(3, rating);
                        info.executeUpdate();

                        System.out.println("[SUCCESS] Saved '" + title + "' to the database!");
                        break;
                    }

                    case 2: {
                        System.out.println("\n--- YOUR MANHWA LIST ---");
                        String selectSQL = "SELECT * FROM my_list";

                        try (java.sql.Statement stmt = conn.createStatement();
                             java.sql.ResultSet rs = stmt.executeQuery(selectSQL)) {

                            while (rs.next()) {
                                String title = rs.getString("title");
                                String chapter = rs.getString("chapter");
                                double rating = rs.getDouble("rating");

                                // Convert rating out of 10 into a 5-star visual system
                                int starCount = (int) Math.round(rating / 2.0);
                                String stars = "⭐".repeat(Math.max(0, starCount));

                                System.out.println("Title: " + title + " | Chapter: " + chapter + " | Rating: " + rating + "/10 " + stars);
                            }
                        } catch (SQLException e) {
                            System.out.println("[Error] Could not fetch data");
                            e.printStackTrace();
                        }
                        break;
                    }

                    case 3: {
                        System.out.print("Enter the Title of the Manhwa you want to Update: ");
                        String updateTitle = scanner.nextLine();

                        System.out.print("Please enter the New Chapter Count: ");
                        String newchapter = scanner.nextLine();

                        String updateSQL = "UPDATE my_list SET chapter = ? WHERE title = ?";

                        try (PreparedStatement updatestmt = conn.prepareStatement(updateSQL)) {
                            updatestmt.setString(1, newchapter);
                            updatestmt.setString(2, updateTitle);

                            int rowsAffected = updatestmt.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("[SUCCESS] Updated chapter count for '" + updateTitle + "'!");
                            } else {
                                System.out.println("[ERROR] Manhwa not found. Check your spelling!");
                            }
                        } catch (SQLException e) {
                            System.out.println("[Error] Could not update the data");
                            e.printStackTrace();
                        }
                        break;
                    }

                    case 4: {
                        System.out.println("\n--- SELECT MANHWA TO DELETE ---");
                        ArrayList<String> titleList = new ArrayList<>();

                        // Fetch titles with a numbered index
                        String showSQL = "SELECT title FROM my_list";
                        try (java.sql.Statement stmt = conn.createStatement();
                             java.sql.ResultSet rs = stmt.executeQuery(showSQL)) {

                            int index = 1;
                            while (rs.next()) {
                                String t = rs.getString("title");
                                titleList.add(t);
                                System.out.println(index + ". " + t);
                                index++;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        if (titleList.isEmpty()) {
                            System.out.println("Your list is empty! Nothing to delete.");
                            break;
                        }

                        System.out.print("\nEnter the number of the Manhwa you want to delete: ");
                        int targetIndex = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer

                        if (targetIndex < 1 || targetIndex > titleList.size()) {
                            System.out.println("[ERROR] Invalid number selection.");
                            break;
                        }

                        // Get the title using the list index (subtract 1 for 0-based indexing)
                        String deleteTitle = titleList.get(targetIndex - 1);
                        String deleteSQL = "DELETE FROM my_list WHERE title = ?";

                        try (PreparedStatement deletestmt = conn.prepareStatement(deleteSQL)) {
                            deletestmt.setString(1, deleteTitle);
                            int rowsAffected = deletestmt.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("[SUCCESS] Deleted '" + deleteTitle + "'!");
                            } else {
                                System.out.println("[ERROR] Could not delete.");
                            }
                            System.out.println("Please wait...");
                            Thread.sleep(2000);
                        } catch (SQLException | InterruptedException e) {
                            System.out.println("[Error] Could not delete the manhwa");
                            e.printStackTrace();
                        }
                        break;
                    }

                    case 5: {
                        System.out.println("Closing Program. Goodbye!");
                        System.exit(0);
                        break;
                    }

                    default:
                        System.out.println("Invalid choice. Please pick between 1 and 5.");
                }
            }

        } catch (SQLException e) {
            System.out.println("[FAILED] Database error.");
            e.printStackTrace();
        }
    }
}