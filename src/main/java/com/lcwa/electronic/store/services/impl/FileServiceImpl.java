package com.lcwa.electronic.store.services.impl;

import com.lcwa.electronic.store.exceptions.BadApiRequest;
import com.lcwa.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl  implements FileService {

   private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String UploadFile(MultipartFile file, String path) throws IOException {

        // 2 file k same name hoskta he islie random string generate krlete he
        String originalFilename=file.getOriginalFilename();
        logger.info("Original file name : {}",originalFilename);

        //String filename= UUID.randomUUID().toString();
        // to get the shorter string
        int ind=originalFilename.indexOf(".");
        String filename = originalFilename.substring(0,ind)+ UUID.randomUUID().toString().replace("-", "").substring(0,8);

        assert originalFilename != null;
        String extension=originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileNameWithExtension=filename+extension;

        // is path par image ko upload krege
        // File.separator is used to construct the filepath that is platform independent like windows
        // bcz windows uses backslash(\)  as the separator while mac uses forward(/) slash
        // hardcoding these separators in our java code can lead to issues when your application runs  on different OS

        String fullPathFileName=path +fileNameWithExtension;


        logger.info("Full file path that need to be created : {}",fullPathFileName);
        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")|| extension.equalsIgnoreCase(".jpeg")){

            logger.info("file extension : {}",extension);
            // file save
            File folder=new File(path);
            System.out.println(folder);
            if(!folder.exists()) {
                // create folder

                folder.mkdirs();
                logger.info("Folder created {}",folder );
            }

            // upload the file
            // file original file jo are he usko read kia then copy kia
            Files.copy(file.getInputStream(), Paths.get(fullPathFileName));

            logger.info("one more extension {}",folder.exists());

            return fileNameWithExtension;
        }
        else{

             throw new BadApiRequest("File with extension " + extension + " not allowed !!" );
        }


    }

    @Override
    public InputStream getResources(String path, String imgname) throws FileNotFoundException {

        String fullpath= path + imgname;

        // FileInputStream is used to read the data from a file in the form of a stream of bytes
        //and is specifically designed for reading binary data like images, audio, or any raw byte stream. However, it can also be used to read text files, though it's generally recommended to use higher-level classes like FileReader or BufferedReader for text.
      // its byte-oriented

        // when to use FileReader(When you need to process data as characters rather than raw bytes)
        // : Ideal for reading .txt, .csv, .json, .xml, and other text-based files.

       //When to Use FileInputStream:
        //Reading binary files (e.g., images, audio, videos).

        InputStream inputStream=new  FileInputStream(fullpath);

        return inputStream;
    }
}
