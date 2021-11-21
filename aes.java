import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class imageEncDec {
    /*
    *
    * Directory of image where will save
    *
    **/
    public static String saveImgDirPath(){
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString()+"\\Desktop\\";
        return s;
    }

    /*
    *
    * Get file extension
    *
    **/
    public static String fileExtension(String imgPath){
        String[] arrOfStr = imgPath.split("\\.");
        int getL =  arrOfStr.length - 1;
        return arrOfStr[getL];

    }

    /*
    *
    * Function for encryption procedure for files
    * @param String imgPath (file path which will be encrypt)
    *
    **/
    public static void encrypt(String imgPath) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            cipher.init(Cipher.ENCRYPT_MODE, secretKey); 

            UUID randomName = UUID.randomUUID(); //Generates random UUID  
            String imgOutPath = saveImgDirPath()+randomName+"."+fileExtension(imgPath); //image path for save

            CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(new File(imgPath)), cipher);

            FileOutputStream fileOutPut = new FileOutputStream(new File(imgOutPath));

            int i;
            while((i=cipherInputStream.read())!=-1)
            {
                fileOutPut.write(i);
            }
            System.out.println("Please save this Secret key for decrypt: "+encodedKey);
            System.out.println("Encript img path: "+imgOutPath);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    /*
    *
    * Function for decryption procedure for files
    * @param String imgUrl, String key ('imgUrl' for file path & 'key' is secrete key which will be decrypt)
    *
    **/
    public static void decrypt(String imgUrl, String key)
    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
    {
        try{
            Cipher cipher = Cipher.getInstance("AES");
            // decode the base64 encoded string
            byte[] decodedKey = Base64.getDecoder().decode(key);

            // rebuild key using SecretKeySpec
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 

            UUID randomName = UUID.randomUUID(); //Generates random UUID  
            String imgOutPath = saveImgDirPath()+randomName+"."+fileExtension(imgUrl);

            cipher.init(Cipher.DECRYPT_MODE, originalKey);
            
            CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(new File(imgUrl)), cipher);

            FileOutputStream fileOutPut = new FileOutputStream(new File(imgOutPath));

            int j;
            while((j = cipherInputStream.read()) != -1)
            {
                fileOutPut.write(j);
            }
            System.out.println("Decrypt img successfully done, img path is: "+imgOutPath);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /*
    *
    * Main function for execute
    *
    **/
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter 1 or 2:");
        System.out.println("1: Encrypt Image");
        System.out.println("2: Decrypt Image");

        int getInput = input.nextInt();
        if(getInput == 1){
            input.nextLine();
            System.out.println("Enter Image path from your computer:");
            String imgUrl = input.nextLine();
            if(!imgUrl.isEmpty()){
                try {
                    imageEncDec.encrypt(imgUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("Image path must require");
            }
        }else if(getInput == 2){
            input.nextLine();
            System.out.println("Enter decrypted image path from your computer:");
            String imgUrl = input.nextLine();

            System.out.println("Enter Secret Key:");
            String key = input.nextLine();
            if(!imgUrl.isEmpty() && !key.isEmpty()){
                try {
                    imageEncDec.decrypt(imgUrl, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("Image path & key must require");
            }
        }else{
            System.out.println("Invalid input");
        }

    }
}