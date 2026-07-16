package com.example.recipelog.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.recipelog.database.RecipeDbHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RecipeBackupManager {

    private final Context context;

    public RecipeBackupManager(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * SQLite DBを、ユーザーが指定した保存先へコピーする。
     */
    public void backupToUri(Uri destinationUri) throws IOException {
        closeDatabase();

        File databaseFile =
                context.getDatabasePath(RecipeDbHelper.DATABASE_NAME);

        if (!databaseFile.exists()) {
            throw new IOException("バックアップ対象のDBが存在しません");
        }

        try (
                InputStream inputStream =
                        new FileInputStream(databaseFile);

                OutputStream outputStream =
                        context.getContentResolver()
                                .openOutputStream(destinationUri)
        ) {
            if (outputStream == null) {
                throw new IOException("保存先を開けません");
            }

            copy(inputStream, outputStream);
        }
    }

    /**
     * ユーザーが選択したDBファイルで、現在のSQLite DBを全置換する。
     */
    public void restoreFromUri(Uri sourceUri) throws IOException {
        closeDatabase();

        File databaseFile =
                context.getDatabasePath(RecipeDbHelper.DATABASE_NAME);

        File parentDirectory = databaseFile.getParentFile();

        if (parentDirectory != null && !parentDirectory.exists()) {
            if (!parentDirectory.mkdirs()) {
                throw new IOException("DB保存フォルダを作成できません");
            }
        }

        /*
         * 直接本番DBへ書かず、一度一時ファイルへコピーする。
         * コピー成功後に本番DBと置き換える。
         */
        File temporaryFile =
                new File(
                        parentDirectory,
                        RecipeDbHelper.DATABASE_NAME + ".restore_tmp"
                );

        try (
                InputStream inputStream =
                        context.getContentResolver()
                                .openInputStream(sourceUri);

                OutputStream outputStream =
                        new FileOutputStream(temporaryFile)
        ) {
            if (inputStream == null) {
                throw new IOException("リストア元ファイルを開けません");
            }

            copy(inputStream, outputStream);
        } catch (IOException e) {
            if (temporaryFile.exists()) {
                temporaryFile.delete();
            }

            throw e;
        }

        if (temporaryFile.length() == 0) {
            temporaryFile.delete();
            throw new IOException("選択したファイルが空です");
        }

        deleteDatabaseRelatedFiles(databaseFile);

        if (!temporaryFile.renameTo(databaseFile)) {
            temporaryFile.delete();
            throw new IOException("DBファイルを置き換えられません");
        }
    }

    private void closeDatabase() {
        RecipeDbHelper dbHelper = new RecipeDbHelper(context);

        SQLiteDatabase database =
                dbHelper.getWritableDatabase();

        database.close();
        dbHelper.close();
    }

    /**
     * WALモードなどで作成される関連ファイルも削除する。
     */
    private void deleteDatabaseRelatedFiles(File databaseFile)
            throws IOException {

        File walFile =
                new File(databaseFile.getPath() + "-wal");

        File shmFile =
                new File(databaseFile.getPath() + "-shm");

        File journalFile =
                new File(databaseFile.getPath() + "-journal");

        deleteIfExists(walFile);
        deleteIfExists(shmFile);
        deleteIfExists(journalFile);
        deleteIfExists(databaseFile);
    }

    private void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException(
                    "ファイルを削除できません: " + file.getName()
            );
        }
    }

    private void copy(
            InputStream inputStream,
            OutputStream outputStream
    ) throws IOException {

        byte[] buffer = new byte[8192];
        int readLength;

        while ((readLength = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readLength);
        }

        outputStream.flush();
    }
}