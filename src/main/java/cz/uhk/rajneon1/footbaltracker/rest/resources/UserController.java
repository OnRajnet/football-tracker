package cz.uhk.rajneon1.footbaltracker.rest.resources;

import cz.uhk.rajneon1.footbaltracker.dao.UserRepository;
import cz.uhk.rajneon1.footbaltracker.exception.auth.AuthorizationHeaderMissingException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.MalformedAuthorizationHeaderException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.UserVerificationException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.BadRequestException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.ForbiddenResourceException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.ResourceNotFoundException;
import cz.uhk.rajneon1.footbaltracker.model.Player;
import cz.uhk.rajneon1.footbaltracker.model.User;
import cz.uhk.rajneon1.footbaltracker.model.UserRole;
import cz.uhk.rajneon1.footbaltracker.rest.dto.request.ChangePasswordDto;
import cz.uhk.rajneon1.footbaltracker.rest.dto.request.NewPlayerDto;
import cz.uhk.rajneon1.footbaltracker.rest.dto.request.RequestBodyVerifier;
import cz.uhk.rajneon1.footbaltracker.rest.dto.response.PerformanceDto;
import cz.uhk.rajneon1.footbaltracker.rest.dto.response.PlayerDto;
import cz.uhk.rajneon1.footbaltracker.rest.dto.response.SimpleIdsDto;
import cz.uhk.rajneon1.footbaltracker.security.PasswordHandler;
import cz.uhk.rajneon1.footbaltracker.security.RequestVerifier;
import cz.uhk.rajneon1.footbaltracker.security.ResourceVerifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private RequestVerifier requestVerifier;
    private ResourceVerifier resourceVerifier;
    private UserRepository userRepository;
    private RequestBodyVerifier requestBodyVerifier;
    private PasswordHandler passwordHandler;

    public UserController(RequestVerifier requestVerifier, ResourceVerifier resourceVerifier, UserRepository userRepository,
                          RequestBodyVerifier requestBodyVerifier, PasswordHandler passwordHandler) {
        this.requestVerifier = requestVerifier;
        this.resourceVerifier = resourceVerifier;
        this.userRepository = userRepository;
        this.requestBodyVerifier = requestBodyVerifier;
        this.passwordHandler = passwordHandler;
    }

    @GetMapping("/api/player/{login}")
    public ResponseEntity<PlayerDto> retrievePlayer(@PathVariable("login") String login, HttpServletRequest request)
            throws ForbiddenResourceException, UserVerificationException, MalformedAuthorizationHeaderException,
            AuthorizationHeaderMissingException, BadRequestException, ResourceNotFoundException {
        User requestingUser = requestVerifier.verifyRequest(request, UserRole.PLAYER, UserRole.TRAINER);
        List<String> allowedUsersToRequestThisResource = userRepository
                .findByUserRole(UserRole.TRAINER)
                .stream()
                .map(User::getLogin)
                .collect(Collectors.toList());
        allowedUsersToRequestThisResource.add(login);
        resourceVerifier.verifyResource(requestingUser, allowedUsersToRequestThisResource.toArray(new String[0]));
        User playerDetail = userRepository.getOneByLogin(login);
        if (playerDetail == null) {
            throw new ResourceNotFoundException("Player with login '" + login + "' not found.");
        } else if (!(playerDetail instanceof Player)) {
            throw new BadRequestException("Request resource is not a player.");
        }
        List<PerformanceDto> performanceDtos = ((Player) playerDetail).getPerformances().stream().map(PerformanceDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(new PlayerDto(playerDetail.getLogin(), performanceDtos));
    }

    @GetMapping("/api/player")
    public ResponseEntity<SimpleIdsDto> getAllPlayers(HttpServletRequest request) throws ForbiddenResourceException,
            UserVerificationException, MalformedAuthorizationHeaderException, AuthorizationHeaderMissingException {
        User requestingUser = requestVerifier.verifyRequest(request, UserRole.TRAINER);
        resourceVerifier.verifyResource(requestingUser, userRepository
                .findByUserRole(UserRole.TRAINER)
                .stream()
                .map(User::getLogin).toArray(String[]::new));

        List<User> players = userRepository.findByUserRole(UserRole.PLAYER);
        return ResponseEntity.ok(new SimpleIdsDto(players
                .stream()
                .map(User::getLogin)
                .collect(Collectors.toList())
        ));
    }

    @PostMapping("/api/user")
    public ResponseEntity createPlayer(@RequestBody NewPlayerDto dto) throws BadRequestException {
        if (userRepository.getOneByLogin(dto.getLogin()) != null) {
            throw new BadRequestException("User with this login already exists.");
        }
        Player newPlayer = new Player(dto.getLogin(), passwordHandler.hashPassword(dto.getPlainPass()));
        userRepository.save(newPlayer);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/user/{login}")
    public ResponseEntity changePassword(@PathVariable("login") String login, @RequestBody ChangePasswordDto dto, HttpServletRequest request)
            throws ForbiddenResourceException, UserVerificationException, MalformedAuthorizationHeaderException,
            AuthorizationHeaderMissingException, BadRequestException {
        User requstingUser = requestVerifier.verifyRequest(request, UserRole.PLAYER, UserRole.TRAINER);
        resourceVerifier.verifyResource(requstingUser, login);
        requestBodyVerifier.verifyChangePassword(dto);
        if (!passwordHandler.matchPasswords(dto.getOldPassword(), requstingUser.getPassword())) {
            throw new BadRequestException("Old password does not match.");
        }
        requstingUser.setPassword(passwordHandler.hashPassword(dto.getNewPassword()));
        userRepository.save(requstingUser);
        return ResponseEntity.noContent().build();
    }

}
