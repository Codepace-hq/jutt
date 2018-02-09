package io.codepace.jutt.file;


import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static io.codepace.jutt.file.Sizes.*;

public class Utils {

    public static final int EOF = -1;

    /**
     * Gets the path to the system temp directory
     *
     * @return The path to the system temp directory
     */
    public static String getOsTempDirPath() {
        return System.getProperty("java.io.tempdir");
    }

    /**
     * Returns a {@link File} representing the system temp directory.
     *
     * @return The system temp directory
     */
    public static File getOsTempDir() {
        return new File(getOsTempDirPath());
    }

    /**
     * Returns the path to the user's home directory.
     *
     * @return the path to the user's home directory.
     */
    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    /**
     * Returns a {@link File} representing the user's home directory.
     *
     * @return the user's home directory.
     */
    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    //=========================================

    /**
     * TODO
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("\'" + file + "\' is a directory");
            }

            if (!file.canRead()) {
                throw new IOException("The current user is unable to read " + file);
            }
        } else {
            throw new FileNotFoundException("File " + file + " not found");
        }
        return new FileInputStream(file);
    }

    /**
     * TODO
     *
     * @param file
     * @param append
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            final File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    /**
     * TODO
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    /**
     * @see #touch(File, long)
     * @param file The file to create
     * @return If the file was created
     * @throws IOException If there was a problem creating the file
     */
    public static boolean touch(File file) throws IOException{
        return touch(file, System.currentTimeMillis());
    }


    /**
     * Creates a new blank file
     * @param file The file to create
     * @return If the file was created
     * @throws IOException If there was a problem creating the file
     */
    public static boolean touch(File file, long timestamp) throws IOException{
        if (!file.exists()) {
            new FileOutputStream(file).close();
        }
        return file.setLastModified(timestamp);
    }

    public static boolean contentEquals(File file1, File file2) throws IOException{
        final boolean file1Exists = file1.exists();

        if (file1Exists != file2.exists()){
            return false;
        }

        if (!file1Exists){
            return true;
        }

        if (file1.isDirectory() || file2.isDirectory()){
            throw new IOException("Unable to compare directories!");
        }

        // Compare file lengths
        if (file1.length() != file2.length()){
            return false;
        }

        // Compare if the files are the same
        if (file1.getCanonicalFile().equals(file2)){
            return true;
        }

        try (InputStream input1 = new FileInputStream(file1);
             InputStream input2 = new FileInputStream(file2)) {
            return streamContentEquals(input1, input2);
        }

    }

    public static boolean streamContentEquals(InputStream input1, InputStream input2)
            throws IOException {
        if (input1 == input2) {
            return true;
        }
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }

        int ch = input1.read();
        while (EOF != ch) {
            final int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        final int ch2 = input2.read();
        return ch2 == EOF;
    }

    public static String getHumanReadableFileSize(File file) throws IOException{
        return byteCountToDisplaySize(BigInteger.valueOf(file.length()));
    }

    public static String byteCountToDisplaySize(final BigInteger size) {
        String displaySize;

        if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_EB_BI)) + " EB";
        } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_PB_BI)) + " PB";
        } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_TB_BI)) + " TB";
        } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_GB_BI)) + " GB";
        } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_MB_BI)) + " MB";
        } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_KB_BI)) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }

    /**
     * Emptys a directory without actually deleting it
     * @param dir The directory to empty
     * @throws IOException
     */
    public static void emptyDirectory(File dir) throws IOException{
        final File[] files = verifiedListFiles(dir);

        IOException ex = null;
        for (File file : files) {
            try{
                rmrf(file);
            } catch (IOException ioe){
                ex = ioe;
            }
        }

        if (null != ex){
            throw ex;
        }
    }

    /**
     * Lists the contents of the directory
     * @param directory
     * @return
     * @throws IOException
     */
    private static File[] verifiedListFiles(final File directory) throws IOException {
        if (!directory.exists()) {
            final String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            final String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        final File[] files = directory.listFiles();
        if (files == null) {  // Security restrictions
            throw new IOException("Failed to list contents of " + directory);
        }
        return files;
    }

    public static void rmrf(final File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            final boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                final String message =
                        "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    public static void deleteDirectory(final File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!Files.isSymbolicLink(directory.toPath())) {
            emptyDirectory(directory);
        }

        if (!directory.delete()) {
            final String message =
                    "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    public static String getFileContents(File file) throws IOException{
        return getFileContents(file, StandardCharsets.UTF_8);
    }

    public static String getFileContents(File file, Charset charset) throws IOException{
        byte[] enc = Files.readAllBytes(file.toPath());
        return new String(enc, charset);
    }

}
