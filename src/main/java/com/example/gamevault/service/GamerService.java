/**
 * Service class responsible for managing Gamer-related operations.
 * It interacts with the `GamerRepository` to persist and retrieve gamer data and handles various exceptions related
 * to input validation, authentication, and transaction processing.
 */
package com.example.gamevault.service;

import com.example.gamevault.exception.*;
import com.example.gamevault.model.*;
import com.example.gamevault.security.PersonPrincipal;
import com.example.gamevault.repository.GamerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Optional;

@Service
public class GamerService {

    /**
     * Logger to monitor operational flow and facilitate troubleshooting.
     */
    private static final Logger logger = LogManager.getLogger(GamerService.class);

    /**
     * Regular expression of a valid email address.
     */
    private static final String EMAIL_REGEX = "[a-zA-Z][\\w._-]*@[a-z]+\\.com$";

    /**
     * Regular expression of a valid name.
     */
    private static final String NAME_REGEX = "^[A-Z][a-z]{2,}(?: [A-Z][a-z]{2,})* [A-Z][a-z]+$";

    /**
     * Regular expression of a valid password.
     */
    private static final String PASSWORD_REGEX = "^(?=(?:.*[A-Z]){3,})(?=(?:.*[a-z]){3,})(?=(?:.*\\d){2,})[A-Za-z\\d@!#$%^&*()_+={}\\[\\]:;\"'<>?,./~`-]*$";

    /**
     * Regular expression of a valid username.
     */
    private static final String USERNAME_REGEX = "[a-zA-Z][a-zA-Z0-9]*$";

    /**
     * DecimalFormat object to format values in 2 decimal places.
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    /**
     * {@link GamerRepository} for managing {@link Gamer} entities.
     */
    @Autowired
    private GamerRepository gamerRepository;

    /**
     * The {@link AuthenticationManager} used for handling authentication operations,
     * such as validating user credentials and creating an {@link Authentication} object
     * to establish the user's authentication in the security context.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * The {@link PasswordEncoder} used for encoding and validating user passwords.
     * It ensures that passwords are securely stored and compared, supporting various
     * encryption algorithms for secure password handling.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@link GamerService} with the specified {@link GamerRepository}.
     * This constructor is used for injecting the repository dependency into the service
     * to perform database operations related to gamers.
     *
     * @param gamerRepository the {@link GamerRepository} to be used by this service
     */
    public GamerService(GamerRepository gamerRepository) {
        this.gamerRepository = gamerRepository;
    }

    /**
     * Registers a new {@link Gamer} entity based on specified name, username, email address, and password.
     * Validates the input fields and checks for username and email availability.
     *
     * @param name      The name of the {@link Gamer}.
     * @param username  The username of the {@link Gamer}.
     * @param email     The email address of the {@link Gamer}.
     * @param password  The password of the {@link Gamer}.
     * @return          Newly-registered {@link Gamer} entity.
     * @throws InvalidNameException             if the name is invalid.
     * @throws InvalidUsernameException         if the username is invalid.
     * @throws InvalidEmailAddressException     if the email address is invalid.
     * @throws InvalidPasswordException         if the password is invalid.
     * @throws UnavailableUsernameException     if the username is unavailable.
     * @throws UnavailableEmailAddressException if the email address is unavailable.
     */
    public Person register(String name, String username, String email, String password)
            throws InvalidNameException, InvalidUsernameException, InvalidEmailAddressException, InvalidPasswordException, UnavailableUsernameException, UnavailableEmailAddressException {
        if (!isValidName(name)) {
            logger.error("Unsuccessful registration due to invalid name: {}", name);
            throw new InvalidNameException();

        } else if (!isValidUsername(username)) {
            logger.error("Unsuccessful registration due to invalid username: {}", username);
            throw new InvalidUsernameException();

        } else if (!isValidEmailAddress(email)) {
            logger.error("Unsuccessful registration due to invalid email address: {}", email);
            throw new InvalidEmailAddressException();

        } else if (!isValidPassword(password)) {
            logger.error("Unsuccessful registration due to invalid password.");
            throw new InvalidPasswordException();

        } else if (!isAvailableUsername(username)) {
            logger.error("Unsuccessful registration due to unavailable username: {}", username);
            throw new UnavailableUsernameException();

        } else if (!isAvailableEmailAddress(email)) {
            logger.error("Unsuccessful registration due to unavailable email address: {}", email);
            throw new UnavailableEmailAddressException();
        }

        Gamer gamer = new Gamer(name, username, email, passwordEncoder.encode(password));
        saveGamer(gamer);
        logger.info("Successful registration of Gamer: {}", gamer.toString());
        return gamer;
    }

