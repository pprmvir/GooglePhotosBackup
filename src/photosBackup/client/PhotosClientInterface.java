package photosBackup.client;

import com.google.photos.library.v1.PhotosLibraryClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface PhotosClientInterface {
    PhotosLibraryClient getPhotosClient() throws IOException, GeneralSecurityException;
}
