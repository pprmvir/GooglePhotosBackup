package photosBackup.reader;

import photosBackup.client.ClientFactory;
import photosBackup.client.PhotosClientInterface;
import photosBackup.client.SourceClient;
import com.google.api.gax.rpc.ApiException;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.Album;

import java.io.IOException;
import java.security.GeneralSecurityException;
public class GooglePhotosReader {
    public void getAlbums() {
        try {
            // Make a request to list all albums in the user's library
            // Iterate over all the albums in this list
            // Pagination is handled automatically
            PhotosClientInterface readerClient = ClientFactory.getPhotosClient("source");
            PhotosLibraryClient photosLibraryClient = readerClient.getPhotosClient();
            InternalPhotosLibraryClient.ListAlbumsPagedResponse response = photosLibraryClient.listAlbums();

            for (Album album : response.iterateAll()) {
                // Get some properties of an album
                String id = album.getId();
                String title = album.getTitle();
                String productUrl = album.getProductUrl();
                String coverPhotoBaseUrl = album.getCoverPhotoBaseUrl();
                // The cover photo media item id field may be empty
                String coverPhotoMediaItemId = album.getCoverPhotoMediaItemId();
                boolean isWritable = album.getIsWriteable();
                long mediaItemsCount = album.getMediaItemsCount();
            }
            System.out.println("Albums read from source");

        } catch (ApiException | IOException | GeneralSecurityException e) {
            // Handle error
        }
    }
}
