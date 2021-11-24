package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.server.dao.ImageDAOInterface;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public class S3DAO implements ImageDAOInterface {

    @Inject
    public S3DAO() {}

    private static final String BUCKET_NAME = "my-tweeter-bucket";

    /**
     * Upload image to s3
     *
     * @param alias username associated with image
     * @param base64Image String image inside RegisterRequest
     * @return image url
     */
    @Override
    public String uploadImage(String alias, String base64Image) throws RuntimeException {

        URL url = null;
        try {
            AmazonS3 s3 = AmazonS3ClientBuilder
                    .standard()
                    .withRegion(Regions.US_WEST_2)
                    .build();

            // Create user profile image file to upload
            String filename = String.format("%s_profile_image", alias);
            System.out.println("Filename = " + filename);

            // Get image bytes
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            System.out.println("image bytes decoded");

            // Set image metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageBytes.length);
            metadata.setContentType("image/jpeg");

            System.out.println("Created metadata = " + metadata);

            // Upload image (Setting image to be publicly accessible)
        InputStream is = new ByteArrayInputStream(imageBytes);
            PutObjectRequest imageFileRequest = new PutObjectRequest(BUCKET_NAME, filename,
                    is ,metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            System.out.println("is stream");

            s3.putObject(imageFileRequest);

            System.out.println("put request object");
            is.close();
            // Get url of image
            url = s3.getUrl(BUCKET_NAME, filename);
        } catch (AmazonServiceException | IOException e){
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to upload image to s3.");

        }
        return url.toString();
    }
}