    /**
     * Checks if the email address is available for registration.
     *
     * @param email the email address to check.
     * @return {@code true} if the email address is available for registration, {@code false} otherwise.
     */
    private boolean isAvailableEmailAddress(String email) {
        return gamerRepository.findByEmail(email).isEmpty();
    }

    /**
     * Checks if the username is available for registration.
     *
     * @param username the email address to check.
     * @return {@code true} if the email address is available for registration, {@code false} otherwise.
     */
    private boolean isAvailableUsername(String username) {
        return gamerRepository.findByUsername(username).isEmpty();
    }

    /**
     * Checks if the password is valid.
     *
     * @param password the password to check.
     * @return {@code true} if the password is valid, {@code false} otherwise.
     */
    private boolean isValidPassword(String password) {
        logger.info("Checking validity of password.");
        if (!password.matches(PASSWORD_REGEX)) {
            logger.error("Invalid password. ");
            return false;
        }
        logger.info("Valid password. ");
        return true;
    }

    /**
     * Checks if the email address is valid.
     *
     * @param email the email address to check.
     * @return {@code true} if the email address is valid, {@code false} otherwise.
     */
    private boolean isValidEmailAddress(String email) {
        logger.info("Checking validity of email address: {}", email);
        if (!email.matches(EMAIL_REGEX)) {
            logger.error("Invalid email address: {}", email);
            return false;
        }
        logger.info("Valid email address: {}", email);
        return true;
    }

    /**
     * Checks if the username is valid.
     *
     * @param username the username to check.
     * @return {@code true} if the username is valid, {@code false} otherwise.
     */
    private boolean isValidUsername(String username) {
        logger.info("Checking validity of username: {}", username);
        if (username.length() < 8 || !username.matches(USERNAME_REGEX)) {
            logger.error("Invalid username: {}", username);
            return false;
        }
        logger.info("Valid username: {}", username);
        return true;
    }

    /**
     * Checks if the name is valid.
     *
     * @param name the username to check.
     * @return {@code true} if the name is valid, {@code false} otherwise.
     */
    private boolean isValidName(String name) {
        logger.info("Checking validity of name: {}", name);
        if (!name.matches(NAME_REGEX)) {
            logger.error("Invalid name: {}", name);
            return false;
        }
        logger.info("Valid name: {}", name);
        return true;
    }

