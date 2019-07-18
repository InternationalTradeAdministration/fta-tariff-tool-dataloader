package gov.ita.terrafreights;

import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.blob.models.BlobHTTPHeaders;
import com.microsoft.azure.storage.blob.models.ContainerItem;
import com.microsoft.azure.storage.blob.models.PublicAccessType;
import com.microsoft.rest.v2.http.HttpPipeline;
import com.microsoft.rest.v2.util.FlowableUtil;
import gov.ita.terrafreights.country.Country;
import gov.ita.terrafreights.country.CountryList;
import io.reactivex.Flowable;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.Objects;

@Service
@Profile("production")
public class ProductionStorage implements Storage {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${terrafreights.azure-storage-account}")
  private String accountName;

  @Value("${terrafreights.azure-storage-account-key}")
  private String accountKey;

  @Value("${terrafreights.azure-storage-container}")
  private String containerName;

  @Override
  public void save(String fileName, String fileContent, String contentType, String user) {
    ContainerURL containerURL = makeContainerUrl();
    BlockBlobURL blobURL = containerURL.createBlockBlobURL(fileName);
    blobURL.upload(Flowable.just(ByteBuffer.wrap(fileContent.getBytes())), fileContent.getBytes().length,
      makeHeader(contentType), makeMetaData(user), null, null)
      .flatMap(blobsDownloadResponse ->
        // Download the blob's content.
        blobURL.download())
      .flatMap(blobsDownloadResponse ->
        // Verify that the blob data round-tripped correctly.
        FlowableUtil.collectBytesInBuffer(blobsDownloadResponse.body(null))
          .doOnSuccess(byteBuffer -> {
            if (byteBuffer.compareTo(ByteBuffer.wrap(fileContent.getBytes())) != 0) {
              throw new Exception("The downloaded data does not match the uploaded data.");
            }
          }))
      .blockingGet();
  }

  @Override
  public boolean containerExists() {
    ServiceURL serviceURL = makeServiceURL();
    List<ContainerItem> containerItems = serviceURL.listContainersSegment(null, null)
      .blockingGet().body().containerItems();
    return containerItems.stream().anyMatch(containerItem -> containerItem.name().equals(containerName));
  }

  @Override
  public void createContainer() {
    try {
      ContainerURL containerURL = makeContainerUrl();
      BlockBlobURL blobURL = containerURL.createBlockBlobURL("countries.json");
      InputStream in = TerraFreightsInitializer.class.getResourceAsStream("/fixtures/countries.json");
      String data = IOUtils.toString(new InputStreamReader(in));

      containerURL
        .create(makeMetaData(accountName), PublicAccessType.BLOB, null)
        .flatMap(containerCreateResponse ->
           /*
             Create the blob with string (plain text) content.
             NOTE: It is imperative that the provided length matches the actual length exactly.
            */
          blobURL.upload(Flowable.just(ByteBuffer.wrap(data.getBytes())), data.getBytes().length,
            makeHeader("application/json"), makeMetaData(accountName), null, null)
        )
        .flatMap(blobsDownloadResponse ->
          // Download the blob's content.
          blobURL.download())
        .flatMap(blobsDownloadResponse ->
          // Verify that the blob data round-tripped correctly.
          FlowableUtil.collectBytesInBuffer(blobsDownloadResponse.body(null))
            .doOnSuccess(byteBuffer -> {
              if (byteBuffer.compareTo(ByteBuffer.wrap(data.getBytes())) != 0) {
                throw new Exception("The downloaded data does not match the uploaded data.");
              }
            }))
        .blockingGet();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Country> getCountries() {
    String url = String.format("https://%s.blob.core.windows.net/%s/countries.json", accountName, containerName);
    return Objects.requireNonNull(restTemplate.getForObject(url, CountryList.class)).getCountries();
  }

  private ContainerURL makeContainerUrl() {
    ServiceURL serviceURL = makeServiceURL();
    return serviceURL.createContainerURL(containerName);
  }

  private ServiceURL makeServiceURL() {
    try {
      SharedKeyCredentials credential = new SharedKeyCredentials(accountName, accountKey);
      HttpPipeline pipeline = StorageURL.createPipeline(credential, new PipelineOptions());
      URL url = new URL(String.format("https://%s.blob.core.windows.net/", accountName));
      return new ServiceURL(url, pipeline);
    } catch (InvalidKeyException | MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Metadata makeMetaData(String user) {
    Metadata metadata = new Metadata();
    metadata.put("uploaded_by", user);
    return metadata;
  }

  private BlobHTTPHeaders makeHeader(String contentType) {
    BlobHTTPHeaders headers = new BlobHTTPHeaders();
    headers.withBlobContentType(contentType);
    return headers;
  }
}
