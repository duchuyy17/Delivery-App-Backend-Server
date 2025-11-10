package com.laptrinhjavaweb.news.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "configuration")
public class ConfigurationDocument {
    @Id
    private String id;

    // ======= EMAIL CONFIG =======
    private String email;
    private String emailName;
    private String password;
    private boolean enableEmail;
    private String formEmail;

    // ======= PAYMENT CONFIG =======
    private String clientId;
    private String clientSecret;
    private boolean sandbox;
    private String publishableKey;
    private String secretKey;
    private String currency;
    private String currencySymbol;
    private double deliveryRate;
    private String costType;

    // ======= TWILIO (SMS) CONFIG =======
    private String twilioAccountSid;
    private String twilioAuthToken;
    private String twilioPhoneNumber;
    private boolean twilioEnabled;

    // ======= SENDGRID CONFIG =======
    private String sendGridApiKey;
    private boolean sendGridEnabled;
    private String sendGridEmail;
    private String sendGridEmailName;
    private String sendGridPassword;

    // ======= CLOUDINARY CONFIG =======
    private String cloudinaryUploadUrl;
    private String cloudinaryApiKey;

    // ======= GOOGLE CONFIG =======
    private String googleApiKey;
    private String googleMapLibraries;
    private String googleColor;
    private String webClientID;
    private String androidClientID;
    private String iOSClientID;
    private String expoClientID;

    // ======= FIREBASE CONFIG =======
    private String firebaseKey;
    private String authDomain;
    private String projectId;
    private String storageBucket;
    private String msgSenderId;
    private String appId;
    private String measurementId;
    private String vapidKey;

    // ======= SENTRY CONFIG =======
    private String dashboardSentryUrl;
    private String webSentryUrl;
    private String apiSentryUrl;
    private String customerAppSentryUrl;
    private String restaurantAppSentryUrl;
    private String riderAppSentryUrl;

    // ======= AMPLITUDE CONFIG =======
    private String webAmplitudeApiKey;
    private String appAmplitudeApiKey;

    // ======= LEGAL LINKS =======
    private String termsAndConditions;
    private String privacyPolicy;

    // ======= SYSTEM FLAGS =======
    private boolean isPaidVersion;
    private boolean skipEmailVerification;
    private boolean skipMobileVerification;

    // ======= TEST DATA =======
    private String testOtp;
}