    /**
     * Authenticates the gamer by username and password.
     * Throws an exception if authentication fails.
     *
     * @param username the gamer's username.
     * @param password the gamer's password.
     * @return the authenticated user's details.
     * @throws UnsuccessfulLoginException if login fails due to invalid credentials.
     */
    public PersonPrincipal login(String username, String password) throws UnsuccessfulLoginException {
        logger.info("Validating login credentials (username & password). Username: {}", username);
        Optional<Person> userOptional = gamerRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            Person person = userOptional.get();
            logger.info("Username found in database. Person: {}", person.toString());
            if (passwordEncoder.matches(password, person.getPassword())) {
                logger.info("Raw password matches encoded password for Person: {}", person.toString());
            } else {
                logger.error("Raw password does not match encoded password for Person: {}", person.toString());
            }
        } else {
            logger.error("Username not found in database: {}.", username);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            logger.info("Successful login for Gamer. Username: {}", username);
            return (PersonPrincipal) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            logger.error("Unsuccessful login due to invalid username and/or password.");
            throw new UnsuccessfulLoginException();
        }
    }

    /**
     * Checks if a Gamer can afford a Purchase or Reservation of {@link VideoGame} for the specified quantity.
     * @param gamer             The {@link Gamer} attempting a purchase or reservation.
     * @param videoGame         The {@link VideoGame} being purchased or reserved.
     * @param quantity          The quantity of {@link VideoGame} to purchase or reserve.
     * @param transactionType   The type of {@link Transaction} (purchase or reservation).
     * @return                  {@code true} if the {@link Gamer} can afford the transaction, false otherwise.
     * @throws InsufficientCreditsForTransactionException if the {@link Gamer} does not have sufficient credits.
     */
    public boolean canAffordTransaction(Gamer gamer, VideoGame videoGame, int quantity, String transactionType) throws InsufficientCreditsForTransactionException {
        double videoGameCredit = videoGame.getCredits();
        double totalCost = 0;

        if (transactionType.equals("complete purchase of reservation")) {
            totalCost = 0.8 * videoGameCredit * (double) quantity;
        } else if (transactionType.equals("purchase") || transactionType.equals("reservation")){
            totalCost = videoGameCredit * (double) quantity;
        }

        logger.info("Location: canAffordPurchase method | Gamer: {}, Video Game Credit: {}, Quantity to Buy: {}, Total Cost: {}", gamer, videoGameCredit, quantity, totalCost);
        if (gamer.getTotalCredits() >= totalCost) {
            logger.info("Gamer has sufficient credit to pay total cost.");
            return true;
        }
        logger.error("Gamer has insufficient credit to pay total cost. InsufficientCreditsForTransactionException thrown");
        throw new InsufficientCreditsForTransactionException();
    }

    /**
     * Deducts the number of a {@link Gamer}'s credits by the quantity of {@link VideoGame} purchased or reserved.
     *
     * @param gamer The {@link Gamer} whose credits will be deducted.
     * @param videoGame The {@link VideoGame} being purchased or reserved.
     * @param quantity The quantity of {@link VideoGame} being purchased or reserved.
     * @param transactionType The type of transaction (purchase or reservation).
     */
    public void deductCredits(Gamer gamer, VideoGame videoGame, int quantity, String transactionType) {
        logger.info("At deductCredits method. Gamer: {}, videoGame: {}, quantity: {}, transactionType: {}", gamer, videoGame, quantity, transactionType);
        double videoGameCost = videoGame.getCredits();
        double totalGamerCredits = gamer.getTotalCredits();
        double totalCost;

        if (transactionType.equals("purchase")) {
           totalCost = videoGameCost * (double) quantity;
        } else {
            totalCost = 0.8 * videoGameCost * (double) quantity;
        }

        logger.info("Reducing Gamer's credit by totalCost ({}). Before - Gamer: {}", totalCost, gamer.toString());
        gamer.setTotalCredits(Double.parseDouble(decimalFormat.format(totalGamerCredits - totalCost)));
        logger.info("Reducing Gamer's credit by total cost. After - Gamer: {}", gamer.toString());
        saveGamer(gamer);
    }

    /**
     * Gets the current logged-in gamer.
     * @return the current logged-in gamer.
     */
    public Gamer getCurrentGamer() {
        PersonPrincipal person = (PersonPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (Gamer) person.getPerson();
    }

    /**
     * Adds a {@link Purchase} to a {@link Gamer}'s purchase history.
     * @param gamer     The {@link Gamer} making the purchase.
     * @param purchase  The {@link Purchase} object to be added.
     */
    @Transactional
    public void addPurchaseToGamerPurchaseHistory(Gamer gamer, Purchase purchase) {
        logger.info("Adding Purchase({}) to Gamer's({}) purchase history.", purchase.toString(), gamer.toString());
        gamer.addPurchaseTransaction(purchase);
        saveGamer(gamer);
    }

    /**
     * Adds a {@link Reservation} to a {@link Gamer}'s reservation history.
     * @param gamer         The {@link Gamer} making the purchase.
     * @param reservation   The {@link Reservation} object to be added.
     */
    public void addReservationToGamerReservationHistory(Gamer gamer, Reservation reservation) {
        logger.info("Adding Reservation({}) to Gamer's({}) reservation history.", reservation.toString(), gamer.toString());
        gamer.addReservationTransaction(reservation);
        saveGamer(gamer);
    }

    /**
     * Adds a {@link Cancellation} to a {@link Gamer}'s reservation history.
     * @param gamer         The {@link Gamer} making the purchase.
     * @param cancellation  The {@link Cancellation} object to be added.
     */
    public void addCancellationToGamerCancellationHistory(Gamer gamer, Cancellation cancellation) {
        logger.info("Adding Cancellation ({}) for Gamer ({})", cancellation.toString(), gamer.toString());
        gamer.addCancelTransaction(cancellation);
        logger.info("Added Cancellation ({}) for Gamer ({})", cancellation.toString(), gamer.toString());
    }

    /**
     * Removes a {@link Reservation} from a {@link Gamer}'s reservation history.
     * @param gamer         The {@link Gamer} that made the {@link Reservation}.
     * @param reservation   The {@link Reservation} object to be removed.
     */
    public void removeReservationFromGamerReservationHistory(Gamer gamer, Reservation reservation) {
        logger.info("Removing Reservation ({}) for Gamer ({}).", reservation, gamer);
        gamer.removeReservationTransaction(reservation);
        logger.info("Removing aforementioned Reservation for Gamer ({}).", gamer);
    }

    /**
     * Saves the given {@link Gamer} into the {@link GamerRepository} gamer repository.
     * @param gamer The {@link Gamer} to be added to the {@link GamerRepository} gamer repository.
     */
    private void saveGamer(Gamer gamer) {
        logger.info("Saving Gamer: {}", gamer.toString());
        gamerRepository.save(gamer);
        logger.info("Saved Gamer: {}", gamer.toString());
    }

}
