package com.movieapp.menu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

import org.bson.BsonValue;

import com.mongodb.client.result.InsertOneResult;
import com.movieapp.database.Database;
import com.movieapp.movie.Movie;
import com.movieapp.nlp.TFIDF;
import com.movieapp.nlp.MovieRecommender;
import com.movieapp.nlp.Processor;

public class Menu {

    private Processor processor = new Processor("src/main/resources/listOfStopWords.txt");
    private TFIDF tfidf = new TFIDF(processor);

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
        String csvFile = "src/main/resources/movie_data_test.csv";
        String line;
        String delimiter = "#";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the first header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] movieData = line.split(delimiter);
                String originalTitle = movieData[0];
                String overview = movieData[1];
                Double rating = Double.parseDouble(movieData[movieData.length - 2]);

                Movie movieObject = new Movie(originalTitle, overview, rating);
                InsertOneResult result = movieDatabase.addToDatabase(movieObject.getDocument());
                BsonValue id = result.getInsertedId();

                tfidf.addSample(id, movieObject);

            }

            tfidf.calculateIDF();

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

    public void addMovieToDatabase(Scanner scanner) {

        System.out.println("Please enter the name of the movie");
        String movieName = scanner.nextLine();
        System.out.println("Please enter overview of the movie");
        String movieOverview = scanner.nextLine();
        System.out.println("Please enter the overall rating of the movie (out of 10)");
        Double movieRating = Double.parseDouble(scanner.nextLine());

        Movie userMovie = new Movie(movieName, movieOverview, movieRating);
        Database movieDatabase = new Database("movie_app_database", "movie_data");

        InsertOneResult result = movieDatabase.addToDatabase(userMovie.getDocument());
        tfidf.addSample(result.getInsertedId(), userMovie);
        System.out.println("Movie " + movieName + " successfully added to the database!");

    }

    public void findSimilarMovies(Scanner scanner){            

        System.out.println("Please enter overview of the movie");
        String movieOverview = scanner.nextLine();

        System.out.println("How many movies would you like to see?");
        int numRecommendations = scanner.nextInt();

        System.out.println("Finding similar movies...");

        MovieRecommender recommender = new MovieRecommender(processor, tfidf);
        ArrayList<Movie> recommendations = recommender.recommendMovies(movieOverview, numRecommendations);

        for(Movie movie : recommendations){
            System.out.println("Name: " + movie.getName());
            System.out.println("Overview: " + movie.getOverview());
            System.out.println("Rating: " + movie.getRating());
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
        Scanner scanner = new Scanner(System.in);
        
        int choice = 0;

        while(choice != 6){

            System.out.println("Please select one of the following options ");
            System.out.println("1. Add a movie to the database.");
            System.out.println("2. Get details of a movie from the database.");
            System.out.println("3. Find similar movies.");
            System.out.println("4. Add movie reviews.");
            System.out.println("5. Get movie reviews.");
            System.out.println("6. Exit.");
    
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.println("Adding a movie to the database");
                    menu.addMovieToDatabase(scanner);
                    break;

                case 2:
                    System.out.println("Reading a movie from the database");
                    break;

                case 3:
                    System.out.println("Ok, let's find you some similar movies!");
                    menu.findSimilarMovies(scanner);
                    break;
                
                case 6:
                    System.out.println("Exiting the movie app...");
                    menu.shutDown();
                    scanner.close();
                    break;  

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }


    }
}
