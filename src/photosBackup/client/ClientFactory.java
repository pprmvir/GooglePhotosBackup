package photosBackup.client;
public class ClientFactory {

    public static final String SOURCE = "source";
    public static final String DESTINATION = "destination";

    public static PhotosClientInterface getPhotosClient(String identifier) {
        switch (identifier) {
            case SOURCE:
                return new SourceClient();
            case DESTINATION:
                return new DestinationClient();
        }
        return null;
    }
}
