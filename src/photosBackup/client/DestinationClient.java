package photosBackup.client;
import com.google.common.collect.ImmutableList;

import java.io.File;
import java.util.List;

public class DestinationClient extends AbstractPhotosClient {

    @Override
    protected List<String> getRequiredScopes() {
        // TODO update it to write scope
        return ImmutableList.of("https://www.googleapis.com/auth/photoslibrary.readonly");
    }

    /**
     * Remember to add User to this secret file
     * @return
     */
    @Override
    protected String getCredentialsPath() {
        return System.getProperty("user.dir") + "/destination_client_secret_GooglePhotosBackup.json";
    }

    @Override
    protected Integer getReciverPort() {
        return 7080;
    }

    @Override
    protected File getDataStorePath() {
        return new File(DestinationClient.class.getResource("/").getPath(), "/DestinationCredentials");
    }
}
