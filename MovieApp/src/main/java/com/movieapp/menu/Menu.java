package com.movieapp.menu;

import org.bson.Document;

import com.movieapp.database.Database;
import com.movieapp.movie.Movie;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Menu {

    /*
     * This method is called before showing the menu options to the user. It creates the necessary collections in the database.
     * It also parses the necessary CSV files and populatest the collection.
     * 
     * You would also want to add other features such 
     */
    public void startUp() {

        // Create a collection in the database to store Movie objects
        Database movieDatabase = new Database("movie_app_database", "movie_data");
        movieDatabase.createCollection();

        // Parse test_movie_metadata.csv
        String csvFile = "src/main/resources/test_movie_metadata.csv";
        String line;
        String delimiter = "#";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the first header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] movieData = line.split(delimiter);
                String originalTitle = movieData[1];
                String overview = movieData[0];
                Float rating = Float.parseFloat(movieData[movieData.length - 2]);

                Movie movieObject = new Movie(originalTitle, overview, rating);
                movieDatabase.addToDatabase(movieObject.getDocument());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * This method is called whenever user selects the Exit option. This method deletes the collection created in the database.
     */
    public void shutDown() {

        Database movieDatabase = new Database("movie_app_database", "movie_data");
        movieDatabase.deleteCollection();

    }

    public void addMovieToDatabase() {

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Please enter the name of the movie");
            String movieName = scanner.nextLine();
            System.out.println("Please enter overview of the movie");
            String movieOverview = scanner.nextLine();
            System.out.println("Please enter the overall rating of the movie (out of 10)");
            Float movieRating = Float.parseFloat(scanner.nextLine());

            Movie userMovie = new Movie(movieName, movieOverview, movieRating);
            Database movieDatabase = new Database("movie_app_database", "movie_data");

            movieDatabase.addToDatabase(userMovie.getDocument());

        }
    }

    public static void main(String[] args) {

        System.out.println("Initializing the movie app...");
        
        //Call the startUp method
        Menu menu = new Menu();
        menu.startUp();
        
        // Ideally you want to make this menu an endless loop until the user enters to exit the app.
        // When they select the option, you call the shutDown method.
        System.out.println("Hello! Welcome to the movie app!");
        
        System.out.println("Please select one of the following options ");
        System.out.println("1. Add a movie to the database.");
        System.out.println("2. Get details of a movie from the database.");
        System.out.println("3. Find similar movies.");
        System.out.println("4. Add movie reviews.");
        System.out.println("5. Get movie reviews.");
        System.out.println("6. Exit.");

        System.out.print("Enter you choice: ");

        try (Scanner scanner = new Scanner(System.in)) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    menu.addMovieToDatabase();
                    break;

                case 2:
                    System.out.println("Reading a movie from the database");
                    break;

                case 3:
                    System.out.println("Find similar movies");
                    break;
                
                case 6:
                    System.out.println("Exiting the movie app...");
                    menu.shutDown();
                    break;  

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }


    }
}
