package com.example.aivestorBackend.s3;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    @Value("${s3.bucket-name}")
    private String bucketName;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.access-key}")
    private String accessKey;

    @Value("${s3.secret-key}")
    private String secretKey;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    /**
     * S3에 파일을 업로드하고 업로드된 파일의 URL을 반환합니다.
     * @param multipartFile 업로드할 파일
     * @param dirName S3 버킷 내의 디렉토리 이름 (예: "thumbnails", "news-images")
     * @return 업로드된 파일의 S3 URL
     * @throws IOException 파일 처리 중 발생할 수 있는 예외
     */
    public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 파일 이름 중복 방지를 위해 UUID 사용
        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String s3FileName = dirName + "/" + UUID.randomUUID().toString() + ext;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3FileName)
                    .contentType(multipartFile.getContentType())
                    .contentLength(multipartFile.getSize())
                    .build();

            s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            // S3 객체 URL 반환 (퍼블릭 접근이 가능하도록 설정되어 있어야 함)
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3FileName;
        } catch (S3Exception e) {
            throw new IOException("S3 파일 업로드 실패: " + e.getMessage(), e);
        }
    }

    /**
     * S3에서 파일을 삭제합니다.
     * @param fileUrl 삭제할 파일의 S3 URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        try {
            // URL에서 S3 key 추출
            String s3Key = fileUrl.substring(fileUrl.indexOf(".com/") + 5); // ".com/" 이후의 경로가 key
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            System.err.println("S3 파일 삭제 실패: " + e.getMessage());
            // 실제 애플리케이션에서는 로깅 프레임워크를 사용하고 적절한 예외 처리를 해야 합니다.
        }
    }
}
