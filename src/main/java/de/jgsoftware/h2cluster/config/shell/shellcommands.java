package de.jgsoftware.h2cluster.config.shell;



import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.h2.tools.CreateCluster;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
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



    @ShellMethod("helpcluster")
    public String helpcluster()
    {
        System.out.print("start h2 cluster" + "\n");
        System.out.print("----------------" + "\n");
        System.out.print("to start the cluster on the source maschine " + "\n");
        System.out.print("\n");
        System.out.print("\n");
        System.out.print("java org.h2.tools.CreateCluster" + "\n");
        System.out.print("-urlSource jdbc:h2:tcp://localhost:9101/~/demodb" + "\n");
        System.out.print("-urlTarget jdbc:h2:tcp://localhost:9102/~/demodb" + "\n");
        System.out.print("-user root" + "\n");
        System.out.print("-password jj78mvpr52k1" + "\n");
        System.out.print("-serverList 192.168.178.5:9101,192.168.178.6:9102" + "\n");

        System.out.print("------------------------" + "\n");
        System.out.print("192.168.178.5 -> webshop" + "\n");
        System.out.print("192.168.178.6 -> target h2 container" + "\n");

        System.out.print("the url: http://www.h2database.com/html/advanced.html#clustering" + "\n");



        return "----------------------";
    }

    @ShellMethod("help")
    public String help()
    {
        System.out.print("\n");
        System.out.print("cluster h2 database -> helpcluster" + "\n");
        System.out.print("create a h2 cluster -> startcluster ->  urlsource urltarget user password serverlocal servertaget" + "\n");


        return "----------------------";
    }

    @ShellMethod("create h2 cluster --->  startcluster urlSource urlTaget user password serverList")
    public CreateCluster startcluster(String sturlsource, String sturltarget, String stuser, String stpassword, String stservlocal, String stservtarget)
    {
        /*

            java org.h2.tools.CreateCluster
                -urlSource jdbc:h2:tcp://localhost:9101/~/test
                -urlTarget jdbc:h2:tcp://localhost:9102/~/test
                -user sa
                -serverList localhost:9101,localhost:9102

         */

        /*
            String urlSource = sturlsource
            String urlTarget = sturltarget
            String user = stuser
         */
        System.out.print("urlSource " + sturlsource + "\n");
        System.out.print("urlTarget " + stservtarget + "\n");
        System.out.print("username " + stuser + "\n");
        System.out.print("password " + stpassword +"\n");
        System.out.print("ServerList " + "\n" + "first source db"  + sturlsource+ "\n" + "second targetdb" + stservtarget + "\n");

        org.h2.tools.CreateCluster h2cluster = new org.h2.tools.CreateCluster();


        try {


            h2cluster.execute(sturlsource, sturltarget, stuser, stpassword, stservlocal + ", " + stservtarget);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return h2cluster;
    }



    @ShellMethod("starttargetserver")
    public String starttargetserver() {


        // h2 server

        // java org.h2.tools.Server
        //    -tcp -tcpPort 9102
        //    -baseDir server1

        String userdir = System.getProperty("user.home");
        org.h2.tools.Server h2Servertarget;

        try {
            h2Servertarget = org.h2.tools.Server.createPgServer("-tcp", "-tcpPort", "9102", "-baseDir", userdir);
            h2Servertarget.start();
            String h2status = (String) h2Servertarget.getStatus();
            Integer h2port = (Integer) h2Servertarget.getPort();
            System.out.print("Directory is " + userdir + "\n");

            if (h2Servertarget.isRunning(true)) {
                System.out.print("H2 Clustering startet as server1." + "\n");


            } else {

                throw new RuntimeException("Could not start H2 server." + "\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start H2 server: " + e + "\n");
        }


        return "server status " + h2Servertarget.getStatus();

    }



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
