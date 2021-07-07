package cz.uhk.rajneon1.footbaltracker.rest.resources;

import cz.uhk.rajneon1.footbaltracker.dao.FootballMatchRepository;
import cz.uhk.rajneon1.footbaltracker.dao.UserRepository;
import cz.uhk.rajneon1.footbaltracker.exception.auth.AuthorizationHeaderMissingException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.MalformedAuthorizationHeaderException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.UserVerificationException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.BadRequestException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.ForbiddenResourceException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.ResourceNotFoundException;
import cz.uhk.rajneon1.footbaltracker.googlehttpclient.FitHttpClient;
import cz.uhk.rajneon1.footbaltracker.model.*;
import cz.uhk.rajneon1.footbaltracker.rest.dto.request.NewFootballMatchDto;
import cz.uhk.rajneon1.footbaltracker.rest.dto.request.RequestBodyVerifier;
import cz.uhk.rajneon1.footbaltracker.rest.dto.response.FootballMatchDto;
import cz.uhk.rajneon1.footbaltracker.rest.dto.response.SimpleIdsDto;
import cz.uhk.rajneon1.footbaltracker.security.RequestVerifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FootballMatchController {

    private FootballMatchRepository footballMatchRepository;
    private RequestVerifier requestVerifier;
    private RequestBodyVerifier requestBodyVerifier;
    private UserRepository userRepository;
    private FitHttpClient fitHttpClient;

    public FootballMatchController(FootballMatchRepository footballMatchRepository, RequestVerifier requestVerifier,
                                   RequestBodyVerifier requestBodyVerifier, UserRepository userRepository, FitHttpClient fitHttpClient) {
        this.footballMatchRepository = footballMatchRepository;
        this.requestVerifier = requestVerifier;
        this.requestBodyVerifier = requestBodyVerifier;
        this.userRepository = userRepository;
        this.fitHttpClient = fitHttpClient;
    }

    @GetMapping("/api/match/{id}")
    public ResponseEntity<FootballMatchDto> retrieveMatch(@PathVariable("id") int id, HttpServletRequest request)
            throws ForbiddenResourceException, UserVerificationException, MalformedAuthorizationHeaderException,
            AuthorizationHeaderMissingException, ResourceNotFoundException {
        requestVerifier.verifyRequest(request, UserRole.TRAINER);
        FootballMatch match = footballMatchRepository.getOneById(id);
        if (match == null) {
            throw new ResourceNotFoundException("Football match with id '" + id + "' not found.");
        }
        return ResponseEntity.ok(new FootballMatchDto(match));
    }

    @GetMapping("/api/match")
    public ResponseEntity<SimpleIdsDto> retrieveAllMatches(HttpServletRequest request) throws ForbiddenResourceException,
            UserVerificationException, MalformedAuthorizationHeaderException, AuthorizationHeaderMissingException {
        requestVerifier.verifyRequest(request, UserRole.TRAINER);
        return ResponseEntity.ok(
                new SimpleIdsDto(footballMatchRepository.findAll().stream().map(FootballMatch::getId).collect(Collectors.toList()))
        );
    }

    @PostMapping("/api/match")
    public ResponseEntity createMatch(@RequestBody NewFootballMatchDto matchDto, HttpServletRequest request)
            throws ForbiddenResourceException, UserVerificationException, MalformedAuthorizationHeaderException,
            AuthorizationHeaderMissingException, BadRequestException, IllegalBlockSizeException, NoSuchAlgorithmException,
            IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        Trainer trainer = (Trainer) requestVerifier.verifyRequest(request, UserRole.TRAINER);
        requestBodyVerifier.verifyNewFootballMatch(matchDto);
        List<User> players = matchDto.getPlayersLogins().stream().map(it -> userRepository.getOneByLogin(it)).collect(Collectors.toList());
        List<PlayerPerformancePerMatch> performances = new ArrayList<>();
        for (User player : players) {
            if (player instanceof Trainer) throw new BadRequestException("List of match players contains a trainer(s)");
            performances.add(fitHttpClient.getPlayerPerformance((Player) player, matchDto.getStartTime(), matchDto.getEndTime()));
        }
        FootballMatch footballMatch = new FootballMatch(performances, trainer, matchDto.getStartTime(), matchDto.getEndTime());
        footballMatchRepository.save(footballMatch);
        return ResponseEntity.noContent().build();
    }

}
