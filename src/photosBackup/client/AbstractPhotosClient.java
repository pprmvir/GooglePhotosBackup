package photosBackup.client;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

public abstract class  AbstractPhotosClient implements PhotosClientInterface {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static final String AUTH_GOOGLE_CALLBACK = "/auth/google/callback";

    protected abstract List<String> getRequiredScopes();
    protected abstract String getCredentialsPath();
    protected abstract Integer getReciverPort();
    protected abstract File getDataStorePath();

    @Override
    public PhotosLibraryClient getPhotosClient() throws IOException, GeneralSecurityException {
        return PhotosLibraryClient.initialize(PhotosLibrarySettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(getCredentials()))
                .build());
    }

    private Credentials getCredentials() throws IOException, GeneralSecurityException {
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(getCredentialsPath())));
        String clientId = clientSecrets.getDetails().getClientId();
        String clientSecret = clientSecrets.getDetails().getClientSecret();

        GoogleAuthorizationCodeFlow flow = getGoogleAuthorizationCodeFlow(clientSecrets);
        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder()
                        .setCallbackPath(AUTH_GOOGLE_CALLBACK)
                        .setPort(getReciverPort())
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver)
                .authorize(clientSecrets.get("user").toString());

        return UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setAccessToken(getAccessToken(credential))
                .setRefreshToken(credential.getRefreshToken())
                .build();
    }

    private AccessToken getAccessToken(Credential credential) {
        return new AccessToken(credential.getAccessToken(), new Date(credential.getExpirationTimeMilliseconds()));
    }

    private GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow(GoogleClientSecrets clientSecrets)
            throws IOException, GeneralSecurityException {
        return new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                getRequiredScopes())
                .setDataStoreFactory(new FileDataStoreFactory(getDataStorePath()))
                .setAccessType("offline")
                .build();
    }


}
