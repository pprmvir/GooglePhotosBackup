package photosBackup.writer;

import com.google.api.gax.rpc.ApiException;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.BatchCreateMediaItemsResponse;
import com.google.photos.library.v1.proto.NewMediaItem;
import com.google.photos.types.proto.Album;
import photosBackup.client.ClientFactory;
import photosBackup.client.PhotosClientInterface;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GooglePhotosWriter {
    // for prototype take one photo from reader and upload it writer
    public void creatNewMedia(List<NewMediaItem> newMediaItems) {
        PhotosClientInterface writerClient = ClientFactory.getPhotosClient("destination");
        try {
            PhotosLibraryClient photosLibraryClient = writerClient.getPhotosClient();
            BatchCreateMediaItemsResponse response = photosLibraryClient.batchCreateMediaItems(newMediaItems);
            System.out.println("upload status:: " + response.getNewMediaItemResults(0).getStatus());
        } catch (IOException | GeneralSecurityException e) {

        }
    }

    public void getAlbums() {
        try {
            // Make a request to list all albums in the user's library
            // Iterate over all the albums in this list
            // Pagination is handled automatically
            PhotosClientInterface readerClient = ClientFactory.getPhotosClient("destination");
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
                System.out.println("writer title:: " + title);
            }
            System.out.println("Albums read from destination");

        } catch (ApiException | IOException | GeneralSecurityException e) {
            System.out.println(e);
        }
    }

}
