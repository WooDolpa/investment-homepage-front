package san.investment.front.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

/**
 * packageName : san.investment.front.utils
 * className : FileUtil
 * user : jwlee
 * date : 2025. 12. 28.
 * description :
 */
@Slf4j
@Component
public class FileUtil {

    @Value("${file.save.url}")
    private String fileUrl;


    public String convertToWebPath(String filePath) {

        if(filePath == null || filePath.isEmpty()) {
            return null;
        }

        if(filePath.startsWith("/uploads/")) {
            return filePath;
        }

        String normalizedPath = filePath.replace("\\", "/");
        String normalizedSaveUrl = Paths.get(fileUrl).toString().replace("\\", "/");

        int saveUrlIndex = normalizedPath.indexOf(normalizedSaveUrl);

        if (saveUrlIndex >= 0) {
            // Extract relative path after saveUrl
            String relativePath = normalizedPath.substring(saveUrlIndex + normalizedSaveUrl.length());
            // Remove leading slash if present
            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }
            return "/uploads/" + relativePath;
        }

        return filePath;
    }
}
