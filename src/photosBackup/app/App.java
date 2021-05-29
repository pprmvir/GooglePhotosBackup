package photosBackup.app;

import photosBackup.reader.GooglePhotosReader;
import photosBackup.writer.GooglePhotosWriter;

public class App {
    public static void main(String[] args) {
        (new GooglePhotosReader()).getAlbums();
       (new GooglePhotosWriter()).getAlbums();
    }
}
