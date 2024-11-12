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
    private static final Logger logger = LogManager.getLogger(GamerService.class);
    private static final String EMAIL_REGEX = "[a-zA-Z][\\w._-]*@[a-z]+\\.com$";
    private static final String NAME_REGEX = "^[A-Z][a-z]{2,}(?: [A-Z][a-z]{2,})* [A-Z][a-z]+$";
    private static final String PASSWORD_REGEX = "^(?=(?:.*[A-Z]){3,})(?=(?:.*[a-z]){3,})(?=(?:.*\\d){2,})[A-Za-z\\d@!#$%^&*()_+={}\\[\\]:;\"'<>?,./~`-]*$";
    private static final String USERNAME_REGEX = "[a-zA-Z][a-zA-Z0-9]*$";
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Autowired
    private GamerRepository gamerRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public GamerService(GamerRepository gamerRepository) {
        this.gamerRepository = gamerRepository;
    }

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
        gamerRepository.save(gamer);
        logger.info("Successful registration of Gamer: {}", gamer.toString());
        return gamer;
    }

    private boolean isAvailableEmailAddress(String email) {
        return gamerRepository.findByEmail(email).isEmpty();
    }

    private boolean isAvailableUsername(String username) {
        return gamerRepository.findByUsername(username).isEmpty();
    }

    private boolean isValidPassword(String password) {
        logger.info("Checking validity of password.");
        if (!password.matches(PASSWORD_REGEX)) {
            logger.error("Invalid password: {}", password);
            return false;
        }
        logger.info("Valid password: {}", password);
        return true;
    }

    private boolean isValidEmailAddress(String email) {
        logger.info("Checking validity of email address: {}", email);
        if (!email.matches(EMAIL_REGEX)) {
            logger.error("Invalid email address: {}", email);
            return false;
        }
        logger.info("Valid email address: {}", email);
        return true;
    }

    private boolean isValidUsername(String username) {
        logger.info("Checking validity of username: {}", username);
        if (username.length() < 8 || !username.matches(USERNAME_REGEX)) {
            logger.error("Invalid username: {}", username);
            return false;
        }
        logger.info("Valid username: {}", username);
        return true;
    }

    private boolean isValidName(String name) {
        logger.info("Checking validity of name: {}", name);
        if (!name.matches(NAME_REGEX)) {
            logger.error("Invalid name: {}", name);
            return false;
        }
        logger.info("Valid name: {}", name);
        return true;
    }

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

    public void deductCreditsForReservation(Gamer gamer, VideoGame videoGame, int quantity, String transactionType) {
        logger.info("At deductCredits method for reservation. Gamer: {}, videoGame: {}, quantity: {}, transactionType: {}", gamer, videoGame, quantity, transactionType);
        logger.info("Reducing Gamer's credit by 20% of total cost. Before - Gamer: {}", gamer.toString());
        double gameCreditsCost = videoGame.getCredits();
        double totalCredits = gamer.getTotalCredits();
        double totalPurchaseCost = 0.2 * gameCreditsCost * (double) quantity;
        gamer.setTotalCredits(Double.parseDouble(decimalFormat.format(totalCredits - totalPurchaseCost)));
        logger.info("Reduced Gamer's credit by total cost. After - Gamer: {}", gamer.toString());
        saveGamer(gamer);
    }

    @Transactional
    public void addPurchaseTransactionForGamer(Gamer gamer, PurchaseTransaction purchaseTransaction) {
        logger.info("Adding PurchaseTransaction({}) to Gamer's({}) purchase history.", purchaseTransaction.toString(), gamer.toString());
        gamer.addPurchaseTransaction(purchaseTransaction);
        saveGamer(gamer);
    }

    public Gamer getCurrentGamer() {
        PersonPrincipal person = (PersonPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (Gamer) person.getPerson();
    }

    public void addReservationTransactionForGamer(Gamer gamer, Reservation reservation) {
        logger.info("Adding Reservation({}) to Gamer's({}) purchase history.", reservation.toString(), gamer.toString());
        gamer.addReservationTransaction(reservation);
        saveGamer(gamer);
    }

    public void addCancelTransaction(Gamer gamer, CancelTransaction cancelTransaction) {
        logger.info("Adding CancelTransaction ({}) for Gamer ({})", cancelTransaction.toString(), gamer.toString());
        gamer.addCancelTransaction(cancelTransaction);
        logger.info("Added CancelTransaction ({}) for Gamer ({})", cancelTransaction.toString(), gamer.toString());
    }

    public void removeReservationTransaction(Gamer gamer, Reservation reservation) {
        logger.info("Removing Reservation ({}) for Gamer ({}).", reservation, gamer);
        gamer.removeReservationTransaction(reservation);
        logger.info("Removing aforementioned Reservation for Gamer ({}).", gamer);
    }

    private void saveGamer(Gamer gamer) {
        logger.info("Saving Gamer - Before: {}", gamer.toString());
        gamerRepository.save(gamer);
        logger.info("Saved Gamer - After: {}", gamer.toString());
    }

}
