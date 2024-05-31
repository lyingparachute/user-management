package com.example.usermanagement.verification;


import com.example.usermanagement.api.model.UserAccountRequest;
import com.example.usermanagement.mail_sender.MailSenderService;
import com.example.usermanagement.model.UserAccount;
import com.example.usermanagement.model.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.usermanagement.mail_sender.MailSenderService.getAppUrl;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final VerificationTokenRepository tokenRepository;
    private final UserAccountRepository userRepository;
    private final MailSenderService mailService;

    public void sendAccountVerificationEmail(final HttpServletRequest request, final UserAccount user) {
        if (user.isActivated())
            throw new RuntimeException("Account with email '" + user.getUserDetails().email() + "' is already verified.");
        revokeAllVerificationTokensForUser(user.getId());
        final var verificationToken = createAndSaveEmailVerificationToken(user);
        mailService.sendEmail(user.getUserDetails().email(),
            "Verify your email address.",
            "account-create-confirmation.html",
            ofEntries(
                entry("username", user.getUserDetails().username()),
                entry("confirmLink", getRegistrationConfirmationLink(request,
                    verificationToken)),
                entry("websiteLink", getAppUrl(request))
            )
        );
    }

    void validateVerificationToken(final String token) throws RuntimeException {
        final var verificationToken = getVerificationToken(token);
        if (!verificationToken.isValid())
            throw new RuntimeException("Verification Token expired or revoked: " + token);
    }

    @Transactional
    void verifyAccount(final String token) {
        final var user = getUserByVerificationToken(token);
        user.enableAccount();
        userRepository.save(user);
        revokeVerificationToken(token);
    }

    private void revokeAllVerificationTokensForUser(final Long id) {
        tokenRepository.findAllValidTokenByUserId(id)
            .forEach(VerificationToken::setRevoked);
    }

    @Transactional
    protected void revokeVerificationToken(final String token) {
        final var verificationToken = getVerificationToken(token);
        verificationToken.setRevoked();
        tokenRepository.save(verificationToken);
    }

    private UserAccount getUserByVerificationToken(final String token) {
        return getVerificationToken(token).getUser();
    }

    private String getRegistrationConfirmationLink(final HttpServletRequest request, final String token) {
        return getAppUrl(request) + "/account/verify?token=" + token;
    }

    private VerificationToken getVerificationToken(final String token) {
        return tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid Verification Token: " + token));
    }

    private String createAndSaveEmailVerificationToken(final UserAccount user) {
        final var myToken = new VerificationToken(
            user,
            UUID.randomUUID().toString()
        );
        return tokenRepository.save(myToken).getToken();
    }
}