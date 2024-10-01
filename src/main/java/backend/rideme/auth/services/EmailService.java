package backend.rideme.auth.services;

import backend.rideme.auth.dto.converters.ApiResponse;
import backend.rideme.auth.entities.enums.EmailType;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    ApiResponse senEmail(EmailType emailType, String email, String token, String name) throws MessagingException, UnsupportedEncodingException;
}
