package gov.ita.tarifftooldataloader.storage;

import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.blob.models.BlobHTTPHeaders;
import com.microsoft.azure.storage.blob.models.ContainerItem;
import com.microsoft.azure.storage.blob.models.PublicAccessType;
import com.microsoft.rest.v2.http.HttpPipeline;
import com.microsoft.rest.v2.util.FlowableUtil;
import gov.ita.tarifftooldataloader.tariff.TariffRatesMetadata;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile({"production", "staging"})
public class ProductionStorage implements Storage {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${tarifftooldataloader.azure-storage-account}")
  private String accountName;

  @Value("${tarifftooldataloader.azure-storage-account-key}")
  private String accountKey;

  @Value("${tarifftooldataloader.azure-storage-container}")
  private String containerName;

  @Override
  public void save(String fileName, String fileContent, String user) {
    if (user == null) user = accountName;
    ContainerURL containerURL = makeContainerUrl();
    BlockBlobURL blobURL = containerURL.createBlockBlobURL(fileName);
    blobURL.upload(Flowable.just(ByteBuffer.wrap(fileContent.getBytes())), fileContent.getBytes().length,
      makeHeader(fileName), makeMetaData(user), null, null)
      .flatMap(blobsDownloadResponse ->
        blobURL.download())
      .flatMap(blobsDownloadResponse ->
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
    assert serviceURL != null;
    List<ContainerItem> containerItems = serviceURL.listContainersSegment(null, null)
      .blockingGet().body().containerItems();
    return containerItems.stream().anyMatch(containerItem -> containerItem.name().equals(containerName));
  }

  @Override
  public void createContainer() {
    makeContainerUrl()
      .create(makeMetaData(accountName), PublicAccessType.CONTAINER, null)
      .blockingGet();
  }

  @Override
  public String getBlobAsString(String blobName) {
    String url = String.format("https://%s.blob.core.windows.net/%s/%s", accountName, containerName, blobName);
    return Objects.requireNonNull(restTemplate.getForObject(url, String.class));
  }

  @Override
  public List<TariffRatesMetadata> getBlobsMetadata(String prefix) {
    ListBlobsOptions listBlobsOptions = new ListBlobsOptions();
    listBlobsOptions.withPrefix(prefix);
    BlobListDetails details = new BlobListDetails();
    details.withMetadata(true);
    listBlobsOptions.withDetails(details);
    List<TariffRatesMetadata> meta = makeContainerUrl()
      .listBlobsFlatSegment(null, listBlobsOptions, null).blockingGet().body().segment()
      .blobItems()
      .stream().map(
        x -> new TariffRatesMetadata(
          x.name(),
          buildUrlForBlob(x.name()),
          x.metadata().get("uploaded_by"),
          x.properties().lastModified().toLocalDateTime(),
          x.properties().contentLength()
        ))
      .collect(Collectors.toList());
    return meta;
  }

  @Override
  public LocalDateTime getLastModifiedAt() {
    List<TariffRatesMetadata> blobsMetadata = getBlobsMetadata(null);
    return blobsMetadata.stream()
      .max(Comparator.comparing(TariffRatesMetadata::getUploadedAt))
      .map(TariffRatesMetadata::getUploadedAt).orElse(null);
  }

  private String buildUrlForBlob(String blobName) {
    return String.format("https://%s.blob.core.windows.net/%s/%s", accountName, containerName, blobName);
  }

  private ContainerURL makeContainerUrl() {
    ServiceURL serviceURL = makeServiceURL();
    assert serviceURL != null;
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

  private BlobHTTPHeaders makeHeader(String fileName) {
    String contentType;

    if (fileName.contains(".csv"))
      contentType = "text/csv";
    else
      contentType = "application/json";

    BlobHTTPHeaders headers = new BlobHTTPHeaders();
    headers.withBlobContentType(contentType);
    return headers;
  }
}
