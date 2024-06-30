package tihonel.com.github.workpermit.utils;

import org.springframework.stereotype.Component;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class ShaEncoder {
    public String checkSum(WorkPermit workPermit){
        return checkSum(workPermit.toString().getBytes());
    }

    public String checkSum(File file){
        try (FileInputStream inputStream = new FileInputStream(file)){
            return checkSum(inputStream.readAllBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String checkSum(byte[] input){
        byte[] sum;
        try {
            sum = MessageDigest.getInstance("SHA-256").digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return toHexString(sum);
    }

    private String toHexString(byte[] sum) {
        StringBuilder builder = new StringBuilder();
        for(byte b : sum){
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
