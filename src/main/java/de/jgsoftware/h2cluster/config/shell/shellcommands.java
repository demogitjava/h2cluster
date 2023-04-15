package de.jgsoftware.h2cluster.config.shell;



import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.hibernate.engine.jdbc.StreamUtils.copy;

/**
 *
 * @author hoscho
 */

@ShellComponent
public class shellcommands
{




    // idemodatabases demo
    @ShellMethod("install h2 databases from github with type command --->  idemodatabase getinstall")
    public String idemodatabase(String database) {
        System.out.print("install databases" + "\n");
        File path = new File(System.getProperty("user.home"));


        // https://github.com/demogitjava/demodatabase/archive/refs/heads/master.zip
        int BUFFER = 2048;


        /*
                load file from internet to disk

         */
        try {
            URL url = new URL("https://github.com/demogitjava/demodatabase/archive/refs/heads/master.zip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            FileOutputStream out = new FileOutputStream(path + "/master.zip");
            copy(in, out, 1024);

            in.close();
            out.close();



            /*

                unzip file to

                / demodatabase-master

             */
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(path + "/" + "master.zip"));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {

                String filePath = File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();


        } catch(Exception e)
        {
            System.out.print("Fehler " + e);
        }




       /*

                copy files to the directory of the user

                like /root

        */
        //Files.copy("/demodatabase-master/demodb.mv.db", path + "/" + "demodb.mv.db");


        try {

            // copy demodatabses to user path  ----> like root


            // demo
            String demodb = path + "/"+ "demodb.mv.db";
            if(demodb.isEmpty())
            {
                System.out.print("folder demodatabase not exit please type command again");
            }
            else {
                copyFile(new File("/demodatabase-master/demodb.mv.db"), new File(path + "/"+ "demodb.mv.db"));


            }

            // mawi
            String mawi = path + "/"+ "mawi.mv.db";
            if(mawi.isEmpty())
            {
                System.out.print("folder demodatabase not exit please type command again");
            }
            else {
                copyFile(new File("/demodatabase-master/mawi.mv.db"), new File(path + "/"+ "mawi.mv.db"));
            }


            // shopdb.mv.db
            String shopdb = path + "/"+ "shopdb.mv.db";
            if(mawi.isEmpty())
            {
                System.out.print("folder demodatabase not exit please type command again");
            }
            else {
                copyFile(new File("/demodatabase-master/shopdb.mv.db"), new File(path + "/"+ "shopdb.mv.db"));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }





        return "installed";
    }

    private static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }


    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException
    {
        File path = new File(System.getProperty("user.home"));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[2048];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }


}
