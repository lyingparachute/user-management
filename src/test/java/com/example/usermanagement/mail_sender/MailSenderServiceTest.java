package com.example.usermanagement.mail_sender;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.ITemplateEngine;

import java.util.Map;

import static com.example.usermanagement.mail_sender.MailSenderService.getAppUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceTest {

    private static final String USER_EMAIL = "test@example.com";
    private static final String TEST_SUBJECT = "Test Subject";
    private static final String TEMPLATE_LOCATION = "/location/template.html";
    private static final String HTML_BODY = "Test HTML Body";
    private static final Map<String, Object> TEMPLATE_MODEL = Map.ofEntries(
        Map.entry("key", "value")
    );
    @InjectMocks
    MailSenderService underTest;
    @Mock
    JavaMailSender javaMailSender;
    @Mock
    MimeMessage mimeMessage;
    @Spy
    ITemplateEngine thymeleafTemplateEngine;
    @Spy
    MockHttpServletRequest servletRequest;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(underTest, "sender", "sender@example.com");
    }

    @Nested
    class SendEmail {
        @Test
        void createsAndSendsEmail_givenParameters() {
            // When
            when(thymeleafTemplateEngine.process(anyString(), any())).thenReturn(HTML_BODY);
            when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
            underTest.sendEmail(USER_EMAIL, TEST_SUBJECT, TEMPLATE_LOCATION, TEMPLATE_MODEL);

            // Then
            then(javaMailSender).should().send(mimeMessage);
        }

        @Test
        void throwsEmailException_whenMessagingExceptionOccurs() {
            // When
            when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(thymeleafTemplateEngine.process(anyString(), any())).thenReturn(HTML_BODY);
            doThrow(new RuntimeException("Failed to send email."))
                .when(javaMailSender)
                .send(mimeMessage);

            // Then
            assertThatThrownBy(() -> {
                underTest.sendEmail(USER_EMAIL, TEST_SUBJECT, TEMPLATE_LOCATION, TEMPLATE_MODEL);
            })
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to send email.");
        }
    }

    @Nested
    class GetAppUrl {
        @Test
        void returnsAppUrlAsString_givenHttpServletRequest() {
            // Given
            final var expected = "http://localhost:80";

            // When
            final var appUrl = getAppUrl(servletRequest);

            // Then
            assertThat(appUrl).isNotNull().isNotEmpty().isEqualTo(expected);
        }
    }
}